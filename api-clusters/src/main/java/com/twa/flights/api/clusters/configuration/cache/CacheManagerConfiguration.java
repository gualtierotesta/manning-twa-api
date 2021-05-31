package com.twa.flights.api.clusters.configuration.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.twa.flights.api.clusters.serializer.CitySerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheManagerConfiguration {

    public static final String CATALOG_CITY = "catalogCity";
    private final JedisConnectionFactory jedisConnectionFactory;
    private final CitySerializer citySerializer;
    private final CacheConfiguration cacheConfiguration;

    public CacheManagerConfiguration(
            @Qualifier("jedisCacheConnectionFactory") final JedisConnectionFactory pJedisConnectionFactory,
            final CitySerializer pCitySerializer, final CacheConfiguration pCacheConfiguration) {
        jedisConnectionFactory = pJedisConnectionFactory;
        citySerializer = pCitySerializer;
        cacheConfiguration = pCacheConfiguration;
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Lists.newArrayList(RedisCacheManager.builder(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration()).build().getCache(CATALOG_CITY)));

        return simpleCacheManager;
    }

    private RedisCacheConfiguration redisCacheConfiguration() {
        SingleCacheConfiguration cacheConfiguration = this.cacheConfiguration.getCacheConfiguration(CATALOG_CITY);
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(citySerializer))
                .entryTtl(Duration.ofMinutes(cacheConfiguration.getDuration()));
    }
}
