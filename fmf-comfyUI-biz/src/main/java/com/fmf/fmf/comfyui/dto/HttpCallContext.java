package com.fmf.fmf.comfyui.dto;

import com.fmf.fmf.comfyui.enums.TimeoutLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author:吴金才
 * @Date:2024/2/2 11:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpCallContext<T> {

    /**
     * 三方请求地址域名
     */
    private String host;

    /**
     * 接口url
     */
    private String url;

    /**
     * 请求method， 如GET、POST
     */
    private String method;

    /**
     * 请求url中的path变量参数
     */
    private Map<String, Object> urlParams;

    /**
     * 请求的queryString
     */
    private Map<String, Object> queryString;

    /**
     * 请求header头
     */
    private Map<String, String> headers;

    /**
     * 超时设置等级
     */
    private TimeoutLevelEnum timeoutLevel;

    /**
     * 是否已进行了encode
     */
    private Boolean encoded;

    /**
     * 请求payload对象
     */
    private T body;
}
