package com.fmf.fmf.comfyui.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/1/30 16:51
 */
@NoArgsConstructor
@Data
public class PromptResponse {

    @JSONField(name = "type")
    private String type;
    @JSONField(name = "data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "status")
        private StatusDTO status;

        @NoArgsConstructor
        @Data
        public static class StatusDTO {
            @JSONField(name = "exec_info")
            private ExecInfoDTO execInfo;

            @NoArgsConstructor
            @Data
            public static class ExecInfoDTO {
                @JSONField(name = "queue_remaining")
                private Integer queueRemaining;
            }
        }
    }
}
