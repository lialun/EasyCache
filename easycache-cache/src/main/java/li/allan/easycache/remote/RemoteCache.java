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

package li.allan.easycache.remote;

import li.allan.easycache.ValueWrapper;
import li.allan.easycache.remote.serializer.Serializer;

import java.util.concurrent.TimeUnit;

/**
 * @author lialun
 */
public interface RemoteCache<K, V> {
    void put(K key, V value, long expireAfterCreate, TimeUnit timeUnit, Class<Serializer> keySerializer, Class<Serializer> valueSerializer);

    V get(K key, Class<Serializer> keySerializer);

    ValueWrapper<V> getValueWrapper(K key, Class<Serializer> keySerializer);

    boolean contains(K key, Class<Serializer> keySerializer);

    void invalidate(K key, Class<Serializer> keySerializer);

    long expireTimestampInMills(K key, Class<Serializer> keySerializer);

    long size();
}
