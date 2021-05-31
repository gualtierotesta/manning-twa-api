package com.twa.flights.api.clusters.configuration;

import com.twa.flights.api.clusters.dto.CityDTO;
import com.twa.flights.api.clusters.serializer.CitySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.twa.flights.api.clusters.configuration.settings.RedisSettings;
import com.twa.flights.api.clusters.dto.ClusterSearchDTO;
import com.twa.flights.api.clusters.serializer.ClusterSearchSerializer;

import java.util.Map;

@Configuration
@ConfigurationProperties
public class RedisConfiguration {

    private Map<String, RedisSettings> redis;

    private final ClusterSearchSerializer clusterSearchSerializer;
    private final CitySerializer citySerializer;

    @Autowired
    public RedisConfiguration(final ClusterSearchSerializer pClusterSearchSerializer,
            final CitySerializer pCitySerializer) {
        clusterSearchSerializer = pClusterSearchSerializer;
        citySerializer = pCitySerializer;
    }

    // REDIS DB
    @Bean
    public JedisConnectionFactory jedisDBConnectionFactory() {
        RedisSettings settings = redis.get("db");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(settings.getHost(),
                settings.getPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, ClusterSearchDTO> redisDBTemplate() {
        RedisTemplate<String, ClusterSearchDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisDBConnectionFactory());
        redisTemplate.setValueSerializer(clusterSearchSerializer);
        return redisTemplate;
    }

    // REDIS CACHE
    @Bean
    public JedisConnectionFactory jedisCacheConnectionFactory() {
        RedisSettings settings = redis.get("cache");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(settings.getHost(),
                settings.getPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, CityDTO> redisCacheTemplate() {
        RedisTemplate<String, CityDTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisCacheConnectionFactory());
        redisTemplate.setValueSerializer(citySerializer);
        return redisTemplate;
    }

    public Map<String, RedisSettings> getRedis() {
        return redis;
    }

    public void setRedis(final Map<String, RedisSettings> pRedis) {
        redis = pRedis;
    }

}