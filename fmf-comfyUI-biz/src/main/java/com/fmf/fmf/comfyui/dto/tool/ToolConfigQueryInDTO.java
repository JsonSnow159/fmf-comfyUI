package com.fmf.fmf.comfyui.dto.tool;

import com.fmf.fmf.comfyui.common.PageSearchDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/17 12:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolConfigQueryInDTO extends PageSearchDTO {
    /**
     * 工具编码
     */
    private String toolCode;
    /**
     * 工具版本
     */
    private Integer version;
}
