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
import static org.junit.Assert.*;

import org.junit.Test;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DefaultStandardDateFactoryTest {

    @Test
    public void testNewZonedDateTimeWithoutClock() {

        StandardDateFactory factory = new DefaultStandardDateFactory();

        ZonedDateTime now = ZonedDateTime.now().minusDays(1);

        ZonedDateTime result = factory.newZonedDateTime();

        assertThat(result.isEqual(now) || result.isAfter(now), is(true));
        assertThat(result.getZone(), is(ZoneId.systemDefault()));

    }

    @Test
    public void testNewZonedDateTimeWithClock() {

        ZonedDateTime now = ZonedDateTime.now().minusDays(1);
        StandardDateFactory factory = new DefaultStandardDateFactory(Clock
                .fixed(now.toInstant(), ZoneId.systemDefault()));

        ZonedDateTime result = factory.newZonedDateTime();

        assertThat(result, is(now));

    }
}
