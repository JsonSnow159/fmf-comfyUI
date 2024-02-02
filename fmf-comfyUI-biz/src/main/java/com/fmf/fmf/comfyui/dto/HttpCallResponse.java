package com.fmf.fmf.comfyui.dto;

import lombok.Data;

/**
 * @Author:吴金才
 * @Date:2024/2/2 11:32
 */
@Data
public class HttpCallResponse{

    /**
     * http状态码
     */
    private Integer statusCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 返回结果对象
     */
    private String data;
}
