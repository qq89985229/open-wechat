package com.qq89985229.openwechat.service;
import reactor.core.publisher.Mono;
import java.time.Duration;

public interface RedisService {
    Mono<Boolean> set(String key, Object value, Duration timeout);

    Mono<Object> get(String key);
    Mono<Boolean> hasKey(String key);
}
