package com.fmf.fmf.comfyui.service.impl;

import com.fmf.fmf.comfyui.dto.HttpCallContext;
import com.fmf.fmf.comfyui.dto.HttpCallResponse;
import com.fmf.fmf.comfyui.dto.QueuePromptResponse;
import com.fmf.fmf.comfyui.dto.ViewQueueResponse;
import com.fmf.fmf.comfyui.enums.ComfyUIAPIEnum;
import com.fmf.fmf.comfyui.service.AbstractComfyUIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author:吴金才
 * @Date:2024/1/30 16:21
 */
@Slf4j
@Service
public class ComfyUIQueueServiceImpl extends AbstractComfyUIService<String, ViewQueueResponse> {
    public ViewQueueResponse viewQueue(String serviceAddress) {
        return super.doInvoke(serviceAddress, null, null);
    }

    @Override
    public HttpCallContext<String> transform(String serviceAddress, String requestBody, Map<String, Object> queryString) {
        return super.buildHttpCallContext(serviceAddress, ComfyUIAPIEnum.QUEUE, requestBody, queryString);
    }

    @Override
    protected ViewQueueResponse aggregate(HttpCallResponse httpCallResponse) {
        ViewQueueResponse promptResponse = super.parseResponse(httpCallResponse, ViewQueueResponse.class);
        return promptResponse;
    }
}
