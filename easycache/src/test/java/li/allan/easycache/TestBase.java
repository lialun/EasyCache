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

package li.allan.easycache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Random;

/**
 * @author LiALuN
 */
@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
//加载配置文件
public class TestBase {
    private static final int TEST_REDIS_PORT = 16394;

    RedisServer redisServer;

    @Test
    public void empty() {

    }

//    @Before
//    public void startEasyCache() {
//        ConfigProperties configProperties = new ConfigProperties();
//        CaffeineConfig caffeineConfig = new CaffeineConfig();
//        caffeineConfig.setCleanUpIntervalInSecond(1);
//        configProperties.setLocalCacheConfig(caffeineConfig);
//        EasyCacheConfig.init(configProperties);
//    }

    @Before
    public void startRedis() throws IOException {
        redisServer = new RedisServer(TEST_REDIS_PORT);
        redisServer.start();
    }

    @After
    public void stopRedis() {
        redisServer.stop();
    }

    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(str.length());
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }
}