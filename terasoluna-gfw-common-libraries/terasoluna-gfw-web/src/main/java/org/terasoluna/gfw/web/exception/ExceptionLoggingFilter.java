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
package org.terasoluna.gfw.web.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;
import org.terasoluna.gfw.common.exception.ExceptionLogger;

/**
 * Servlet filter class for log output of exception.
 * <p>
 * This class outputs the log of exceptions which do not get handled by
 * {@link org.springframework.web.servlet.HandlerExceptionResolver} of SpringMVC.<br>
 * Must be specified in Servlet filter. <br>
 * </p>
 * <p>
 * <strong>[Example of handling done in this class]</strong>
 * <ul>
 * <li>Exceptions that occur in {@link org.springframework.web.servlet.View}({@code JSP} etc)</li>
 * <li>Exceptions that occur beyond the boundary of SpringMVC ({@code javax.servlet.Filter} etc).</li>
 * <li>Fatal errors that occur within the boundary of SpringMVC (classed inherited from {@code java.lang.Error}).</li>
 * </ul>
 * <h2>Example of the Settings for inject a custom ExceptionLogger</h2><br>
 * <strong>[applicationContext.xml]</strong><br>
 * define bean of custom ExceptionLogger and ExceptionLoggingFilter.
 * 
 * <pre>
 * &lt;bean id=&quot;exceptionLogger&quot; class=&quot;org.terasoluna.gfw.common.exception.ExceptionLogger&quot;&gt;
 *   &lt;!-- ... --&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id=&quot;exceptionLoggingFilter&quot;
 * class=&quot;org.terasoluna.gfw.web.exception.ExceptionLoggingFilter&quot;&gt;
 *   &lt;property name=&quot;exceptionLogger&quot; ref=&quot;exceptionLogger&quot; /&gt;
 * &lt;/bean&gt;
 * 
 * </pre>
 * 
 * <strong>[web.xml]</strong><br>
 * filter-class specify the 'org.springframework.web.filter.DelegatingFilterProxy', and fiter-name specify the bean name of
 * ExceptionLoggingFilter.
 * 
 * <pre>
 * &lt;filter&gt;
 *   &lt;filter-name&gt;exceptionLoggingFilter&lt;/filter-name&gt;
 *   &lt;filter-class&gt;org.springframework.web.filter.DelegatingFilterProxy&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * </pre>
 */
public class ExceptionLoggingFilter extends GenericFilterBean {

    /**
     * Logger object for exception output.
     */
    private ExceptionLogger exceptionLogger = null;

    /**
     * Sets logger object for exception output
     * <p>
     * If not set, default logger object for exception output is used. <br>
     * </p>
     * @param exceptionLogger any exception logger.
     */
    public void setExceptionLogger(final ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    /**
     * Executes next filter. logs exception if exception is occurred.
     * @param servletRequest {@link ServletRequest}
     * @param servletResponse {@link ServletResponse}
     * @param filterChain {@link FilterChain}
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     *      javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException e) {
            logIOException(e, servletRequest, servletResponse);
            throw e;
        } catch (ServletException e) {
            logServletException(e, servletRequest, servletResponse);
            throw e;
        } catch (RuntimeException e) {
            logRuntimeException(e, servletRequest, servletResponse);
            throw e;
        }
    }

    /**
     * Initializes the exception filter.
     * <p>
     * If exception logger object is not set, use {@link org.terasoluna.gfw.common.exception.ExceptionLogger}.
     * </p>
     * <p>
     * default exception logger's name is 'org.terasoluna.gfw.web.exception.ExceptionLoggingFilter'<br>
     * (this interceptor's class name).
     * </p>
     * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
     */
    @Override
    protected void initFilterBean() throws ServletException {
        if (exceptionLogger == null) {
            exceptionLogger = new ExceptionLogger(getClass().getName());
            exceptionLogger.afterPropertiesSet();
        }
    }

    /**
     * Logs IOException.
     * @param ex Exception
     * @param request HTTP servlet request
     * @param response HTTP servlet response
     */
    protected void logIOException(IOException ex, ServletRequest request,
            ServletResponse response) {
        exceptionLogger.error(ex);
    }

    /**
     * Logs ServletException
     * @param ex Exception
     * @param request HTTP servlet request
     * @param response HTTP servlet response
     */
    protected void logServletException(ServletException ex,
            ServletRequest request, ServletResponse response) {
        exceptionLogger.error(ex);
    }

    /**
     * Logs RuntimeException
     * @param ex Exception
     * @param request HTTP servlet request
     * @param response HTTP servlet response
     */
    protected void logRuntimeException(RuntimeException ex,
            ServletRequest request, ServletResponse response) {
        exceptionLogger.error(ex);
    }

    /**
     * Fetches logger object that outputs exception
     * @return logger object that outputs exception
     */
    protected ExceptionLogger getExceptionLogger() {
        return exceptionLogger;
    }

}
