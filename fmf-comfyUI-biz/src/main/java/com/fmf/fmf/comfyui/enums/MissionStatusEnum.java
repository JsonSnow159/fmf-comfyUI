package com.fmf.fmf.comfyui.enums;

import lombok.Getter;

@Getter
public enum MissionStatusEnum {
    INIT(0, "初始化"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败"),
    ;

    private Integer code;
    private String msg;

    MissionStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
