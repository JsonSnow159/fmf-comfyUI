package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.Mission;
import com.fmf.fmf.comfyUI.dal.entity.MissionOutput;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 14:24
 */
public interface MissionOutputMapper {
    List<MissionOutput> findAll();
    List<MissionOutput> findByMissionId(String missionId);
    int insert(MissionOutput missionOutput);
    int update(MissionOutput missionOutput);
}
