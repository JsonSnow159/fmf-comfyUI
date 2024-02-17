package com.fmf.fmf.comfyui.service;

import com.alibaba.fastjson.JSON;
import com.fmf.fmf.comfyUI.dal.entity.CloudMachine;
import com.fmf.fmf.comfyUI.dal.mapper.CloudMachineMapper;
import com.fmf.fmf.comfyui.common.CommonConstant;
import com.fmf.fmf.comfyui.common.ICacheService;
import com.fmf.fmf.comfyui.common.impl.RedissonDistributedLock;
import com.fmf.fmf.comfyui.dto.WsMessage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private WebSocketMsgHandleService webSocketMsgHandleService;
    @Resource
    private RedissonDistributedLock redissonDistributedLock;

    public void init() {
        List<CloudMachine> cloudMachineList = cloudMachineMapper.findAll();
        log.info("进入初始化脚本");
        try {
            redissonDistributedLock.lock(CommonConstant.INIT_MACHINE_KEY, 2, TimeUnit.MINUTES);
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
                        public void onMessage(String message) {
                            log.info("收到websocket返回:{}", message);
                            WsMessage wsMessage = JSON.parseObject(message, WsMessage.class);
                            String machineAddress = cloudMachine.getIp() + ":" + cloudMachine.getPort();
                            if (Objects.equals(wsMessage.getType(), "status")) {
                                Integer queueRemaining = wsMessage.getData().getStatus().getExecInfo().getQueueRemaining();
                                cacheService.zadd(CommonConstant.CLOUD_MACHINE, machineAddress, queueRemaining.doubleValue());
                                cacheService.zadd(CommonConstant.MACHINE_STATUS, machineAddress, 1);
                            }
                            webSocketMsgHandleService.handleMessage(machineAddress, message);
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
                } catch (Exception e) {
                    log.info("websocket异常", e);
                }
            }
        } catch (Exception e) {
            log.info("锁竞争失败");
        }
    }
}
