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

import java.time.Clock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;

public class DefaultClockFactoryTest {

    ClockFactory clockFactory = new DefaultClockFactory();

    @Test
    public void testFixed() throws Exception {

        Clock clock = clockFactory.fixed();

        ZonedDateTime standard = ZonedDateTime.now();
        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.withNano(0).isEqual(standard.withNano(0)), is(true));
        assertThat(now.getZone(), is(standard.getZone()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isEqual(now), is(true));
    }

    @Test
    public void testFixedUTC() throws Exception {

        Clock clock = clockFactory.fixed(ZoneOffset.UTC);

        ZonedDateTime standard = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.withNano(0).isEqual(standard.withNano(0)), is(true));
        assertThat(now.getZone(), is(standard.getZone()));
    }

    @Test
    public void testTick() throws Exception {

        Clock clock = clockFactory.tick();

        ZonedDateTime standard = ZonedDateTime.now();
        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.withNano(0).isEqual(standard.withNano(0)), is(true));
        assertThat(now.getZone(), is(standard.getZone()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isAfter(now), is(true));
    }

    @Test
    public void testTickUTC() throws Exception {

        Clock clock = clockFactory.tick(ZoneOffset.UTC);

        ZonedDateTime standard = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime now = ZonedDateTime.now(clock);
        assertThat(now.withNano(0).isEqual(standard.withNano(0)), is(true));
        assertThat(now.getZone(), is(standard.getZone()));

        Thread.sleep(100);

        ZonedDateTime now2 = ZonedDateTime.now(clock);
        assertThat(now2.isAfter(now), is(true));
    }
}
