package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "工具对象", description = "工具类型维护")
public class Tool extends BaseEntity{
    @ApiModelProperty(value = "工具类型")
    private Integer toolType;
    @ApiModelProperty(value = "工具名称")
    private String toolName;
    @ApiModelProperty(value = "版本")
    private String version;
}
