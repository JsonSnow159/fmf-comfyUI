package com.fmf.fmf.comfyui.web.tsp;

import com.fmf.fmf.comfyui.common.R;
import com.fmf.fmf.comfyui.service.UploadImgService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:01
 */
@RestController
@RequestMapping("/tsp")
public class TspController {

    @Resource
    private UploadImgService uploadImgService;

    @RequestMapping("/uploadImg")
    //用户填入参数，寻找模版
    public R generatePhoto(){
        uploadImgService.uploadImg();
        return R.ok();
    }
}
