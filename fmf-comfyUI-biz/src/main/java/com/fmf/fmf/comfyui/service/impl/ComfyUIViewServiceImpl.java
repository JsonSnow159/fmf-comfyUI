package com.fmf.fmf.comfyui.service.impl;

import com.fmf.fmf.comfyui.dto.HttpCallContext;
import com.fmf.fmf.comfyui.dto.HttpCallResponse;
import com.fmf.fmf.comfyui.dto.ViewQueueResponse;
import com.fmf.fmf.comfyui.enums.ComfyUIAPIEnum;
import com.fmf.fmf.comfyui.service.AbstractComfyUIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:吴金才
 * @Date:2024/1/30 16:21
 */
@Slf4j
@Service
public class ComfyUIViewServiceImpl extends AbstractComfyUIService<String, String> {
    public String viewQueue(String serviceAddress, String fileName, String type) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("filename", fileName);
        queryParams.put("type", type);
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://").append(serviceAddress).append(ComfyUIAPIEnum.VIEW.getPath());
        urlBuilder.append("?");
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                String encodedKey = URLEncoder.encode(key, "UTF-8");
                String encodedValue = URLEncoder.encode(value.toString(), "UTF-8");
                urlBuilder.append(encodedKey).append("=").append(encodedValue).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        urlBuilder.setLength(urlBuilder.length() - 1);
        return urlBuilder.toString();
    }

    @Override
    protected HttpCallContext<String> transform(String serviceAddress, String requestBody, Map<String, Object> queryString) {
        return super.buildHttpCallContext(serviceAddress, ComfyUIAPIEnum.VIEW, null, queryString);
    }

    @Override
    protected String aggregate(HttpCallResponse httpCallResponse) {
        return super.parseResponse(httpCallResponse);
    }
}
