package com.fmf.fmf.comfyui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolRequestDTO {
    private String uid;
    private String toolCode;
    private String version;
    private List<ToolConfigDTO> toolConfigList;
}
