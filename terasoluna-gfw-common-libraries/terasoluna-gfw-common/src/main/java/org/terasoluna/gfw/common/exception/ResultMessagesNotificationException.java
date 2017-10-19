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

import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * Abstract implementation of {@link RuntimeException}.
 * <p>
 * When a concrete implementation of this class is thrown, <br>
 * it provides the mechanism to indicate that contents of <br>
 * {@link ResultMessages} have to be logged. <br>
 * </p>
 */
public abstract class ResultMessagesNotificationException extends
                                                          RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Instance of {@link ResultMessages}
     */
    private final ResultMessages resultMessages;

    /**
     * Single argument constructor
     * @param messages instance of {@link ResultMessages}
     */
    protected ResultMessagesNotificationException(ResultMessages messages) {
        this(messages, null);
    }

    /**
     * Two argument constructor
     * @param messages instance of {@link ResultMessages}
     * @param cause {@link Throwable} instance
     */
    public ResultMessagesNotificationException(ResultMessages messages,
            Throwable cause) {
        super(cause);
        if (messages == null) {
            throw new IllegalArgumentException("messages must not be null");
        }
        this.resultMessages = messages;
    }

    /**
     * Returns the messages in String format
     * @return String messages
     */
    @Override
    public String getMessage() {
        return resultMessages.toString();
    }

    /**
     * Returns the {@link ResultMessages} instance
     * @return {@link ResultMessages} instance
     */
    public ResultMessages getResultMessages() {
        return resultMessages;
    }

}
