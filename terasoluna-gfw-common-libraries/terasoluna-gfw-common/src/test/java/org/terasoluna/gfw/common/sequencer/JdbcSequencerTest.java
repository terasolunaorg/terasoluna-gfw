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
package org.terasoluna.gfw.common.sequencer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;

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
public class JdbcSequencerTest {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate.getJdbcOperations().execute(
                "CREATE SEQUENCE TEST_SEQ START WITH 1 INCREMENT BY 1");
    }

    @Test
    public void testGetNext01() {
        // create target
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setDataSource(dataSource);

        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");

        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
        String nextVal1 = jdbcSequencer.getNext();
        String nextVal2 = jdbcSequencer.getNext();

        assertThat(nextVal1, is(String.valueOf(1)));
        assertThat(nextVal2, is(String.valueOf(2)));
    }

    @Test
    public void testGetCurrent01() {
        // create target
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setDataSource(dataSource);

        jdbcSequencer.setCurrentValueQuery("SELECT CURRVAL('TEST_SEQ')");
        jdbcSequencer.setNextValueQuery("SELECT NEXTVAL('TEST_SEQ')");

        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
        String currentVal1 = jdbcSequencer.getCurrent();
        String nextVal = jdbcSequencer.getNext();
        String currentVal2 = jdbcSequencer.getCurrent();

        assertThat(currentVal1, is(String.valueOf(0)));
        assertThat(nextVal, is(String.valueOf(1)));
        assertThat(currentVal2, is(String.valueOf(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_jdbcTemplateIsNullAndDataSourceIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(null);
        jdbcSequencer.setDataSource(null);
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_sequecnceClasIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(null);
        jdbcSequencer.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_NextValueQueryIsEmpty() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_NextValueQueryIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery(null);
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_CurrentValueQueryIsEmpty() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("");
        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_CurrentValueQueryIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery(null);
        jdbcSequencer.setSequenceClass(String.class);
        jdbcSequencer.afterPropertiesSet();
    }
}
