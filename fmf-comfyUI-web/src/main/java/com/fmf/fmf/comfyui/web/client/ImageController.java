package com.fmf.fmf.comfyui.web.client;

import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.dto.tool.ImageQueryInDTO;
import com.fmf.fmf.comfyui.dto.tool.ImageQueryOutDTO;
import com.fmf.fmf.comfyui.service.ImageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author:吴金才
 * @Date:2024/2/17 20:22
 */
@RestController
@RequestMapping("/client/img")
public class ImageController {
    @Resource
    private ImageService imageService;

    @PostMapping("/queryImg")
    public PR queryAllImg(@RequestBody  ImageQueryInDTO inDTO) {
        PR resp = imageService.findAll(inDTO);
        return resp;
    }
}
