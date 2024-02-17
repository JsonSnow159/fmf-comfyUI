package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "图片对象", description = "用户上传的图片信息")
public class Image extends BaseEntity{
    @ApiModelProperty(value = "图片上传路径")
    private String imageUrl;
    @ApiModelProperty(value = "用户id")
    private String uid;
}
