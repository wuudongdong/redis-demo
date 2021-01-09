package com.example.redisdemo.manager;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Implement distributed lock base on Redis.
 *
 * @author wuudongdong
 * @date 2020/01/06
 */
@Component
public class RedisManager {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    public Boolean lock(String lock, String val) {
        return redisTemplate.opsForValue().setIfAbsent(lock, val, 30000, TimeUnit.MILLISECONDS);
    }

    public void unLock(String lock, String val) {
        String lua = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        RedisScript<Object> redisScript = RedisScript.of(lua);
        redisTemplate.execute(redisScript, Collections.singletonList(lock), val);
    }

    public Boolean decrement(String key, Long value) {
        String lua = "local result = redis.call('get', KEYS[1]) - ARGV[1] " +
                "if result >= 0 then return redis.call('set', KEYS[1], result).ok == 'OK' else return 0 end";
        RedisScript<Boolean> redisScript = RedisScript.of(lua, Boolean.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key), value);
    }
}
