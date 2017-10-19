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
package org.terasoluna.gfw.common.date.jodatime;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Collections;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
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
// Changed by SPR-13277
public class JdbcFixedJodaTimeDateFactoryTest {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * normal case 1. <br>
     * use prepared table.
     */
    @Test
    public void testNewDateTime01() throws Exception {
        // crate table
        jdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE system_date(now timestamp NOT NULL)");
        jdbcTemplate.update("INSERT INTO system_date(now) VALUES (:now)",
                Collections.singletonMap("now",
                        new DateTime(2012, 9, 11, 2, 25, 15, 100).toDate()));

        JdbcFixedJodaTimeDateFactory dateFactory = new JdbcFixedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setCurrentTimestampQuery("SELECT now FROM system_date");

        DateTime now = dateFactory.newDateTime();
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthOfYear(), is(9));
        assertThat(now.getDayOfMonth(), is(11));
        assertThat(now.getHourOfDay(), is(2));
        assertThat(now.getMinuteOfHour(), is(25));
        assertThat(now.getSecondOfMinute(), is(15));
        assertThat(now.getMillisOfSecond(), is(100));

        // drop table
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_date");
    }

    /**
     * normal case 2. <br>
     * use literal.
     */
    @Test
    public void testNewDateTime02() throws Exception {
        JdbcFixedJodaTimeDateFactory dateFactory = new JdbcFixedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setCurrentTimestampQuery(
                "SELECT Timestamp '2012-09-09 2:33:10.222'");

        DateTime now = dateFactory.newDateTime();
        assertThat(now.getYear(), is(2012));
        assertThat(now.getMonthOfYear(), is(9));
        assertThat(now.getDayOfMonth(), is(9));
        assertThat(now.getHourOfDay(), is(2));
        assertThat(now.getMinuteOfHour(), is(33));
        assertThat(now.getSecondOfMinute(), is(10));
        assertThat(now.getMillisOfSecond(), is(222));
    }

    /**
     * abnormal case 1. <br>
     * doesn't return timestamp.
     */
    @Test(expected = DataAccessException.class)
    public void testNewDateTime03() throws Exception {
        JdbcFixedJodaTimeDateFactory dateFactory = new JdbcFixedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setCurrentTimestampQuery("SELECT 1+1");

        dateFactory.newDateTime();
    }
}
