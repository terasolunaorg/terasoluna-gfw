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
package org.terasoluna.gfw.common.time;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.junit.Test;

public class ConfigurableClockFactoryTest {

    ClockFactory clockFactory = new ConfigurableClockFactory("2012-09-11T02:25:15");

    @Test
    public void testFixed() throws Exception {

        Clock clock = clockFactory.fixed();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), is(0));
        assertThat(now.getZone(), is(ZoneId.systemDefault()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now), is(true));
    }

    @Test
    public void testFixedUTC() throws Exception {

        Clock clock = clockFactory.fixed(ZoneOffset.UTC);

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), is(0));
        assertThat(now.getZone(), is(ZoneOffset.UTC));
    }

    @Test
    public void testTick() throws Exception {

        Clock clock = clockFactory.tick();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), greaterThanOrEqualTo(0));
        assertThat(now.getZone(), is(ZoneId.systemDefault()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isAfter(now), is(true));
    }

    @Test
    public void testTickUTC() throws Exception {

        Clock clock = clockFactory.tick(ZoneOffset.UTC);

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), greaterThanOrEqualTo(0));
        assertThat(now.getZone(), is(ZoneOffset.UTC));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isAfter(now), is(true));
    }

    @Test
    public void testFormatPattern() throws Exception {

        ClockFactory formatPatternClockFactory =
                new ConfigurableClockFactory("2012/09/11 02:25:15", "yyyy/MM/dd HH:mm:ss");
        Clock clock = formatPatternClockFactory.fixed();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), is(0));
        assertThat(now.getZone(), is(ZoneId.systemDefault()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now), is(true));
    }

    @Test
    public void testFormatStyle() throws Exception {

        LocalDateTime localDateTime = LocalDateTime.of(2012, 9, 11, 2, 25, 15);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        String formattedDateTime = localDateTime.format(dateTimeFormatter);
        ClockFactory formatStyleClockFactory = new ConfigurableClockFactory(
                formattedDateTime, FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        Clock clock = formatStyleClockFactory.fixed();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), is(0));
        assertThat(now.getZone(), is(ZoneId.systemDefault()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now), is(true));
    }
}
