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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    static class CalendarWrapper {
        Calendar cal;

        public boolean isAfter(final CalendarWrapper dateWrapper) {
            return cal.after(dateWrapper.getCal());
        }

        public boolean isBefore(final CalendarWrapper dateWrapper) {
            return cal.before(dateWrapper.getCal());
        }

        public CalendarWrapper addSecond(final int second) {
            cal.add(Calendar.SECOND, second);
            return this;
        }

        public CalendarWrapper addMinute(final int minute) {
            cal.add(Calendar.MINUTE, minute);
            return this;
        }

        public CalendarWrapper addHour(final int hour) {
            cal.add(Calendar.HOUR, hour);
            return this;
        }

        public CalendarWrapper addDay(final int day) {
            cal.add(Calendar.DATE, day);
            return this;
        }

        public CalendarWrapper addMonth(final int month) {
            cal.add(Calendar.MONTH, month);
            return this;
        }

        public int diffTime(final CalendarWrapper calendarWrapper) {
            return TimeUtils.diffTime(this, calendarWrapper);
        }

        private Calendar getCal() {
            return cal;
        }

        public CalendarWrapper(Calendar cal) {
            this.cal = cal;
        }

        @Override
        public String toString() {
            return String.valueOf(TimeUtils.diffTime(this, now()));
        }
    }

    public static CalendarWrapper now() {
        Calendar cal = Calendar.getInstance();
        return new CalendarWrapper(cal);
    }

    public static CalendarWrapper today() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new CalendarWrapper(cal);
    }

    public static CalendarWrapper tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new CalendarWrapper(cal);
    }

    public static int diffTime(final CalendarWrapper c1, final CalendarWrapper c2) {
        long diffTime = c1.getCal().getTimeInMillis() - c2.getCal().getTimeInMillis();
        return (diffTime > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (diffTime > 0 ? (int) (diffTime / 1000) : 0);
    }

    /**
     * @param timeStr
     * @return
     */
    public static boolean isDurationString(String timeStr) {
        return timeStr.matches("[\\d+[yMwdhms]]+");
    }


    private static final Pattern timeStringPattern = Pattern.compile("\\d+[yMwdhms]");

    /**
     * parseToSeconds duration string like 'nYnMnDnHnMnS' to seconds
     *
     * @param timeStr duration string like 'nYnMnDnHnMnS'
     * @return duration in seconds
     */
    public static int parseDurationString(String timeStr) {
        int seconds = 0;
        Matcher matcher = timeStringPattern.matcher(timeStr);
        while (matcher.find()) {
            String s = matcher.group();
            String[] parts = s.split("(?<=[yMwdhms])(?=\\d)|(?<=\\d)(?=[yMwdhms])");
            int numb = Integer.parseInt(parts[0]);
            String type = parts[1];
            switch (type) {
                case "s":
                    seconds = seconds + (numb);
                    break;
                case "m":
                    seconds = seconds + (numb * 60);
                    break;
                case "h":
                    seconds = seconds + (numb * 60 * 60);
                    break;
                case "d":
                    seconds = seconds + (numb * 60 * 60 * 24);
                    break;
                case "w":
                    seconds = seconds + (numb * 60 * 60 * 24 * 7);
                    break;
                case "M":
                    seconds = seconds + (numb * 60 * 60 * 24 * 30);
                    break;
                case "y":
                    seconds = seconds + (numb * 60 * 60 * 24 * 365);
                    break;
                default:
            }
        }
        return seconds;
    }
}
