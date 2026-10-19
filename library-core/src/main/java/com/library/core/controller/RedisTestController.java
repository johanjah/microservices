package com.library.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    @PostMapping("/test")
    public Map<String, String> testRedis() {
        redisTemplate.opsForValue().set(
                "library:test",
                "Redis Connected!"
        );

        String value = redisTemplate.opsForValue()
                .get("library:test");

        return Map.of(
                "key", "library:test",
                "value", value
        );
    }
}