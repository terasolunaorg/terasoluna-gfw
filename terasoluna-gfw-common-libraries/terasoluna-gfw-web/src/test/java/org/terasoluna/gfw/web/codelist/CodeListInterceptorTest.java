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
package org.terasoluna.gfw.web.codelist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
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

import ch.qos.logback.classic.Logger;

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

    @Before
    public void before() {
        this.testTarget = new CodeListInterceptor();
        testTarget.setApplicationContext(
                new StaticApplicationContext(getApplicationContext()));

        this.mockRequest = new MockHttpServletRequest();
        this.mockResponse = new MockHttpServletResponse();
    }

    /**
     * [postHandle] Case of CodeList is zero.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>do nothing.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPostHandle_zero() throws Exception {

        // do setup.
        testTarget.setApplicationContext(new StaticApplicationContext());
        testTarget.afterPropertiesSet();

        // do test.
        testTarget.postHandle(mockRequest, mockResponse, null, null);

        // do assert.
        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));

    }

    /**
     * [postHandle] Case of CodeList is one.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>set CodeList to attribute of HttpServletRequest.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPostHandle_one() throws Exception {

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
        testTarget.postHandle(mockRequest, mockResponse, null, null);

        // do assert.
        Enumeration<String> actualAttributeNames = mockRequest
                .getAttributeNames();
        assertThat(actualAttributeNames.hasMoreElements(), is(true));
        actualAttributeNames.nextElement();
        assertThat(actualAttributeNames.hasMoreElements(), is(false));
        assertThat(mockRequest.getAttribute("simpleMapCodeList"), is(
                simpleMapCodeList.asMap()));

    }

    /**
     * [postHandle] Case of CodeList is one.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>set CodeList to attribute of HttpServletRequest.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPostHandle_multi() throws Exception {

        // do setup.
        mockRequest.addPreferredLocale(Locale.ENGLISH);
        LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(mockRequest
                .getLocale()));

        testTarget.setCodeListIdPattern(Pattern.compile("C_.+"));
        testTarget.afterPropertiesSet();

        // do test.
        testTarget.postHandle(mockRequest, mockResponse, null, null);

        // do assert.
        SimpleMapCodeList simpleMapCodeList = getApplicationContext().getBean(
                "C_simpleMapCodeList", SimpleMapCodeList.class);
        SimpleI18nCodeList simpleI18nCodeList = getApplicationContext().getBean(
                "C_simpleI18nCodeList", SimpleI18nCodeList.class);
        assertThat(mockRequest.getAttribute("C_simpleMapCodeList"), is(
                simpleMapCodeList.asMap()));
        assertThat(mockRequest.getAttribute("C_simpleI18nCodeList"), is(
                simpleI18nCodeList.asMap(Locale.ENGLISH)));

    }

    /**
     * [postHandle] Case of not call afterPropertiesSet.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>do nothing.(don't occur error.)</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @Test
    public void testPostHandle_not_call_afterPropertiesSet() throws Exception {

        // do setup.
        // do nothing.

        // do test.
        testTarget.postHandle(mockRequest, mockResponse, null, null);

        // do assert.
        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));

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

        assertThat(testTarget.getCodeLists(), contains(expectedCodeListMap
                .values().toArray()));

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
        assertThat(testTarget.getCodeLists(), is(empty()));

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
        assertThat(testTarget.getCodeLists(), is(empty()));
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
        CodeList expectedCodeList = mockApplicationContext.getBean(
                "simpleMapCodeList", CodeList.class);

        assertThat(testTarget.getCodeLists(), contains(expectedCodeList));

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
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    testTarget.afterPropertiesSet();
                });
        // do assert.
        assertThat(e.getMessage(), is("applicationContext is null."));

    }

}
