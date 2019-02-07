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
    @Qualifier("CL_testSetRows01")
    protected SimpleI18nCodeList testSetRows01;

    @Autowired
    @Qualifier("CL_testSetRows02")
    protected SimpleI18nCodeList testSetRows02;

    @Autowired
    @Qualifier("CL_testSetRowsByCodeList01")
    protected SimpleI18nCodeList testSetRowsByCodeList01;

    @Autowired
    @Qualifier("CL_testSetColumns01")
    protected SimpleI18nCodeList testSetColumns01;

    @Autowired
    @Qualifier("CL_testFallbackTo")
    protected SimpleI18nCodeList testSetFallbackTo;

    @Autowired
    @Qualifier("CL_testResolveLocale01")
    protected SimpleI18nCodeList testResolveLocale01;

    @Autowired
    @Qualifier("CL_testResolveLocale02")
    protected SimpleI18nCodeList testResolveLocale02;

    @BeforeClass
    public static void setDefaultLocale() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void testAsMap01() {

        Map<String, String> row1 = testSetRows01.asMap();
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));
    }

    @Test
    public void testSetRows01() {
        assertThat(testSetRows01.codeListTable.size(), is(14)); // 2 rows x 7
                                                                // columns

        Map<String, String> row1 = testSetRows01.asMap(Locale.ENGLISH);
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));

        Map<String, String> row2 = testSetRows01.asMap(Locale.JAPANESE);
        assertThat(row2, is(notNullValue()));
        assertThat(row2.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row2.get("0"), is("日"));
        assertThat(row2.get("1"), is("月"));
        assertThat(row2.get("2"), is("火"));
        assertThat(row2.get("3"), is("水"));
        assertThat(row2.get("4"), is("木"));
        assertThat(row2.get("5"), is("金"));
        assertThat(row2.get("6"), is("土"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetRows02() {
        // check unmodifiable
        testSetRows01.asMap(Locale.ENGLISH).put("0", "Sunday");
    }

    @Test
    public void testSetRows03() {
        assertThat(testSetRows01.codeListTable.size(), is(14)); // 2 rows x 7
                                                                // columns

        Map<String, String> row1 = testSetRows01.asMap(Locale.US);
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));

        Map<String, String> row2 = testSetRows01.asMap(Locale.JAPAN);
        assertThat(row2, is(notNullValue()));
        assertThat(row2.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row2.get("0"), is("日"));
        assertThat(row2.get("1"), is("月"));
        assertThat(row2.get("2"), is("火"));
        assertThat(row2.get("3"), is("水"));
        assertThat(row2.get("4"), is("木"));
        assertThat(row2.get("5"), is("金"));
        assertThat(row2.get("6"), is("土"));
    }

    @Test
    public void testSetRows04() {
        assertThat(testSetRows01.codeListTable.size(), is(14)); // 2 rows x 7
                                                                // columns

        Map<String, String> row1 = testSetRows01.asMap(Locale.CHINESE);
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));
    }

    @Test
    public void testSetRows05() {
        Locale.setDefault(Locale.JAPAN);
        assertThat(testSetRows02.codeListTable.size(), is(7)); // 1 rows x 7
                                                               // columns

        Map<String, String> row = testSetRows02.asMap(Locale.CHINESE);
        assertThat(row, is(notNullValue()));
        assertThat(row.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                          // order
        assertThat(row.get("0"), is("Sun."));
        assertThat(row.get("1"), is("Mon."));
        assertThat(row.get("2"), is("Tue."));
        assertThat(row.get("3"), is("Wed."));
        assertThat(row.get("4"), is("Thu."));
        assertThat(row.get("5"), is("Fri."));
        assertThat(row.get("6"), is("Sat."));

        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    public void testSetRowsByCodeList01() {
        assertThat(testSetRowsByCodeList01.codeListTable.size(), is(14)); // 2
                                                                          // rows
                                                                          // x 7
                                                                          // columns

        Map<String, String> row1 = testSetRowsByCodeList01.asMap(
                Locale.ENGLISH);
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));

        Map<String, String> row2 = testSetRowsByCodeList01.asMap(
                Locale.JAPANESE);
        assertThat(row2, is(notNullValue()));
        assertThat(row2.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row2.get("0"), is("日"));
        assertThat(row2.get("1"), is("月"));
        assertThat(row2.get("2"), is("火"));
        assertThat(row2.get("3"), is("水"));
        assertThat(row2.get("4"), is("木"));
        assertThat(row2.get("5"), is("金"));
        assertThat(row2.get("6"), is("土"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetRowsByCodeList02() {
        // check unmodifiable
        testSetRowsByCodeList01.asMap(Locale.ENGLISH).put("0", "Sunday");
    }

    @Test
    public void testSetColumns01() {
        assertThat(testSetColumns01.codeListTable.size(), is(14)); // 2 rows x 7
                                                                   // columns

        Map<String, String> row1 = testSetColumns01.asMap(Locale.ENGLISH);
        assertThat(row1, is(notNullValue()));
        assertThat(row1.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row1.get("0"), is("Sun."));
        assertThat(row1.get("1"), is("Mon."));
        assertThat(row1.get("2"), is("Tue."));
        assertThat(row1.get("3"), is("Wed."));
        assertThat(row1.get("4"), is("Thu."));
        assertThat(row1.get("5"), is("Fri."));
        assertThat(row1.get("6"), is("Sat."));

        Map<String, String> row2 = testSetColumns01.asMap(Locale.JAPANESE);
        assertThat(row2, is(notNullValue()));
        assertThat(row2.keySet().toString(), is("[0, 1, 2, 3, 4, 5, 6]")); // check
                                                                           // order
        assertThat(row2.get("0"), is("日"));
        assertThat(row2.get("1"), is("月"));
        assertThat(row2.get("2"), is("火"));
        assertThat(row2.get("3"), is("水"));
        assertThat(row2.get("4"), is("木"));
        assertThat(row2.get("5"), is("金"));
        assertThat(row2.get("6"), is("土"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetColumnss02() {
        // check unmodifiable
        testSetColumns01.asMap(Locale.ENGLISH).put("0", "Sunday");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFallbackTo_invalid() {
        testSetFallbackTo.setFallbackTo(Locale.US);
        testSetFallbackTo.afterPropertiesSet();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfterProperitesSet_invalid() {
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        codeList.afterPropertiesSet();
    }

    @Test
    public void testResolveLocale01() {
        assertThat(testResolveLocale01.resolveLocale(Locale.ENGLISH),is(Locale.ENGLISH));
    }

    @Test
    public void testResolveLocale02() {
        assertThat(testResolveLocale01.resolveLocale(Locale.US),is(Locale.ENGLISH));
    }

    @Test
    public void testResolveLocale03() {
        assertThat(testResolveLocale01.resolveLocale(Locale.JAPANESE),is(Locale.ENGLISH));
    }

    @Test
    public void testResolveLocale04() {
        assertThat(testResolveLocale02.resolveLocale(Locale.ENGLISH),is(Locale.US));
    }

}
