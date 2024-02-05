package com.fmf.fmf.comfyui.common;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: API定义
 * @Author: ludan
 * @Date: 2023/2/13
 **/
@Data
@Builder
public class API {
    private String method;
    private String path;
    private String version;

    public API(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }
}
