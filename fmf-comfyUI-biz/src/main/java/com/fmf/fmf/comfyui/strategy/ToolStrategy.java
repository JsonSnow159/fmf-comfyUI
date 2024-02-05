package com.fmf.fmf.comfyui.strategy;

import com.fmf.fmf.comfyui.dto.ToolRequestDTO;

/**
 * @Author:吴金才
 * @Date:2024/2/5 16:41
 */
public interface ToolStrategy {
    String getToolCode();
    void handler(ToolRequestDTO toolRequest);
}
