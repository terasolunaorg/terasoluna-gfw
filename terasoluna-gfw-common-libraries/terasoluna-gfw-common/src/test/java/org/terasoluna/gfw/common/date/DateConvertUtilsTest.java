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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DateConvertUtilsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDateConvertUtils() throws Exception {
        // set up
        Constructor<DateConvertUtils> constructor = DateConvertUtils.class
                .getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());

        constructor.setAccessible(false);
    }

    @Test
    public void testConvertToTimestamp01() {
        // setup a test data
        DateTime date = new DateTime(2012, 9, 3, 23, 7, 11, 100);

        // testing
        Timestamp timestamp = DateConvertUtils.convertToTimestamp(date
                .toDate());

        // assertions
        DateTime result = new DateTime(timestamp.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }

    @Test
    public void testConvertToTimestamp02() {
        // set up a exception assertion
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("date must not be null"));

        // testing
        DateConvertUtils.convertToTimestamp(null);
    }

    @Test
    public void testConvertToSqlDate01() {
        // setup test data
        DateTime date = new DateTime(2012, 9, 3, 23, 7, 11, 100);

        // testing
        java.sql.Date sqlDate = DateConvertUtils.convertToSqlDate(date
                .toDate());

        // assertion
        DateTime result = new DateTime(sqlDate.getTime());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthOfYear(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHourOfDay(), is(0));
        assertThat(result.getMinuteOfHour(), is(0));
        assertThat(result.getSecondOfMinute(), is(0));
        assertThat(result.getMillisOfSecond(), is(0));
    }

    @Test
    public void testConvertToSqlDate02() {
        // set up a exception assertion
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("date must not be null"));

        // testing
        DateConvertUtils.convertToSqlDate(null);
    }

    @Test
    public void testConvertToTime01() {
        // set up a test data
        DateTime date = new DateTime(2012, 9, 3, 23, 7, 11, 100);

        // testing
        Time time = DateConvertUtils.convertToTime(date.toDate());

        // assertion
        DateTime result = new DateTime(time.getTime());
        assertThat(result.getYear(), is(1970));
        assertThat(result.getMonthOfYear(), is(1));
        assertThat(result.getDayOfMonth(), is(1));
        assertThat(result.getHourOfDay(), is(23));
        assertThat(result.getMinuteOfHour(), is(7));
        assertThat(result.getSecondOfMinute(), is(11));
        assertThat(result.getMillisOfSecond(), is(100));
    }

    @Test
    public void testConvertToTime02() {
        // set up a exception assertion
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("date must not be null"));

        // testing
        DateConvertUtils.convertToTime(null);
    }

}
