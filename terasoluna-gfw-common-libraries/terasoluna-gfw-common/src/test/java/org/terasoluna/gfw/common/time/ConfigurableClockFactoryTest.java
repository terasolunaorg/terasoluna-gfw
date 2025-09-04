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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.FormatStyle;
import org.junit.jupiter.api.Test;

public class ConfigurableClockFactoryTest {

    ClockFactory clockFactory = new ConfigurableClockFactory("2012-09-11T02:25:15");

    @Test
    public void testFixed() throws Exception {

        Clock clock = clockFactory.fixed();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear()).isEqualTo(2012);
        assertThat(now.getMonthValue()).isEqualTo(9);
        assertThat(now.getDayOfMonth()).isEqualTo(11);
        assertThat(now.getHour()).isEqualTo(2);
        assertThat(now.getMinute()).isEqualTo(25);
        assertThat(now.getSecond()).isEqualTo(15);
        assertThat(now.getNano()).isEqualTo(0);
        assertThat(now.getZone()).isEqualTo(ZoneId.systemDefault());

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now)).isTrue();
    }

    @Test
    public void testFixedUTC() throws Exception {

        Clock clock = clockFactory.fixed(ZoneOffset.UTC);

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear()).isEqualTo(2012);
        assertThat(now.getMonthValue()).isEqualTo(9);
        assertThat(now.getDayOfMonth()).isEqualTo(11);
        assertThat(now.getHour()).isEqualTo(2);
        assertThat(now.getMinute()).isEqualTo(25);
        assertThat(now.getSecond()).isEqualTo(15);
        assertThat(now.getNano()).isEqualTo(0);
        assertThat(now.getZone()).isEqualTo(ZoneOffset.UTC);
    }

    @Test
    public void testTick() throws Exception {

        Clock clock = clockFactory.tick();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear()).isEqualTo(2012);
        assertThat(now.getMonthValue()).isEqualTo(9);
        assertThat(now.getDayOfMonth()).isEqualTo(11);
        assertThat(now.getHour()).isEqualTo(2);
        assertThat(now.getMinute()).isEqualTo(25);
        assertThat(now.getSecond()).isEqualTo(15);
        assertThat(now.getNano()).isGreaterThanOrEqualTo(0);
        assertThat(now.getZone()).isEqualTo(ZoneId.systemDefault());

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isAfter(now)).isTrue();
    }

    @Test
    public void testTickUTC() throws Exception {

        Clock clock = clockFactory.tick(ZoneOffset.UTC);

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear()).isEqualTo(2012);
        assertThat(now.getMonthValue()).isEqualTo(9);
        assertThat(now.getDayOfMonth()).isEqualTo(11);
        assertThat(now.getHour()).isEqualTo(2);
        assertThat(now.getMinute()).isEqualTo(25);
        assertThat(now.getSecond()).isEqualTo(15);
        assertThat(now.getNano()).isGreaterThanOrEqualTo(0);
        assertThat(now.getZone()).isEqualTo(ZoneOffset.UTC);

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isAfter(now)).isTrue();
    }

    @Test
    public void testFormatPattern() throws Exception {

        ClockFactory formatPatternClockFactory =
                new ConfigurableClockFactory("2012/09/11 02:25:15", "yyyy/MM/dd HH:mm:ss");
        Clock clock = formatPatternClockFactory.fixed();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear()).isEqualTo(2012);
        assertThat(now.getMonthValue()).isEqualTo(9);
        assertThat(now.getDayOfMonth()).isEqualTo(11);
        assertThat(now.getHour()).isEqualTo(2);
        assertThat(now.getMinute()).isEqualTo(25);
        assertThat(now.getSecond()).isEqualTo(15);
        assertThat(now.getNano()).isEqualTo(0);
        assertThat(now.getZone()).isEqualTo(ZoneId.systemDefault());

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now)).isTrue();
    }

    @Test
    public void testFormatStyle() throws Exception {

        ClockFactory formatStyleClockFactory = new ConfigurableClockFactory("2012/09/11 02:25:15",
                FormatStyle.MEDIUM, FormatStyle.MEDIUM);
        Clock clock = formatStyleClockFactory.fixed();

        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.getYear()).isEqualTo(2012);
        assertThat(now.getMonthValue()).isEqualTo(9);
        assertThat(now.getDayOfMonth()).isEqualTo(11);
        assertThat(now.getHour()).isEqualTo(2);
        assertThat(now.getMinute()).isEqualTo(25);
        assertThat(now.getSecond()).isEqualTo(15);
        assertThat(now.getNano()).isEqualTo(0);
        assertThat(now.getZone()).isEqualTo(ZoneId.systemDefault());

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now)).isTrue();
    }
}
