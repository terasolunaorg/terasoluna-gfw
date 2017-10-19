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

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.Assert;

/**
 * Contains static utility methods to resolve ResultMessage into corresponding message strings<br>
 */
public final class ResultMessageUtils {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(
            ResultMessageUtils.class);

    /**
     * Default Constructor.
     */
    private ResultMessageUtils() {
    };

    /**
     * resolve message text of <code>ResultMessage</code><br>
     * <ol>
     * <li>if <code>ResultMessage</code> has message code, try to resolve message using it
     * <ol>
     * <li>if there is no message for that code, try to use text of <code>ResultMessage</code>.</li>
     * <li>if there is no text, throw {@link NoSuchMessageException}</li>
     * </ol>
     * </li>
     * <li>return text of <code>ResultMessage</code> even if it is <code>null</code></li>
     * </ol>
     * @param message result message to resolve (must not be <code>null</code>)
     * @param messageSource message source (must not be <code>null</code>)
     * @param locale locate to resolve message (must not be <code>null</code>)
     * @return message text (must not be <code>null</code>)
     * @throws NoSuchMessageException if message is not found and no default text is given
     * @throws IllegalArgumentException if message or messageSoruce or locale is <code>null</code>
     */
    public static String resolveMessage(ResultMessage message,
            MessageSource messageSource,
            Locale locale) throws NoSuchMessageException {
        Assert.notNull(messageSource, "messageSource must not be null!");
        Assert.notNull(message, "message must not be null!");
        Assert.notNull(locale, "locale must not be null!");

        String msg;
        String code = message.getCode();
        if (code != null) {
            // try to resolve from code at first.
            try {
                msg = messageSource.getMessage(code, message.getArgs(), locale);
            } catch (NoSuchMessageException e) {
                String text = message.getText();
                if (text != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("messege is not found under code '" + code
                                + "' for '" + locale + "'. use '" + text
                                + "' instead", e);
                    }
                    // if ResultMessage has a text, then use it.
                    msg = text;
                } else {
                    // otherwise throw exception
                    throw e;
                }
            }
        } else {
            msg = message.getText();
        }
        return msg;
    }

    /**
     * resolve message text of <code>ResultMessage</code><br>
     * <ol>
     * <li>if <code>ResultMessage</code> has message code, try to resolve message using it
     * <ol>
     * <li>if there is no message for that code, try to use text of <code>ResultMessage</code>.</li>
     * <li>if there is no text, throw {@link NoSuchMessageException}</li>
     * </ol>
     * </li>
     * <li>return text of <code>ResultMessage</code> even if it is <code>null</code></li>
     * </ol>
     * @param message ResultMessage instance
     * @param messageSource a MessageSource instance for solving a complete message
     * @return message text
     * @throws NoSuchMessageException If does not resolve a message
     */
    public static String resolveMessage(ResultMessage message,
            MessageSource messageSource) {
        return resolveMessage(message, messageSource, Locale.getDefault());
    }
}
