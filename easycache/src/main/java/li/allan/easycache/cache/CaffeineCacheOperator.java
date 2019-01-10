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

package li.allan.easycache.cache;

import li.allan.easycache.ValueWrapper;
import li.allan.easycache.local.caffeine.CaffeineCache;
import li.allan.easycache.local.caffeine.CaffeineCacheBuilder;
import li.allan.easycache.local.caffeine.CaffeineConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author lialun
 */
public class CaffeineCacheOperator extends CacheOperator {
    private CaffeineConfig caffeineConfig;
    private Map<String, CaffeineCache<String, Object>> cacheBucket;
    private ScheduledExecutorService cleanUpExecutor;

    public CaffeineCacheOperator(CaffeineConfig caffeineConfig) {
        this.caffeineConfig = caffeineConfig;
        this.cacheBucket = new HashMap<>();

        //create executor in operator rather than cache to avoid create so much threads
        cleanUpExecutor = Executors.newScheduledThreadPool(1);
        if (caffeineConfig.getCleanUpIntervalInSecond() > 0) {
            cleanUpExecutor.scheduleWithFixedDelay(
                    () -> {
                        Iterator<String> bucketIterator = cacheBucket.keySet().iterator();
                        if (bucketIterator.hasNext()) {
                            cacheBucket.get(bucketIterator.next()).cleanUp();
                        }
                    },
                    caffeineConfig.getCleanUpIntervalInSecond(), caffeineConfig.getCleanUpIntervalInSecond(),
                    TimeUnit.SECONDS);
        }
    }

    @Override
    public void put(String cacheName, String cacheKey, Object value, long expireInSecond, int maximumSize) {
        /*
         * create cache bucket if not exist
         */
        if (!cacheBucket.containsKey(cacheName)) {
            synchronized (this) {
                if (!cacheBucket.containsKey(cacheName)) {
                    cacheBucket.put(cacheName, new CaffeineCacheBuilder<String, Object>(caffeineConfig.clone(maximumSize)).build());
                }
            }
        }
        /*
         * put cache into bucket
         */
        cacheBucket.get(cacheName).put(cacheKey, value, expireInSecond, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> ValueWrapper<V> get(String cacheName, String cacheKey, Class<V> type) {
        CaffeineCache<String, Object> cache = cacheBucket.get(cacheName);
        if (cache == null) {
            return null;
        }
        return (ValueWrapper<V>) cache.getValueWrapper(cacheKey);
    }

    @Override
    public void remove(String cacheName, String cacheKey) {
        CaffeineCache<String, Object> cache = getCache(cacheName);
        if (cache == null) {
            return;
        }
        cache.invalidate(cacheKey);
    }

    @Override
    public CaffeineCache<String, Object> getCache(String cacheName) {
        return cacheBucket.get(cacheName);
    }
}
