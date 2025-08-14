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
package org.terasoluna.gfw.common.sequencer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig(locations = {"classpath:test-context.xml"})
@Transactional
@Rollback
// Changed by SPR-13277
public class JdbcSequencerTest {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    public void before() throws Exception {
        jdbcTemplate.getJdbcOperations()
                .execute("CREATE SEQUENCE TEST_SEQ START WITH 1 INCREMENT BY 1");
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

        assertThat(nextVal1).isEqualTo(String.valueOf(1));
        assertThat(nextVal2).isEqualTo(String.valueOf(2));
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
        String nextVal1 = jdbcSequencer.getNext();
        String currentVal1 = jdbcSequencer.getCurrent();
        String nextVal2 = jdbcSequencer.getNext();
        String currentVal2 = jdbcSequencer.getCurrent();

        assertThat(nextVal1).isEqualTo(String.valueOf(1));
        assertThat(currentVal1).isEqualTo(String.valueOf(1));
        assertThat(nextVal2).isEqualTo(String.valueOf(2));
        assertThat(currentVal2).isEqualTo(String.valueOf(2));
    }

    @Test
    public void testAfterPropertiesSet_jdbcTemplateIsNullAndDataSourceIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(null);
        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.setDataSource(null);
        });
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.afterPropertiesSet();
        });
    }

    @Test
    public void testAfterPropertiesSet_sequecnceClasIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(null);

        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.afterPropertiesSet();
        });
    }

    @Test
    public void testAfterPropertiesSet_NextValueQueryIsEmpty() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("");
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.afterPropertiesSet();
        });
    }

    @Test
    public void testAfterPropertiesSet_NextValueQueryIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery(null);
        jdbcSequencer.setCurrentValueQuery("SELECT currval('TEST_SEQ')");
        jdbcSequencer.setSequenceClass(String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.afterPropertiesSet();
        });
    }

    @Test
    public void testAfterPropertiesSet_CurrentValueQueryIsEmpty() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery("");
        jdbcSequencer.setSequenceClass(String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.afterPropertiesSet();
        });
    }

    @Test
    public void testAfterPropertiesSet_CurrentValueQueryIsNull() {
        JdbcSequencer<String> jdbcSequencer = new JdbcSequencer<String>();
        jdbcSequencer.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcSequencer.setNextValueQuery("SELECT nextval('TEST_SEQ')");
        jdbcSequencer.setCurrentValueQuery(null);
        jdbcSequencer.setSequenceClass(String.class);

        assertThrows(IllegalArgumentException.class, () -> {
            jdbcSequencer.afterPropertiesSet();
        });
    }
}
