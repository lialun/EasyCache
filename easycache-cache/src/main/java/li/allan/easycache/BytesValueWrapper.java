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

/**
 * @author lialun
 */
public class BytesValueWrapper {
    private byte[] value;
    private long expireTimestampInMills;

    public BytesValueWrapper(byte[] value, long expireTimestampInMills) {
        this.value = value;
        this.expireTimestampInMills = expireTimestampInMills;
    }

    public BytesValueWrapper(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
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
}
