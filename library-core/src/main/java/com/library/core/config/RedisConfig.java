package com.library.core.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        return new StringRedisTemplate(connectionFactory);
    }

//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration() {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        objectMapper.registerModule(new JavaTimeModule());
//
//        objectMapper.disable(
//                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
//        );
//
//        objectMapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL
//        );
//
//        var serializer =
//                new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))
//                .serializeKeysWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(new StringRedisSerializer())
//                )
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(serializer)
//                );
//    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 1. Atur konfigurasi dasar cache
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // Durasi default cache aktif (TTL): 1 Jam
                .entryTtl(Duration.ofHours(1))
                // Jangan simpan data jika nilainya null
                .disableCachingNullValues()
                // Konfigurasi Serializer untuk Key (String)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // Konfigurasi Serializer untuk Value (JSON)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 2. Buat Cache Manager menggunakan koneksi Redis dan konfigurasi di atas
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}