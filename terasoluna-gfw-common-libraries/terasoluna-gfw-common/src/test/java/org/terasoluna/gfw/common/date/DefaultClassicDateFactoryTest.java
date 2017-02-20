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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DefaultClassicDateFactoryTest {
    DefaultClassicDateFactory factory = new DefaultClassicDateFactory() {
        @Override
        public Date newDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2012);
            calendar.set(Calendar.MONTH, 9 - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 3);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 7);
            calendar.set(Calendar.SECOND, 11);
            calendar.set(Calendar.MILLISECOND, 100);
            return calendar.getTime();
        }
    };

    @Test
    public void testNewDate() {
        Date now = factory.newDate();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }

    @Test
    public void testNewTimeStamp() {
        Timestamp now = factory.newTimestamp();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }

    @Test
    public void testNewSqlDate() {
        java.sql.Date now = factory.newSqlDate();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(0));
        assertThat(result.getMinuteOfHour(), is(0));
        assertThat(result.getSecondOfMinute(), is(0));
        assertThat(result.getMillisOfSecond(), is(0));
    }

    @Test
    public void testNewTime() {
        Time now = factory.newTime();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear(), is(1970));
        assertThat(result.getMonthOfYear(), is(1));
        assertThat(result.getDayOfMonth(), is(1));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }
}
