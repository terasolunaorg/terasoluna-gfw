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
package org.terasoluna.gfw.web.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.h2.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.terasoluna.gfw.web.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@ContextConfiguration(locations = "classpath:/test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TraceLoggingInterceptorTest {
    @Inject
    NamedParameterJdbcTemplate jdbcTemplate;

    TraceLoggingInterceptor interceptor;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    Method[] method;

    TraceLoggingInterceptorController controller;

    ModelAndView model;

    Logger logger = (Logger) LoggerFactory.getLogger(
            TraceLoggingInterceptor.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
        // reset log
        new SimpleDriverDataSource(Driver
                .load(), "jdbc:h2:mem:terasolung-gfw-web;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:h2.sql'", "sa", "")
                        .getConnection().close();

        // prepare request object
        request = new MockHttpServletRequest();

        // prepare response object
        response = new MockHttpServletResponse();

        controller = new TraceLoggingInterceptorController();
        method = controller.getClass().getMethods();

        model = mock(ModelAndView.class);

        interceptor = new TraceLoggingInterceptor();
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * PreHandleHttpServletRequestHttpServletResponseObject<br>
     * Log output
     * @throws Exception
     * @throws NoSuchMethodException
     */
    @Test
    public void testPreHandle_LogOutput() throws Exception {

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, TraceLoggingInterceptorController.class
                .getMethod("createForm"));

        try {
            // run
            interceptor.preHandle(request, response, paramHandler);
        } catch (Exception e) {
            fail("illegal case");
        }

        String logMessage = jdbcTemplate.queryForObject(
                "SELECT FORMATTED_MESSAGE FROM LOGGING_EVENT WHERE EVENT_ID=:id",
                Collections.singletonMap("id", 1), String.class);
        Long startTime = (Long) request.getAttribute(
                TraceLoggingInterceptor.class.getName() + ".startTime");

        assertThat(logMessage, is(
                "[START CONTROLLER] TraceLoggingInterceptorController.createForm()"));
        assertThat(startTime, notNullValue());
    }

    /**
     * PreHandleHttpServletRequestHttpServletResponseObject<br>
     * parameter not handlermethod
     */
    @Test
    public void testPreHandle_ParamNotHandler() {

        // parameter create
        Object paramHandler = new Object();

        try {
            // run
            interceptor.preHandle(request, response, paramHandler);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        Long startTime = (Long) request.getAttribute(
                TraceLoggingInterceptor.class.getName() + ".startTime");

        long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM LOGGING_EVENT", Collections.singletonMap(
                        "", ""), Long.class);
        // assert
        assertThat(startTime, nullValue());
        assertThat(count, is(0L));
    }

    @Test
    public void testPreHandleIsTraceEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // parameter create
        Object paramHandler = new Object();

        try {
            // run
            interceptor.preHandle(request, response, paramHandler);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        Long startTime = (Long) request.getAttribute(
                TraceLoggingInterceptor.class.getName() + ".startTime");

        long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM LOGGING_EVENT", Collections.singletonMap(
                        "", ""), Long.class);
        // assert
        assertThat(startTime, nullValue());
        assertThat(count, is(0L));
        assertThat(logger.isDebugEnabled(), is(false));

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * Normal Log Output
     */
    @Test
    public void testPostHandle_NormalLogOutput() throws Exception {

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, TraceLoggingInterceptorController.class
                .getMethod("createForm"));
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String logMessage1 = jdbcTemplate.queryForObject(
                "SELECT FORMATTED_MESSAGE FROM LOGGING_EVENT WHERE EVENT_ID=:id",
                Collections.singletonMap("id", 1), String.class);
        String logMessage2 = jdbcTemplate.queryForObject(
                "SELECT FORMATTED_MESSAGE FROM LOGGING_EVENT WHERE EVENT_ID=:id",
                Collections.singletonMap("id", 2), String.class);

        assertThat(logMessage1, is(
                "[END CONTROLLER  ] TraceLoggingInterceptorController.createForm()-> view=null, model={}"));
        assertThat(logMessage2.startsWith(
                "[HANDLING TIME   ] TraceLoggingInterceptorController.createForm()->"),
                is(true));
    }

    /**
     * Normal Log Output
     */
    @Ignore
    @Test
    public void testPostHandle_NormalLogOutput_ViewNotNull() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, method[3]);

        // mocketd
        View view = mock(View.class);
        when(model.getView()).thenReturn(view);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.second(SampleForm,Model)->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);

        // assert
        verifyLogging(expectedLogStr, levelList, 2);

    }

    /**
     * Normal Log Output
     */
    @Ignore
    @Test
    public void testPostHandle_NormalLogOutput_ModelNull() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, method[0]);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, null);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);

        // assert
        verifyLogging(expectedLogStr, levelList, 2);

    }

    /**
     * Warn Log Output
     */
    @Ignore
    @Test
    public void testPostHandle_WarnlLogOutput() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, method[0]);

        try {
            // run
            interceptor.setWarnHandlingNanos(1L);
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);
        levelList.add(Level.WARN);

        // assert
        verifyLogging(expectedLogStr, levelList, 2);

    }

    /**
     * not started
     */
    @Ignore
    @Test
    public void testPostHandle_NotStarted() {
        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, method[0]);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);

        // assert
        verifyLogging(expectedLogStr, levelList, 1);
    }

    /**
     * not handler
     */
    @Ignore
    @Test
    public void testPostHandle_NotHandler() {
        // parameter create
        Object paramHandler = new Object();

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        final String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";

        // assert
        // verify(mockAppender, never()).doAppend(
        // argThat(new ArgumentMatcher<LoggingEvent>() {
        // @Override
        // public boolean matches(Object argument) {
        // return ((LoggingEvent) argument).getFormattedMessage()
        // .contains(expectedLogStr);
        // }
        // }));
    }

    /**
     * not handler Normal log output
     */
    @Ignore
    @Test
    public void testPostHandle_NotHandler_Normal() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);
        Object paramHandler = new Object();

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "[HANDLING TIME   ]->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);

        // assert
        verifyLogging(expectedLogStr, levelList, 1);
    }

    /**
     * not handler warn log output
     */
    @Ignore
    @Test
    public void testPostHandle_NotHandler_Warn() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);
        Object paramHandler = new Object();

        try {
            // run
            interceptor.setWarnHandlingNanos(1L);
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "[HANDLING TIME   ]->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.WARN);

        // assert
        verifyLogging(expectedLogStr, levelList, 1);
    }

    @Test
    public void testPostHandleNullStartAttr() throws Exception {
        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, TraceLoggingInterceptorController.class
                .getMethod("createForm"));
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", null);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, null);
        } catch (Exception e) {
            fail("illegal case");
        }

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";
        List<Level> levelList = new ArrayList<Level>();
        levelList.add(Level.TRACE);

        // assert
        verifyLogging(expectedLogStr, levelList, 2);
    }

    @Test
    public void testIsEnabledLogLevelIsWarnEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, TraceLoggingInterceptorController.class
                .getMethod("createForm"));
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // assert
        assertThat(logger.isDebugEnabled(), is(false));

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    @Test
    public void testIsEnabledLogLevelIsTraceEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, TraceLoggingInterceptorController.class
                .getMethod("createForm"));
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName()
                + ".startTime", startTime);

        try {
            // run
            interceptor.postHandle(request, response, paramHandler, model);
        } catch (Exception e) {
            fail("illegal case");
        }

        // assert
        assertThat(logger.isDebugEnabled(), is(false));

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     * @param expectedCallCount expected call count
     */
    private void verifyLogging(final String expectedLogMessage,
            final List<Level> expectedLogLevel, final int expectedCallCount) {
        // verify(mockAppender, times(expectedCallCount)).doAppend(
        // argThat(new ArgumentMatcher<LoggingEvent>() {
        // @Override
        // public boolean matches(Object argument) {
        // return (((LoggingEvent) argument).getFormattedMessage()
        // .contains(expectedLogMessage));
        // }
        // }));
        // verify(mockAppender, times(expectedCallCount)).doAppend(
        // argThat(new ArgumentMatcher<LoggingEvent>() {
        // @Override
        // public boolean matches(Object argument) {
        // return expectedLogLevel
        // .contains(((LoggingEvent) argument).getLevel());
        // }
        // }));

    }
}
