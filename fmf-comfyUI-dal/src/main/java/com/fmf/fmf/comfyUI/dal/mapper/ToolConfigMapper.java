package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.ToolConfig;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 15:01
 */
public interface ToolConfigMapper {
    List<ToolConfig> findByTool(String toolType, String version);

    int insert(ToolConfig toolConfig);

    int update(ToolConfig toolConfig);
}
