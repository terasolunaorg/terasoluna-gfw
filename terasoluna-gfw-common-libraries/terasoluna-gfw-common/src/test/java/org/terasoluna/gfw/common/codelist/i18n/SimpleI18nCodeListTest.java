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
package org.terasoluna.gfw.common.codelist.i18n;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Locale;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***
 * {@link Lazy} is used to fix Locale's setting. (see setDefaultLocale method)
 */
@Lazy
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:org/terasoluna/gfw/common/codelist/i18n/simpleI18nCodeList.xml" })
public class SimpleI18nCodeListTest {
    @Autowired
    @Qualifier("CL_testSetRows")
    protected SimpleI18nCodeList testSetRows;

    @Autowired
    @Qualifier("CL_testSetRowsByCodeList")
    protected SimpleI18nCodeList testSetRowsByCodeList;

    @Autowired
    @Qualifier("CL_testSetColumns")
    protected SimpleI18nCodeList testSetColumns;

    @Autowired
    @Qualifier("CL_testFallbackTo")
    protected SimpleI18nCodeList testSetFallbackTo;

    @Autowired
    @Qualifier("CL_testResolveLocale")
    protected SimpleI18nCodeList testResolveLocale;

    @Autowired
    @Qualifier("CL_testResolveLocaleFallbackToUS")
    protected SimpleI18nCodeList testResolveLocaleFallbackToUS;

    @BeforeClass
    public static void setDefaultLocale() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void testAsMap() {

        Map<String, String> row1 = testSetRows.asMap();
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]"));

        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));
    }

    @Test
    public void testAsMapLocaleSpecification() {

        Map<String, String> row1 = testSetRows.asMap(Locale.ENGLISH);
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]"));

        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));

        Map<String, String> row2 = testSetRows.asMap(Locale.JAPANESE);
        assertThat(row2, is(notNullValue()));
        assertThat(row2.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]"));

        assertThat(row2.get("0"), is("日"));
        assertThat(row2.get("1"), is("月"));
        assertThat(row2.get("2"), is("火"));
        assertThat(row2.get("3"), is("水"));
        assertThat(row2.get("4"), is("木"));
        assertThat(row2.get("5"), is("金"));
        assertThat(row2.get("6"), is("土"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAsMapCheckUnmodifiable() {
        testSetRows.asMap(Locale.ENGLISH).put("0", "Sunday");
    }

    @Test
    public void testSetRows() {
        assertThat(testSetRows.codeListTable.size(), is(14)); // 2 rows x 7
                                                              // columns
    }

    @Test
    public void testSetRowsByCodeList() {
        assertThat(testSetRowsByCodeList.codeListTable.size(), is(14)); // 2 rows x 7
                                                                        // columns
    }

    @Test
    public void testSetColumns() {
        assertThat(testSetColumns.codeListTable.size(), is(14)); // 2 rows x 7
                                                                 // columns
    }

    public void testSetFallbackTo() {
        testSetFallbackTo.setFallbackTo(Locale.JAPANESE);
        testSetFallbackTo.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFallbackToInvalid() {
        testSetFallbackTo.setFallbackTo(Locale.US);
        testSetFallbackTo.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterProperitesSetInvalid() {
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        codeList.afterPropertiesSet();
    }

    @Test
    public void testResolveLocale() {
        assertThat(testResolveLocale.resolveLocale(Locale.JAPANESE), is(
                Locale.JAPANESE));
    }

    @Test
    public void testResolveLocalePrioritizeExactMatch() {
        assertThat(testResolveLocale.resolveLocale(Locale.UK), is(Locale.UK));
    }

    @Test
    public void testResolveLocaleMatchLanguage() {
        assertThat(testResolveLocale.resolveLocale(Locale.US), is(
                Locale.ENGLISH));
    }

    @Test
    public void testResolveLocaleUseFallbackTo() {
        assertThat(testResolveLocale.resolveLocale(Locale.CHINESE), is(
                Locale.JAPAN));
    }

    @Test
    public void testResolveLocaleUnmatchNation() {
        assertThat(testResolveLocaleFallbackToUS.resolveLocale(Locale.ENGLISH),
                is(Locale.US));
    }

}
