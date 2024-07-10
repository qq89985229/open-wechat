package com.qq89985229.openwechat.controller;

import com.qq89985229.openwechat.entity.Authorizer;
import com.qq89985229.openwechat.service.AuthorizerService;
import com.qq89985229.openwechat.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController(value = "test")
@RequestMapping(value = "test")
public class TestController {
    @Resource
    RedisService redisService;
    @Resource
    AuthorizerService authorizerService;


    @GetMapping(value = "set")
    public Mono<Boolean> set(@RequestParam(value = "value") String value){
        return redisService.set("name", value, Duration.ofMinutes(5));
    }

    @GetMapping(value = "get")
    public Mono<Object> get(){
        return redisService.get("name");
    }

    @GetMapping(value = "authorizer")
    public Flux<Authorizer> authorizerFlux(){
        return authorizerService.findAll();
    }
}
