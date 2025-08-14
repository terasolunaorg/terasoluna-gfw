/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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
package org.terasoluna.gfw.web.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.terasoluna.gfw.common.exception.ExceptionLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

public class ExceptionLoggingFilterTest {

    /**
     * instance of test target.
     */
    private ExceptionLoggingFilter testTarget;

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
    private FilterChain mockFilterChain;

    /**
     * mock instance of exception logger.
     */
    private ExceptionLogger mockExceptionLogger;

    /**
     * setup all test case.
     * @throws Exception
     */
    @BeforeEach
    public void before() throws Exception {

        // create test target.
        this.testTarget = new ExceptionLoggingFilter();
        this.mockExceptionLogger = spy(new ExceptionLogger());
        mockExceptionLogger.afterPropertiesSet();
        testTarget.setExceptionLogger(mockExceptionLogger);
        testTarget.afterPropertiesSet();

        // setup mock.
        this.mockRequest = new MockHttpServletRequest();
        this.mockResponse = new MockHttpServletResponse();
        this.mockFilterChain = mock(FilterChain.class);

    }

    @Test
    public void testDoFilter_normal() throws IOException, ServletException {

        testTarget.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);

    }

    @Test
    public void testDoFilter_occur_ioexception() throws IOException, ServletException {

        IOException occurException = new IOException("io exception.");

        doThrow(occurException).when(mockFilterChain).doFilter(mockRequest, mockResponse);

        IOException e = assertThrows(IOException.class, () -> {
            testTarget.doFilter(mockRequest, mockResponse, mockFilterChain);
        });
        assertThat(e).isEqualTo(occurException);

        verify(mockExceptionLogger, times(1)).error(occurException);

    }

    @Test
    public void testDoFilter_occur_servletexception() throws IOException, ServletException {

        ServletException occurException = new ServletException("servlet exception.");

        doThrow(occurException).when(mockFilterChain).doFilter(mockRequest, mockResponse);

        ServletException e = assertThrows(ServletException.class, () -> {
            testTarget.doFilter(mockRequest, mockResponse, mockFilterChain);
        });
        assertThat(e).isEqualTo(occurException);

        verify(mockExceptionLogger, times(1)).error(occurException);

    }

    @Test
    public void testDoFilter_occur_runtimeexception() throws IOException, ServletException {

        NullPointerException occurException = new NullPointerException("null pointer exception.");

        NullPointerException e = assertThrows(NullPointerException.class, () -> {
            doThrow(occurException).when(mockFilterChain).doFilter(mockRequest, mockResponse);
            testTarget.doFilter(mockRequest, mockResponse, mockFilterChain);
        });
        assertThat(e).isEqualTo(occurException);

        verify(mockExceptionLogger, times(1)).error(occurException);

    }

    @Test
    public void testDoFilter_occur_error() throws IOException, ServletException {

        OutOfMemoryError occurError = new OutOfMemoryError("out of memory error.");

        doThrow(occurError).when(mockFilterChain).doFilter(mockRequest, mockResponse);

        OutOfMemoryError e = assertThrows(OutOfMemoryError.class, () -> {
            doThrow(occurError).when(mockFilterChain).doFilter(mockRequest, mockResponse);
            testTarget.doFilter(mockRequest, mockResponse, mockFilterChain);
        });
        assertThat(e).isEqualTo(occurError);

        verify(mockExceptionLogger, times(0)).error((Exception) any());

    }

    @Test
    public void testDoFilter_occur_ioexception_exceptionLogger_is_default()
            throws IOException, ServletException {

        testTarget = new ExceptionLoggingFilter();
        testTarget.afterPropertiesSet();
        this.mockExceptionLogger = spy(testTarget.getExceptionLogger());
        testTarget.setExceptionLogger(mockExceptionLogger);

        IOException occurException = new IOException("io exception.");

        doThrow(occurException).when(mockFilterChain).doFilter(mockRequest, mockResponse);

        IOException e = assertThrows(IOException.class, () -> {
            testTarget.doFilter(mockRequest, mockResponse, mockFilterChain);
        });
        assertThat(e).isEqualTo(occurException);

        verify(mockExceptionLogger, times(1)).error(occurException);

    }

}
