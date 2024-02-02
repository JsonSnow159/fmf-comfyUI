package com.fmf.fmf.comfyui.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:吴金才
 * @Date:2024/2/2 11:24
 */
@AllArgsConstructor
@Getter
public enum TimeoutLevelEnum {
    LEVEL_FAST("fast", "1s超时"),
    LEVEL_MEDIUM("medium","3s超时"),
    LEVEL_SLOW("slow","5s超时"),
    LEVEL_SUPERSLOW("superslow","10s超时"),
    LEVEL_LEGACY("legacy","兼容历史实现");

    private String name;

    private String desc;

    public static TimeoutLevelEnum valueOfName(String levelName){
        TimeoutLevelEnum typeEnum = LEVEL_LEGACY;
        for(TimeoutLevelEnum level: TimeoutLevelEnum.values()){
            if(level.name.equalsIgnoreCase(levelName)){
                typeEnum = level;
            }
        }
        return typeEnum;
    }
}
