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

import org.junit.Assert;
import org.junit.Test;

import java.time.format.DateTimeParseException;

import static li.allan.easycache.DurationUtils.*;

/**
 * @author lialun
 */
public class DurationUtilsTest {
    @Test
    public void match() {
        Assert.assertTrue(DurationUtils.match("1D"));
        Assert.assertTrue(DurationUtils.match("1H"));
        Assert.assertTrue(DurationUtils.match("1M"));
        Assert.assertTrue(DurationUtils.match("1S"));
        Assert.assertTrue(DurationUtils.match("1D1H1M1S"));
        Assert.assertTrue(DurationUtils.match("1D1M1S"));

        Assert.assertTrue(DurationUtils.match("P1D"));
        Assert.assertTrue(DurationUtils.match("P1H"));
        Assert.assertTrue(DurationUtils.match("P1M"));
        Assert.assertTrue(DurationUtils.match("P1S"));
        Assert.assertTrue(DurationUtils.match("P1DT1H1M1S"));
        Assert.assertTrue(DurationUtils.match("P1M1S"));

        Assert.assertFalse(DurationUtils.match(null));
        Assert.assertFalse(DurationUtils.match(""));
        Assert.assertFalse(DurationUtils.match("1"));
        Assert.assertFalse(DurationUtils.match("1D1S1M"));
    }

    @Test
    public void parse() {
        Assert.assertEquals(SECONDS_PER_DAY, DurationUtils.parseToSeconds("1D"));
        Assert.assertEquals(SECONDS_PER_HOUR, DurationUtils.parseToSeconds("1H"));
        Assert.assertEquals(SECONDS_PER_MINUTE, DurationUtils.parseToSeconds("1M"));
        Assert.assertEquals(1, DurationUtils.parseToSeconds("1S"));
        Assert.assertEquals(SECONDS_PER_DAY + SECONDS_PER_HOUR + SECONDS_PER_MINUTE + 1,
                DurationUtils.parseToSeconds("1D1H1M1S"));
    }

    @Test
    public void parseISO8601() {
        Assert.assertEquals(SECONDS_PER_DAY, DurationUtils.parseToSeconds("P1D"));
        Assert.assertEquals(SECONDS_PER_HOUR, DurationUtils.parseToSeconds("P1H"));
        Assert.assertEquals(SECONDS_PER_MINUTE, DurationUtils.parseToSeconds("P1M"));
        Assert.assertEquals(1, DurationUtils.parseToSeconds("P1S"));
        Assert.assertEquals(SECONDS_PER_DAY + SECONDS_PER_HOUR + SECONDS_PER_MINUTE + 1,
                DurationUtils.parseToSeconds("P1DT1H1M1S"));
    }

    @Test(expected = DateTimeParseException.class)
    public void parseEmpty() {
        DurationUtils.parseToSeconds("");
    }

    @Test(expected = DateTimeParseException.class)
    public void parseNull() {
        DurationUtils.parseToSeconds(null);
    }

    @Test(expected = DateTimeParseException.class)
    public void parseFailure() {
        DurationUtils.parseToSeconds("1");
    }

    @Test(expected = DateTimeParseException.class)
    public void parseWrongNumber() {
        DurationUtils.parseToSeconds(Long.MAX_VALUE + "0D");
    }
}