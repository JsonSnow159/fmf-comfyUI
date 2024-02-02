package com.fmf.fmf.comfyui.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fmf.fmf.comfyui.dto.HttpCallContext;
import com.fmf.fmf.comfyui.dto.HttpCallResponse;
import com.fmf.fmf.comfyui.enums.TimeoutLevelEnum;
import com.fmf.fmf.comfyui.exception.BizException;
import com.fmf.fmf.comfyui.exception.ErrorCodeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;


/**
 * @Author:吴金才
 * @Date:2024/2/2 11:20
 */

@Component
@Slf4j
public class HttpCallRestTemplate {
    private RestTemplate lookupRestTemplateByLevel(TimeoutLevelEnum timeoutLevelEnum){
        RestTemplate restTemplate;
        if(timeoutLevelEnum == null){
            timeoutLevelEnum = TimeoutLevelEnum.LEVEL_SLOW;
        }
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(5000);
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(3000);
        switch (timeoutLevelEnum) {
            case LEVEL_FAST:
                httpRequestFactory.setReadTimeout(1000);
                break;
            case LEVEL_MEDIUM:
                httpRequestFactory.setReadTimeout(3000);
                break;
            case LEVEL_SLOW:
                httpRequestFactory.setReadTimeout(5000);
                break;
            case LEVEL_SUPERSLOW:
                httpRequestFactory.setReadTimeout(10000);
                break;
            default:
                break;
        }
        log.info("Got restTemplate for timeout level:{}", timeoutLevelEnum.getDesc());

        restTemplate = new RestTemplate(httpRequestFactory);
        return restTemplate;
    }

    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public <RequestType> HttpCallResponse doInvoke(HttpCallContext<RequestType> requestContext) {
        return invoke(requestContext, Boolean.FALSE);
    }

    public <RequestType> HttpCallResponse doInvoke(HttpCallContext<RequestType> requestContext, Boolean notThrowError) {
        return invoke(requestContext, notThrowError);
    }

