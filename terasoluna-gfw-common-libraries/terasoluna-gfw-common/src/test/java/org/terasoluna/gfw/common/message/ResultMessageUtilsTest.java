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
package org.terasoluna.gfw.common.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.util.Locale;

import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.terasoluna.gfw.common.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Logger;

public class ResultMessageUtilsTest {

    private Logger logger = (Logger) LoggerFactory.getLogger(
            ResultMessageUtils.class);

    @Test
    public void testResultMessageUtils() throws Exception {
        // set up
        Constructor<ResultMessageUtils> constructor = ResultMessageUtils.class
                .getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());

        constructor.setAccessible(false);
    }

    @Test
    public void testResolveMessageLocaleNotPassed() {
        ResultMessage message = mock(ResultMessage.class);
        MessageSource messageSource = mock(MessageSource.class);

        when(message.getCode()).thenReturn("MSG001");
        when(message.getArgs()).thenReturn(null);
        when(messageSource.getMessage("MSG001", null, Locale.getDefault()))
                .thenReturn("MESSAGE_TEXT");

        String msg = ResultMessageUtils.resolveMessage(message, messageSource);
        assertThat(msg, is("MESSAGE_TEXT"));
    }

    @Test
    public void testResolveMessageWithLocale() {
        ResultMessage message = mock(ResultMessage.class);
        MessageSource messageSource = mock(MessageSource.class);
        Locale locale = Locale.getDefault();
        when(message.getCode()).thenReturn("MSG001");
        when(message.getArgs()).thenReturn(null);
        when(messageSource.getMessage("MSG001", null, locale)).thenReturn(
                "MESSAGE_TEXT");

        String msg = ResultMessageUtils.resolveMessage(message, messageSource,
                locale);
        assertThat(msg, is("MESSAGE_TEXT"));
    }

    @Test
    public void testResolveMessageNullMessageCode() {
        ResultMessage message = mock(ResultMessage.class);
        MessageSource messageSource = mock(MessageSource.class);

        when(message.getCode()).thenReturn(null);
        when(message.getText()).thenReturn("MESSAGE_TEXT");

        String msg = ResultMessageUtils.resolveMessage(message, messageSource);
        assertThat(msg, is("MESSAGE_TEXT"));
    }

    @Test
    public void testResolveMessageWithNoSuchMessageException() {
        ResultMessage message = mock(ResultMessage.class);
        MessageSource messageSource = mock(MessageSource.class);
        Locale locale = Locale.getDefault();

        when(message.getCode()).thenReturn("MSG001");
        when(message.getArgs()).thenReturn(null);
        when(message.getText()).thenReturn("MESSAGE_TEXT");

        when(messageSource.getMessage("MSG001", null, locale)).thenThrow(
                new NoSuchMessageException("MSG001"));

        String msg = ResultMessageUtils.resolveMessage(message, messageSource);
        assertThat(msg, is("MESSAGE_TEXT"));
    }

    @Test(expected = NoSuchMessageException.class)
    public void testNoSuchMessageException() {
        ResultMessage message = mock(ResultMessage.class);
        MessageSource messageSource = mock(MessageSource.class);
        Locale locale = Locale.getDefault();

        when(message.getCode()).thenReturn("MSG001");
        when(message.getArgs()).thenReturn(null);
        when(message.getText()).thenReturn(null);

        when(messageSource.getMessage("MSG001", null, locale)).thenThrow(
                new NoSuchMessageException("MSG001"));

        ResultMessageUtils.resolveMessage(message, messageSource);
    }

    @Test
    public void testResolveMessageIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        ResultMessage message = mock(ResultMessage.class);
        MessageSource messageSource = mock(MessageSource.class);
        Locale locale = Locale.getDefault();

        when(message.getCode()).thenReturn("MSG001");
        when(message.getArgs()).thenReturn(null);
        when(message.getText()).thenReturn("MESSAGE_TEXT");
        when(messageSource.getMessage("MSG001", null, locale)).thenThrow(
                new NoSuchMessageException("MSG001"));

        String msg = ResultMessageUtils.resolveMessage(message, messageSource,
                locale);

        // assert
        assertThat(msg, is("MESSAGE_TEXT"));
        assertThat(logger.isDebugEnabled(), is(false));

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

}
