/*
 * Copyright (C) 2013-2017 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.common.date;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Utility methods for Date relation conversions.
 */
public final class DateConvertUtils {

    /**
     * Calendar default year.
     */
    private static final int DEFAULT_YEAR = 1970;

    /**
     * Default Constructor.
     */
    private DateConvertUtils() {
    };

    /**
     * Converts java.util.Date to java.sql.Timestamp<br>
     * @param date before
     * @return after
     * @throws IllegalArgumentException if the date is null
     */
    public static Timestamp convertToTimestamp(java.util.Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * Converts java.util.Date to java.sql.Date<br>
     * <p>
     * normalize hour,minute,second,milliSecond to 0.
     * </p>
     * @param date before
     * @return after
     * @throws IllegalArgumentException if the date is null
     */
    public static java.sql.Date convertToSqlDate(java.util.Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
        return sqlDate;
    }

    /**
     * Converts java.util.Date to java.sql.Time<br>
     * <p>
     * normalize year=1970,,month=1,day=1.
     * </p>
     * @param date before
     * @return after
     * @throws IllegalArgumentException if the date is null
     */
    public static Time convertToTime(java.util.Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, DEFAULT_YEAR);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Time time = new Time(cal.getTimeInMillis());
        return time;
    }
}
