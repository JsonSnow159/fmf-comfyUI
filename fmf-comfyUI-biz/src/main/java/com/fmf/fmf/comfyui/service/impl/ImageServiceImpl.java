package com.fmf.fmf.comfyui.service.impl;

import com.fmf.fmf.comfyUI.dal.entity.Image;
import com.fmf.fmf.comfyUI.dal.entity.ToolInfo;
import com.fmf.fmf.comfyUI.dal.mapper.ImageMapper;
import com.fmf.fmf.comfyUI.dal.mapper.ToolMapper;
import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.common.PageSearchDTO;
import com.fmf.fmf.comfyui.dto.tool.ImageQueryInDTO;
import com.fmf.fmf.comfyui.dto.tool.ImageQueryOutDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolQueryOutDTO;
import com.fmf.fmf.comfyui.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/17 20:25
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageMapper imageMapper;

    @Override
    public PR findAll(ImageQueryInDTO inDTO) {
        List<Image> imageList = imageMapper.findAll(inDTO.getUid());
//        Page<Tool> page = PageHelper.startPage(inDTO.getPage(), inDTO.getSize())
//                .doSelectPage(() -> toolMapper.findAll1());
//        List<Tool> result = page.getResult();
        long total = imageList.size();
        if (CollectionUtils.isEmpty(imageList)) {
            return PR.ofEmpty(total);
        }
//        return null;
        List<ImageQueryOutDTO> dtOs = convertImageList2ImageQueryOutDTOList(imageList);
        return PR.ok(dtOs, total);
    }

    private List<ImageQueryOutDTO> convertImageList2ImageQueryOutDTOList(List<Image> imageList) {
        List<ImageQueryOutDTO> imageQueryOutDTOList = new ArrayList<>();
        for (Image image : imageList) {
            ImageQueryOutDTO imageQueryOutDTO = convertImage2ImageQueryOutDTO(image);
            imageQueryOutDTOList.add(imageQueryOutDTO);
        }
        return imageQueryOutDTOList;
    }

    private ImageQueryOutDTO convertImage2ImageQueryOutDTO(Image image) {
        ImageQueryOutDTO imageQueryOutDTO = ImageQueryOutDTO.builder()
                .imageId(image.getId())
                .uid(image.getUid())
                .imageUrl(image.getImageUrl())
                .build();
        return imageQueryOutDTO;
    }
}
