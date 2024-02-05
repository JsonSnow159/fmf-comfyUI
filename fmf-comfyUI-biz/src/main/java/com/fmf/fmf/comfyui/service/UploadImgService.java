package com.fmf.fmf.comfyui.service;

import cn.hutool.core.io.IoUtil;
import com.fmf.fmf.comfyUI.dal.entity.Mission;
import com.fmf.fmf.comfyUI.dal.entity.MissionOutput;
import com.fmf.fmf.comfyUI.dal.mapper.MissionMapper;
import com.fmf.fmf.comfyUI.dal.mapper.MissionOutputMapper;
import com.fmf.fmf.comfyui.dto.ViewQueueResponse;
import com.fmf.fmf.comfyui.service.impl.ComfyUIViewServiceImpl;
import com.fmf.fmf.comfyui.utils.MinioUtil;
import jodd.introspector.Mapper;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author:吴金才
 * @Date:2024/2/4 15:50
 */
@Service
public class UploadImgService {
    @Resource
    private MissionOutputMapper missionOutputMapper;
    @Resource
    private MissionMapper missionMapper;
    @Resource
    private ComfyUIViewServiceImpl comfyUIViewService;
    @Resource
    private MinioUtil minioUtil;
    @Value("${minio.comfyui.publicEndpoint}")
    private String publicEndpoint;
    @Value("${minio.comfyui.bucket}")
    private String bucketName;
    public void uploadImg() {
        List<MissionOutput> missionOutputs = missionOutputMapper.findAll();
        for (MissionOutput missionOutput : missionOutputs) {
            try {
                String missionId = missionOutput.getMissionId();
                Mission mission = missionMapper.findByMissionId(missionId);
                String cloudMachineIp = mission.getCloudMachineIp();
                String cloudMachinePort = mission.getCloudMachinePort();
                String cloudMachineAddress = cloudMachineIp + ":" + cloudMachinePort;
                String fileName = missionOutput.getImageData();
                String urlStr = comfyUIViewService.viewQueue(cloudMachineAddress, fileName, "output");
                URL url = new URL(urlStr);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                minioUtil.uploadFile(fileName, inputStream, ContentType.IMAGE_JPEG.getMimeType());
                StringBuilder urlBuilder = new StringBuilder();
                StringBuilder imgUrlSb = urlBuilder.append(publicEndpoint).append("/").append(bucketName).append("/").append(fileName);
                missionOutput.setImageUrl(imgUrlSb.toString());
                missionOutput.setStatus(1);
                missionOutputMapper.update(missionOutput);
            } catch (Exception e) {

            }

        }
    }
}
