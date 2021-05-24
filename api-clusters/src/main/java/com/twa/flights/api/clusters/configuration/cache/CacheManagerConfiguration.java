package com.twa.flights.api.clusters.configuration.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheManagerConfiguration {

    @Bean
    public CacheManager cacheManager(final CacheConfiguration pCacheConfiguration) {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(buildCaffeineCache("catalogCity", pCacheConfiguration)));
        return simpleCacheManager;
    }

    private CaffeineCache buildCaffeineCache(final String pCacheName, final CacheConfiguration pCacheConfiguration) {
        SingleCacheConfiguration configuration = pCacheConfiguration.getCacheConfiguration(pCacheName);
        Cache<Object, Object> objectObjectCaffeine = Caffeine.newBuilder().maximumSize(configuration.getMaxSize())
                .expireAfterAccess(configuration.getDuration(), TimeUnit.SECONDS)
                .expireAfterWrite(configuration.getDuration(), TimeUnit.SECONDS).build();
        return new CaffeineCache(pCacheName, objectObjectCaffeine);
    }

}
