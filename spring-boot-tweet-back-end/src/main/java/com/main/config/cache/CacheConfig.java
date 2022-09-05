package com.main.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

import static com.main.constants.CacheConstant.FIND_ALL_EMPLOYEE_CACHE_NAME;
import static com.main.constants.CacheConstant.FIND_SINGLE_EMPLOYEE_CACHE_NAME;

@Configuration
@EnableCaching
@Profile("redis") // ("!redis") // use cache when active profile redis only
public class CacheConfig {

    @Autowired(required = false)
    private CacheManager cacheManager;

    @Value("${redis.cache.dynamic.ttl}")
    private int dynamicCacheTtl;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration(FIND_ALL_EMPLOYEE_CACHE_NAME,
                        getCacheConfiguration(dynamicCacheTtl))
                .withCacheConfiguration(FIND_SINGLE_EMPLOYEE_CACHE_NAME,
                        getCacheConfiguration(dynamicCacheTtl))
                .build();
    }

    private RedisCacheConfiguration getCacheConfiguration(int cacheTtl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheTtl));
    }

    @Bean
    @Profile("redis")
    public void onApplicationEvent() {
        cacheManager.getCacheNames().parallelStream()
                .forEach(cache -> cacheManager.getCache(cache).clear());
    }
}
