package com.fmf.fmf.comfyui.comfig;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:吴金才
 * @Date:2024/2/5 10:03
 */
@Configuration
public class MinoConfig {
    @Value("${minio.comfyui.endpoint}")
    private String endpoint;

    @Value("${minio.comfyui.port}")
    private int port;

    @Value("${minio.comfyui.publicEndpoint}")
    private String publicEndpoint;

    @Value("${minio.comfyui.accessKey}")
    private String accessKey;

    @Value("${minio.comfyui.secretKey}")
    private String secretKey;


    @Bean(name = "IMinioClient")
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(publicEndpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}