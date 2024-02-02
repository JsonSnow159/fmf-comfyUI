package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.Image;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 15:19
 */
public interface ImageMapper {
    List<Image> findAll(String uid);
    int insert(Image image);
}
