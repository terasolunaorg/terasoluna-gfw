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
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpSession;
import org.terasoluna.gfw.web.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public class HttpSessionEventLoggingListenerTest {

    private MockHttpSession mockHttpSession;

    private HttpSessionEventLoggingListener listener;

    private HttpSessionEvent httpSessionEvent;

    private Appender<ILoggingEvent> mockAppender;

    private HttpSessionBindingEvent sessionBindingEvent;

    private Logger logger;

    @Before
    public void setup() throws Exception {
        mockHttpSession = new MockHttpSession();
        httpSessionEvent = new HttpSessionEvent(mockHttpSession);
        sessionBindingEvent = new HttpSessionBindingEvent(mockHttpSession, "terasoluna", "AA");

        listener = new HttpSessionEventLoggingListener();

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        this.mockAppender = mockAppender;
        logger = (Logger) LoggerFactory.getLogger(
                HttpSessionEventLoggingListener.class);
        logger.addAppender(mockAppender);
    }

    @After
    public void tearDown() throws Exception {
        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * session passivate logging
     */
    @Test
    public void testSessionWillPassivate() {
        // expected
        String passivateStr = "SESSIONID#" + mockHttpSession.getId()
                + " sessionWillPassivate : " + mockHttpSession.toString();

        // run
        listener.sessionWillPassivate(httpSessionEvent);

        // assert
        verifyLogging(passivateStr, Level.DEBUG);
    }

    @Test
    public void testSessionWillPassivateIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.sessionWillPassivate(httpSessionEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
    }

    /**
     * session DidActivate logging
     */
    @Test
    public void testSessionDidActivate() {
        // expected
        String didActivateStr = "SESSIONID#" + mockHttpSession.getId()
                + " sessionDidActivate : " + mockHttpSession.toString();
        // run
        listener.sessionDidActivate(httpSessionEvent);

        // assert
        verifyLogging(didActivateStr, Level.DEBUG);
    }

    @Test
    public void testSessionDidActivateIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.sessionDidActivate(httpSessionEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
    }

    /**
     * attribute add logging
     */
    @Test
    public void testAttributeAdded() {
        // expected
        String attributeAddedStr = "SESSIONID#" + mockHttpSession.getId()
                + " attributeAdded : terasoluna=AA";

        // run
        listener.attributeAdded(sessionBindingEvent);

        // assert
        verifyLogging(attributeAddedStr, Level.DEBUG);
    }

    @Test
    public void testAttributeAddedIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.attributeAdded(sessionBindingEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
    }

    /**
     * attribute removed logging
     */
    @Test
    public void testAttributeRemoved() {
        // expected
        String attributeRemovedStr = "SESSIONID#" + mockHttpSession.getId()
                + " attributeRemoved : terasoluna=AA";

        // run
        listener.attributeRemoved(sessionBindingEvent);

        // assert
        verifyLogging(attributeRemovedStr, Level.DEBUG);
    }

    @Test
    public void testAttributeRemovedIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.attributeRemoved(sessionBindingEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
    }

    /**
     * attribute replaced logging
     */
    @Test
    public void testAttributeReplaced() {
        // expected
        String attributeRemovedStr = "SESSIONID#" + mockHttpSession.getId()
                + " attributeReplaced : terasoluna=AA";

        // run
        listener.attributeReplaced(sessionBindingEvent);

        // assert
        verifyLogging(attributeRemovedStr, Level.TRACE);
    }

    @Test
    public void testAttributeReplacedisTraceEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.attributeReplaced(sessionBindingEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
    }

    /**
     * session create logging
     */
    @Test
    public void testSessionCreated() {
        // expected
        String sessionCreatedStr = "SESSIONID#" + mockHttpSession.getId()
                + " sessionCreated : " + mockHttpSession.toString();

        // run
        listener.sessionCreated(httpSessionEvent);

        // assert
        verifyLogging(sessionCreatedStr, Level.DEBUG);
    }

    @Test
    public void testSessionCreatedIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.sessionCreated(httpSessionEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
    }

    /**
     * session Destroyed logging
     */
    @Test
    public void testSessionDestroyed() {
        // expected
        String sessionDestroyedStr = "SESSIONID#" + mockHttpSession.getId()
                + " sessionDestroyed : " + mockHttpSession.toString();

        // run
        listener.sessionDestroyed(httpSessionEvent);

        // assert
        verifyLogging(sessionDestroyedStr, Level.DEBUG);
    }

    @Test
    public void testSessionDestroyedIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // run
        listener.sessionDestroyed(httpSessionEvent);

        // assert
        assertThat(logger.isDebugEnabled(), is(false));
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
