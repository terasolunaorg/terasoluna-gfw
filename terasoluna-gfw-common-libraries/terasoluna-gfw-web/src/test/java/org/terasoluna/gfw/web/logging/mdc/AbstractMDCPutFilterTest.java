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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter;

/**
 * Test class for AbstractMDCPutFilter.
 */
public class AbstractMDCPutFilterTest {

    /**
     * instance of test target.
     */
    private AbstractMDCPutFilter testTarget;

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
    private MockFilterChainForAbstractMDCPutFilterTest mockFilterChain;

    /**
     * setup all test case.
     */
    @Before
    public void setup() {

        // create test target.
        this.testTarget = new AbstractMDCPutFilter() {

            @Override
            protected String getMDCKey(HttpServletRequest request,
                    HttpServletResponse response) {
                return "key";
            }

            @Override
            protected String getMDCValue(HttpServletRequest request,
                    HttpServletResponse response) {
                return "value";
            }
        };

        // setup mock.
        this.mockRequest = new MockHttpServletRequest();
        this.mockResponse = new MockHttpServletResponse();
        this.mockFilterChain = spy(
                new MockFilterChainForAbstractMDCPutFilterTest());

        // setup MDC.
        MDC.clear();

    }

    /**
     * [doFilterInternal] Case of remove value.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>put value to MDC.</li>
     * <li>call filter chain.</li>
     * <li>remove value form MDC.</li>
     * <li>not remove other value from MDC.</li>
     * </ol>
     * </p>
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternal_remove_value() throws ServletException, IOException {

        // do setup for test case.
        testTarget.setRemoveValue(true);
        MDC.put("dummyKey", "dummyValue");

        // do test.
        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // do assert.
        // put value to MDC.
        assertEquals(2, mockFilterChain.actualMdcContextMap.size());
        assertEquals("value", mockFilterChain.actualMdcPutValue);

        // call filter chain.
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);

        // remove value in MDC.
        // not remove other value from MDC.
        assertEquals(1, MDC.getCopyOfContextMap().size());
        assertEquals("dummyValue", MDC.get("dummyKey"));

    }

    /**
     * [doFilterInternal] Case of remove value & occur servlet exception.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>remove value from MDC.</li>
     * <li>throws original servlet exception.</li>
     * </ol>
     * </p>
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternal_remove_value_and_occur_ServletException() throws ServletException, IOException {

        // do setup for test case.
        testTarget.setRemoveValue(true);
        MDC.put("dummyKey", "dummyValue");
        ServletException occurException = new ServletException("test");
        doThrow(occurException).when(mockFilterChain).doFilter(mockRequest,
                mockResponse);

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
        // remove value in MDC.
        // not remove other value from MDC.
        assertEquals(1, MDC.getCopyOfContextMap().size());
        assertEquals("dummyValue", MDC.get("dummyKey"));

    }

    /**
     * [doFilterInternal] Case of leave value.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>keep value in MDC.</li>
     * </ol>
     * </p>
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternal_leave_value() throws ServletException, IOException {

        // do setup for test case.
        MDC.put("dummyKey", "dummyValue");

        // do test.
        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // do assert.
        // not remove other value from MDC.
        assertEquals(2, MDC.getCopyOfContextMap().size());
        assertEquals("dummyValue", MDC.get("dummyKey"));
        assertEquals("value", MDC.get("key"));

    }

    /**
     * [doFilterInternal] Case of keep value & occur io exception.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>keep value in MDC.</li>
     * <li>throws original io exception.</li>
     * </ol>
     * </p>
     * @throws ServletException
     * @throws IOException
     */
    @Test
    public void testDoFilterInternal_keep_value_and_occur_IOException() throws ServletException, IOException {

        // do setup for test case.
        testTarget.setRemoveValue(false);
        MDC.put("dummyKey", "dummyValue");
        IOException occurException = new IOException("test");
        doThrow(occurException).when(mockFilterChain).doFilter(mockRequest,
                mockResponse);

        // do test.
        try {
            testTarget.doFilterInternal(mockRequest, mockResponse,
                    mockFilterChain);
            fail("don't occur IOException.");
        } catch (IOException e) {
            // do assert.
            // throws original io exception.
            assertThat(e, is(occurException));
        }

        // do assert.
        // not remove other value from MDC.
        assertEquals(2, MDC.getCopyOfContextMap().size());
        assertEquals("dummyValue", MDC.get("dummyKey"));
        assertEquals("value", MDC.get("key"));

    }

