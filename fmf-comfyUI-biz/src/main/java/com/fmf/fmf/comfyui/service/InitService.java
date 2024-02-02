package com.fmf.fmf.comfyui.service;

import com.fmf.fmf.comfyUI.dal.entity.CloudMachine;
import com.fmf.fmf.comfyUI.dal.mapper.CloudMachineMapper;
import com.fmf.fmf.comfyui.common.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:吴金才
 * @Date:2024/2/2 14:18
 */
@Service
@Slf4j
public class InitService {
    @Resource
    private CloudMachineMapper cloudMachineMapper;
    @Resource
    private ICacheService cacheService;
    private static final String CLOUD_MACHINE = "cloud_machine";
    private static final String MACHINE_STATUS = "machine_status";

    public void init() {
        List<CloudMachine> cloudMachineList = cloudMachineMapper.findAll();
        log.info("进入初始化脚本");
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
                        log.info("websocket返回值");
                        //TODO 做业务的地方
                        log.info("收到websocket返回:{}", s);
                        System.out.println("收到websocket返回" + s);
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

        cacheService.zadd(CLOUD_MACHINE, "127.0.0.2:8188", 5);
        cacheService.zadd(MACHINE_STATUS, "127.0.0.2:8188", 1);

        cacheService.zadd(CLOUD_MACHINE, "127.0.0.3:8188", 2);
        cacheService.zadd(MACHINE_STATUS, "127.0.0.3:8188", 1);
    }
}
