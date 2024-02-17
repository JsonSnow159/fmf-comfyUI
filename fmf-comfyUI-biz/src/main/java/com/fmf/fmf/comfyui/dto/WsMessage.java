package com.fmf.fmf.comfyui.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:吴金才
 * @Date:2024/2/6 11:05
 */
@NoArgsConstructor
@Data
public class WsMessage {

    @JSONField(name = "type")
    private String type;
    @JSONField(name = "data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JSONField(name = "status")
        private StatusDTO status;
        @JSONField(name = "sid")
        private String sid;

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
