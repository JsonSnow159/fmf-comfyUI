package com.fmf.fmf.comfyui.common.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fmf.fmf.comfyui.common.ICacheService;
import com.fmf.fmf.comfyui.exception.CacheException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisCacheServiceImpl implements ICacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String val) {

        if (StringUtils.isAnyBlank(key, val)) {
            return;
        }

        try {
            stringRedisTemplate.opsForValue().set(key, val);
        } catch (Exception e) {

            log.warn("【redis client set exception】 key:{}", key, e);
            throw new CacheException("设置redis失败");

        }
    }


    @Override
    public void setWithTtL(String prefix, String key, Object obj, long time, TimeUnit timeUnit) {
        if (ObjectUtils.isEmpty(obj) || StringUtils.isEmpty(key)) {
            return;
        }
        try {

            stringRedisTemplate.opsForValue()
                    .set(prefix + key, JSON.toJSONString(obj), time, timeUnit);
        } catch (Exception e) {

            log.warn("【redis client set exception】 key:{}", key, e);
            throw new CacheException("设置redis失败");

        }
    }

    @Override
    public void set(String key, Object obj) {

        if (ObjectUtils.isEmpty(obj) || StringUtils.isEmpty(key)) {
            return;
        }

        try {

            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
        } catch (Exception e) {

            log.warn("【redis client set exception】 key:{}", key, e);
            throw new CacheException("设置redis失败");

        }
    }

    @Override
    public void set(String key, List<Object> obj) {

        if (CollectionUtils.isEmpty(obj)) {
            return;
        }

        try {

            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(obj));
        } catch (Exception e) {

            log.warn("【redis client set exception】 key:{}", key, e);
            throw new CacheException("设置redis失败");

        }
    }

    @Override
    public void setWithTtL(String key, String val, long time, TimeUnit timeUnit) {

        if (StringUtils.isAnyBlank(key, val)) {
            return;
        }

        try {
            stringRedisTemplate.opsForValue().set(key, val, time, timeUnit);
        } catch (Exception e) {

            log.warn("【redis client set exception】 key:{}", key, e);
            throw new CacheException("设置redis失败");

        }
    }

    @Override
    public String get(String key) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");

        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            String res = stringRedisTemplate.opsForValue().get(key);

            return JSONObject.parseObject(res, clazz);

        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");
        }
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            String res = stringRedisTemplate.opsForValue().get(key);

            Object object = JSON.parse(res);
            if (object instanceof JSONObject) {

                return Lists.newArrayList(JSONObject.parseObject(res, clazz));
            } else if (object instanceof JSONArray) {

                return JSONArray.parseArray(res, clazz);
            }
            return null;
        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");
        }
    }

    @Override
    public boolean remove(String key) {

        if (StringUtils.isEmpty(key)) {
            return true;
        }

        try {
            stringRedisTemplate.delete(key);
            return true;
        } catch (Exception e) {

            log.warn("【redis client remove exception】 key:{}", key, e);
            throw new CacheException("移除redis失败");

        }
    }

    @Override
    public Boolean zadd(String key, String value, double score) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            return stringRedisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");

        }
    }

    @Override
    public Long zrem(String key, String value) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            return stringRedisTemplate.opsForZSet().remove(key, value);
        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");

        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            return stringRedisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");

        }
    }

    @Override
    public Long increment(String key, long delta, long timeout, TimeUnit timeUnit) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            Long result = stringRedisTemplate.opsForValue().increment(key, delta);
            stringRedisTemplate.expire(key, timeout, timeUnit);
            return result;

        } catch (Exception e) {

            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");

        }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zSort(String key) {
        Set<ZSetOperations.TypedTuple<String>> result;
        try {
            // 一次性按照score升序取出数据
            result = stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        } catch (Exception e) {
            log.warn("【redis client getByName exception】 key:{}", key, e);
            throw new CacheException("读取redis失败");
        }
        return result;
    }
}