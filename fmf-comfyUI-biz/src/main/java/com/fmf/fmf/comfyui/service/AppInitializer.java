package com.fmf.fmf.comfyui.service;

/**
 * @Author:吴金才
 * @Date:2024/2/2 14:18
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner {

    @Autowired
    private WebSocketService webSocketService;

    public AppInitializer(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void run(String... args) throws Exception {
        webSocketService.init();
    }
}

