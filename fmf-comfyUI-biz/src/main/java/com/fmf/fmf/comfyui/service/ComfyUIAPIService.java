package com.fmf.fmf.comfyui.service;

import com.fmf.fmf.comfyui.dto.PromptRequest;
import com.fmf.fmf.comfyui.dto.QueuePromptResponse;
import org.springframework.stereotype.Service;

/**
 * @Author:吴金才
 * @Date:2024/1/30 16:13
 */
@Service
public interface ComfyUIAPIService {
    QueuePromptResponse prompt(String serviceAddress,String prompt);
}
