package com.fmf.fmf.comfyui.dto.tool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author:吴金才
 * @Date:2024/2/7 16:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageQueryOutDTO implements Serializable {
    /**
     * 图片id
     */
    private Long imageId;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 图片路径
     */
    private String imageUrl;
}
