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

import com.google.common.base.Strings;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lialun
 */
public class DurationUtils {
    /**
     * Hours per day.
     */
    static final int HOURS_PER_DAY = 24;
    /**
     * Minutes per hour.
     */
    static final int MINUTES_PER_HOUR = 60;
    /**
     * Seconds per minute.
     */
    static final int SECONDS_PER_MINUTE = 60;
    /**
     * Seconds per hour.
     */
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    /**
     * Seconds per day.
     */

    static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;

    private static Pattern PATTERN = Pattern.compile("P?(?:([0-9]+)D)?T?(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)S)?",
            Pattern.CASE_INSENSITIVE);

    public static boolean match(String text) {
        return !Strings.isNullOrEmpty(text) && PATTERN.matcher(text).matches();
    }

    public static long parseToSeconds(String text) {
        if (Strings.isNullOrEmpty(text)) {
            throw new DateTimeParseException("Null or empty text cannot be parsed to a Duration: ", "", 0);
        }
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            String dayMatch = matcher.group(1);
            String hourMatch = matcher.group(2);
            String minuteMatch = matcher.group(3);
            String secondMatch = matcher.group(4);
            if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
                long daysAsSecs = parseNumber(text, dayMatch, SECONDS_PER_DAY, "days");
                long hoursAsSecs = parseNumber(text, hourMatch, SECONDS_PER_HOUR, "hours");
                long minutesAsSecs = parseNumber(text, minuteMatch, SECONDS_PER_MINUTE, "minutes");
                long seconds = parseNumber(text, secondMatch, 1, "seconds");
                return daysAsSecs + hoursAsSecs + minutesAsSecs + seconds;
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Duration: ", text, 0);
    }

    private static long parseNumber(CharSequence text, String parsed, int multiplier, String errorText) {
        // regex limits to [-+]?[0-9]+
        if (parsed == null) {
            return 0;
        }
        try {
            long val = Long.parseLong(parsed);
            return Math.multiplyExact(val, multiplier);
        } catch (NumberFormatException | ArithmeticException ex) {
            throw (DateTimeParseException) new DateTimeParseException("Text cannot be parsed to a Duration: " + errorText, text, 0).initCause(ex);
        }
    }
}
