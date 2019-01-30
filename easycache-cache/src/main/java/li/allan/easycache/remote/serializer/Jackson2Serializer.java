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

//import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import li.allan.exception.SerializationException;
//import org.springframework.util.Assert;
//
//import static li.allan.utils.Constants.EMPTY_ARRAY;
//import static li.allan.utils.Constants.NO_DATA;
//
///**
// * for jdk8 or higher version
// */
public class Jackson2Serializer extends Serializer {
    @Override
    public byte[] serialize(Object t) throws SerializationException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {
        return null;
    }
//
//    private final ObjectMapper mapper;
//
//    Jackson2Serializer() {
//        mapper = new ObjectMapper();
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.registerModule(new JavaTimeModule());
//        mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
//        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
//    }
//
//    @Override
//    public byte[] serialize(Object source) throws SerializationException {
//        if (source == null) {
//            return EMPTY_ARRAY;
//        }
//        try {
//            return mapper.writeValueAsBytes(source);
//        } catch (JsonProcessingException e) {
//            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public Object deserialize(byte[] source, Class type) throws SerializationException {
//        Assert.notNull(type, "Deserialization type must not be null! Please provide Object.class to make use of Jackson2 default typing.");
//        if (source == null) {
//            return NO_DATA;
//        }
//        if (source.length == 0) {
//            return null;
//        }
//        try {
//            return mapper.readValue(source, type);
//        } catch (Exception ex) {
//            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
//        }
//    }
}
