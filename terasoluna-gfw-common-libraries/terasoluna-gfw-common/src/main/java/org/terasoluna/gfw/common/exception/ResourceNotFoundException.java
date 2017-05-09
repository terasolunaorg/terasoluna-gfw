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

import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * Exception to inform you that resource not found.<br>
 */
public class ResourceNotFoundException extends
                                       ResultMessagesNotificationException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor for specify a message.
     * <p>
     * generate a {@link ResultMessages} instance of error type and add a message.
     * </p>
     * @param message result message
     */
    public ResourceNotFoundException(String message) {
        super(ResultMessages.error().add(ResultMessage.fromText(message)));
    }

    /**
     * Constructor for specify messages.
     * <p>
     * Takes multiple {@code String} messages as argument.
     * </p>
     * @param messages {@link ResultMessages} instance
     */
    public ResourceNotFoundException(ResultMessages messages) {
        super(messages);
    }

    /**
     * Constructor for specify messages and exception.
     * <p>
     * Takes multiple {@code String} messages and cause of exception as argument.
     * </p>
     * @param messages {@link ResultMessages} instance
     * @param cause {@link Throwable} instance
     */
    public ResourceNotFoundException(ResultMessages messages, Throwable cause) {
        super(messages, cause);
    }

}
