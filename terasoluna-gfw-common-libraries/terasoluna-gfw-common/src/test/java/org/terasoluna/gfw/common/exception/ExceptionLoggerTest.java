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
package org.terasoluna.gfw.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public class ExceptionLoggerTest {

    private ExceptionLogger testTarget;

    private Appender<ILoggingEvent> mockApplicationLoggerAppender;

    private Appender<ILoggingEvent> mockMonitoringLoggerAppender;

    private Logger applicationLogger;

    private Logger monitoringLogger;

    @Before
    public void setUp() throws Exception {
        this.testTarget = new ExceptionLogger();
        testTarget.afterPropertiesSet();

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockApplicationLoggerAppender = mock(
                Appender.class);
        this.mockApplicationLoggerAppender = mockApplicationLoggerAppender;
        this.applicationLogger = (Logger) testTarget.getApplicationLogger();
        applicationLogger.addAppender(mockApplicationLoggerAppender);
        applicationLogger.setAdditive(false);

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> monitoringLoggerAppender = mock(Appender.class);
        this.mockMonitoringLoggerAppender = monitoringLoggerAppender;
        this.monitoringLogger = (Logger) testTarget.getMonitoringLogger();
        monitoringLogger.addAppender(mockMonitoringLoggerAppender);
        monitoringLogger.setAdditive(false);
    }

    @Test
    public void testFormatLogMessage_code_and_message_is_notnull() {

        // do setup.
        String code = "code";
        String message = "message";

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[" + code + "] " + message;
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_code_is_null() {

        // do setup.
        String code = null;
        String message = "message";

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE] " + message;
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_code_is_empty() {

        // do setup.
        String code = "";
        String message = "message";

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE] " + message;
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_message_is_null() {

        // do setup.
        String code = "code";
        String message = null;

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[" + code + "] UNDEFINED-MESSAGE";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_message_is_empty() {

        // do setup.
        String code = "code";
        String message = "";

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[" + code + "] UNDEFINED-MESSAGE";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_code_and_message_is_null() {

        // do setup.
        String code = null;
        String message = null;

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE] UNDEFINED-MESSAGE";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_trimMessage() {

        // do setup.
        testTarget.setDefaultMessage("");
        String code = null;
        String message = null;

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE]";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testFormatLogMessage_trimMessage_false() {

        // do setup.
        testTarget.setTrimLogMessage(false);
        testTarget.setDefaultMessage("");
        String code = null;
        String message = null;

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE] ";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testSetLogMessageFormat() {

        // do setup.
        testTarget.setLogMessageFormat("logId:{0}\tlogMessage:{1}");
        String code = "exceptionCode";
        String message = "exceptionMessage";

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "logId:exceptionCode\tlogMessage:exceptionMessage";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testSetDefaultCodeAndMessage() {

        // do setup.
        testTarget.setDefaultCode("DEFAULT-CODE");
        testTarget.setDefaultMessage("DEFAULT-MESSAGE");
        String code = null;
        String message = null;

        // do test.
        String actualLogMessage = testTarget.formatLogMessage(code, message);

        // do assert.
        String expectedLogMessage = "[DEFAULT-CODE] DEFAULT-MESSAGE";
        assertThat(actualLogMessage, is(expectedLogMessage));
    }

    @Test
    public void testMakeLogMessage_exceptionCodeResolver_is_notnull() {

        // do setup.
        Exception ex = new FileNotFoundException("occur file not found exception.");
        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code2");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        String actualLogMessage = testTarget.makeLogMessage(ex);

        // do assert.
        String expectedLogMessage = "[code2] occur file not found exception.";
        assertThat(actualLogMessage, is(expectedLogMessage));

    }

    @Test
    public void testMakeLogMessage_exceptionCodeResolver_is_null() {

        // do setup.
        Exception ex = new FileNotFoundException("occur file not found exception.");

        // do test.
        String actualLogMessage = testTarget.makeLogMessage(ex);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE] occur file not found exception.";
        assertThat(actualLogMessage, is(expectedLogMessage));

    }

    @Test
    public void testWarn_application_and_monitoring_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.WARN);
        monitoringLogger.setLevel(Level.WARN);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.warn(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.WARN, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.WARN,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testWarn_application_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.WARN);
        monitoringLogger.setLevel(Level.ERROR);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.warn(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.WARN, ex,
                mockApplicationLoggerAppender);
        verify(mockMonitoringLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
    }

    @Test
    public void testWarn_monitoring_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.ERROR);
        monitoringLogger.setLevel(Level.WARN);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.warn(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verify(mockApplicationLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
        verifyLogging(expectedLogMessage, Level.WARN,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testWarn_application_and_monitoring_is_not_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.ERROR);
        monitoringLogger.setLevel(Level.ERROR);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.warn(ex);

        // do assert.
        verify(mockApplicationLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
        verify(mockMonitoringLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());

    }

    @Test
    public void testError_application_and_monitoring_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.ERROR);
        monitoringLogger.setLevel(Level.ERROR);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.error(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.ERROR, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.ERROR,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testError_application_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.ERROR);
        monitoringLogger.setLevel(Level.OFF);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.error(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.ERROR, ex,
                mockApplicationLoggerAppender);
        verify(mockMonitoringLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
    }

    @Test
    public void testError_monitoring_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.OFF);
        monitoringLogger.setLevel(Level.ERROR);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.error(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verify(mockApplicationLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
        verifyLogging(expectedLogMessage, Level.ERROR,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testError_application_and_monitoring_is_not_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.OFF);
        monitoringLogger.setLevel(Level.OFF);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.error(ex);

        // do assert.
        verify(mockApplicationLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
        verify(mockMonitoringLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());

    }

    @Test
    public void testLog_defined_exceptionlevel() {
        // do setup.
        applicationLogger.setLevel(Level.INFO);
        monitoringLogger.setLevel(Level.INFO);

        Exception ex = new SystemException("i.xx", "system error.");

        ExceptionLevelResolver mockExceptionCodeResolver = mock(
                ExceptionLevelResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionLevel(ex)).thenReturn(
                ExceptionLevel.INFO);
        testTarget.setExceptionLevelResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.log(ex);

        // do assert.
        String expectedLogMessage = "[i.xx] system error.";
        verifyLogging(expectedLogMessage, Level.INFO, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.INFO,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testLog_undefined_exceptionlevel() throws Exception {
        // do setup.
        applicationLogger.setLevel(Level.INFO);
        monitoringLogger.setLevel(Level.INFO);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionLevelResolver mockExceptionCodeResolver = mock(
                ExceptionLevelResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionLevel(ex)).thenReturn(
                null);
        testTarget.setExceptionLevelResolver(mockExceptionCodeResolver);
        testTarget.afterPropertiesSet();

        // do test.
        testTarget.log(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.ERROR, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.ERROR,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testInfo_application_and_monitoring_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.INFO);
        monitoringLogger.setLevel(Level.INFO);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.info(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.INFO, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.INFO,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testInfo_application_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.INFO);
        monitoringLogger.setLevel(Level.OFF);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.info(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verifyLogging(expectedLogMessage, Level.INFO, ex,
                mockApplicationLoggerAppender);
        verify(mockMonitoringLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
    }

    @Test
    public void testInfo_monitoring_is_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.OFF);
        monitoringLogger.setLevel(Level.INFO);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.info(ex);

        // do assert.
        String expectedLogMessage = "[code01] system error.";
        verify(mockApplicationLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
        verifyLogging(expectedLogMessage, Level.INFO,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testInfo_application_and_monitoring_is_not_enabled() {
        // do setup.
        applicationLogger.setLevel(Level.OFF);
        monitoringLogger.setLevel(Level.OFF);

        Exception ex = new SystemException("code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);

        // do test.
        testTarget.info(ex);

        // do assert.
        verify(mockApplicationLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());
        verify(mockMonitoringLoggerAppender, times(0)).doAppend(
                (ILoggingEvent) anyObject());

    }

    @Test
    public void testAfterPropertiesSet() throws Exception {

        // do setup.
        applicationLogger.setLevel(Level.INFO);
        monitoringLogger.setLevel(Level.INFO);

        Exception ex = new SystemException("i.code01", "system error.");

        ExceptionCodeResolver mockExceptionCodeResolver = mock(
                ExceptionCodeResolver.class);
        when(mockExceptionCodeResolver.resolveExceptionCode(ex)).thenReturn(
                "i.code01");
        testTarget.setExceptionCodeResolver(mockExceptionCodeResolver);
        testTarget.setExceptionLevelResolver(null);
        testTarget.afterPropertiesSet();

        // do test.
        testTarget.log(ex);

        // do assert.
        String expectedLogMessage = "[i.code01] system error.";
        verifyLogging(expectedLogMessage, Level.INFO, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.INFO,
                mockMonitoringLoggerAppender);

    }

    @Test
    public void testExceptionLogger_default() {
        // do assert.
        assertThat(applicationLogger.getName(), is(testTarget.getClass()
                .getName()));
        assertThat(monitoringLogger.getName(), is(testTarget.getClass()
                .getName() + ".Monitoring"));
    }

    @Test
    public void testExceptionLogger_exceptionCodeResolver_isNull() {

        // do setup.
        applicationLogger.setLevel(Level.INFO);
        monitoringLogger.setLevel(Level.INFO);

        testTarget.setExceptionCodeResolver(null);

        Exception ex = new SystemException("i.code01", "system error.");

        // do test.
        testTarget.log(ex);

        // do assert.
        String expectedLogMessage = "[UNDEFINED-CODE] system error.";
        verifyLogging(expectedLogMessage, Level.INFO, ex,
                mockApplicationLoggerAppender);
        verifyLogging(expectedLogMessage, Level.INFO,
                mockMonitoringLoggerAppender);
    }

    @Test
    public void testExceptionLogger_LogMessageFormat_isNull() {

        testTarget.setLogMessageFormat(null);

        try {
            testTarget.afterPropertiesSet();
            fail("if logMessageFormat is null, must be occur IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced with exception message. current logMessageFormat is \"null\"."));
        }

    }

    @Test
    public void testExceptionLogger_LogMessageFormat_isBlank() {

        testTarget.setLogMessageFormat("");

        try {
            testTarget.afterPropertiesSet();
            fail("if logMessageFormat is null, must be occur IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced with exception message. current logMessageFormat is \"\"."));
        }

    }

    @Test
    public void testExceptionLogger_LogMessageFormat_notContainsPlaceholderOfExceptionCode() {

        testTarget.setLogMessageFormat("{1}");

        try {
            testTarget.afterPropertiesSet();
            fail("if logMessageFormat not contains placeholder({0}), must be occur IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced with exception message. current logMessageFormat is \"{1}\"."));
        }

    }

    @Test
    public void testExceptionLogger_LogMessageFormat_notContainsPlaceholderOfExceptionMessage() {

        testTarget.setLogMessageFormat("{0}");

        try {
            testTarget.afterPropertiesSet();
            fail("if logMessageFormat not contains placeholder({1}), must be occur IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced with exception message. current logMessageFormat is \"{0}\"."));
        }

    }

    @Test
    public void testExceptionLogger_name() {

        // do test.
        testTarget = new ExceptionLogger("test");
        // do assert.
        assertThat(testTarget.getApplicationLogger().getName(), is("test"));
        assertThat(testTarget.getMonitoringLogger().getName(), is(
                "test.Monitoring"));
    }

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     * @param mockAppender mock appender.
     */
    private void verifyLogging(final String expectedLogMessage,
            final Level expectedLogLevel,
            final Appender<ILoggingEvent> mockAppender) {
        verifyLogging(expectedLogMessage, expectedLogLevel, null, mockAppender);
    }

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     * @param mockAppender mock appender.
     */
    private void verifyLogging(final String expectedLogMessage,
            final Level expectedLogLevel, final Exception expectedException,
            final Appender<ILoggingEvent> mockAppender) {
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
        verify(mockAppender).doAppend(argThat(
                new ArgumentMatcher<LoggingEvent>() {
                    @Override
                    public boolean matches(Object argument) {
                        LoggingEvent loggingEvent = (LoggingEvent) argument;
                        if (expectedException == null) {
                            return loggingEvent.getThrowableProxy() == null;
                        }
                        if (!expectedException.getClass().getName().equals(
                                loggingEvent.getThrowableProxy()
                                        .getClassName())) {
                            return false;
                        }
                        if (!expectedException.getMessage().equals(loggingEvent
                                .getThrowableProxy().getMessage())) {
                            return false;
                        }
                        return true;
                    }
                }));

    }
}
