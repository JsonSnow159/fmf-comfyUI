package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.ToolConfig;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 15:01
 */
public interface ToolConfigMapper {
    List<ToolConfig> findAll(String toolCode, Integer version);
    ToolConfig findParam(String toolCode, Integer version, String paramId);
    ToolConfig findToolConfig(Long id);

    int insert(ToolConfig toolConfig);

    int update(ToolConfig toolConfig);

    int delete(Long id);


}
