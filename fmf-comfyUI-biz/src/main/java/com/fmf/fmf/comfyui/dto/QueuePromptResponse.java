package com.fmf.fmf.comfyui.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/1 17:33
 */
@NoArgsConstructor
@Data
public class QueuePromptResponse {
    @JSONField(name = "prompt_id")
    private String promptId;
    @JSONField(name = "number")
    private Integer number;
    @JSONField(name = "node_errors")
    private NodeErrorsDTO nodeErrors;

    @NoArgsConstructor
    @Data
    public static class NodeErrorsDTO {
    }
}
