/*
 * Copyright (C) 2013-2016  NTT DATA Corporation
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
package org.terasoluna.gfw.web.token.transaction;

/**
 * Thrown when transaction token is invalid<br>
 */
public class InvalidTransactionTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE = "Invalid Transaction Token Exception !!!";

    /**
     * No argument constructor <br>
     */
    public InvalidTransactionTokenException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Single argument constructor that takes the message string to be displayed when this exception is thrown<br>
     * @param message message string
     */
    public InvalidTransactionTokenException(String message) {
        super(message);
    }
}
