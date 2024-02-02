package com.fmf.fmf.comfyui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolConfigDTO {
    private String classId;
    private String classType;
    private String paramName;
    private String paramType;
    private String paramValue;
}
