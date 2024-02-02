package com.fmf.fmf.comfyui.exception;

import lombok.Getter;

@Getter
public enum CloudMachineStatusEnum {
    ENABLE(1, "可用"),
    DISABLE(2, "不可用"),
    ;

    private Integer code;
    private String msg;

    CloudMachineStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
