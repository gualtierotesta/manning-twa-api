package com.twa.flights.api.clusters.configuration.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfiguration {

    private Map<String, SingleCacheConfiguration> configuration;

    public SingleCacheConfiguration getCacheConfiguration(final String pCacheName) {
        return configuration.getOrDefault(pCacheName, SingleCacheConfiguration.DEFAULT);
    }

    public Map<String, SingleCacheConfiguration> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(final Map<String, SingleCacheConfiguration> pConfiguration) {
        configuration = pConfiguration;
    }

}
