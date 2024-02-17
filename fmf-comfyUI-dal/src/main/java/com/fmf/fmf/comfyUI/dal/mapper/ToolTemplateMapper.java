package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.Tool;
import com.fmf.fmf.comfyUI.dal.entity.ToolTemplate;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

/**
 * @Author:吴金才
 * @Date:2024/2/1 15:12
 */
public interface ToolTemplateMapper {
    ToolTemplate findByTool(@Param("toolCode") String toolCode, @Param("version") String version);
    int insert(ToolTemplate toolTemplate);
    int update(ToolTemplateMapper toolTemplateMapper);
}
