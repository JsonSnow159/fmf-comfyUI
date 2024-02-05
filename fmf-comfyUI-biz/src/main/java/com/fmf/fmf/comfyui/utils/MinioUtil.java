package com.fmf.fmf.comfyui.utils;

import cn.hutool.core.io.IoUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author:吴金才
 * @Date:2024/2/5 10:10
 */
@Component
@Slf4j
public class MinioUtil {
    private MinioClient minioClient;
    @Autowired
    public void setMinioClient(@Qualifier("IMinioClient") MinioClient minioClient){
        this.minioClient = minioClient;
    }

    @Value("${minio.comfyui.bucket}")
    private String defaultBucketName;


    /**
     * 上传文件到MinIO桶
     *
     * @param objectName  存储对象的名称
     * @param inputStream 文件输入流
     * @param contentType 文件类型
     */
    public void uploadFile(String objectName, InputStream inputStream, String contentType) {
        this.uploadFile(defaultBucketName, objectName, inputStream, contentType);
    }

    /**
     * 上传文件到MinIO桶
     *
     * @param bucketName  MinIO桶的名称
     * @param objectName  存储对象的名称
     * @param inputStream 文件输入流
     * @param contentType 文件类型
     */
    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {

        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            handleMinioException(e);
        }
    }


    /**
     * 从MinIO桶下载文件
     *
     * @param objectName 存储对象的名称
     * @return 文件输入流
     */
    public InputStream downloadFile(String objectName) {
        return this.downloadFile(defaultBucketName, objectName);
    }

    /**
     * 从MinIO桶下载文件
     *
     * @param bucketName MinIO桶的名称
     * @param objectName 存储对象的名称
     * @return 文件输入流
     */
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();

            // 读取文件内容到字节数组
            try (InputStream inputStream = minioClient.getObject(getObjectArgs)) {
                byte[] byteArray = IoUtil.readBytes(inputStream);
                return new ByteArrayInputStream(byteArray);
            }
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            handleMinioException(e);
            return null;
        }
    }


    /**
     * 从MinIO桶删除文件
     *
     * @param objectName 存储对象的名称
     */
    public void deleteFile(String objectName) {
        this.deleteFile(defaultBucketName, objectName);
    }

    /**
     * 从MinIO桶删除文件
     *
     * @param bucketName MinIO桶的名称
     * @param objectName 存储对象的名称
     */
    public void deleteFile(String bucketName, String objectName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();

            minioClient.removeObject(removeObjectArgs);
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            handleMinioException(e);
        }
    }


    /**
     * 批量删除MinIO桶中的文件
     *
     * @param objectNames 存储对象的名称列表
     */
    public void deleteFiles(List<String> objectNames) {
        this.deleteFiles(defaultBucketName, objectNames);
    }

    /**
     * 批量删除MinIO桶中的文件
     *
     * @param bucketName  MinIO桶的名称
     * @param objectNames 存储对象的名称列表
     */
    public void deleteFiles(String bucketName, List<String> objectNames) {
        try {
            List<DeleteObject> objectsToDelete = new ArrayList<>();
            for (String objectName : objectNames) {
                objectsToDelete.add(new DeleteObject(objectName));
            }

            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(objectsToDelete)
                    .build();

            minioClient.removeObjects(removeObjectsArgs);
        } catch (Exception e) {
            handleMinioException(e);
        }
    }

    /**
     * 判断MinIO桶中的对象是否存在
     *
     * @param objectName 存储对象的名称
     * @return 文件是否存在
     */
    public boolean doesObjectExist(String objectName) {
        return this.doesObjectExist(defaultBucketName, objectName);
    }

    /**
     * 判断MinIO桶中的对象是否存在
     *
     * @param bucketName MinIO桶的名称
     * @param objectName 存储对象的名称
     * @return 文件是否存在
     */
    public boolean doesObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 处理MinIO异常
     *
     * @param e MinIO异常
     */
    private void handleMinioException(Exception e) {
        log.error("minio操作出现异常，异常为:{}", e.toString());
        throw new RuntimeException(e);
    }

    /**
     * 获取对象url
     * @param objectName
     * @return
     */
    public String getPresignedObjectUrl(String objectName){
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .bucket(defaultBucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(600, TimeUnit.SECONDS)
                    .build();
            return minioClient.getPresignedObjectUrl(args);
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            handleMinioException(e);
        }
        return null;
    }

    /**
     * 校验是否存在bucket
     *
     * @param bucketName
     */
    public boolean checkBucket(String bucketName){
        try {
            BucketExistsArgs bucket = BucketExistsArgs.builder().bucket(bucketName).build();
            return minioClient.bucketExists(bucket);
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            handleMinioException(e);
            return false;
        }
    }
}
