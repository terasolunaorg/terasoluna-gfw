/*
 * Copyright(c) 2013 NTT DATA Corporation.
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
package org.terasoluna.gfw.common.codelist.i18n;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.codelist.JdbcCodeList;
import org.terasoluna.gfw.common.codelist.ReloadableCodeList;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
@Rollback
// Changed by SPR-13277
public class SimpleReloadableI18nCodeListTest {

    static final Map<Locale, String> locales = ImmutableMap.of(Locale.ENGLISH,
            "label%03d", Locale.JAPANESE, "ラベル%03d");

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    Table<Locale, String, String> tableInput;

    SimpleReloadableI18nCodeList reloadableI18nCodeList;

    JdbcCodeList codeListEnglish;

    JdbcCodeList codeListJapanese;

    private Locale originalLocale;

    @Before
    public void before() throws Exception {
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        tableInput = HashBasedTable.create();
        for (Entry<Locale, String> locale : locales.entrySet()) {
            for (int i = 0; i < 10; i++) {
                tableInput.put(locale.getKey(), String.format("%03d", i), String
                        .format(locale.getValue(), i));
            }
        }

        jdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE codelist_en(code_id character varying(3) PRIMARY KEY, code_name character varying(50))");
        for (Entry<String, String> column : tableInput.row(Locale.ENGLISH)
                .entrySet()) {
            jdbcTemplate.update(
                    "INSERT INTO codelist_en (code_id, code_name) VALUES (:code_id, :code_name)",
                    ImmutableMap.of("code_id", column.getKey(), "code_name",
                            column.getValue()));
        }

        jdbcTemplate.getJdbcOperations().execute(
                "CREATE TABLE codelist_ja(code_id character varying(3) PRIMARY KEY, code_name character varying(50))");
        for (Entry<String, String> column : tableInput.row(Locale.JAPANESE)
                .entrySet()) {
            jdbcTemplate.update(
                    "INSERT INTO codelist_ja (code_id, code_name) VALUES (:code_id, :code_name)",
                    ImmutableMap.of("code_id", column.getKey(), "code_name",
                            column.getValue()));
        }

        codeListEnglish = new JdbcCodeList();
        codeListEnglish.setBeanName("CL_TEST_EN");
        codeListEnglish.setDataSource(dataSource);
        codeListEnglish.setLabelColumn("code_name");
        codeListEnglish.setValueColumn("code_id");
        codeListEnglish.setQuerySql(
                "Select code_id, code_name from codelist_en");

        codeListJapanese = new JdbcCodeList();
        codeListJapanese.setBeanName("CL_TEST_JA");
        codeListJapanese.setDataSource(dataSource);
        codeListJapanese.setLabelColumn("code_name");
        codeListJapanese.setValueColumn("code_id");
        codeListJapanese.setQuerySql(
                "Select code_id, code_name from codelist_ja");

        reloadableI18nCodeList = new SimpleReloadableI18nCodeList();
        reloadableI18nCodeList.setBeanName("CL_TEST");
        reloadableI18nCodeList.setRowsByCodeList(ImmutableMap
                .<Locale, ReloadableCodeList> of(Locale.ENGLISH,
                        codeListEnglish, Locale.JAPANESE, codeListJapanese));
    }

    @After
    public void after() throws Exception {
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE codelist_en");
        jdbcTemplate.getJdbcOperations().execute("DROP TABLE codelist_ja");

        Locale.setDefault(originalLocale);
    }

    @Test
    public void testSetRowsByCodeList() {

        afterPropertiesSet();
        assertCodeListMap(10);
    }

    @Test
    public void testRefresh() {

        afterPropertiesSet();

        // before refresh.
        assertCodeListMap(10);

        // update tables of database.
        updateRegisteredCodeLists(11);

        // refresh codelist.
        reloadableI18nCodeList.refresh();

        // reflect changes of registered codelists.
        assertCodeListMap(11);
    }

    @Test
    public void testRefreshRecursively() {

        afterPropertiesSet();

        // before refresh.
        assertCodeListMap(10);

        // update tables of database.
        updateRegisteredCodeLists(11);

        // refresh codelist recursively.
        reloadableI18nCodeList.refresh(true);

        // reflect changes of registered codelists.
        assertCodeListMap(11);
    }

    @Test
    public void testRefreshNonRecursively() {

        afterPropertiesSet();

        // before refresh.
        assertCodeListMap(10);

        // update tables of database.
        updateRegisteredCodeLists(11);

        // refresh codelist non recursively.
        reloadableI18nCodeList.refresh(false);

        // no reflect changes of registered codelists.
        assertCodeListMap(10);
    }

    @Test
    public void testLazyInit() {

        reloadableI18nCodeList.setLazyInit(true);
        afterPropertiesSet();

        assertThat(ReflectionTestUtils.getField(reloadableI18nCodeList,
                "codeListTable"), nullValue());
        assertCodeListMap(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterPropertiesSet() {

        reloadableI18nCodeList.setRowsByCodeList(null);
        afterPropertiesSet();
    }

    private void afterPropertiesSet() {

        codeListEnglish.afterPropertiesSet();
        codeListJapanese.afterPropertiesSet();
        reloadableI18nCodeList.afterPropertiesSet();
    }

    private void assertCodeListMap(int mapSize) {

        for (Locale locale : tableInput.rowKeySet()) {

            Map<String, String> mapInput = tableInput.row(locale);
            Map<String, String> mapOutput = reloadableI18nCodeList.asMap(
                    locale);
            assertThat(mapOutput, aMapWithSize(mapSize));
            for (int i = 0; i < mapSize; i++) {
                assertThat(mapOutput.get(String.format("%03d", i)), is(mapInput
                        .get(String.format("%03d", i))));
            }
        }
    }

    private void updateRegisteredCodeLists(int newCode) {

        Locale locale = Locale.ENGLISH;
        String column = String.format("%03d", newCode);
        String value = String.format(locales.get(locale), newCode);
        tableInput.put(locale, column, value);
        jdbcTemplate.update(
                "INSERT INTO codelist_en (code_id, code_name) VALUES (:code_id, :code_name)",
                ImmutableMap.of("code_id", column, "code_name", value));

        locale = Locale.JAPANESE;
        value = String.format(locales.get(locale), newCode);
        tableInput.put(locale, column, value);
        jdbcTemplate.update(
                "INSERT INTO codelist_ja (code_id, code_name) VALUES (:code_id, :code_name)",
                ImmutableMap.of("code_id", column, "code_name", value));
    }
}
