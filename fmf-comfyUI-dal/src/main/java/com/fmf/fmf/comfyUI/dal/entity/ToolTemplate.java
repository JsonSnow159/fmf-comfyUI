package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "工具模版对象", description = "工具模版维护")
public class ToolTemplate extends BaseEntity{
    @ApiModelProperty(value = "工具类型")
    private String toolType;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "模版内容")
    private String templateContent;
}
