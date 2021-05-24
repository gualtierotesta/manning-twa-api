package com.twa.flights.api.clusters.configuration.cache;

class SingleCacheConfiguration {

    private long maxSize;
    private long duration; // seconds

    static final SingleCacheConfiguration DEFAULT = new SingleCacheConfiguration(1000, 15);

    public SingleCacheConfiguration() {
        // empty
    }

    SingleCacheConfiguration(final long pMaxSize, final long pDuration) {
        maxSize = pMaxSize;
        duration = pDuration;
    }

    long getMaxSize() {
        return maxSize;
    }

    void setMaxSize(final long pMaxSize) {
        maxSize = pMaxSize;
    }

    long getDuration() {
        return duration;
    }

    void setDuration(final long pDuration) {
        duration = pDuration;
    }
}
