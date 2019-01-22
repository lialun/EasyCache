/*
 * Copyright (c) 2017-2018. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package li.allan.easycache.local.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import li.allan.easycache.ValueWrapper;
import li.allan.easycache.local.LocalCache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author lialun
 */

public class CaffeineCache<K, V> implements LocalCache<K, V> {
    private CaffeineConfig caffeineConfig;
    private Cache<K, ValueWrapper<V>> cache;
    private ScheduledExecutorService cleanUpExecutor;

    public static CaffeineCacheBuilder<Object, Object> builder() {
        return new CaffeineCacheBuilder<>();
    }

    @SuppressWarnings("unchecked")
    CaffeineCache(CaffeineConfig caffeineConfig) {
        this.caffeineConfig = caffeineConfig;

        Caffeine caffeine = Caffeine.newBuilder();
        if (caffeineConfig.getMaximumSize() >= 0) {
            caffeine = caffeine.maximumSize(caffeineConfig.getMaximumSize());
        }
        if (caffeineConfig.isSoftValues()) {
            caffeine = caffeine.softValues();
        }

        caffeine = caffeine.expireAfter(new Expiry<K, ValueWrapper<V>>() {
            @Override
            public long expireAfterCreate(K key, ValueWrapper<V> value, long currentTime) {
                return MILLISECONDS.toNanos(value.getExpireTimestampInMills() - System.currentTimeMillis());
            }

            @Override
            public long expireAfterUpdate(K key, ValueWrapper<V> value, long currentTime, long currentDuration) {
                return currentDuration;
            }

            @Override
            public long expireAfterRead(K key, ValueWrapper<V> value, long currentTime, long currentDuration) {
                return currentDuration;
            }
        });
        cache = caffeine.build();

        cleanUpExecutor = Executors.newScheduledThreadPool(1);
        if (getConfig().getCleanUpIntervalInSecond() > 0) {
            cleanUpExecutor.scheduleWithFixedDelay(() -> cache.cleanUp(),
                    getConfig().getCleanUpIntervalInSecond(), getConfig().getCleanUpIntervalInSecond(), TimeUnit.SECONDS);
        }
    }

    @Override
    public void put(K key, V value, long expireAfterCreate, TimeUnit timeUnit) {
        cache.put(key, new ValueWrapper<>(value, TimeUnit.MILLISECONDS.convert(expireAfterCreate, timeUnit) + System.currentTimeMillis()));
    }

    @Override
    public V get(K key) {
        ValueWrapper<V> valueWrapper = getValueWrapper(key);
        if (valueWrapper != null) {
            return valueWrapper.getValue();
        }
        return null;
    }

    @Override
    public ValueWrapper<V> getValueWrapper(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public boolean contains(K key) {
        return cache.getIfPresent(key) != null;
    }

    @Override
    public void invalidate(K key) {
        cache.invalidate(key);
    }

    @Override
    public long expireTimestampInMills(K key) {
        ValueWrapper<V> valueWrapper = getValueWrapper(key);
        if (valueWrapper != null) {
            return valueWrapper.getExpireTimestampInMills();
        }
        return -1;
    }

    @Override
    public long size() {
        cleanUp();
        return cache.estimatedSize();
    }

    public CaffeineConfig getConfig() {
        return caffeineConfig;
    }

    public void cleanUp() {
        cache.cleanUp();
    }
}
