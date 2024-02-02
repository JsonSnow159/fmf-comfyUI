package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.MissionOutput;
import com.fmf.fmf.comfyUI.dal.entity.Tool;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 14:41
 */

public interface ToolMapper {
    List<Tool> findAll();
    int insert(MissionOutput missionOutput);
    int update(String imageUrl,Integer status, String failReason);
}
