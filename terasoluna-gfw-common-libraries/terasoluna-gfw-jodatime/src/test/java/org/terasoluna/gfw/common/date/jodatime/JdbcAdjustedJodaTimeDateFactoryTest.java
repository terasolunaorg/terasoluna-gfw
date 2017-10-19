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
import org.springframework.jdbc.core.JdbcTemplate;
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
public class JdbcAdjustedJodaTimeDateFactoryTest {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        // crate table
        jdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE system_adjusted_date(diff long)");
    }

    @After
    public void tearDown() throws Exception {
        // drop table
        jdbcTemplate.getJdbcOperations().execute(
                "DROP TABLE system_adjusted_date");
    }

    @Test
    public void testNewDateTime01() throws Exception {

        jdbcTemplate.update(
                "INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
                Collections.singletonMap("diff", 30)); // plus 30 minute

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as minutes

        DateTime now = new DateTime();
        DateTime result = dateFactory.newDateTime();

        assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                / 60.0 / 1000.0), is(30));
    }

    @Test
    public void testNewDateTime03() throws Exception {

        jdbcTemplate.update(
                "INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
                Collections.singletonMap("diff", 30)); // plus 30 minute

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setUseCache(false);
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as milliseconds
        dateFactory.afterPropertiesSet();

        {
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(30)); // plus 30 minute

        }
        {
            jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff",
                    Collections.singletonMap("diff", 60)); // minus 60 minute
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(60));// plus 60 minute
        }
    }

    @Test
    public void testNewDateTime04() throws Exception {

        jdbcTemplate.update(
                "INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
                Collections.singletonMap("diff", 30)); // plus 30 minute

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setUseCache(true);
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as minutes
        dateFactory.afterPropertiesSet();

        {
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(30)); // plus 30 minute

        }
        {
            jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff",
                    Collections.singletonMap("diff", 60)); // plus 60 minute
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(30));// still plus 30 minute
        }
        {
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(30));// still plus 30 minute
        }
    }

    @Test
    public void testNewDateTime05() throws Exception {

        jdbcTemplate.update(
                "INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
                Collections.singletonMap("diff", 30)); // plus 30 minute

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setUseCache(true);
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as minutes
        dateFactory.afterPropertiesSet();

        {
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(30)); // plus 30 minute

        }
        {
            jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff",
                    Collections.singletonMap("diff", 60)); // plus 60 minute
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(30));// still plus 30 minute
        }
        {
            dateFactory.reload(); // reload!
            DateTime now = new DateTime();
            DateTime result = dateFactory.newDateTime();

            assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                    / 60.0 / 1000.0), is(60));// plus 60 minute
        }
    }

    /**
     * Testing the case when adjustment value fetched from DB is null.
     * @throws Exception
     */
    @Test
    public void testNewDateTime06() throws Exception {

        // added data to db table
        jdbcTemplate.update(
                "INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
                Collections.singletonMap("diff", null)); // setting empty string

        // created datefactory instance
        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setDataSource(dataSource);
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as minutes

        // created comparision targets
        DateTime now = new DateTime();
        DateTime result = dateFactory.newDateTime();

        // asserts
        assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                / 60.0 / 1000.0), is(0));
    }

    @Test
    public void testNewDateTime_setJdbcTemaplteDirectly() throws Exception {

        jdbcTemplate.update(
                "INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
                Collections.singletonMap("diff", 30)); // plus 30 minute

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setJdbcTemplate(new JdbcTemplate(dataSource));
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as minutes

        DateTime now = new DateTime();
        DateTime result = dateFactory.newDateTime();

        assertThat((int) (Math.round(result.getMillis() - now.getMillis())
                / 60.0 / 1000.0), is(30));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterProperitesSet_jdbcTemplateAndDataSourceIsNull() throws Exception {

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setAdjustedValueQuery(
                "SELECT diff * 60 * 1000 FROM system_adjusted_date");
        dateFactory.setDataSource(null);
        dateFactory.setJdbcTemplate(null);

        dateFactory.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterProperitesSet_adjustedValueQueryIsNull() throws Exception {

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setAdjustedValueQuery(null);
        dateFactory.setDataSource(dataSource);

        dateFactory.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterProperitesSet_adjustedValueQueryIsEmpty() throws Exception {

        JdbcAdjustedJodaTimeDateFactory dateFactory = new JdbcAdjustedJodaTimeDateFactory();
        dateFactory.setAdjustedValueQuery("");
        dateFactory.setDataSource(dataSource);

        dateFactory.afterPropertiesSet();

    }
}
