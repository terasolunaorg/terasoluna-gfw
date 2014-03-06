/*
 * Copyright (C) 2013-2014 terasoluna.org
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
import org.terasoluna.gfw.web.logging.HttpSessionEventLoggingListener;

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

    @Before
    public void setup() throws Exception {
        mockHttpSession = new MockHttpSession();
        httpSessionEvent = new HttpSessionEvent(mockHttpSession);
        sessionBindingEvent = new HttpSessionBindingEvent(mockHttpSession, "terasoluna", "AA");

        listener = new HttpSessionEventLoggingListener();

        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        this.mockAppender = mockAppender;
        Logger logger = (Logger) LoggerFactory
                .getLogger(HttpSessionEventLoggingListener.class);
        logger.addAppender(mockAppender);

    }

    @After
    public void tearDown() throws Exception {
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

    /**
     * session ValueBound logging
     */
    @Test
    public void testValueBound() {
        // expected
        final String valueBoundStr = "SESSIONID#" + mockHttpSession.getId()
                + " valueBound : terasoluna=AA";

        // run
        listener.valueBound(sessionBindingEvent);

        // assert
        verifyLogging(valueBoundStr, Level.DEBUG);
    }

    /**
     * session Unbound logging
     */
    @Test
    public void testValueUnbound() {
        // expected
        String valueUnBoundStr = "SESSIONID#" + mockHttpSession.getId()
                + " valueUnbound : terasoluna=AA";

        // run
        listener.valueUnbound(sessionBindingEvent);

        // assert
        verifyLogging(valueUnBoundStr, Level.DEBUG);

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

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     */
    private void verifyLogging(final String expectedLogMessage,
            final Level expectedLogLevel) {
        verify(mockAppender).doAppend(
                argThat(new ArgumentMatcher<LoggingEvent>() {
                    @Override
                    public boolean matches(Object argument) {
                        return ((LoggingEvent) argument).getFormattedMessage()
                                .equals(expectedLogMessage);
                    }
                }));
        verify(mockAppender).doAppend(
                argThat(new ArgumentMatcher<LoggingEvent>() {
                    @Override
                    public boolean matches(Object argument) {
                        return expectedLogLevel
                                .equals(((LoggingEvent) argument).getLevel());
                    }
                }));

    }

}
