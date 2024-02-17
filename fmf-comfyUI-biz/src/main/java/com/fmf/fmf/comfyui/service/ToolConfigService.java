package com.fmf.fmf.comfyui.service;

import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigOutDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigQueryInDTO;

/**
 * @Author:吴金才
 * @Date:2024/2/7 13:41
 */
public interface ToolConfigService {
    boolean addToolConfig(ToolConfigInDTO inDTO);
    boolean updateToolConfig(ToolConfigInDTO inDTO);
    PR findAll(ToolConfigQueryInDTO inDTO);
    ToolConfigOutDTO findDetail(ToolConfigInDTO inDTO);
    boolean deleteToolConfig(ToolConfigInDTO inDTO);
}
