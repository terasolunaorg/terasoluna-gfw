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
package org.terasoluna.gfw.common.codelist;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
@Rollback
// Changed by SPR-13277
public class JdbcCodeListTest {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    private Map<String, String> mapInput = new HashMap<String, String>();

    @Before
    public void setUp() throws Exception {
        jdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE codelist(code_id character varying(3) NOT NULL, code_name character varying(50),CONSTRAINT pk_code_id PRIMARY KEY (code_id))");
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code_id", String.format("%03d", i));
            map.put("code_name", String.format("label%03d", i));
            jdbcTemplate.update(
                    "INSERT INTO codelist (code_id, code_name) VALUES (:code_id, :code_name)",
                    map);
            mapInput.put(String.format("%03d", i), String.format("label%03d",
                    i));
        }
    }

    @After
    public void tearDown() throws Exception {
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE codelist");
    }

    /**
     * check retrieveMap method for normal return
     */
    @Test
    public void testRetrieveMap01() {

        // setup target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        Map<String, String> mapOutput = jdbcCodeList.retrieveMap();

        assertThat(mapOutput.size(), is(mapInput.size()));
        for (int i = 0; i < 10; i++) {
            assertThat(mapOutput.get(String.format("%03d", i)), is(mapInput.get(
                    String.format("%03d", i))));
        }

    }

    /**
     * check retrieveMap method in case of Data Access related exception
     */
    @Test(expected = BadSqlGrammarException.class)
    public void testRetrieveMap02() {

        // setup target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql(
                "Select code_id, code_name_temp from codelist");

        Map<String, String> mapOutput = jdbcCodeList.retrieveMap();

        assertThat(mapOutput.size(), is(mapInput.size()));
        for (int i = 0; i < 10; i++) {
            assertThat(mapOutput.get("%03d"), is(mapInput.get("%03d")));
        }

    }

    /**
     * check retrieveMap method.　Setting jdbcTemplate instead of dataSource.
     */
    @Test
    public void testRetrieveMap03() {

        // setup target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setJdbcTemplate(new JdbcTemplate(dataSource));
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        Map<String, String> mapOutput = jdbcCodeList.retrieveMap();

        assertThat(mapOutput.size(), is(mapInput.size()));
        for (int i = 0; i < 10; i++) {
            assertThat(mapOutput.get(String.format("%03d", i)), is(mapInput.get(
                    String.format("%03d", i))));
        }

    }

    @Test
    public void testRetrieveKeyAndValueNull() {
        // setup target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("valueColumn");
        jdbcCodeList.setValueColumn("labelColumn");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        Map<String, String> mapOutput = jdbcCodeList.retrieveMap();

        // assert
        assertThat(mapOutput.size(), is(0));

    }

    @Test
    public void testRetrieveValueNull() {
        // setup target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("labelColumn");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        Map<String, String> mapOutput = jdbcCodeList.retrieveMap();

        // assert
        assertThat(mapOutput.size(), is(0));

    }

    @Test
    public void testRetrieveKeyNull() {
        // setup target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("valueColumn");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        Map<String, String> mapOutput = jdbcCodeList.retrieveMap();

        // assert
        assertThat(mapOutput.size(), is(0));

    }

    /**
     * In case LazyInit is set to false
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAfterPropertiesSet01() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        // fetch exposed map for the first time

        Field f = ReflectionUtils.findField(JdbcCodeList.class, "exposedMap");
        ReflectionUtils.makeAccessible(f);
        Map<String, String> exposedMapFirstFetch = (Map<String, String>) f.get(
                jdbcCodeList);

        // assert
        assertNull(exposedMapFirstFetch);

        jdbcCodeList.afterPropertiesSet();

        Map<String, String> exposedMapSecondFetch = (Map<String, String>) f.get(
                jdbcCodeList);
        // assert
        assertThat(exposedMapSecondFetch.size(), is(mapInput.size()));
        for (String key : exposedMapSecondFetch.keySet()) {
            assertThat(exposedMapSecondFetch.get(key), is(mapInput.get(key)));
        }
    }

    /**
     * In case LazyInit is set to true
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAfterPropertiesSet02() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLazyInit(true);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("Select code_id, code_name from codelist");

        // fetch exposed map for the first time

        Field f = ReflectionUtils.findField(JdbcCodeList.class, "exposedMap");
        ReflectionUtils.makeAccessible(f);
        Map<String, String> exposedMapFirstFetch = (Map<String, String>) f.get(
                jdbcCodeList);

        // assert
        assertNull(exposedMapFirstFetch);

        // run
        jdbcCodeList.afterPropertiesSet();

        // assert again
        assertNull(exposedMapFirstFetch);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_querySqlIsNull() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql(null);

        jdbcCodeList.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_querySqlIsEmpty() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters\
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("");

        jdbcCodeList.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_labelColumnIsNull() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn(null);
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("select code_id, code_name from codelist");
        jdbcCodeList.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_labelColumnIsEmpty() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("select code_id, code_name from codelist");
        jdbcCodeList.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_valueColumnIsNull() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn(null);
        jdbcCodeList.setQuerySql("select code_id, code_name from codelist");
        jdbcCodeList.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_valueColumnIsEmpty() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("");
        jdbcCodeList.setQuerySql("select code_id, code_name from codelist");
        jdbcCodeList.afterPropertiesSet();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet_dataSourceAndJdbcTemplateAreNull() throws Exception {
        // create target
        JdbcCodeList jdbcCodeList = new JdbcCodeList();

        // setup parameters
        jdbcCodeList.setLabelColumn("code_name");
        jdbcCodeList.setValueColumn("code_id");
        jdbcCodeList.setQuerySql("select code_id, code_name from codelist");
        jdbcCodeList.afterPropertiesSet();

    }

}
