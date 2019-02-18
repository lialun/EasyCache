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
import li.allan.easycache.local.LocalCache;
import li.allan.easycache.remote.serializer.Serializer;

/**
 * @author lialun
 */
public abstract class AbstractOperator {
    public abstract void put(String cacheName, String cacheKey, Object value, Class<? extends Serializer> valueSerializer, long expireInSecond, int cacheSize);

    public abstract <V> ValueWrapper<V> get(String cacheName, String cacheKey, Class<? extends Serializer> valueSerializer, Class<V> type);

    public abstract void remove(String cacheName, String cacheKey);

    public abstract LocalCache<String, Object> getCache(String cacheName);
}
