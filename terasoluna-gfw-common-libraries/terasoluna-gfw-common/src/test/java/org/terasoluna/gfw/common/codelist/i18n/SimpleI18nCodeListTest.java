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
package org.terasoluna.gfw.common.codelist.i18n;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Locale;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

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

    @SuppressWarnings("unchecked")
    private static Appender<ILoggingEvent> mockAppender = mock(Appender.class);

    private static Locale originalLocale;

    @BeforeClass
    public static void beforeClass() {
        Logger logger = (Logger) LoggerFactory.getLogger(
                SimpleI18nCodeList.class);
        logger.addAppender(mockAppender);

        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterClass
    public static void afterClass() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testAsMap() {

        Map<String, String> row1 = testSetRows.asMap();
        assertThat(row1, is(notNullValue()));
        assertThat(row1, aMapWithSize(7));

        assertThat(row1, hasEntry("0", "Sun."));
        assertThat(row1, hasEntry("1", "Mon."));
        assertThat(row1, hasEntry("2", "Tue."));
        assertThat(row1, hasEntry("3", "Wed."));
        assertThat(row1, hasEntry("4", "Thu."));
        assertThat(row1, hasEntry("5", "Fri."));
        assertThat(row1, hasEntry("6", "Sat."));
    }

    @Test
    public void testAsMapLocaleSpecification() {

        Map<String, String> row1 = testSetRows.asMap(Locale.ENGLISH);
        assertThat(row1, is(notNullValue()));
        assertThat(row1, aMapWithSize(7));

        assertThat(row1, hasEntry("0", "Sun."));
        assertThat(row1, hasEntry("1", "Mon."));
        assertThat(row1, hasEntry("2", "Tue."));
        assertThat(row1, hasEntry("3", "Wed."));
        assertThat(row1, hasEntry("4", "Thu."));
        assertThat(row1, hasEntry("5", "Fri."));
        assertThat(row1, hasEntry("6", "Sat."));

        Map<String, String> row2 = testSetRows.asMap(Locale.JAPANESE);
        assertThat(row2, is(notNullValue()));
        assertThat(row2, aMapWithSize(7));

        assertThat(row2, hasEntry("0", "日"));
        assertThat(row2, hasEntry("1", "月"));
        assertThat(row2, hasEntry("2", "火"));
        assertThat(row2, hasEntry("3", "水"));
        assertThat(row2, hasEntry("4", "木"));
        assertThat(row2, hasEntry("5", "金"));
        assertThat(row2, hasEntry("6", "土"));
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

    @Test
    public void testDuplicateCodeListTable() {
        verify(mockAppender, never()).doAppend(argThat(argument -> argument
                .getLevel().equals(Level.WARN)));

        // load lazy-init bean.
        SimpleI18nCodeList testDuplicateCodeListTable = getApplicationContext()
                .getBean("CL_testDuplicateCodeListTable",
                        SimpleI18nCodeList.class);
        assertThat(testDuplicateCodeListTable.codeListTable.size(), is(14)); // 2 rows x 7
                                                                             // columns
        verify(mockAppender, times(2)).doAppend(argThat(argument -> argument
                .getLevel().equals(Level.WARN)));
    }

    @Test(expected = None.class)
    public void testSetFallbackTo() {
        testSetFallbackTo.setFallbackTo(Locale.JAPANESE);
        testSetFallbackTo.afterPropertiesSet();
    }

    @Test
    public void testSetFallbackToNull() {
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    codeList.setFallbackTo(null);
                });
        assertThat(e.getMessage(), is("fallbackTo must not be null"));
    }

    @Test
    public void testSetFallbackToInvalidLanguage() {
        BeanCreationException e = assertThrows(BeanCreationException.class,
                () -> {
                    super.getApplicationContext().getBean(
                            "CL_testFallbackToInvalidLanguage");
                });
        Throwable cause = e.getCause();
        assertThat(cause, instanceOf(IllegalArgumentException.class));
        assertThat(cause.getMessage(), is(
                "No codelist found for fallback locale 'fr', it must be defined."));
    }

    @Test
    public void testSetFallbackToInvalidLanguageMatchingNation() {
        BeanCreationException e = assertThrows(BeanCreationException.class,
                () -> {
                    super.getApplicationContext().getBean(
                            "CL_testFallbackToInvalidLanguageMatchingNation");
                });
        Throwable cause = e.getCause();
        assertThat(cause, instanceOf(IllegalArgumentException.class));
        assertThat(cause.getMessage(), is(
                "No codelist found for fallback locale 'en_US', it must be defined."));
    }

    @Test
    public void testAfterProperitesSetInvalidInitializedCodeList() {
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    codeList.afterPropertiesSet();
                });
        assertThat(e.getMessage(), is("codeListTable is not initialized!"));

    }

    @Test
    public void testAfterProperitesSetInvalidResolveDefaultLocale() {
        BeanCreationException e = assertThrows(BeanCreationException.class,
                () -> {
                    super.getApplicationContext().getBean(
                            "CL_testAfterPropertiesSetInvalidResolveDefaultLocale");
                });
        Throwable cause = e.getCause();
        assertThat(cause, instanceOf(IllegalArgumentException.class));
        assertThat(cause.getMessage(), is(
                "No codelist for default locale ('en_US' and 'en'). "
                        + "Please define codelist for default locale or set locale already defined in codelist to fallbackTo."));
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
