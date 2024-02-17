package com.fmf.fmf.comfyui.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/6 16:28
 */
@NoArgsConstructor
@Data
public class ImageUploadResponse {
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "subfolder")
    private String subfolder;
    @JSONField(name = "type")
    private String type;
}
