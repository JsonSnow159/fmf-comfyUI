package com.fmf.fmf.comfyui.dto.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/7 12:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolAddInDTO {
    /**
     * 工具名称
     */
    private String toolName;
    /**
     * 工具编码
     */
    private String toolCode;
    /**
     * 工具版本
     */
    private Integer version;
    /**
     * 排序顺序
     */
    private Integer sort;
    /**
     * 工具logo
     */
    private String logo;
    /**
     * 是否启用
     */
    private Integer enable;
    /**
     * 工具描述
     */
    private String description;
    /**
     * 模版内容
     */
    private String templateContent;
}
