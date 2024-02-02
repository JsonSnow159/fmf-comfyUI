package com.fmf.fmf.comfyui.common;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface ICacheService {

    /**
     * redis set
     *
     * @param key
     * @param val
     */
    void set(String key, String val);

    /**
     * redis set
     *
     * @param key
     * @param obj
     */
    void set(String key, Object obj);

    /**
     * 数组
     *
     * @param key
     * @param obj
     */
    void set(String key, List<Object> obj);

    /**
     * redis set
     *
     * @param prefix
     * @param key
     * @param obj
     * @param time
     * @param timeUnit
     */
    void setWithTtL(String prefix, String key, Object obj, long time, TimeUnit timeUnit);

    /**
     * redis set with TimeOut
     *
     * @param key
     * @param val
     * @param time     超时时间
     * @param timeUnit 事件单位
     */
    void setWithTtL(String key, String val, long time, TimeUnit timeUnit);

    /**
     * redis get
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * redis get
     *
     * @param clazz
     * @param key
     * @return
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取缓存列表
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * reids remove
     *
     * @param key
     * @return
     */
    boolean remove(String key);

    /**
     * redis zadd
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    Boolean zadd(String key, String value, double score);

    /**
     * reids zrem
     *
     * @param key
     * @param value
     * @return
     */
    Long zrem(String key, String value);

    /**
     * redis zrange
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<String> zrange(String key, long start, long end);

    /**
     * redis increment
     *
     * @param key
     * @param delta
     * @param timeout
     * @param timeUnit
     * @return
     */
    Long increment(String key, long delta, long timeout, TimeUnit timeUnit);


    Set<ZSetOperations.TypedTuple<String>> zSort(String key);
}

