package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:14
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "工具对象", description = "工具类型维护")
public class Tool extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "工具类型")
    private String toolCode;
    @ApiModelProperty(value = "工具名称")
    private String toolName;
    @ApiModelProperty(value = "版本")
    private Integer version;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "是否启用，0未启用，1启用")
    private Integer isEnable;
    @ApiModelProperty(value = "logo")
    private String logo;
    @ApiModelProperty(value = "工具描述")
    private String description;
}
