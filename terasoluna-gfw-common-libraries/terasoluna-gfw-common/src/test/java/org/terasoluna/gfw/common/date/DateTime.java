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

import java.util.Calendar;
import java.util.Date;

/**
 * DateTime class which imitates org.joda.time
 */
public class DateTime {
    private final Calendar calendar;

    /**
     * Create from the specified timestamp
     * @param timestamp timestamp
     */
    public DateTime(long timestamp) {
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(timestamp);
    }

    /**
     * Create from the specified date
     * @param year year
     * @param month month
     * @param dayOfMonth day
     * @param hourOfDay hour
     * @param minute minute
     * @param second second
     * @param millisecond mill second
     */
    public DateTime(int year, int month, int dayOfMonth, int hourOfDay,
            int minute, int second, int millisecond) {
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.YEAR, year);
        this.calendar.set(Calendar.MONTH, month - 1);
        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.calendar.set(Calendar.MINUTE, minute);
        this.calendar.set(Calendar.SECOND, second);
        this.calendar.set(Calendar.MILLISECOND, millisecond);
    }

    /**
     * return year
     * @return year
     */
    public int getYear() {
        return this.calendar.get(Calendar.YEAR);
    }

    /**
     * returns month
     * @return month
     */
    public int getMonthOfYear() {
        return this.calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * returns day
     * @return day
     */
    public int getDayOfMonth() {
        return this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * returns hour
     * @return hour
     */
    public int getHourOfDay() {
        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * returns minute
     * @return minute
     */
    public int getMinuteOfHour() {
        return this.calendar.get(Calendar.MINUTE);
    }

    /**
     * returns second
     * @return second
     */
    public int getSecondOfMinute() {
        return this.calendar.get(Calendar.SECOND);
    }

    /**
     * returns mill second
     * @return mill second
     */
    public int getMillisOfSecond() {
        return this.calendar.get(Calendar.MILLISECOND);
    }

    /**
     * converts to {@code java.util.Date}
     * @return date
     */
    public Date toDate() {
        return this.calendar.getTime();
    }
}
