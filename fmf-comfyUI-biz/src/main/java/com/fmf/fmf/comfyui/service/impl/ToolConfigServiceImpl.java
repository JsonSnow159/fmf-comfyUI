package com.fmf.fmf.comfyui.service.impl;

import com.fmf.fmf.comfyUI.dal.entity.ToolConfig;
import com.fmf.fmf.comfyUI.dal.mapper.ToolConfigMapper;
import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigInDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigOutDTO;
import com.fmf.fmf.comfyui.dto.tool.ToolConfigQueryInDTO;
import com.fmf.fmf.comfyui.service.ToolConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author:吴金才
 * @Date:2024/2/17 10:38
 */
@Service
@Slf4j
public class ToolConfigServiceImpl implements ToolConfigService {
    @Resource
    private ToolConfigMapper toolConfigMapper;

    @Override
    public boolean addToolConfig(ToolConfigInDTO inDTO) {
        ToolConfig existToolConfig = toolConfigMapper.findParam(inDTO.getToolCode(), inDTO.getVersion(), inDTO.getParamId());
        if(Objects.nonNull(existToolConfig)){
            return true;
        }
        ToolConfig toolConfig = new ToolConfig();
        toolConfig.setToolCode(inDTO.getToolCode());
        toolConfig.setVersion(inDTO.getVersion());
        toolConfig.setParamId(inDTO.getParamId());
        toolConfig.setParamName(inDTO.getParamName());
        toolConfig.setParamType(inDTO.getParamType());
        int insertResult = toolConfigMapper.insert(toolConfig);
        return insertResult > 0;
    }

    @Override
    public boolean updateToolConfig(ToolConfigInDTO inDTO) {
        ToolConfig toolConfig = new ToolConfig();
        toolConfig.setId(inDTO.getConfigId());
        toolConfig.setToolCode(inDTO.getToolCode());
        toolConfig.setVersion(inDTO.getVersion());
        toolConfig.setParamId(inDTO.getParamId());
        toolConfig.setParamName(inDTO.getParamName());
        toolConfig.setParamType(inDTO.getParamType());
        int updateResult = toolConfigMapper.update(toolConfig);
        return updateResult > 0;
    }


    @Override
    public PR findAll(ToolConfigQueryInDTO inDTO) {
        List<ToolConfig> toolConfigList = toolConfigMapper.findAll(inDTO.getToolCode(), inDTO.getVersion());
//        Page<ToolConfig> page = PageHelper.startPage(inDTO.getPage(), inDTO.getSize())
//                .doSelectPage(() -> toolMapper.findAll(inDTO.getToolCode(), inDTO.getVersion()));
//        List<ToolConfig> result = page.getResult();
        long total = toolConfigList.size();
        if (CollectionUtils.isEmpty(toolConfigList)) {
            return PR.ofEmpty(total);
        }
//        return null;
        List<ToolConfigOutDTO> toolConfigOutList = convertToolConfigList2ToolConfigQueryOutDTOList(toolConfigList);
        return PR.ok(toolConfigOutList, total);
    }

    @Override
    public ToolConfigOutDTO findDetail(ToolConfigInDTO inDTO) {
        ToolConfig toolConfig = toolConfigMapper.findToolConfig(inDTO.getConfigId());
        return convertToolConfig2ToolConfigOutDTO(toolConfig);
    }

    private List<ToolConfigOutDTO> convertToolConfigList2ToolConfigQueryOutDTOList(List<ToolConfig> toolConfigList) {
        List<ToolConfigOutDTO> toolQueryOutDTOList = new ArrayList<>();
        for (ToolConfig toolConfig : toolConfigList) {
            ToolConfigOutDTO toolConfigOutDTO = convertToolConfig2ToolConfigOutDTO(toolConfig);
            toolQueryOutDTOList.add(toolConfigOutDTO);
        }
        return toolQueryOutDTOList;
    }


    private ToolConfigOutDTO convertToolConfig2ToolConfigOutDTO(ToolConfig toolConfig) {
        ToolConfigOutDTO toolConfigOutDTO = new ToolConfigOutDTO();
        toolConfigOutDTO.setConfigId(toolConfig.getId());
        toolConfigOutDTO.setToolCode(toolConfig.getToolCode());
        toolConfigOutDTO.setVersion(toolConfig.getVersion());
        toolConfigOutDTO.setParamId(toolConfig.getParamId());
        toolConfigOutDTO.setParamName(toolConfig.getParamName());
        toolConfigOutDTO.setParamType(toolConfig.getParamType());
        return toolConfigOutDTO;
    }

    @Override
    public boolean deleteToolConfig(ToolConfigInDTO inDTO) {
        int delete = toolConfigMapper.delete(inDTO.getConfigId());
        return delete > 0;
    }
}
