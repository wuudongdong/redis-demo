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
        return redisTemplate.opsForValue().setIfAbsent(lock, val, 3000, TimeUnit.MILLISECONDS);
    }

    public void unLock(String lock, String val) {
        String lua = "local val = ARGV[1] local curr=redis.call('get', KEYS[1]) "
                + "if val==curr then redis.call('del', KEYS[1]) end return 'OK'";
        RedisScript<Object> redisScript = RedisScript.of(lua);
        redisTemplate.execute(redisScript, Collections.singletonList(lock), val);
    }

    public Boolean decrement(String key, Long value) {
        String lua = "local val = ARGV[1] local curr=redis.call('get', KEYS[1]) local result = curr-val "
                + "if result>=0 then redis.call('set', KEYS[1], result) return '1' else return '0' end";
        RedisScript<Integer> redisScript = RedisScript.of(lua, Integer.class);
        Integer result = redisTemplate.execute(redisScript, Collections.singletonList(key), value);
        return Objects.nonNull(result) && 1 == result;
    }
}
