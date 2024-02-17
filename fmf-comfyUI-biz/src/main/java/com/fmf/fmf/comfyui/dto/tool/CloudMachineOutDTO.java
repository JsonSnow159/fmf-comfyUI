package com.fmf.fmf.comfyui.dto.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/7 12:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudMachineOutDTO {

    private Long cloudMachineId;
    /**
     * 机器所在区域
     */
    private String area;
    /**
     * 机器ip
     */
    private String ip;
    /**
     * 机器端口
     */
    private String port;
    /**
     * 机器状态0-不可用，1-可用
     */
    private Integer status;
}
