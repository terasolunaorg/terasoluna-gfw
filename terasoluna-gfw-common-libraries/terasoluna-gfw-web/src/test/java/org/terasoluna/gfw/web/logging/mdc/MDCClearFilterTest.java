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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.terasoluna.gfw.web.logging.mdc.MDCClearFilter;

public class MDCClearFilterTest {
    /**
     * instance of test target.
     */
    private MDCClearFilter testTarget;

    /**
     * mock instance of http servlet requset.
     */
    private MockHttpServletRequest mockRequest;

    /**
     * mock instance of http servlet response.
     */
    private MockHttpServletResponse mockResponse;

    /**
     * mock instance of filter chain.
     */
    private MockFilterChainForMDCClearFilterTest mockFilterChain;

    /**
     * setup all test case.
     */
    @Before
    public void setup() {

        // create test target.
        this.testTarget = new MDCClearFilter();

        // setup mock.
        this.mockRequest = new MockHttpServletRequest();
        this.mockResponse = new MockHttpServletResponse();
        this.mockFilterChain = spy(new MockFilterChainForMDCClearFilterTest());

        // setup MDC.
        MDC.clear();

    }

    /**
     * [doFilterInternal] Case of normal(not occur exception).
     * <p>
     * [Expected Result]
     * <ol>
     * <li>not remove existing values from MDC on before chain.</li>
     * <li>remove all values from MDC on after chain.</li>
     * </ol>
     * </p>
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternal_normal() throws ServletException, IOException {

        // do setup test case.
        MDC.put("key0", "value0");

        // do test.
        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // do assert.
        // not remove existing values from MDC on before chain.
        assertThat(mockFilterChain.actualMdcContextMap.size(), is(1));
        assertThat(mockFilterChain.actualMdcContextMap.containsKey("key0"), is(
                true));
        // remove all values from MDC on after chain.
        assertThat(MDC.getCopyOfContextMap(), is(nullValue()));
    }

    /**
     * [doFilterInternal] Case of occur exception.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>remove all values from MDC on after chain.</li>
     * </ol>
     * </p>
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternal_occur_exception() throws ServletException, IOException {
        // do setup.
        ServletException occurException = new ServletException("test");
        doThrow(occurException).when(mockFilterChain).doFilter(mockRequest,
                mockResponse);
        MDC.put("key0", "value0");

        // do test.
        try {
            testTarget.doFilterInternal(mockRequest, mockResponse,
                    mockFilterChain);
            fail("don't occur ServletException.");
        } catch (ServletException e) {
            // do assert.
            // throws original exception.
            assertThat(e, is(occurException));
        }

        // do assert.
        // remove all values from MDC on after chain.
        assertThat(MDC.getCopyOfContextMap(), is(nullValue()));

    }

    /**
     * Mock class of FilterChain.
     */
    private class MockFilterChainForMDCClearFilterTest implements FilterChain {
        @SuppressWarnings("rawtypes")
        private Map actualMdcContextMap;

        @Override
        public void doFilter(ServletRequest request,
                ServletResponse response) throws ServletException, IOException {
            this.actualMdcContextMap = MDC.getCopyOfContextMap();
        }
    }

}
