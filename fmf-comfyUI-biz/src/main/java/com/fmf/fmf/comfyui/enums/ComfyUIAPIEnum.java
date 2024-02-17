package com.fmf.fmf.comfyui.enums;

import com.fmf.fmf.comfyui.common.API;
import lombok.Data;

/**
 * @Author:吴金才
 * @Date:2024/2/4 12:27
 */
@Data
public class ComfyUIAPIEnum {
    // 请求方式
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";


    /**
     * 任务发放
     */
    public static API PROMPT = new API(POST, "/prompt", "");
    /**
     * 查询当前执行任务
     */
    public static API QUEUE = new API(GET, "/queue", "");
    /**
     * 查看历史任务
     */
    public static API HISTORY = new API(GET, "/history", "");
    /**
     * 预览图片
     */
    public static API VIEW = new API(GET, "/view", "");
    /**
     * 上传input图片
     */
    public static API UPLOAD_IMAGE = new API(POST, "/upload/image", "");
}
