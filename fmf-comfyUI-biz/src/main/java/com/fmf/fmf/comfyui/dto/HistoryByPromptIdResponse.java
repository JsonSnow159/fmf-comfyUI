package com.fmf.fmf.comfyui.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/4 15:09
 */
@NoArgsConstructor
@Data
public class HistoryByPromptIdResponse {
    @JSONField(name = "prompt")
    private List<Object> prompt;
    @JSONField(name = "outputs")
    private OutputsDTO outputs;
    @JSONField(name = "status")
    private StatusDTO status;

    @NoArgsConstructor
    @Data
    public static class OutputsDTO {
        @JSONField(name = "9")
        private _$9DTO nine;

        @NoArgsConstructor
        @Data
        public static class _$9DTO {
            @JSONField(name = "images")
            private List<ImagesDTO> images;

            @NoArgsConstructor
            @Data
            public static class ImagesDTO {
                @JSONField(name = "filename")
                private String filename;
                @JSONField(name = "subfolder")
                private String subfolder;
                @JSONField(name = "type")
                private String type;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class StatusDTO {
        @JSONField(name = "status_str")
        private String statusStr;
        @JSONField(name = "completed")
        private Boolean completed;
        @JSONField(name = "messages")
        private List<List<Object>> messages;
    }
}
