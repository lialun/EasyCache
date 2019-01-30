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

import li.allan.easycache.remote.Constants;
import li.allan.easycache.remote.serializer.exception.SerializationException;

import java.io.*;

import static li.allan.easycache.remote.Constants.NULL_OBJECT;


/**
 * @author lialun
 */
public class JdkSerializer extends Serializer implements Serializable {

    @Override
    public <T> Object deserialize(byte[] source, Class<T> type) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteStream)) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof Constants.NullObject) {
                return null;
            }
            return obj;
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize object type", e);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            object = NULL_OBJECT;
        }
        if (!(object instanceof Serializable)) {
            throw new SerializationException(getClass().getSimpleName() + " requires a Serializable payload " +
                    "but received an object of type [" + object.getClass().getName() + "]");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
