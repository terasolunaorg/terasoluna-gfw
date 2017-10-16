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

/**
 * Exception to indicate that it has detected a condition that should not occur when the system is running normally.<br>
 * <p>
 * System exception is to be thrown if something that must exist, is not there (like file, directory, master data etc)
 * </p>
 */
public class SystemException extends RuntimeException implements
                             ExceptionCodeProvider {

    private static final long serialVersionUID = 1L;

    /**
     * exception code.
     */
    private final String code;

    /**
     * Constructor<br>
     * <p>
     * {@link ExceptionCodeProvider}, message to be displayed and underlying cause of exception can be specified.
     * </p>
     * @param code ExceptionCode {@link ExceptionCodeProvider}
     * @param message message to be displayed
     * @param cause underlying cause of exception
     */
    public SystemException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Constructor<br>
     * <p>
     * {@link ExceptionCodeProvider}, message to be displayed can be specified.
     * </p>
     * @param code ExceptionCode {@link ExceptionCodeProvider}
     * @param message message to be displayed
     */
    public SystemException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Constructor<br>
     * <p>
     * {@link ExceptionCodeProvider} and underlying cause of exception can be specified.
     * </p>
     * @param code ExceptionCode {@link ExceptionCodeProvider}
     * @param cause underlying cause of exception
     */
    public SystemException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Returns the {@link ExceptionCodeProvider}
     * @see org.terasoluna.gfw.common.exception.ExceptionCodeProvider#getCode()
     */
    @Override
    public String getCode() {
        return code;
    }

}
