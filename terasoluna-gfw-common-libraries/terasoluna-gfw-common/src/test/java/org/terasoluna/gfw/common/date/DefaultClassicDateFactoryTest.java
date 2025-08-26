/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

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
        assertThat(result.getYear()).isEqualTo(2012);
        assertThat(result.getMonthOfYear()).isEqualTo(9);
        assertThat(result.getDayOfMonth()).isEqualTo(3);
        assertThat(result.getHourOfDay()).isEqualTo(23);
        assertThat(result.getMinuteOfHour()).isEqualTo(7);
        assertThat(result.getSecondOfMinute()).isEqualTo(11);
        assertThat(result.getMillisOfSecond()).isEqualTo(100);

        // use actual #newDate().
        DefaultClassicDateFactory actualFactory = new DefaultClassicDateFactory();
        Date actualNow = actualFactory.newDate();
        LocalDateTime actualResult =
                actualNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        assertThat(actualResult.isAfter(LocalDateTime.now().minusDays(1))).isEqualTo(true);
    }

    @Test
    public void testNewTimeStamp() {
        Timestamp now = factory.newTimestamp();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear()).isEqualTo(2012);
        assertThat(result.getMonthOfYear()).isEqualTo(9);
        assertThat(result.getDayOfMonth()).isEqualTo(3);
        assertThat(result.getHourOfDay()).isEqualTo(23);
        assertThat(result.getMinuteOfHour()).isEqualTo(7);
        assertThat(result.getSecondOfMinute()).isEqualTo(11);
        assertThat(result.getMillisOfSecond()).isEqualTo(100);
    }

    @Test
    public void testNewSqlDate() {
        java.sql.Date now = factory.newSqlDate();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear()).isEqualTo(2012);
        assertThat(result.getMonthOfYear()).isEqualTo(9);
        assertThat(result.getDayOfMonth()).isEqualTo(3);
        assertThat(result.getHourOfDay()).isEqualTo(0);
        assertThat(result.getMinuteOfHour()).isEqualTo(0);
        assertThat(result.getSecondOfMinute()).isEqualTo(0);
        assertThat(result.getMillisOfSecond()).isEqualTo(0);
    }

    @Test
    public void testNewTime() {
        Time now = factory.newTime();
        DateTime result = new DateTime(now.getTime());
        assertThat(result.getYear()).isEqualTo(1970);
        assertThat(result.getMonthOfYear()).isEqualTo(1);
        assertThat(result.getDayOfMonth()).isEqualTo(1);
        assertThat(result.getHourOfDay()).isEqualTo(23);
        assertThat(result.getMinuteOfHour()).isEqualTo(7);
        assertThat(result.getSecondOfMinute()).isEqualTo(11);
        assertThat(result.getMillisOfSecond()).isEqualTo(100);
    }
}
