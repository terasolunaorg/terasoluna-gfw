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

import org.junit.Test;
import org.junit.Test.None;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:org/terasoluna/gfw/common/codelist/i18n/simpleI18nCodeList.xml" })
public class SimpleI18nCodeListTest extends ApplicationObjectSupport {
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

    public SimpleI18nCodeListTest() {
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
        fail("UnsupportedOperationException not occered.");
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

    @Test(expected = None.class)
    public void testSetFallbackTo() {
        testSetFallbackTo.setFallbackTo(Locale.JAPANESE);
        testSetFallbackTo.afterPropertiesSet();
    }

    @Test
    public void testSetFallbackToNull() {
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        try {
            codeList.setFallbackTo(null);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("fallbackTo must not be null"));
        }
    }

    @Test
    public void testSetFallbackToInvalidLanguage() {
        try {
            super.getApplicationContext().getBean(
                    "CL_testFallbackToInvalidLanguage");
            fail("BeanCreationException not occered.");
        } catch (BeanCreationException e) {
            Throwable cause = e.getCause();
            assertThat(cause, instanceOf(IllegalArgumentException.class));
            assertThat(cause.getMessage(), is(
                    "The resolution is to define codelist of default locale or to define 'fr'."));
        }
    }

    @Test
    public void testSetFallbackToInvalidLanguageMatchingNation() {
        try {
            super.getApplicationContext().getBean(
                    "CL_testFallbackToInvalidLanguageMatchingNation");
            fail("BeanCreationException not occered.");
        } catch (BeanCreationException e) {
            Throwable cause = e.getCause();
            assertThat(cause, instanceOf(IllegalArgumentException.class));
            assertThat(cause.getMessage(), is(
                    "The resolution is to define codelist of default locale or to define 'en_US'."));
        }
    }

    @Test
    public void testAfterProperitesSetInvalidInitializedCodeList() {
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        try {
            codeList.afterPropertiesSet();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("codeListTable is not initialized!"));
        }

    }

    @Test
    public void testAfterProperitesSetInvalidResolveDefaultLocale() {
        try {
            super.getApplicationContext().getBean(
                    "CL_testAfterPropertiesSetInvalidResolveDefaultLocale");
            fail("BeanCreationException not occered.");
        } catch (BeanCreationException e) {
            Throwable cause = e.getCause();
            assertThat(cause, instanceOf(IllegalArgumentException.class));
            assertThat(cause.getMessage(), is(
                    "No codelist for 'en_US' and 'en'."));
        }
    }

    @Test
    public void testResolveLocale() {
        assertThat(testResolveLocale.resolveLocale(Locale.FRENCH), is(
                Locale.FRENCH));
    }

    @Test
    public void testResolveLocalePrioritizeExactMatch() {
        assertThat(testResolveLocale.resolveLocale(Locale.CANADA_FRENCH), is(
                Locale.CANADA_FRENCH));
    }

    @Test
    public void testResolveLocaleMatchLanguage() {
        assertThat(testResolveLocale.resolveLocale(Locale.JAPAN), is(
                Locale.JAPANESE));
    }

    @Test
    public void testResolveLocaleUseFallbackTo() {
        assertThat(testResolveLocale.resolveLocale(Locale.ENGLISH), is(
                Locale.GERMAN));
    }

    @Test
    public void testResolveLocaleUnmatchNation() {
        assertThat(testResolveLocale.resolveLocale(Locale.CANADA), is(
                Locale.GERMAN));
    }

}
