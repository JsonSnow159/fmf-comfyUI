package com.fmf.fmf.comfyui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fmf.fmf.comfyui.common.API;
import com.fmf.fmf.comfyui.common.HttpCallRestTemplate;
import com.fmf.fmf.comfyui.dto.HttpCallContext;
import com.fmf.fmf.comfyui.dto.HttpCallResponse;
import com.fmf.fmf.comfyui.enums.TimeoutLevelEnum;
import com.fmf.fmf.comfyui.exception.BizException;
import com.fmf.fmf.comfyui.exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpMethod;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:吴金才
 * @Date:2024/2/4 11:48
 */
@Slf4j
public abstract class AbstractComfyUIService<TripartiteParam, TripartiteResponse> {
    @Resource
    private HttpCallRestTemplate httpCallRestTemplate;

    protected abstract HttpCallContext<String> transform(String serviceAddress, String requestBody, Map<String, Object> queryString);

    protected abstract TripartiteResponse aggregate(HttpCallResponse httpCallResponse);

    public TripartiteResponse doInvoke(String serviceAddress, String requestBody, Map<String, Object> queryString) {
        long st = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        log.info("开始执行三方模板服务类,id:{},tripartiteParam: {}", uuid, JSONObject.toJSONString(requestBody));
        HttpCallContext<String> callContext = transform(serviceAddress, requestBody, queryString);
        HttpCallResponse response = httpCallRestTemplate.doInvoke(callContext);
        TripartiteResponse aggregate = aggregate(response);
        log.info("结束执行三方模板服务类,id:{}, entity:{} ,耗时:{}", uuid, JSONObject.toJSONString(aggregate), (System.currentTimeMillis() - st));
        return aggregate;
    }

    public TripartiteResponse parseResponse(HttpCallResponse result, Class<TripartiteResponse> tClass) {
        if (HttpStatus.SC_OK != result.getStatusCode()) {
            log.error("三方HTTP请求异常：{}", result.getErrorMessage());
            throw new BizException(ErrorCodeEnum.HTTP_CALL_5XX_ERROR.getCode(), ErrorCodeEnum.HTTP_CALL_5XX_ERROR.getMsg());
        }
        String httpData = result.getData();
        return JSON.parseObject(httpData, tClass);
    }

    public String parseResponse(HttpCallResponse result) {
        if (HttpStatus.SC_OK != result.getStatusCode()) {
            log.error("三方HTTP请求异常：{}", result.getErrorMessage());
            throw new BizException(ErrorCodeEnum.HTTP_CALL_5XX_ERROR.getCode(), ErrorCodeEnum.HTTP_CALL_5XX_ERROR.getMsg());
        }
        return result.getData();
    }

    public HttpCallContext<String> buildHttpCallContext(String serviceAddress, API api, String requestBody, Map<String, Object> queryString) {
        HttpCallContext<String> httpCallContext = new HttpCallContext();
        String tripartiteUrl = "http://" + serviceAddress + api.getPath();
        httpCallContext.setUrl(tripartiteUrl);
        httpCallContext.setQueryString(queryString);
        httpCallContext.setMethod(api.getMethod());
        httpCallContext.setBody(requestBody);
        Map<String, String> headers = new HashMap<>();
        httpCallContext.setHeaders(headers);
        httpCallContext.setTimeoutLevel(TimeoutLevelEnum.LEVEL_MEDIUM);
        return httpCallContext;
    }
}
