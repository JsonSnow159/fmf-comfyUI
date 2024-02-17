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
public class ToolConfigInDTO {
    /**
     * 配置id
     */
    private Long configId;
    /**
     * 工具编码
     */
    private String toolCode;
    /**
     * 工具版本
     */
    private Integer version;
    /**
     * 参数id
     */
    private String paramId;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数类型
     */
    private String paramType;
}
