package com.fmf.fmf.comfyui.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/4 14:43
 */
@NoArgsConstructor
@Data
public class ViewQueueResponse {
    @JSONField(name = "queue_running")
    private List<List<Object>> queueRunning;
    @JSONField(name = "queue_pending")
    private List<List<Object>> queuePending;
}
