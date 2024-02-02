package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "云服务器对象", description = "云服务器信息")
public class CloudMachine extends BaseEntity {
    @ApiModelProperty(value = "服务器所在地域")
    private String area;
    @ApiModelProperty(value = "服务器ip")
    private String ip;
    @ApiModelProperty(value = "服务器端口")
    private String port;
    @ApiModelProperty(value = "机器状态0-不可用，1-可用")
    private Integer status;
}
