/*
 * Copyright (C) 2013-2015 terasoluna.org
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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.Time;
import java.sql.Timestamp;
import org.junit.Test;

public class DateConvertUtilsTest {

    @Test
    public void testConvertToTimestamp01() throws Exception {
        DateTime date = new DateTime(2012, 9, 3, 23, 7, 11, 100);
        Timestamp timestamp = DateConvertUtils
                .convertToTimestamp(date.toDate());
        DateTime result = new DateTime(timestamp.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertToTimestamp02() throws Exception {
        DateConvertUtils.convertToTimestamp(null);
    }

    @Test
    public void testConvertToSqlDate01() throws Exception {
        DateTime date = new DateTime(2012, 9, 3, 23, 7, 11, 100);
        java.sql.Date sqlDate = DateConvertUtils
                .convertToSqlDate(date.toDate());
        DateTime result = new DateTime(sqlDate.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(0));
        assertThat(result.getMinuteOfHour(), is(0));
        assertThat(result.getSecondOfMinute(), is(0));
        assertThat(result.getMillisOfSecond(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertToSqlDate02() throws Exception {
        DateConvertUtils.convertToSqlDate(null);
    }

    @Test
    public void testConvertToTime01() throws Exception {
        DateTime date = new DateTime(2012, 9, 3, 23, 7, 11, 100);
        Time time = DateConvertUtils.convertToTime(date.toDate());
        DateTime result = new DateTime(time.getTime());
        assertThat(result.getYear(), is(1970));
        assertThat(result.getMonthOfYear(), is(1));
        assertThat(result.getDayOfMonth(), is(1));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertToTime02() throws Exception {
        DateConvertUtils.convertToTime(null);
    }

}
