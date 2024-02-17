package com.fmf.fmf.comfyui.dto.tool;

import com.fmf.fmf.comfyui.common.PageSearchDTO;
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
public class ImageQueryInDTO extends PageSearchDTO {
    /**
     * 用户id
     */
    private String uid;
}
