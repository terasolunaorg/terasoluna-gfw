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
package org.terasoluna.gfw.web.exception;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ExceptionLogger;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public class HandlerExceptionResolverLoggingInterceptorTest {
    /**
     * instance of test target.
     */
    private HandlerExceptionResolverLoggingInterceptor testTarget;

    /**
     * Mock object of ExceptionLogger.
     */
    private ExceptionLogger mockExceptionLogger;

    /**
     * Mock object of MethodInvocation.
     */
    private MethodInvocation mockMethodInvocation;

    /**
     * mock instance of Appender.
     */
    private Appender<ILoggingEvent> mockAppender;

    private Logger applicationLogger;

    /**
     * setup all test case.
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {

        // create test target.
        this.testTarget = new HandlerExceptionResolverLoggingInterceptor();

        mockMethodInvocation = mock(MethodInvocation.class);
        mockExceptionLogger = mock(ExceptionLogger.class);
        testTarget.setExceptionLogger(mockExceptionLogger);
        testTarget.afterPropertiesSet();

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        this.mockAppender = mockAppender;
        this.applicationLogger = (Logger) LoggerFactory.getLogger(
                HandlerExceptionResolverLoggingInterceptor.class);
        applicationLogger.addAppender(mockAppender);
    }

    @Test
    public void testInvoke_return_value_is_null() throws Throwable {

        // do setup for test case.
        // nothing.
        when(mockMethodInvocation.proceed()).thenReturn(null);

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockMethodInvocation, times(1)).proceed();
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

    }

    @Test
    public void testInvoke_not_HandlerExceptionResolver() throws Throwable {

        // do setup for test case.
        // nothing.
        when(mockMethodInvocation.proceed()).thenReturn("viewname");

        when(mockMethodInvocation.getThis()).thenReturn(
                "none HandlerExceptionResolver.");

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockMethodInvocation, times(1)).proceed();
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

        verifyLogging(
                "target object does not implement the HandlerExceptionResolver interface. target object is 'java.lang.String'.",
                Level.WARN);

    }

    @Test
    public void testInvoke_not_HandlerExceptionResolver_and_loglevel_under() throws Throwable {

        // do setup for test case.
        applicationLogger.setLevel(Level.ERROR);
        when(mockMethodInvocation.proceed()).thenReturn("viewname");

        when(mockMethodInvocation.getThis()).thenReturn(
                "none HandlerExceptionResolver.");

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockMethodInvocation, times(1)).proceed();
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

        verify(mockAppender, times(0)).doAppend((ILoggingEvent) any());

        applicationLogger.setLevel(Level.DEBUG);
    }

    @Test
    public void testInvoke_ExceptionHandlerExceptionResolver_responseCode_1xx() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(100);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

    }

    @Test
    public void testInvoke_DefaultHandlerExceptionResolver_responseCode_2xx() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        DefaultHandlerExceptionResolver resolver = new DefaultHandlerExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

    }

    @Test
    public void testInvoke_ResponseStatusExceptionResolver_responseCode_3xx() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        ResponseStatusExceptionResolver resolver = new ResponseStatusExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(300);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

    }

    @Test
    public void testInvoke_SystemExceptionResolver_responseCode_4xx() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(400);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).warn((Exception) any());

    }

    @Test
    public void testInvoke_SystemExceptionResolver_BusinessException() throws Throwable {

        // do setup for test case.
        BusinessException occurException = new BusinessException("error.");

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, null, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

    }

    @Test
    public void testInvoke_SystemExceptionResolver_ResourceNotFoundException() throws Throwable {

        // do setup for test case.
        ResourceNotFoundException occurException = new ResourceNotFoundException("error.");

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, null, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

    }

    @Test
    public void testInvoke_SystemExceptionResolver_ResourceNotFoundException_not_ignore_responseCode_5xx() throws Throwable {

        // do setup for test case.
        ResourceNotFoundException occurException = new ResourceNotFoundException("error.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(500);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        testTarget.setIgnoreExceptions(null);

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).error((Exception) any());

    }

    @Test
    public void testInvoke_SystemExceptionResolver_ignoreExceptions_is_multi() throws Throwable {

        // do setup for test case.
        FileNotFoundException occurException1 = new FileNotFoundException("error.");
        DuplicateKeyException occurException2 = new DuplicateKeyException("error.");

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, null, null, occurException1 }, new Object[] { null, null,
                        null, occurException2 });

        Set<Class<? extends Exception>> ignoreExceptions = new HashSet<Class<? extends Exception>>();
        ignoreExceptions.add(DataAccessException.class);
        ignoreExceptions.add(IOException.class);
        testTarget.setIgnoreExceptions(ignoreExceptions);

        // do test.
        testTarget.invoke(mockMethodInvocation);
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

    }

    @Test
    public void testInvoke_responseCode_199() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(199);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

    }

    @Test
    public void testInvoke_responseCode_299() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        DefaultHandlerExceptionResolver resolver = new DefaultHandlerExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(299);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

    }

    @Test
    public void testInvoke_responseCode_399() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        ResponseStatusExceptionResolver resolver = new ResponseStatusExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(399);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

    }

    @Test
    public void testInvoke_responseCode_499() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(499);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).warn((Exception) any());

    }

    @Test
    public void testInvoke_responseCode_599() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(599);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).error((Exception) any());

    }

    @Test
    public void testInvoke_responseCode_under_100() throws Throwable {

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        SystemExceptionResolver resolver = new SystemExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(99);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(0)).log((Exception) any());
        verify(mockExceptionLogger, times(0)).info((Exception) any());
        verify(mockExceptionLogger, times(0)).warn((Exception) any());
        verify(mockExceptionLogger, times(0)).error((Exception) any());

    }

    @Test
    public void testafterPropertiesSet() throws Throwable {

        testTarget = new HandlerExceptionResolverLoggingInterceptor();
        testTarget.afterPropertiesSet();
        this.mockExceptionLogger = spy(testTarget.getExceptionLogger());
        testTarget.setExceptionLogger(mockExceptionLogger);

        // do setup for test case.
        NullPointerException occurException = new NullPointerException("null pointer exception.");
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();

        when(mockMethodInvocation.proceed()).thenReturn("viewname");
        when(mockMethodInvocation.getThis()).thenReturn(resolver);
        when(mockResponse.getStatus()).thenReturn(100);
        when(mockMethodInvocation.getArguments()).thenReturn(new Object[] {
                null, mockResponse, null, occurException });

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockExceptionLogger, times(1)).info((Exception) any());

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
