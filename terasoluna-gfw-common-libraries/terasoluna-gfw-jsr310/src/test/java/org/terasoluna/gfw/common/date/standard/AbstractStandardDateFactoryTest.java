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
package org.terasoluna.gfw.common.date.standard;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;

public class AbstractStandardDateFactoryTest {

    @Test
    public void testNewDate() {
        StubDateFactory factory = new StubDateFactory();
        Date now = factory.newDate();
        ZonedDateTime result = ZonedDateTime.ofInstant(now.toInstant(), ZoneId
                .systemDefault());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthValue(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHour(), is(23));
        assertThat(result.getMinute(), is(7));
        assertThat(result.getSecond(), is(11));
        assertThat(result.getNano(), is(999000000));
    }

    @Test
    public void testNewTimeStamp() {
        StubDateFactory factory = new StubDateFactory();
        Timestamp now = factory.newTimestamp();
        ZonedDateTime result = ZonedDateTime.ofInstant(now.toInstant(), ZoneId
                .systemDefault());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthValue(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHour(), is(23));
        assertThat(result.getMinute(), is(7));
        assertThat(result.getSecond(), is(11));
        assertThat(result.getNano(), is(999000000));
    }

    @Test
    public void testNewSqlDate() {
        StubDateFactory factory = new StubDateFactory();
        java.sql.Date now = factory.newSqlDate();
        ZonedDateTime result = ZonedDateTime.ofInstant(new Date(now.getTime())
                .toInstant(), ZoneId.systemDefault());
        assertThat(result.getYear(), is(2012));
        assertThat(result.getMonthValue(), is(9));
        assertThat(result.getDayOfMonth(), is(3));
        assertThat(result.getHour(), is(0));
        assertThat(result.getMinute(), is(0));
        assertThat(result.getSecond(), is(0));
        assertThat(result.getNano(), is(0));
    }

    @Test
    public void testNewTime() {
        StubDateFactory factory = new StubDateFactory();
        Time now = factory.newTime();
        ZonedDateTime result = ZonedDateTime.ofInstant(new Date(now.getTime())
                .toInstant(), ZoneId.systemDefault());
        assertThat(result.getYear(), is(1970));
        assertThat(result.getMonthValue(), is(1));
        assertThat(result.getDayOfMonth(), is(1));
        assertThat(result.getHour(), is(23));
        assertThat(result.getMinute(), is(7));
        assertThat(result.getSecond(), is(11));
        assertThat(result.getNano(), is(999000000));
    }

    private static class StubDateFactory extends AbstractStandardDateFactory {
        @Override
        public ZonedDateTime newZonedDateTime() {
            return ZonedDateTime.of(2012, 9, 3, 23, 7, 11, 999_999_999, ZoneId
                    .systemDefault());
        }
    }

}
