package com.fmf.fmf.comfyUI.dal.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "任务输出对象", description = "任务输出信息")
public class MissionOutput extends BaseEntity{
    @ApiModelProperty(value = "任务id")
    private String missionId;
    @ApiModelProperty(value = "图片数据")
    private String imageData;
    @ApiModelProperty(value = "图片url")
    private String imageUrl;
    @ApiModelProperty(value = "图片上传状态 0 待上传,1 成功,2 失败")
    private String status;
    @ApiModelProperty(value = "失败原因")
    private String failReason;
}
