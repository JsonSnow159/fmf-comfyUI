package com.fmf.fmf.comfyui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author:吴金才
 * @Date:2024/2/5 17:02
 */
@Getter
@AllArgsConstructor
public enum ToolEnum {
    TEXT_GENERATE_IMAGE("textGenerateImage", "文生图"),
    RETOUCH_IMAGE("retouchImage","修图");

    private String code;
    private String desc;

    /**
     * 根据code获取枚举
     * @param code
     * @return
     */
    public static ToolEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (ToolEnum tool : ToolEnum.values()) {
            if (tool.getCode().equals(code)) {
                return tool;
            }
        }
        return null;
    }
}
