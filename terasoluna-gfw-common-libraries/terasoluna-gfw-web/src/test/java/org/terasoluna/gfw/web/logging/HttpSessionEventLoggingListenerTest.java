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
package org.terasoluna.gfw.web.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpSession;
import org.terasoluna.gfw.web.logback.LogLevelChangeUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;

public class HttpSessionEventLoggingListenerTest {

    private MockHttpSession mockHttpSession;

    private HttpSessionEventLoggingListener listener;

    private HttpSessionEvent httpSessionEvent;

    private Appender<ILoggingEvent> mockAppender;

    private HttpSessionBindingEvent sessionBindingEvent;

    private Logger logger;

    @BeforeEach
    public void before() throws Exception {
        mockHttpSession = new MockHttpSession();
        httpSessionEvent = new HttpSessionEvent(mockHttpSession);
        sessionBindingEvent = new HttpSessionBindingEvent(mockHttpSession, "terasoluna", "AA");

        listener = new HttpSessionEventLoggingListener();

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        this.mockAppender = mockAppender;
        logger = (Logger) LoggerFactory.getLogger(HttpSessionEventLoggingListener.class);
        logger.addAppender(mockAppender);
    }

    @AfterEach
    public void after() throws Exception {
        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * session passivate logging
     */
    @Test
    public void testSessionWillPassivate() {
        // expected
        String passivateStr = "SESSIONID#" + mockHttpSession.getId() + " sessionWillPassivate : "
                + mockHttpSession.toString();

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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * session DidActivate logging
     */
    @Test
    public void testSessionDidActivate() {
        // expected
        String didActivateStr = "SESSIONID#" + mockHttpSession.getId() + " sessionDidActivate : "
                + mockHttpSession.toString();
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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * attribute add logging
     */
    @Test
    public void testAttributeAdded() {
        // expected
        String attributeAddedStr =
                "SESSIONID#" + mockHttpSession.getId() + " attributeAdded : terasoluna=AA";

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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * attribute removed logging
     */
    @Test
    public void testAttributeRemoved() {
        // expected
        String attributeRemovedStr =
                "SESSIONID#" + mockHttpSession.getId() + " attributeRemoved : terasoluna=AA";

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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * attribute replaced logging
     */
    @Test
    public void testAttributeReplaced() {
        // expected
        String attributeRemovedStr =
                "SESSIONID#" + mockHttpSession.getId() + " attributeReplaced : terasoluna=AA";

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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * session create logging
     */
    @Test
    public void testSessionCreated() {
        // expected
        String sessionCreatedStr = "SESSIONID#" + mockHttpSession.getId() + " sessionCreated : "
                + mockHttpSession.toString();

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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * session Destroyed logging
     */
    @Test
    public void testSessionDestroyed() {
        // expected
        String sessionDestroyedStr = "SESSIONID#" + mockHttpSession.getId() + " sessionDestroyed : "
                + mockHttpSession.toString();

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
        assertThat(logger.isDebugEnabled()).isFalse();
    }

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     */
    private void verifyLogging(final String expectedLogMessage, final Level expectedLogLevel) {
        verify(mockAppender).doAppend(
                argThat(argument -> argument.getFormattedMessage().equals(expectedLogMessage)));
        verify(mockAppender)
                .doAppend(argThat(argument -> expectedLogLevel.equals(argument.getLevel())));
    }

}
