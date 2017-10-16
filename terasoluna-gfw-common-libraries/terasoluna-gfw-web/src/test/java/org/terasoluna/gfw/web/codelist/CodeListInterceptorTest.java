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
package org.terasoluna.gfw.web.codelist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terasoluna.gfw.common.codelist.CodeList;
import org.terasoluna.gfw.common.codelist.SimpleMapCodeList;
import org.terasoluna.gfw.common.codelist.i18n.SimpleI18nCodeList;
import org.terasoluna.gfw.web.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml",
        "classpath:org/terasoluna/gfw/web/codelist/CodeListInterceptorTest.xml" })
public class CodeListInterceptorTest extends ApplicationObjectSupport {

    private CodeListInterceptor testTarget;

    /**
     * mock instance of http servlet requset.
     */
    private MockHttpServletRequest mockRequest;

    /**
     * mock instance of http servlet response.
     */
    private MockHttpServletResponse mockResponse;

    /**
     * mock instance of Appender.
     */
    private Appender<ILoggingEvent> mockAppender;

    @Before
    public void setUp() {
        this.testTarget = new CodeListInterceptor();
        testTarget.setApplicationContext(
                new StaticApplicationContext(getApplicationContext()));

        this.mockRequest = new MockHttpServletRequest();
        this.mockResponse = new MockHttpServletResponse();

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        this.mockAppender = mockAppender;
        Logger logger = (Logger) LoggerFactory.getLogger(
                CodeListInterceptor.class);
        logger.addAppender(mockAppender);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * [preHandle] Case of CodeList is zero.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>do nothing.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPreHandle_zero() throws Exception {

        // do setup.
        testTarget.setApplicationContext(new StaticApplicationContext());
        testTarget.afterPropertiesSet();

        // do test.
        boolean actualReturnValue = testTarget.preHandle(mockRequest,
                mockResponse, null);

        // do assert.
        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(actualReturnValue, is(true));

    }

    /**
     * [preHandle] Case of CodeList is one.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>set CodeList to attribute of HttpServletRequest.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPreHandle_one() throws Exception {

        // do setup.
        StaticApplicationContext mockApplicationContext = new StaticApplicationContext();
        mockApplicationContext.registerSingleton("simpleMapCodeList",
                SimpleMapCodeList.class);
        SimpleMapCodeList simpleMapCodeList = mockApplicationContext.getBean(
                SimpleMapCodeList.class);
        simpleMapCodeList.setMap(Collections.singletonMap("key", "value"));

        testTarget.setApplicationContext(mockApplicationContext);
        testTarget.afterPropertiesSet();

        // do test.
        boolean actualReturnValue = testTarget.preHandle(mockRequest,
                mockResponse, null);

        // do assert.
        Enumeration<String> actualAttributeNames = mockRequest
                .getAttributeNames();
        assertThat(actualAttributeNames.hasMoreElements(), is(true));
        actualAttributeNames.nextElement();
        assertThat(actualAttributeNames.hasMoreElements(), is(false));
        assertThat(mockRequest.getAttribute("simpleMapCodeList").toString(), is(
                simpleMapCodeList.asMap().toString()));
        assertThat(actualReturnValue, is(true));

    }

    /**
     * [preHandle] Case of CodeList is one.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>set CodeList to attribute of HttpServletRequest.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPreHandle_multi() throws Exception {

        // do setup.
        mockRequest.addPreferredLocale(Locale.ENGLISH);

        testTarget.setCodeListIdPattern(Pattern.compile("C_.+"));
        testTarget.afterPropertiesSet();

        // do test.
        boolean actualReturnValue = testTarget.preHandle(mockRequest,
                mockResponse, null);

        // do assert.
        SimpleMapCodeList simpleMapCodeList = getApplicationContext().getBean(
                "C_simpleMapCodeList", SimpleMapCodeList.class);
        SimpleI18nCodeList simpleI18nCodeList = getApplicationContext().getBean(
                "C_simpleI18nCodeList", SimpleI18nCodeList.class);
        assertThat(mockRequest.getAttribute("C_simpleMapCodeList").toString(),
                is(simpleMapCodeList.asMap().toString()));
        assertThat(mockRequest.getAttribute("C_simpleI18nCodeList").toString(),
                is(simpleI18nCodeList.asMap(Locale.ENGLISH).toString()));
        assertThat(actualReturnValue, is(true));

    }

    /**
     * [preHandle] Case of not call afterPropertiesSet.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>do nothing.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPreHandle_not_call_afterPropertiesSet() throws Exception {

        // do setup.
        // do nothing.

        // do test.
        boolean actualReturnValue = testTarget.preHandle(mockRequest,
                mockResponse, null);

        // do assert.
        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(actualReturnValue, is(true));

    }

    /**
     * [afterPropertiesSet] Case of codeListIdPattern is null.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>target is all bean of CodeList type.(don't filter by bean id.)</li>
     * <li>target list is
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testAfterPropertiesSet_codeListIdPattern_is_null() throws Exception {

        // do setup.
        testTarget.setCodeListIdPattern(null);

        // do test.
        testTarget.afterPropertiesSet();

        // do assert.
        Map<String, CodeList> expectedCodeListMap = new HashMap<String, CodeList>(getApplicationContext()
                .getBeansOfType(CodeList.class));

        assertThat(testTarget.getCodeLists().toString(), is(expectedCodeListMap
                .values().toString()));

    }

    /**
     * [afterPropertiesSet] Case of codeListIdPattern is not null.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>target is bean of CodeList type.(filter by bean id.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testAfterPropertiesSet_codeListIdPattern_is_notnull() throws Exception {

        // do setup.
        testTarget.setCodeListIdPattern(Pattern.compile("A_.+"));

        // do test.
        testTarget.afterPropertiesSet();

        // do assert.
        assertThat(testTarget.getCodeLists(), is(containsInAnyOrder(
                getApplicationContext().getBean("A_simpleI18nCodeList",
                        CodeList.class), getApplicationContext().getBean(
                                "A_numberRangeCodeList", CodeList.class),
                getApplicationContext().getBean("A_simpleMapCodeList",
                        CodeList.class))));

    }

    /**
     * [afterPropertiesSet] Case of not define CodeList.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>target is empty.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testAfterPropertiesSet_not_define_CodeList() throws Exception {

        // do setup.
        testTarget.setApplicationContext(new StaticApplicationContext());

        // do test.
        testTarget.afterPropertiesSet();

        // do assert.
        assertThat(testTarget.getCodeLists().isEmpty(), is(true));

    }

    /**
     * [afterPropertiesSet] Case of log level is higher than debug.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>target is empty.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testAfterPropertiesSet_isDebugEnabled_false() throws Exception {

        Logger logger = (Logger) LoggerFactory.getLogger(
                CodeListInterceptor.class);

        // do setup.
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);
        testTarget.setApplicationContext(new StaticApplicationContext());

        // do test.
        testTarget.afterPropertiesSet();

        // do assert.
        assertThat(testTarget.getCodeLists().isEmpty(), is(true));
        assertThat(logger.isDebugEnabled(), is(false));

        // init log level.
        LogLevelChangeUtil.resetLogLevel();

    }

    /**
     * [afterPropertiesSet] Case of define only one bean of codelist type.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>target is one.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testAfterPropertiesSet_not_define_one_CodeList() throws Exception {

        // do setup.
        StaticApplicationContext mockApplicationContext = new StaticApplicationContext();
        mockApplicationContext.registerSingleton("simpleMapCodeList",
                SimpleMapCodeList.class);
        testTarget.setApplicationContext(mockApplicationContext);

        // do test.
        testTarget.afterPropertiesSet();

        // do assert.
        List<CodeList> expectedCodeLists = new ArrayList<CodeList>();
        expectedCodeLists.add(mockApplicationContext.getBean(
                "simpleMapCodeList", CodeList.class));

        assertThat(testTarget.getCodeLists().toString(), is(expectedCodeLists
                .toString()));

    }

    /**
     * [afterPropertiesSet] Case of applicationContext is null.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>target is one.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testAfterPropertiesSet_applicationContext_is_null() throws Exception {

        // do setup.
        testTarget.setApplicationContext(null);

        // do test.
        try {
            testTarget.afterPropertiesSet();
        } catch (IllegalArgumentException e) {
            // do assert.
            assertThat("applicationContext is null.", is(e.getMessage()));
        }

    }

    /**
     * [getLocalizedCodeListMap] Case of define localized CodeList.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>get localized code list map.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testGetLocalizedCodeListMap_define_localized_odeList() {

        // do setup.
        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        Map<Locale, Map<String, String>> rows = Maps.newLinkedHashMap();
        Map<String, String> enMap = Collections.singletonMap("01", "XXX");
        Map<String, String> jaMap = Collections.singletonMap("01", "YYY");
        rows.put(Locale.ENGLISH, enMap);
        rows.put(Locale.JAPANESE, jaMap);
        codeList.setRows(rows);

        // do test.
        Map<String, String> actualEnMap = testTarget.getLocalizedCodeMap(
                codeList, Locale.ENGLISH);
        Map<String, String> actualJaMap = testTarget.getLocalizedCodeMap(
                codeList, Locale.JAPANESE);

        // do assert.
        assertThat(actualEnMap, is(enMap));
        assertThat(actualJaMap, is(jaMap));

    }

    /**
     * [getLocalizedCodeListMap] Case of match fall back locale.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>get localized code list map by fall back locale.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testGetLocalizedCodeListMap_match_fall_back_locale() {

        // do setup.
        testTarget.setFallbackTo(Locale.ENGLISH);

        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        Map<Locale, Map<String, String>> rows = Maps.newLinkedHashMap();
        Map<String, String> enMap = Collections.singletonMap("01", "XXX");
        rows.put(Locale.ENGLISH, enMap);
        codeList.setRows(rows);

        // do test.
        Map<String, String> actualMap = testTarget.getLocalizedCodeMap(codeList,
                Locale.JAPANESE);

        // do assert.
        assertThat(actualMap, is(enMap));
    }

    /**
     * [getLocalizedCodeListMap] Case of locale and fall back local is same.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>get empty map.(get localized code list map by locale.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testGetLocalizedCodeListMap_locale_and_fall_back_locale_is_same() {

        // do setup.
        testTarget.setFallbackTo(Locale.ENGLISH);

        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        Map<Locale, Map<String, String>> rows = Maps.newLinkedHashMap();
        Map<String, String> enMap = Collections.emptyMap();
        rows.put(Locale.ENGLISH, enMap);
        codeList.setRows(rows);

        // do test.
        Map<String, String> actualMap = testTarget.getLocalizedCodeMap(codeList,
                Locale.ENGLISH);

        // do assert.
        assertThat(actualMap, is(enMap));

    }

    /**
     * [getLocalizedCodeListMap] Case of fall back locale is null.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>get empty map.(get localized code list map by locale.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testGetLocalizedCodeListMap_fall_back_locale_is_null() {

        // do setup.
        testTarget.setFallbackTo(null);

        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        Map<Locale, Map<String, String>> rows = Maps.newLinkedHashMap();
        Map<String, String> enMap = Collections.emptyMap();
        rows.put(Locale.ENGLISH, enMap);
        codeList.setRows(rows);

        // do test.
        Map<String, String> actualMap = testTarget.getLocalizedCodeMap(codeList,
                Locale.ENGLISH);

        // do assert.
        assertThat(actualMap, is(enMap));

    }

    /**
     * [getLocalizedCodeListMap] Case of not define.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>get empty map.</li>
     * <li>output warn log.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testGetLocalizedCodeListMap_not_define() {

        // do setup.
        testTarget.setFallbackTo(Locale.ENGLISH);

        SimpleI18nCodeList codeList = new SimpleI18nCodeList();
        Map<Locale, Map<String, String>> rows = Maps.newLinkedHashMap();
        codeList.setRows(rows);

        // do test.
        Map<String, String> actualMap = testTarget.getLocalizedCodeMap(codeList,
                Locale.JAPANESE);

        // do assert.
        assertThat(actualMap.isEmpty(), is(true));

        verifyLogging("could not fall back to 'en'.", Level.WARN);

    }

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     */
    private void verifyLogging(final String expectedLogMessage,
            final Level expectedLogLevel) {
        verify(mockAppender).doAppend(argThat(
                new ArgumentMatcher<LoggingEvent>() {
                    @Override
                    public boolean matches(Object argument) {
                        return ((LoggingEvent) argument).getFormattedMessage()
                                .equals(expectedLogMessage);
                    }
                }));
        verify(mockAppender).doAppend(argThat(
                new ArgumentMatcher<LoggingEvent>() {
                    @Override
                    public boolean matches(Object argument) {
                        return expectedLogLevel.equals(((LoggingEvent) argument)
                                .getLevel());
                    }
                }));

    }

}
