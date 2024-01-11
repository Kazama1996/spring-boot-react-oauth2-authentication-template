package com.kazama.SpringOAuth2.config;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.address}")
    private String redisAddress;

    @Bean
    public Config config() {
        Config config = new Config();
        // redis://redis:6379

        System.out.println("*********************************************");
        System.out.println("Now Redis url is " + redisAddress);
        System.out.println("*********************************************");
        config.useSingleServer().setAddress(redisAddress).setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(5);
        return config;
    }

    @Bean(name = "springCM")
    public CacheManager cacheManager(Config config) {
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("bucket_cache", RedissonConfiguration.fromConfig(config));
        return manager;
    }

    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(cacheManager.getCache("bucket_cache"));
    }

}
