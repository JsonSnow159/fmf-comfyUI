package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "任务对象", description = "任务维护")
public class Mission extends BaseEntity{
    @ApiModelProperty(value = "任务id")
    private String missionId;
    @ApiModelProperty(value = "用户id")
    private String uid;
    @ApiModelProperty(value = "comfyUI图片任务Id")
    private String promptId;
    @ApiModelProperty(value = "用户录入的请求参数")
    private String promptRequest;
    @ApiModelProperty(value = "本次任务请求的comfyUI服务器ip")
    private String cloudMachineIp;
    @ApiModelProperty(value = "本次任务请求的comfyUI服务器port")
    private String cloudMachinePort;
    @ApiModelProperty(value = "任务状态0 初始化, 1 执行中,2 成功, 3 失败")
    private Integer status;
    @ApiModelProperty(value = "生成的图片url列表")
    private String images;
    @ApiModelProperty(value = "错误信息")
    private String errorData;
}