    @Test
    public void testMaxMDCValueLength_default() throws ServletException, IOException {

        this.testTarget = new AbstractMDCPutFilter() {

            @Override
            protected String getMDCKey(HttpServletRequest request,
                    HttpServletResponse response) {
                return "key";
            }

            @Override
            protected String getMDCValue(HttpServletRequest request,
                    HttpServletResponse response) {
                return "12345678901234567890123456789012a";
            }
        };

        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertThat(mockFilterChain.actualMdcPutValue, is(
                "12345678901234567890123456789012"));

    }

    @Test
    public void testMaxMDCValueLength_custom() throws ServletException, IOException {

        this.testTarget = new AbstractMDCPutFilter() {

            @Override
            protected String getMDCKey(HttpServletRequest request,
                    HttpServletResponse response) {
                return "key";
            }

            @Override
            protected String getMDCValue(HttpServletRequest request,
                    HttpServletResponse response) {
                return "123456789012345678901234567890123a";
            }
        };

        testTarget.setMaxMDCValueLength(33);

        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertThat(mockFilterChain.actualMdcPutValue, is(
                "123456789012345678901234567890123"));

    }

    @Test
    public void testMaxMDCValueLength_0() throws ServletException, IOException {

        this.testTarget = new AbstractMDCPutFilter() {

            @Override
            protected String getMDCKey(HttpServletRequest request,
                    HttpServletResponse response) {
                return "key";
            }

            @Override
            protected String getMDCValue(HttpServletRequest request,
                    HttpServletResponse response) {
                return "123456789012345678901234567890123a";
            }
        };

        testTarget.setMaxMDCValueLength(0);

        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertThat(mockFilterChain.actualMdcPutValue, is(""));

    }

    @Test
    public void testMaxMDCValueLength_negative() throws ServletException, IOException {

        this.testTarget = new AbstractMDCPutFilter() {

            @Override
            protected String getMDCKey(HttpServletRequest request,
                    HttpServletResponse response) {
                return "key";
            }

            @Override
            protected String getMDCValue(HttpServletRequest request,
                    HttpServletResponse response) {
                return "123456789012345678901234567890123a";
            }
        };

        testTarget.setMaxMDCValueLength(-1);

        testTarget.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        assertThat(mockFilterChain.actualMdcPutValue, is(
                "123456789012345678901234567890123a"));

    }

    @Test
    public void testCutValueNull() throws ServletException, IOException {
        // set up
        this.testTarget = new AbstractMDCPutFilter() {

            @Override
            protected String getMDCKey(HttpServletRequest request,
                    HttpServletResponse response) {
                return "MDCKey";
            }

            @Override
            protected String getMDCValue(HttpServletRequest request,
                    HttpServletResponse response) {
                return "MDCValue";
            }
        };

        // assert
        assertNull(testTarget.cutValue(null));
    }

    /**
     * Mock class of FilterChain.
     */
    private class MockFilterChainForAbstractMDCPutFilterTest implements
                                                             FilterChain {
        private String actualMdcPutValue;

        @SuppressWarnings("rawtypes")
        private Map actualMdcContextMap;

        @Override
        public void doFilter(ServletRequest request,
                ServletResponse response) throws ServletException, IOException {
            this.actualMdcPutValue = MDC.get("key");
            this.actualMdcContextMap = MDC.getCopyOfContextMap();
        }
    }

}
