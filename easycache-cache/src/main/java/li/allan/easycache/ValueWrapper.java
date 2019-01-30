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

import java.io.Serializable;
import java.util.Objects;

/**
 * @author lialun
 */
public class ValueWrapper<V> implements Serializable {
    private V value;
    private long expireTimestampInMills;

    public ValueWrapper(V value, long expireTimestampInMills) {
        this.value = value;
        this.expireTimestampInMills = expireTimestampInMills;
    }

    public ValueWrapper(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public long getExpireTimestampInMills() {
        return expireTimestampInMills;
    }

    @Override
    public String toString() {
        return "ValueWrapper{" +
                "value=" + value +
                ", expireTimestampInMills=" + expireTimestampInMills +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueWrapper<?> that = (ValueWrapper<?>) o;
        return expireTimestampInMills == that.expireTimestampInMills &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, expireTimestampInMills);
    }
}
