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

import java.nio.charset.Charset;

public class StringSerializer extends Serializer {

    private final Charset charset;

    StringSerializer() {
        this.charset = Charset.forName("UTF8");
    }

    @Override
    public String deserialize(byte[] bytes, Class type) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(Object string) throws SerializationException {
        return (string == null ? null : ((String) string).getBytes(charset));
    }
}
