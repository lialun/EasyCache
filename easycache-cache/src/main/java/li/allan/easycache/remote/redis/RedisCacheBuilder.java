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

package li.allan.easycache.remote.redis;


import redis.clients.jedis.JedisPool;

/**
 * @author lialun
 */
public class RedisCacheBuilder<K, V> {
    private RedisConfig config;

    RedisCacheBuilder() {
        this.config = new RedisConfig();
    }

    public RedisCacheBuilder(RedisConfig config) {
        this.config = config;
    }

    public RedisCacheBuilder<K, V> setJedisPool(JedisPool jedisPool) {
        this.config.setJedisPool(jedisPool);
        return this;
    }

    public <K1 extends K, V1 extends V> RedisCache<K1, V1> build() {
        if (config.getJedisPool() != null) {
            return new JedisCache<>(config.getJedisPool());
        }
        return null;
    }
}
