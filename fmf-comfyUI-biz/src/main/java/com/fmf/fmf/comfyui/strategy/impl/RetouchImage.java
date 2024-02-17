package com.fmf.fmf.comfyui.strategy.impl;

import cn.hutool.core.lang.UUID;
import com.fmf.fmf.comfyUI.dal.entity.Mission;
import com.fmf.fmf.comfyUI.dal.entity.ToolTemplate;
import com.fmf.fmf.comfyUI.dal.mapper.MissionMapper;
import com.fmf.fmf.comfyUI.dal.mapper.ToolTemplateMapper;
import com.fmf.fmf.comfyui.common.CommonConstant;
import com.fmf.fmf.comfyui.common.ICacheService;
import com.fmf.fmf.comfyui.dto.ImageUploadResponse;
import com.fmf.fmf.comfyui.dto.QueuePromptResponse;
import com.fmf.fmf.comfyui.dto.ToolConfigDTO;
import com.fmf.fmf.comfyui.dto.ToolRequestDTO;
import com.fmf.fmf.comfyui.enums.CloudMachineStatusEnum;
import com.fmf.fmf.comfyui.enums.MissionStatusEnum;
import com.fmf.fmf.comfyui.enums.ToolEnum;
import com.fmf.fmf.comfyui.exception.BizException;
import com.fmf.fmf.comfyui.exception.ErrorCodeEnum;
import com.fmf.fmf.comfyui.service.impl.ComfyUIPromptServiceImpl;
import com.fmf.fmf.comfyui.service.impl.ComfyUIUploadImageServiceImpl;
import com.fmf.fmf.comfyui.strategy.ToolStrategy;
import com.fmf.fmf.comfyui.utils.SeedUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author:吴金才
 * @Date:2024/2/5 16:53
 */
@Slf4j
@Component
public class RetouchImage implements ToolStrategy {
    @Resource
    private ToolTemplateMapper toolTemplateMapper;
    @Resource
    private ComfyUIUploadImageServiceImpl comfyUIUploadImageService;
    @Resource
    private ICacheService cacheService;
    @Resource
    private ComfyUIPromptServiceImpl comfyUIPromptService;
    @Resource
    private MissionMapper missionMapper;

    @Override
    public String getToolCode() {
        return ToolEnum.RETOUCH_IMAGE.getCode();
    }

    @Override
    public void handler(ToolRequestDTO toolRequest) {
        String toolCode = toolRequest.getToolCode();
        String version = toolRequest.getVersion();
        ToolTemplate toolTemplate = toolTemplateMapper.findByTool(toolCode, version);
        if (Objects.isNull(toolTemplate)) {
            log.error("工具未配置模版，暂不可用");
            throw new BizException(ErrorCodeEnum.TOOL_TEMPLATE_ERROR);
        }
        Set<ZSetOperations.TypedTuple<String>> cloudMachineSet = cacheService.zSort(CommonConstant.CLOUD_MACHINE);
        Set<ZSetOperations.TypedTuple<String>> machineStatusSet = cacheService.zSort(CommonConstant.MACHINE_STATUS);
        String enableMachineAddress = null;
        Double score = 0D;
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

        List<ToolConfigDTO> toolConfigList = toolRequest.getToolConfigList();
        String templateContent = toolTemplate.getTemplateContent();

        String missionId = UUID.randomUUID().toString();
        missionId = missionId.replace("-", "");
        templateContent = templateContent.replace("##client_id##", missionId);
        Long seed = 0L;
        for (ToolConfigDTO toolConfig : toolConfigList) {
            String paramId = toolConfig.getParamId();
            paramId = "##" + paramId + "##";
            if (Objects.equals(paramId, "##seed##")) {
                templateContent = templateContent.replace(paramId, toolConfig.getParamValue());
                seed = Long.valueOf(toolConfig.getParamValue());
            } else if (paramId.contains("image")) {
                //url
                String imageUrl = toolConfig.getParamValue();
                String fileName = extractFileName(imageUrl);
                ImageUploadResponse imageUploadResponse = comfyUIUploadImageService.uploadImage(enableMachineAddress, imageUrl, fileName);
                if (Objects.isNull(imageUploadResponse)) {
                    throw new BizException(ErrorCodeEnum.IMAGE_UPLOAD_ERROR);
                }
                //防止重名
                String imageName = imageUploadResponse.getName();
                templateContent = templateContent.replace(paramId, imageName);
            } else {
                templateContent = templateContent.replace(paramId, toolConfig.getParamValue());
            }
        }
        if (seed == 0L) {
            long currentTimeMillis = System.currentTimeMillis();
            int randomNo = SeedUtil.generateRandom4DigitNumber();
            seed = Long.parseLong(currentTimeMillis + "" + randomNo);
            templateContent = templateContent.replace("##seed##", seed.toString());
        }

        if (templateContent.contains("#")) {
            log.error("模版参数未全部填写");
            throw new BizException(ErrorCodeEnum.PARAM_ERROR);
        }
        Mission mission = new Mission();
        mission.setMissionId(missionId);
        mission.setUid(toolRequest.getUid());
//        mission.setPromptRequest(JSON.toJSONString(toolRequest.getToolConfigList()));
        String[] cloudMachineInfo = enableMachineAddress.split(CommonConstant.SPLIT_REGEX);
        mission.setCloudMachineIp(cloudMachineInfo[0]);
        mission.setCloudMachinePort(cloudMachineInfo[1]);
        mission.setStatus(MissionStatusEnum.INIT.getCode());
        int insert = missionMapper.insert(mission);
        if (insert == 0) {
            throw new BizException(ErrorCodeEnum.INSERT_ERROR);
        }
        QueuePromptResponse promptResponse = comfyUIPromptService.prompt(enableMachineAddress, templateContent);
        String promptId = promptResponse.getPromptId();
        mission.setPromptId(promptId);
        mission.setStatus(MissionStatusEnum.RUNNING.getCode());
        missionMapper.update(mission);
        cacheService.zadd(String.format(CommonConstant.RUNNING_MISSION_KEY, enableMachineAddress), promptId, System.currentTimeMillis());
        cacheService.zadd(CommonConstant.CLOUD_MACHINE, enableMachineAddress, score + 1);
    }

    public static String extractFileName(String urlString) {
        try {
            // 创建URL对象
            URL url = new URL(urlString);
            // 获取URL路径部分
            String path = url.getPath();
            // 从路径中提取文件名部分
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
