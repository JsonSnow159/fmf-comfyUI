package com.fmf.fmf.comfyui.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.fmf.fmf.comfyUI.dal.entity.Mission;
import com.fmf.fmf.comfyUI.dal.entity.ToolTemplate;
import com.fmf.fmf.comfyUI.dal.mapper.MissionMapper;
import com.fmf.fmf.comfyUI.dal.mapper.ToolTemplateMapper;
import com.fmf.fmf.comfyui.common.ICacheService;
import com.fmf.fmf.comfyui.dto.GeneratePhotoRequestDTO;
import com.fmf.fmf.comfyui.dto.PromptRequest;
import com.fmf.fmf.comfyui.dto.QueuePromptResponse;
import com.fmf.fmf.comfyui.dto.ToolConfigDTO;
import com.fmf.fmf.comfyui.exception.BizException;
import com.fmf.fmf.comfyui.exception.CloudMachineStatusEnum;
import com.fmf.fmf.comfyui.exception.ErrorCodeEnum;
import com.fmf.fmf.comfyui.service.GeneratePhotoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author:吴金才
 * @Date:2024/2/1 15:46
 */
@Slf4j
@Service
public class GeneratePhotoServiceImpl implements GeneratePhotoService {
    @Resource
    private ToolTemplateMapper toolTemplateMapper;
    @Resource
    private ICacheService cacheService;
    private static final String CLOUD_MACHINE = "cloud_machine";
    private static final String MACHINE_STATUS = "machine_status";
    private static final String RUNNING_MISSION_KEY = "ip_port_queue_running";
    @Resource
    private ComfyUIAPIServiceImpl comfyUIAPIService;
    @Resource
    private MissionMapper missionMapper;

    @Override
    public void generatePhoto(GeneratePhotoRequestDTO generatePhotoRequest) {
        String toolType = generatePhotoRequest.getToolType();
        String version = generatePhotoRequest.getVersion();
        ToolTemplate toolTemplate = toolTemplateMapper.findByTool(toolType, version);
        if (Objects.isNull(toolTemplate)) {
            log.error("工具未配置模版，暂不可用");
            throw new BizException(ErrorCodeEnum.TOOL_TEMPLATE_ERROR);
        }
        List<ToolConfigDTO> toolConfigList = generatePhotoRequest.getToolConfigList();
        //TODO 待优化替换逻辑
        String templateContent = toolTemplate.getTemplateContent();

        String missionId = UUID.randomUUID().toString();
        missionId = missionId.replace("-", "");
        templateContent = templateContent.replace("\"client_id\":\"%s\"", String.format("\"client_id\":\"%s\"", missionId));

        Set<ZSetOperations.TypedTuple<String>> cloudMachineSet = cacheService.zSort(CLOUD_MACHINE);
        Set<ZSetOperations.TypedTuple<String>> machineStatusSet = cacheService.zSort(MACHINE_STATUS);
        String enableMachineAddress = null;
        double score = 0D;
        try {
            for (ZSetOperations.TypedTuple<String> cloudMachine : cloudMachineSet) {
                String machineAddress = cloudMachine.getValue();
                boolean machineEnable = false;
                for (ZSetOperations.TypedTuple<String> machineStatus : machineStatusSet) {
                    String machineStatusAddress = machineStatus.getValue();
                    if (!Objects.equals(machineAddress, machineStatusAddress)) {
                        continue;
                    }
                    int status = Objects.requireNonNull(machineStatus.getScore()).intValue();
                    if (Objects.equals(CloudMachineStatusEnum.ENABLE.getCode(), status)) {
                        machineEnable = true;
                        break;
                    }
                }
                if (machineEnable) {
                    enableMachineAddress = machineAddress;
                    score = cloudMachine.getScore();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("获取可用云算力服务器时异常", e);
        }

        if (StringUtils.isBlank(enableMachineAddress)) {
            log.error("未找到可用机器");
            throw new BizException(ErrorCodeEnum.CLOUD_MACHINE_ERROR);
        }
        Mission mission = new Mission();
        mission.setMissionId(missionId);
        mission.setUid(generatePhotoRequest.getUid());
        mission.setPromptRequest(JSON.toJSONString(generatePhotoRequest.getToolConfigList()));
        String[] cloudMachineInfo = enableMachineAddress.split(":");
        mission.setCloudMachineIp(cloudMachineInfo[0]);
        mission.setCloudMachinePort(cloudMachineInfo[1]);
        mission.setStatus(0);
        int insert = missionMapper.insert(mission);
        if (insert == 0) {
            throw new BizException(ErrorCodeEnum.INSERT_ERROR);
        }
        QueuePromptResponse promptResponse = comfyUIAPIService.prompt(enableMachineAddress, templateContent);

        String promptId = promptResponse.getPromptId();
        mission.setPromptId(promptId);
        mission.setStatus(1);
        missionMapper.update(mission);
        cacheService.zadd(RUNNING_MISSION_KEY, enableMachineAddress + "_" + promptId, System.currentTimeMillis());
        cacheService.zadd(CLOUD_MACHINE, enableMachineAddress, score + 1);
    }
}
