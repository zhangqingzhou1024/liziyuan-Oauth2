package com.liziyuan.hope.oauth.service;

import java.util.concurrent.TimeUnit;

/**
 * RedisService
 *
 * @author zqz
 * @date 2022/6/30
 * @since 1.0.0
 */
public interface RedisService {

    /**
     * 向Redis中存储键值对
     *
     * @param key   KEY
     * @param value VALUE
     * @author zqz
     * @date 2022/6/30 17:02
     * @since 1.0.0
     */
    void set(String key, Object value);

    /**
     * 向Redis中存储键值对，并设置过期时间
     *
     * @param key      KEY
     * @param value    VALUE
     * @param time     过期时间
     * @param timeUnit 时间单位
     * @author zqz
     * @date 2022/6/30 17:02
     * @since 1.0.0
     */
    void setWithExpire(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * 从Redis中获取键值对
     *
     * @param key KEY
     * @return K
     * @author zqz
     * @date 2022/6/30 17:04
     * @since 1.0.0
     */
    <K> K get(String key);

    /**
     * 删除Redis中的某个KEY
     *
     * @param key KEY
     * @return boolean
     * @author zqz
     * @date 2022/6/30 17:10
     * @since 1.0.0
     */
    boolean delete(String key);
}
