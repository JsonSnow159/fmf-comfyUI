package com.fmf.fmf.comfyui.service.impl;

import com.alibaba.fastjson.JSON;
import com.fmf.fmf.comfyui.dto.ImageUploadResponse;
import com.fmf.fmf.comfyui.enums.ComfyUIAPIEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author:吴金才
 * @Date:2024/1/30 16:21
 */
@Slf4j
@Service
public class ComfyUIUploadImageServiceImpl {
    public ImageUploadResponse uploadImage(String serviceAddress, String imgUrl, String fileName) {
        try {
            // 创建一个RestTemplate实例
            RestTemplate restTemplate = new RestTemplate();
            URL url = new URL(imgUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            // 将InputStream转换为字节数组
            byte[] bytes = toByteArray(inputStream);
            // 创建一个ByteArrayResource来封装字节数组
            ByteArrayResource byteArrayResource = new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    return fileName; // 指定文件名
                }
            };

            // 设置请求头，指定Content-Type为multipart/form-data
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            // 创建一个MultiValueMap来存放form-data数据
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", byteArrayResource);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String tripartiteUrl = "http://" + serviceAddress + ComfyUIAPIEnum.UPLOAD_IMAGE.getPath();
            ResponseEntity<String> response = restTemplate.exchange(tripartiteUrl, HttpMethod.POST, requestEntity, String.class);
            String responseBody = response.getBody();
            log.info("图片上传结果:{}", responseBody);
            ImageUploadResponse imageUploadResponse = JSON.parseObject(responseBody, ImageUploadResponse.class);
            return imageUploadResponse;
        } catch (Exception e) {
            log.error("图片上传至comfyUI报错", e);
        }
        return null;
    }

    // 将InputStream转换为字节数组的方法
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

}
