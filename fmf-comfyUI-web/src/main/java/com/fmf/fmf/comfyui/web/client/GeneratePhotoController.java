package com.fmf.fmf.comfyui.web.client;

import com.fmf.fmf.comfyui.common.R;
import com.fmf.fmf.comfyui.dto.GeneratePhotoRequestDTO;
import com.fmf.fmf.comfyui.service.GeneratePhotoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:01
 */
@RestController
@RequestMapping("/client")
public class GeneratePhotoController {

    @Resource
    private GeneratePhotoService generatePhotoService;

    @RequestMapping("/generate")
    //用户填入参数，寻找模版
    public R generatePhoto(@RequestBody GeneratePhotoRequestDTO generatePhotoRequest){
        generatePhotoService.generatePhoto(generatePhotoRequest);
        return R.ok();
    }
}
