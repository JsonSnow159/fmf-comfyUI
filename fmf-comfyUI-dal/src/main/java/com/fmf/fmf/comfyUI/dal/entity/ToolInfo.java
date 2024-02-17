package com.fmf.fmf.comfyUI.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/7 16:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolInfo extends BaseEntity{
    private String toolCode;
    private String toolName;
    private Integer version;
    private Integer sort;
    private Integer isEnable;
    private String logo;
    private String description;
    private String templateContent;
}
