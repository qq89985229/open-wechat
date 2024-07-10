package com.qq89985229.openwechat.service.impl;

import com.qq89985229.openwechat.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Override
    public Mono<Boolean> set(String key, Object value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue().set(key, value, timeout);
    }

    @Override
    public Mono<Object> get(String key) {
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> hasKey(String key) {
        return reactiveRedisTemplate.hasKey(key);
    }
}