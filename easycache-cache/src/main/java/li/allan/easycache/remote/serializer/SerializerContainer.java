/*
 * Copyright  2017-2018. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package li.allan.easycache.remote.serializer;

import li.allan.easycache.remote.serializer.exception.SerializationException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiALuN
 */
public class SerializerContainer {
    private static final Map<Class, Serializer> CONTAINER = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Serializer> T getSerializer(Class<T> serializerCls) {
        if (!CONTAINER.containsKey(serializerCls)) {
            synchronized (CONTAINER) {
                if (!CONTAINER.containsKey(serializerCls)) {
                    try {
                        CONTAINER.put(serializerCls, serializerCls.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new SerializationException("EasyCache can't create serializer: " + serializerCls.getName(), e);
                    }
                }
            }
        }
        return (T) CONTAINER.get(serializerCls);
    }

}
