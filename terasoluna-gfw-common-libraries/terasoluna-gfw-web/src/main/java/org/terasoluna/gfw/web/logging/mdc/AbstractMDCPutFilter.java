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
package org.terasoluna.gfw.web.logging.mdc;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Abstract class of Servlet Filter that encloses the values required for log output, in {@link MDC}.
 * <p>
 * Key and value are fetched using {@link #getMDCKey} and {@link #getMDCValue} respectively and stored in {@link MDC}.
 * </p>
 */
public abstract class AbstractMDCPutFilter extends OncePerRequestFilter {

    /**
     * Flag that controls whether the value created by this filter should also be remove from {@link MDC}.
     * <p>
     * <ul>
     * <li>{@code true} : Value should be remove from {@link MDC}</li>
     * <li>{@code false} : Value should be keep in {@link MDC} (default)</li>
     * </ul>
     * </p>
     */
    private boolean removeValue = false;

    /**
     * max value length to put MDC
     */
    private int maxMDCValueLength = 32;

    /**
     * Sets the flag that controls whether the value created by this filter should also be remove from {@link MDC}.
     * <p>
     * {@code true} should be set if value set in this filter should be remove from {@link MDC}<br>
     * If this method is not called or {@code false} is set in this method, the value set in this filter will be keep in
     * {@link MDC}
     * </p>
     * <p>
     * Points to Note:<br>
     * If {@code false} is specified here, always use this filter along with {@link MDCClearFilter} which clears the values
     * stored in {@link MDC}.<br>
     * If {@link MDCClearFilter} is not used, should be specified {@code true}.
     * </p>
     * @param removeValue Flag that controls whether the value created by this filter should also be remove from {@link MDC}.
     */
    public void setRemoveValue(boolean removeValue) {
        this.removeValue = removeValue;
    }

    /**
     * set max value length to put MDC<br>
     * <p>
     * if specified value is negative, does not cut MDC value.
     * </p>
     * @param maxMDCValueLength max value length to put MDC
     */
    public void setMaxMDCValueLength(int maxMDCValueLength) {
        this.maxMDCValueLength = maxMDCValueLength;
    }

    /**
     * cut the given string if the size of it is over {@link #maxMDCValueLength}<br>
     * @param value original value
     * @return cut value
     */
    protected String cutValue(String value) {
        if (value != null && maxMDCValueLength >= 0 && value
                .length() > maxMDCValueLength) {
            return value.substring(0, maxMDCValueLength);
        } else {
            return value;
        }

    }

    /**
     * Stores the information in {@link MDC} and calls the next filter in chain. Information fetching is to be implemented in
     * subclass of this class.
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param filterChain {@link FilterChain}
     * @throws ServletException If {@link ServletException} occurs in further in the execution chain
     * @throws IOException If {@link IOException} occurs in further in the execution chain
     * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected final void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String key = getMDCKey(request, response);
        String value = getMDCValue(request, response);
        MDC.put(key, cutValue(value));
        try {
            filterChain.doFilter(request, response);
        } finally {
            if (removeValue) {
                MDC.remove(key);
            }
        }
    }

    /**
     * Fetches the key for setting the value in {@link MDC}
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return Key to store the value in {@link MDC}
     */
    protected abstract String getMDCKey(HttpServletRequest request,
            HttpServletResponse response);

    /**
     * Returns the value to be stored in in {@link MDC}
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return Value to be stored in {@link MDC}
     */
    protected abstract String getMDCValue(HttpServletRequest request,
            HttpServletResponse response);

}
