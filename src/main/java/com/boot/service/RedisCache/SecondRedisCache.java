package com.boot.service.RedisCache;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: LiuHeYong
 * @create: 2019-06-03
 * @description: 二级缓存到redis中
 **/
public class SecondRedisCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(SecondRedisCache.class);

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final String id;

    private RedisTemplate redisTemplate;

    public SecondRedisCache(String id) {
        this.id = id;
    }

    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringBeanUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    /**
     * 放入缓存
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value) {
        try {
            RedisTemplate redisTemplate = getRedisTemplate();
            ValueOperations opsForValue = redisTemplate.opsForValue();
            opsForValue.set(key, value, 365, TimeUnit.DAYS);
            logger.debug("缓存成功");
        } catch (Throwable t) {
            logger.error("放入缓存失败", t);
        }
    }

    /**
     * 获取指定缓存
     */
    @Override
    public Object getObject(Object key) {
        try {
            RedisTemplate redisTemplate = getRedisTemplate();
            ValueOperations opsForValue = redisTemplate.opsForValue();
            logger.debug("获取缓存成功");
            return opsForValue.get(key);
        } catch (Throwable e) {
            logger.error("缓存获取失败", e);
            return null;
        }
    }

    /**
     * 清除指定缓存
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        try {
            RedisTemplate redisTemplate = getRedisTemplate();
            redisTemplate.delete(key);
            logger.debug("清除单个缓存成功");
        } catch (Throwable t) {
            logger.error("清除单个缓存失败", t);
        }
        return null;
    }

    /**
     * 清除整个缓存
     */
    @Override
    public void clear() {
        RedisTemplate redisTemplate = getRedisTemplate();

        Set<String> keys = redisTemplate.keys("*:" + this.id + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
        logger.debug("缓存清除完毕");
    }

}
