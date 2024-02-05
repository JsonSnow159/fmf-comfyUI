package com.fmf.fmf.comfyui.strategy.impl;

import com.fmf.fmf.comfyui.dto.ToolRequestDTO;
import com.fmf.fmf.comfyui.enums.ToolEnum;
import com.fmf.fmf.comfyui.strategy.ToolStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author:吴金才
 * @Date:2024/2/5 16:53
 */
@Slf4j
@Component
public class RetouchImage implements ToolStrategy {

    @Override
    public String getToolCode() {
        return ToolEnum.RETOUCH_IMAGE.getCode();
    }

    @Override
    public void handler(ToolRequestDTO toolRequest) {
        System.out.println("修图");
    }
}
