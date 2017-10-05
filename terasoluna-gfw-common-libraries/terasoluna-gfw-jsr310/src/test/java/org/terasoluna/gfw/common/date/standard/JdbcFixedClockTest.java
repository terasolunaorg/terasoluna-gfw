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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
@Rollback
public class JdbcFixedClockTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    /**
     * normal case 1. <br>
     * use prepared table.
     */
    @Test
    public void testNewZonedDateTimeWithoutClock() throws Exception {
        // crate table
        jdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE system_date(now timestamp NOT NULL)");
        jdbcTemplate.update("INSERT INTO system_date(now) VALUES (:now)",
                Collections.singletonMap("now", LocalDateTime.of(2012, 9, 11, 2,
                        25, 15, 999_999_999)));

        JdbcFixedClock clock = new JdbcFixedClock();
        clock.setDataSource(dataSource);
        clock.setCurrentTimestampQuery("SELECT now FROM system_date");

        StandardDateFactory dateFactory = new DefaultStandardDateFactory(clock);

        ZonedDateTime now = dateFactory.newZonedDateTime();
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(25));
        assertThat(now.getSecond(), is(15));
        assertThat(now.getNano(), is(999999999));
        assertThat(now.getZone(), is(ZoneId.systemDefault()));

        // drop table
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_date");
    }

    /**
     * normal case 2. <br>
     * use literal.
     */
    @Test
    public void testNewZonedDateTimeWithClock() {
        JdbcFixedClock clock = new JdbcFixedClock(ZoneId.of("America/Chicago"));
        clock.setDataSource(dataSource);
        clock.setCurrentTimestampQuery(
                "SELECT Timestamp '2012-09-09 2:33:10.222'");

        StandardDateFactory dateFactory = new DefaultStandardDateFactory(clock
                .withZone(ZoneId.of("America/Los_Angeles")));

        ZonedDateTime now = dateFactory.newZonedDateTime();
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(9));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(33));
        assertThat(now.getSecond(), is(10));
        assertThat(now.getNano(), is(222000000));
        assertThat(now.getZone(), is(ZoneId.of("America/Los_Angeles")));
    }

    /**
     * normal case 3. <br>
     * use literal.
     */
    @Test
    public void testNewZonedDateTimeWithClockSameZone() {
        JdbcFixedClock clock = new JdbcFixedClock(ZoneId.of("America/Chicago"));
        clock.setDataSource(dataSource);
        clock.setCurrentTimestampQuery(
                "SELECT Timestamp '2012-09-09 2:33:10.222'");

        StandardDateFactory dateFactory = new DefaultStandardDateFactory(clock
                .withZone(clock.getZone()));

        ZonedDateTime now = dateFactory.newZonedDateTime();
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthValue(), is(9));
        assertThat(now.getDayOfMonth(), is(9));
        assertThat(now.getHour(), is(2));
        assertThat(now.getMinute(), is(33));
        assertThat(now.getSecond(), is(10));
        assertThat(now.getNano(), is(222000000));
        assertThat(now.getZone(), is(ZoneId.of("America/Chicago")));
    }

    /**
     * abnormal case 1. <br>
     * doesn't return timestamp.
     */
    @Test(expected = DataAccessException.class)
    public void testNewZonedDateTimeQueryResultIsNotTimestamp() throws Exception {

        JdbcFixedClock clock = new JdbcFixedClock();
        clock.setDataSource(dataSource);
        clock.setCurrentTimestampQuery("SELECT 1+1");

        StandardDateFactory dateFactory = new DefaultStandardDateFactory(clock);

        dateFactory.newZonedDateTime();
    }

}