    @SneakyThrows
    private <RequestType> HttpCallResponse invoke(HttpCallContext<RequestType> requestContext, Boolean notThrowError) {
        //根据requestContext构建Uri
        URI uri = buildUri(requestContext);

        HttpMethod httpMethod = HttpMethod.GET;
        if(StringUtils.isNotEmpty(requestContext.getMethod())){
            httpMethod = HttpMethod.resolve(requestContext.getMethod());
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> headers = requestContext.getHeaders();
        if(headers != null && headers.size() > 0){
            for(String key : headers.keySet()) {
                httpHeaders.add(key, headers.get(key));
            }
        }

        RequestType requestBody = requestContext.getBody();
        HttpEntity<?> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        long startTime = System.currentTimeMillis();

        RestTemplate restTemplate = lookupRestTemplateByLevel(requestContext.getTimeoutLevel());

        ResponseEntity<String> responseEntity = null;
        Exception httpCallCaughtEx = null;
        try {
            //restTemplate 本身支持urlParams占位符 不需要做额外处理
            responseEntity = restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
        } catch (Exception e) {
            httpCallCaughtEx = e;
        }

        ErrorCodeEnum errorCode = null;
        String errorMessage = "";
        if(Objects.nonNull(httpCallCaughtEx)){
            errorCode = getErrorCodeFromException(httpCallCaughtEx);
            errorMessage = String.format(
                    "HttpCallRestTemplate request error, url:%s, method:%s, headers:%s, body:%s, message:%s, cost:%s",
                    uri.toString(), httpMethod.name(), JSON.toJSONString(headers), JSON.toJSONString(requestBody),
                    httpCallCaughtEx.getMessage(), System.currentTimeMillis() - startTime);
            log.error(errorMessage);
        } else {
            int statusCode = responseEntity.getStatusCodeValue();
            if (HttpStatus.Series.SUCCESSFUL != HttpStatus.Series.valueOf(statusCode)){
                errorCode = getErrorCodeByStatusCode(statusCode);
                errorMessage = String.format(
                        "HttpCallRestTemplate request failed with statusCode:%s, url:%s, headers:%s, body:%s, response body:%s, cost:%s",
                        statusCode, uri.toString(), JSONObject.toJSONString(headers), JSONObject.toJSONString(requestBody),
                        responseEntity.hasBody() ? responseEntity.getBody() : null, System.currentTimeMillis() - startTime);
                log.error(errorMessage);
            }
        }

        //处理异常错误码，根据规则抛出异常或者返回错误码
        if(Objects.nonNull(errorCode)){
            if (BooleanUtils.isTrue(notThrowError)){
                HttpCallResponse httpCallResponse = new HttpCallResponse();
                httpCallResponse.setStatusCode(errorCode.getCode());
                return httpCallResponse;
            } else {
                throw new BizException(errorCode.getCode(), errorMessage);
            }
        }

        int statusCode = responseEntity.getStatusCodeValue();
        String responseBody = responseEntity.hasBody() ? responseEntity.getBody() : null;
        String successMessage = String.format(
                "HttpCallRestTemplate request success, url:%s, headers:%s, body:%s, response body:%s, cost:%s",
                uri.toString(), JSON.toJSONString(headers), JSON.toJSONString(requestBody),
                responseBody != null ? responseBody.trim() : null, System.currentTimeMillis() - startTime);
        log.info(successMessage);

        HttpCallResponse httpCallResponse = new HttpCallResponse();
        httpCallResponse.setStatusCode(statusCode);
        httpCallResponse.setErrorMessage("success");
        httpCallResponse.setData(responseBody);

        return httpCallResponse;
    }


    private ErrorCodeEnum getErrorCodeFromException(Exception e) {
        if(e instanceof NoHttpResponseException){
            return ErrorCodeEnum.HTTP_CALL_NO_RESPONSE_ERROR;
        }
        if(e instanceof ResourceAccessException) {
            if (Objects.nonNull(e.getCause()) && e.getCause() instanceof SocketTimeoutException) {
                return ErrorCodeEnum.HTTP_CALL_TIMEOUT_ERROR;
            }
        }
        if(e instanceof SocketTimeoutException) {
            return ErrorCodeEnum.HTTP_CALL_TIMEOUT_ERROR;
        }
        if(e instanceof HttpClientErrorException){
            return ErrorCodeEnum.HTTP_CALL_4XX_ERROR;
        }
        if(e instanceof HttpServerErrorException){
            return ErrorCodeEnum.HTTP_CALL_5XX_ERROR;
        }
        return ErrorCodeEnum.HTTP_CALL_UNKNOWN_ERROR;
    }

    private ErrorCodeEnum getErrorCodeByStatusCode(int statusCode) {
        return ErrorCodeEnum.HTTP_CALL_UNKNOWN_ERROR;
    }

    /**
     * 根据RequestContext构建URI
     *
     * @param requestContext 请求上下文
     * @param <RequestType>
     * @return
     */
    private static <RequestType> URI buildUri(HttpCallContext<RequestType> requestContext) {
        String url = requestContext.getUrl();
        if(StringUtils.isNotEmpty(requestContext.getHost())){
            url = requestContext.getHost() + url;
        }
        URI uri;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, Object> queryString = requestContext.getQueryString();
        if (!CollectionUtils.isEmpty(queryString)) {
            for (String key : queryString.keySet()) {
                uriComponentsBuilder.queryParam(key, queryString.get(key));
            }
        }
        if (!CollectionUtils.isEmpty(requestContext.getUrlParams())) {
            uri = uriComponentsBuilder.build(BooleanUtils.isTrue(requestContext.getEncoded())).expand(requestContext.getUrlParams()).toUri();
        } else {
            uri = uriComponentsBuilder.build(BooleanUtils.isTrue(requestContext.getEncoded())).toUri();
        }
        return uri;
    }
}

