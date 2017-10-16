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
 * Default class for resolving exception level
 */
public class DefaultExceptionLevelResolver implements ExceptionLevelResolver {

    /**
     * Exception code resolution object
     */
    private ExceptionCodeResolver exceptionCodeResolver;

    /**
     * Default constructor
     */
    public DefaultExceptionLevelResolver() {
        // do nothing for Default constructor
    }

    /**
     * Constructor
     * @param exceptionCodeResolver Exception code resolution object
     */
    public DefaultExceptionLevelResolver(
            ExceptionCodeResolver exceptionCodeResolver) {
        setExceptionCodeResolver(exceptionCodeResolver);
    }

    /**
     * Sets exception code resolution object.
     * @param exceptionCodeResolver Exception code resolution object
     */
    public void setExceptionCodeResolver(
            ExceptionCodeResolver exceptionCodeResolver) {
        this.exceptionCodeResolver = exceptionCodeResolver;
    }

    /**
     * Determines the level of exception.
     * <p>
     * Determines the level of exception based on first letter of exception code.<br>
     * If the first letter (irrespective of letter case) of exception code is:
     * <ul>
     * <li>"i", then ExceptionLevel.INFO</li>
     * <li>"w", then ExceptionLevel.WARN</li>
     * <li>"e", then ExceptionLevel.ERROR</li>
     * <li>Other than above, ExceptionLevel.ERROR</li>
     * </ul>
     * @see org.terasoluna.gfw.common.exception.ExceptionLevelResolver#resolveExceptionLevel(java.lang.Exception)
     */
    @Override
    public ExceptionLevel resolveExceptionLevel(Exception ex) {
        String exceptionCode = resolveExceptionCode(ex);
        if (exceptionCode == null || exceptionCode.isEmpty()) {
            return ExceptionLevel.ERROR;
        }
        String exceptionCodePrefix = exceptionCode.substring(0, 1);
        if ("e".equalsIgnoreCase(exceptionCodePrefix)) {
            return ExceptionLevel.ERROR;
        }
        if ("w".equalsIgnoreCase(exceptionCodePrefix)) {
            return ExceptionLevel.WARN;
        }
        if ("i".equalsIgnoreCase(exceptionCodePrefix)) {
            return ExceptionLevel.INFO;
        }
        return ExceptionLevel.ERROR;
    }

    /**
     * Determines exception code.
     * @param ex exception
     * @return exception code
     */
    protected String resolveExceptionCode(Exception ex) {
        String exceptionCode = null;
        if (exceptionCodeResolver != null) {
            exceptionCode = exceptionCodeResolver.resolveExceptionCode(ex);
        }
        return exceptionCode;
    }

}
