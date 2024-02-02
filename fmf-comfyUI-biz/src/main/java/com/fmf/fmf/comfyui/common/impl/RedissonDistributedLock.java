package com.fmf.fmf.comfyui.common.impl;

import com.fmf.fmf.comfyui.common.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 分布式锁redis实现
 * @Author: ludan
 * @Date: 2024/1/12
 **/
@Slf4j
@Component
public class RedissonDistributedLock implements DistributedLock {
    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean lock(String key, int time, TimeUnit timeUnit) {
        RLock rlock = null;
        try {
            rlock = redissonClient.getLock(key);
        } catch (Exception e) {
            log.error("创建redisson分布式锁失败, key:" + key, e);
        }

        if(Objects.isNull(rlock)) {
            return false;
        }

        boolean tryLock = false;
        try {
            tryLock =  rlock.tryLock(0L, time, timeUnit);
        } catch (WriteRedisConnectionException e) {
            // 写入失败
            log.error("获取分布式锁, redisson加锁失败, key:" + key, e);
        } catch (InterruptedException e) {
            log.error("获取分布式锁, redisson加锁被中断, key:" + key, e);
        }
        return tryLock;
    }

    @Override
    public void unlock(String key){
        RLock rLock = null;
        try {
            rLock = redissonClient.getLock(key);
        } catch (Exception e) {
            log.error("获取分布式锁, redisson unlock失败, key:" + key, e);
        }

        if(Objects.nonNull(rLock)){
            rLock.unlock();
        }
    }
}
