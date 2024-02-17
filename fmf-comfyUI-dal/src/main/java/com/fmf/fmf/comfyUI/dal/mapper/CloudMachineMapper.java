package com.fmf.fmf.comfyUI.dal.mapper;

import com.fmf.fmf.comfyUI.dal.entity.CloudMachine;

import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/1 11:45
 */

public interface CloudMachineMapper {

    List<CloudMachine> findAll();
    CloudMachine findDetail(Long id);
    int insert(CloudMachine cloudMachine);
    int update(CloudMachine cloudMachine);
    int delete(Long id);
}
