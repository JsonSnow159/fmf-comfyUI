package com.fmf.fmf.comfyui.service.impl;

import com.fmf.fmf.comfyUI.dal.entity.CloudMachine;
import com.fmf.fmf.comfyUI.dal.mapper.CloudMachineMapper;
import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.common.PageSearchDTO;
import com.fmf.fmf.comfyui.dto.tool.CloudMachineInDTO;
import com.fmf.fmf.comfyui.dto.tool.CloudMachineOutDTO;
import com.fmf.fmf.comfyui.service.CloudMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/17 16:42
 */
@Service
@Slf4j
public class CloudMachineServiceImpl implements CloudMachineService {
    @Resource
    private CloudMachineMapper cloudMachineMapper;

    @Override
    public boolean add(CloudMachineInDTO inDTO) {
        CloudMachine cloudMachine = new CloudMachine();
        cloudMachine.setArea(inDTO.getArea());
        cloudMachine.setIp(inDTO.getIp());
        cloudMachine.setPort(inDTO.getPort());
        cloudMachine.setStatus(inDTO.getStatus());
        int insertResult = cloudMachineMapper.insert(cloudMachine);
        return insertResult > 0;
    }

    @Override
    public boolean update(CloudMachineInDTO inDTO) {
        CloudMachine cloudMachine = new CloudMachine();
        cloudMachine.setId(inDTO.getCloudMachineId());
        cloudMachine.setArea(inDTO.getArea());
        cloudMachine.setIp(inDTO.getIp());
        cloudMachine.setPort(inDTO.getPort());
        cloudMachine.setStatus(inDTO.getStatus());
        int updateResult = cloudMachineMapper.update(cloudMachine);
        return updateResult > 0;
    }

    @Override
    public PR findAll(PageSearchDTO inDTO) {
        long total = cloudMachineMapper.count();
        List<CloudMachine> cloudMachineList = cloudMachineMapper.findByPage(inDTO.getOffset(),inDTO.getSize());
        if (CollectionUtils.isEmpty(cloudMachineList)) {
            return PR.ofEmpty(total);
        }
        List<CloudMachineOutDTO> dtOs = convertCloudMachine2CloudMachineOutDTOList(cloudMachineList);
        return PR.ok(dtOs, total);
    }

    private List<CloudMachineOutDTO> convertCloudMachine2CloudMachineOutDTOList(List<CloudMachine> cloudMachineList) {
        List<CloudMachineOutDTO> cloudMachineOutDTOS = new ArrayList<>();
        for (CloudMachine cloudMachine : cloudMachineList) {
            CloudMachineOutDTO cloudMachineOutDTO = convertCloudMachine2CloudMachineOutDTO(cloudMachine);
            cloudMachineOutDTOS.add(cloudMachineOutDTO);
        }
        return cloudMachineOutDTOS;
    }

    private CloudMachineOutDTO convertCloudMachine2CloudMachineOutDTO(CloudMachine cloudMachine) {
        CloudMachineOutDTO cloudMachineOutDTO = new CloudMachineOutDTO();
        cloudMachineOutDTO.setCloudMachineId(cloudMachine.getId());
        cloudMachineOutDTO.setArea(cloudMachine.getArea());
        cloudMachineOutDTO.setIp(cloudMachine.getIp());
        cloudMachineOutDTO.setPort(cloudMachine.getPort());
        cloudMachineOutDTO.setStatus(cloudMachine.getStatus());
        return cloudMachineOutDTO;
    }

    @Override
    public CloudMachineOutDTO findDetail(Long id) {
        CloudMachine cloudMachine = cloudMachineMapper.findDetail(id);
        return convertCloudMachine2CloudMachineOutDTO(cloudMachine);
    }

    @Override
    public boolean delete(Long id) {
        int deleteResult = cloudMachineMapper.delete(id);
        return deleteResult > 0;
    }
}
