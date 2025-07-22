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
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig(locations = {"classpath:test-context.xml"})
@Transactional
public class JdbcClockFactoryTest {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    ClockFactory clockFactory;

    @BeforeEach
    public void before() {
        // crate table
        jdbcTemplate.getJdbcOperations()
                .execute("CREATE TABLE system_date(now timestamp NOT NULL)");
        jdbcTemplate.update("INSERT INTO system_date(now) VALUES (:now)",
                Collections.singletonMap("now", LocalDateTime.of(2012, 9, 11, 2, 25, 15, 0)));

        clockFactory = new JdbcClockFactory(dataSource, "SELECT now FROM system_date");
    }

    @AfterEach
    public void after() {
        // drop table
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_date");
    }

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
    public void testInstantNull() throws Exception {
        clockFactory = new JdbcClockFactory(dataSource, "SELECT null FROM system_date");
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            clockFactory.fixed();
        });
        assertThat(e.getMessage(), is("Failed to retrieve current timestamp from database"));
    }
}
