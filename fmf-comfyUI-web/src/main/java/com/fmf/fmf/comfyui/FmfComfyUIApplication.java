package com.fmf.fmf.comfyui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author:吴金才
 * @Date:2024/1/30 14:54
 */
@SpringBootApplication
@MapperScan("com.fmf.fmf.comfyUI.dal.mapper")
public class FmfComfyUIApplication {
    public static void main(String[] args) {
        SpringApplication.run(FmfComfyUIApplication.class, args);
    }
}
