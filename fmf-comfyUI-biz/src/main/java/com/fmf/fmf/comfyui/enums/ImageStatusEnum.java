package com.fmf.fmf.comfyui.enums;

import lombok.Getter;

@Getter
public enum ImageStatusEnum {
    INIT(0, "初始化"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    ;

    private Integer status;
    private String msg;

    ImageStatusEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
