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

import li.allan.easycache.local.LocalCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author lialun
 */
public class CaffeineCacheBuilderTest {

    @Test
    public void testBuild() {
        //empty config
        CaffeineConfig caffeineConfig = new CaffeineConfig();
        LocalCache cache = new CaffeineCacheBuilder().build();
        assertEquals(caffeineConfig, ((CaffeineCache) cache).getConfig());

        //full config
        caffeineConfig = new CaffeineConfig();
        caffeineConfig.setMaximumSize(10);
        caffeineConfig.setSoftValues(true);
        caffeineConfig.setCleanUpIntervalInSecond(100);

        cache = new CaffeineCacheBuilder()
                .setMaximumSize(caffeineConfig.getMaximumSize())
                .setSoftValues(caffeineConfig.isSoftValues())
                .enableAutoCleanUp(100)
                .build();
        assertEquals(caffeineConfig, ((CaffeineCache) cache).getConfig());
    }
}