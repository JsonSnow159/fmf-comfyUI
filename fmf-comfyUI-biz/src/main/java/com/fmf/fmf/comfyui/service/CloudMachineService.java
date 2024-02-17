package com.fmf.fmf.comfyui.service;

import com.fmf.fmf.comfyui.common.PR;
import com.fmf.fmf.comfyui.common.PageSearchDTO;
import com.fmf.fmf.comfyui.dto.tool.*;

/**
 * @Author:吴金才
 * @Date:2024/2/7 13:41
 */
public interface CloudMachineService {
    boolean add(CloudMachineInDTO inDTO);
    boolean update(CloudMachineInDTO inDTO);
    PR findAll(PageSearchDTO pageSearchDTO);
    CloudMachineOutDTO findDetail(Long id);
    boolean delete(Long id);
}
