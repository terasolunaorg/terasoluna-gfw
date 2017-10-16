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
 * Servlet filter class that clears all the values stored in {@link MDC}.
 * <p>
 * If {@code MDC} is being used, it is has to be put to put this filter before all other servlet filters (Mandatory condition).
 * </p>
 */
public class MDCClearFilter extends OncePerRequestFilter {

    /**
     * Clears all the values from {@link MDC} after execution of all the filters is completed.
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param filterChain {@link FilterChain}
     * @throws ServletException If {@link ServletException} occurs further in the execution chain.
     * @throws IOException If IOException {@link IOException} occurs further in the execution chain.
     * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected final void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

}
