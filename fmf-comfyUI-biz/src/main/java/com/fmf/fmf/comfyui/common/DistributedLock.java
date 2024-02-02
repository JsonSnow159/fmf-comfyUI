package com.fmf.fmf.comfyui.common;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 分布式锁
 * @Author: ludan
 * @Date: 2024/1/12
 **/
public interface DistributedLock {
    boolean lock(String key, int time, TimeUnit timeUnit);

    void unlock(String key);
}
