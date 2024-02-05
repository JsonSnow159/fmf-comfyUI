package com.fmf.fmf.comfyui.service;

import com.fmf.fmf.comfyUI.dal.entity.CloudMachine;
import com.fmf.fmf.comfyUI.dal.entity.Mission;
import com.fmf.fmf.comfyUI.dal.entity.MissionOutput;
import com.fmf.fmf.comfyUI.dal.mapper.CloudMachineMapper;
import com.fmf.fmf.comfyUI.dal.mapper.MissionMapper;
import com.fmf.fmf.comfyUI.dal.mapper.MissionOutputMapper;
import com.fmf.fmf.comfyui.common.ICacheService;
import com.fmf.fmf.comfyui.common.impl.RedissonDistributedLock;
import com.fmf.fmf.comfyui.dto.HistoryByPromptIdResponse;
import com.fmf.fmf.comfyui.dto.ViewQueueResponse;
import com.fmf.fmf.comfyui.service.impl.ComfyUIQueryByPromptIdServiceImpl;
import com.fmf.fmf.comfyui.service.impl.ComfyUIQueueServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author:吴金才
 * @Date:2024/2/2 14:18
 */
@Service
@Slf4j
public class WebSocketService {
    @Resource
    private CloudMachineMapper cloudMachineMapper;
    @Resource
    private ICacheService cacheService;
    @Resource
    private ComfyUIQueueServiceImpl comfyUIQueueService;
    @Resource
    private ComfyUIQueryByPromptIdServiceImpl comfyUIQueryByPromptIdService;
    @Resource
    private MissionOutputMapper missionOutputMapper;
    @Resource
    private MissionMapper missionMapper;
    @Resource
    private RedissonDistributedLock redissonDistributedLock;
    private static final String CLOUD_MACHINE = "cloud_machine";
    private static final String MACHINE_STATUS = "machine_status";
    private static final String RUNNING_MISSION_KEY = "ip_port_queue_running_%s";
    private static final String INIT_MACHINE_KEY = "init_machine_lock";

    public void init() {
        List<CloudMachine> cloudMachineList = cloudMachineMapper.findAll();
        log.info("进入初始化脚本");
        try {
            redissonDistributedLock.lock(INIT_MACHINE_KEY, 2, TimeUnit.MINUTES);
            for (CloudMachine cloudMachine : cloudMachineList) {
                try {
                    String ws = "ws://" + cloudMachine.getIp() + ":" + cloudMachine.getPort() + "/ws";
                    URI uri = new URI(ws);
                    //设置请求头
                    Map<String, String> headers = new HashMap<>(8);
                    WebSocketClient client = new WebSocketClient(uri, headers) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            log.info("websocket已连接");
                        }

                        @Override
                        public void onMessage(String s) {
                            log.info("收到websocket返回:{}", s);
                            String machineAddress = uri.getHost() + ":" + uri.getPort();
                            ViewQueueResponse viewQueueResponse = comfyUIQueueService.viewQueue(machineAddress);
                            List<List<Object>> queueRunning = viewQueueResponse.getQueueRunning();
                            //全部执行完成
                            String runningPromptId = null;
                            if (!CollectionUtils.isEmpty(queueRunning)) {
                                List<Object> queueRunningInfo = queueRunning.get(0);
                                runningPromptId = (String) queueRunningInfo.get(1);
                            }
                            String runningMissionKey = String.format(RUNNING_MISSION_KEY, machineAddress);
                            Set<ZSetOperations.TypedTuple<String>> runningMissionSet = cacheService.zSort(runningMissionKey);
                            for (ZSetOperations.TypedTuple<String> runningMission : runningMissionSet) {
                                String promptId = runningMission.getValue();
                                if (Objects.equals(runningPromptId, promptId)) {
                                    break;
                                }
                                //查询任务情况
                                HistoryByPromptIdResponse historyByPromptIdResponse = comfyUIQueryByPromptIdService.historyByPromptId(machineAddress, promptId);
                                HistoryByPromptIdResponse.StatusDTO status = historyByPromptIdResponse.getStatus();
                                if (Objects.equals(status.getStatusStr(), "success") && status.getCompleted()) {
                                    HistoryByPromptIdResponse.OutputsDTO outputs = historyByPromptIdResponse.getOutputs();
                                    HistoryByPromptIdResponse.OutputsDTO._$9DTO nine = outputs.getNine();
                                    List<HistoryByPromptIdResponse.OutputsDTO._$9DTO.ImagesDTO> images = nine.getImages();
                                    for (HistoryByPromptIdResponse.OutputsDTO._$9DTO.ImagesDTO image : images) {
                                        String filename = image.getFilename();
                                        Mission mission = missionMapper.findByPromptId(promptId);
                                        MissionOutput missionOutput = new MissionOutput();
                                        missionOutput.setMissionId(mission.getMissionId());
                                        missionOutput.setImageData(filename);
                                        missionOutput.setStatus(0);
                                        missionOutputMapper.insert(missionOutput);
                                    }
                                    cacheService.zrem(runningMissionKey, promptId);
                                }
                            }
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            log.info("websocket已关闭");
                        }

                        @Override
                        public void onError(Exception e) {
                            log.info("websocket异常", e);
                        }
                    };
                    client.connect();
                    cacheService.zadd(CLOUD_MACHINE, cloudMachine.getIp() + ":" + cloudMachine.getPort(), 0);
                    cacheService.zadd(MACHINE_STATUS, cloudMachine.getIp() + ":" + cloudMachine.getPort(), 1);
                } catch (Exception e) {
                    log.info("websocket异常", e);
                }
            }
        } catch (Exception e) {
            log.info("锁竞争失败");
        }
    }
}
