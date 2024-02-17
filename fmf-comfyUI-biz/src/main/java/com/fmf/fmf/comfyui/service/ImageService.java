package com.fmf.fmf.comfyui.service;

import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.common.PageSearchDTO;
import com.fmf.fmf.comfyui.dto.tool.ImageQueryInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolAddInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolQueryInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolQueryOutDTO;

/**
 * @Author:吴金才
 * @Date:2024/2/7 13:41
 */
public interface ImageService {
    PR findAll(ImageQueryInDTO inDTO);
}
