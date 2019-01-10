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

package li.allan.easycache.config;


import li.allan.easycache.CacheKeyGenerator;
import li.allan.easycache.SimpleCacheKeyGenerator;
import li.allan.easycache.cache.CacheOperator;
import li.allan.easycache.cache.CaffeineCacheOperator;
import li.allan.easycache.cache.RemoteCacheOperator;
import li.allan.easycache.exception.EasyCacheInitializeFailureException;
import li.allan.easycache.local.caffeine.CaffeineConfig;

/**
 * @author lialun
 */
public class EasyCacheConfig {
    private static ConfigProperties config;
    private static CacheOperator localCacheOperator;
    private static RemoteCacheOperator remoteCacheOperator;
    private static CacheKeyGenerator cacheKeyGenerator;

    private static boolean isInit = false;

    public static void init(ConfigProperties configProperties) {
        config = configProperties;

        //TODO default local cache
        if (configProperties.getLocalCacheConfig() instanceof CaffeineConfig) {
            localCacheOperator = new CaffeineCacheOperator((CaffeineConfig) configProperties.getLocalCacheConfig());
        }

        if (configProperties.getCacheKeyGenerator() != null) {
            cacheKeyGenerator = configProperties.getCacheKeyGenerator();
        } else {
            cacheKeyGenerator = new SimpleCacheKeyGenerator();
        }
        isInit = true;
    }

    public static ConfigProperties getConfig() {
        isInitializedOrThrowException();
        return config;
    }

    public static CacheOperator getLocalCacheOperator() {
        isInitializedOrThrowException();
        return localCacheOperator;
    }

    public static RemoteCacheOperator getRemoteCacheOperator() {
        isInitializedOrThrowException();
        return remoteCacheOperator;
    }

    public static CacheKeyGenerator getCacheKeyGenerator() {
        isInitializedOrThrowException();
        return cacheKeyGenerator;
    }

    private static void isInitializedOrThrowException() {
        if (!isInit) {
            throw new EasyCacheInitializeFailureException("EasyCache not been initialized yet");
        }
    }
}
