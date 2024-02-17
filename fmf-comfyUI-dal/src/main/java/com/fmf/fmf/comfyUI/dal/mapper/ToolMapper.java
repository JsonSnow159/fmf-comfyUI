package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.MissionOutput;
import com.fmf.fmf.comfyUI.dal.entity.Tool;
import com.fmf.fmf.comfyUI.dal.entity.ToolInfo;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 14:41
 */

public interface ToolMapper {
    Tool findByToolCode(String toolCode, Integer version);
    List<ToolInfo> findAll();
    List<Tool> findEnableTool();
    List<Tool> findAll1();
    ToolInfo findDetail(String toolCode, Integer version);
    int insert(Tool tool);
    int update(Tool tool);
    int delete(String toolCode, Integer version);
}
