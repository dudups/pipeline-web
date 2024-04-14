package com.ezone.devops.pipeline.lock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class DistributedLock {

    private static final String SERVER_ID = UUID.randomUUID().toString();
    // 过期时间 ms
    private static final long DEFAULT_LOCK_EXPIRE_TIME_MS = 600000;
    public static final int DEFAULT_ACQUIRE_RESOLUTION_MILLIS = 100;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX"; // ms

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_SCRIPT = "return redis.call('set',KEYS[1],ARGV[1],ARGV[2],ARGV[3],ARGV[4])";

    private static final String RELEASE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then" + "   return redis.call('del', KEYS[1]) " + "else " + "   return 0 " + "end";

    public boolean lock(String key, String value, long expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取分布式锁
     *
     * @param lockKey     分布式锁的key
     * @param lockTimeout 获得锁后，锁的超时时间
     * @return true:得到分布式锁，false:未得到
     */
    public UUID tryLock(String lockKey, Long lockTimeout) {
        if (StringUtils.isBlank(lockKey) || lockTimeout < 0) {
            return null;
        }

        if (lockTimeout == 0) {
            lockTimeout = DEFAULT_LOCK_EXPIRE_TIME_MS;
        }

        UUID uuid = currentTreadUUID();
        Object[] args = {uuid.toString(), SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, lockTimeout.toString()};

        String result = redisTemplate.execute(new DefaultRedisScript<>(LOCK_SCRIPT, String.class), Collections.singletonList(lockKey), args);
        return StringUtils.equals(LOCK_SUCCESS, result) ? uuid : null;
    }

    public boolean releaseLock(String key, String value) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(RELEASE_SCRIPT);
        redisScript.setResultType(Boolean.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key), value);
    }


    // 保证同一个线程生成的uuid一致
    private static UUID currentTreadUUID() {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            if ("localhost".equals(hostName)) {
                hostName = SERVER_ID;
            }
        } catch (Exception e) {
            hostName = SERVER_ID;
        }
        return UUID.nameUUIDFromBytes((hostName + "-" + Thread.currentThread().getId()).getBytes());
    }


}
