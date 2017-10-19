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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.terasoluna.gfw.common.exception.ExceptionLogger;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;

/**
 * Interceptor class for logging exceptions which are handled by {@link HandlerExceptionResolver}.
 * <p>
 * In this class, log level is changed as per the HTTP response code determined by {@code HandlerExceptionResolver}.
 * </p>
 * <h2>Example of the Settings for inject a custom ExceptionLogger</h2><br>
 * <strong>[applicationContext.xml]</strong><br>
 * define bean of custom ExceptionLogger.
 * 
 * <pre>
 * &lt;bean id=&quot;exceptionLogger&quot; class=&quot;org.terasoluna.gfw.common.exception.ExceptionLogger&quot;&gt;
 *   &lt;!-- ... --&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <strong>[spring-mvc.xml]</strong><br>
 * inject bean of custom ExceptionLogger.
 * 
 * <pre>
 * &lt;bean id=&quot;handlerExceptionResolverLoggingInterceptor&quot;
 *   class=&quot;org.terasoluna.gfw.web.exception.HandlerExceptionResolverLoggingInterceptor&quot;&gt;
 *   &lt;property name=&quot;exceptionLogger&quot; ref=&quot;exceptionLogger&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 */
public class HandlerExceptionResolverLoggingInterceptor implements
                                                        MethodInterceptor,
                                                        InitializingBean {

    /**
     * Logger for output of application log.
     */
    private static final Logger logger = LoggerFactory.getLogger(
            HandlerExceptionResolverLoggingInterceptor.class);

    /**
     * Logger object for exception output.
     */
    private ExceptionLogger exceptionLogger = null;

    /**
     * Set of exception classes for which log output is not to be done.
     */
    private Set<Class<? extends Exception>> ignoreExceptions;

    /**
     * Constructor
     */
    public HandlerExceptionResolverLoggingInterceptor() {
        this.ignoreExceptions = new HashSet<Class<? extends Exception>>();
        ignoreExceptions.add(ResultMessagesNotificationException.class);
    }

    /**
     * Sets Logger object for exception output.
     * <p>
     * If not set, default Logger object for exception output is used. <br>
     * </p>
     * @param exceptionLogger any exception logger.
     */
    public void setExceptionLogger(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    /**
     * Sets 'Set' instance of exception classes for which log output is not to be used.
     * @param ignoreExceptions Set of exception classes for which log output is not to be done.
     */
    public void setIgnoreExceptions(
            Set<Class<? extends Exception>> ignoreExceptions) {
        this.ignoreExceptions = ignoreExceptions;
    }

    /**
     * Log exceptions which are handled by {@link HandlerExceptionResolver}
     * <p>
     * [Output Specifications]
     * <ul>
     * <li>If {@code HandlerExceptionResolver} returns null, exception has not be handled and hence log will not be output</li>
     * <li>If {@code HandlerExceptionResolver} interface is not implemented, exception has not be handled and hence log will not
     * be output</li>
     * <li>If exception class matches with the list of classes for which log output is not to be done, then log output is not
     * done.</li>
     * <li>If HTTP response code by {@code HandlerExceptionResolver} is between 100-399 then INFO log, between 400-499 then WARN
     * log and above 500 ERROR log is output</li>
     * <li>If exception is handled by a class which is not in the list of {@link HandlerExceptionResolver} classes for WARN log
     * output, then output the log with ERROR level.</li>
     * <li></li>
     * </ul>
     * @param invocation {@link MethodInvocation}
     * @return Object returned by target method of interceptor
     * @throws Throwable If error occurred in target method of interceptor
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object returnObj = invocation.proceed();
        if (returnObj == null) {
            return returnObj;
        }

        Object targetObject = invocation.getThis();
        if (!(targetObject instanceof HandlerExceptionResolver)) {
            if (logger.isWarnEnabled()) {
                logger.warn(
                        "target object does not implement the HandlerExceptionResolver interface. target object is '{}'.",
                        targetObject.getClass().getName());
            }
            return returnObj;
        }

        Exception exception = (Exception) invocation.getArguments()[3];
        if (isTargetException(exception)) {
            HttpServletRequest request = (HttpServletRequest) invocation
                    .getArguments()[0];
            HttpServletResponse response = (HttpServletResponse) invocation
                    .getArguments()[1];
            Object handler = invocation.getArguments()[2];
            log(exception, request, response, handler);
        }

        return returnObj;
    }

    /**
     * Initializes the HandlerExceptionResolverLoggingInterceptor.
     * <p>
     * If exception logger object is not set, use {@link org.terasoluna.gfw.common.exception.ExceptionLogger}.
     * </p>
     * <p>
     * default exception logger's name is 'org.terasoluna.gfw.web.exception.HandlerExceptionResolverLoggingInterceptor'<br>
     * (this interceptor's class name).
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (exceptionLogger == null) {
            exceptionLogger = new ExceptionLogger(getClass().getName());
            exceptionLogger.afterPropertiesSet();
        }
    }

    /**
     * Determines if the exception class is in the list of classes for which log is to be output.
     * @param ex Exception
     * @return returns <code>true</code> if in the list.
     */
    protected boolean isTargetException(Exception ex) {
        if (ignoreExceptions == null) {
            return true;
        }
        for (Class<? extends Exception> ignoreClass : ignoreExceptions) {
            if (ignoreClass.isInstance(ex)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Outputs the log
     * @param ex Exception
     * @param request HTTP Servlet Request
     * @param response HTTP Servlet Response
     * @param handler handler
     */
    protected void log(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        int statusCode = response.getStatus();
        if (HttpServletResponse.SC_INTERNAL_SERVER_ERROR <= statusCode) {
            // responseCode is 500 ~
            logServerError(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_BAD_REQUEST <= statusCode) {
            // 400 ~ 499
            logClientError(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_MULTIPLE_CHOICES <= statusCode) {
            // 300 ~ 399
            logRedirection(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_OK <= statusCode) {
            // 200 ~ 299
            logSuccess(ex, request, response, handler);
            return;
        }
        if (HttpServletResponse.SC_CONTINUE <= statusCode) {
            // 100 ~ 199
            logInformational(ex, request, response, handler);
            return;
        }

    }

    /**
     * Outputs the log when HTTP status code is Informational (1xx).
     * <p>
     * Outputs INFO level log.
     * </p>
     * @param ex Exception
     * @param request HTTP Servlet request
     * @param response HTTP Servlet response
     * @param handler handler
     */
    protected void logInformational(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.info(ex);
    }

    /**
     * Outputs the log when HTTP status code is Success (2xx) related.
     * <p>
     * Outputs INFO level log.
     * </p>
     * @param ex Exception
     * @param request HTTP Servlet request
     * @param response HTTP Servlet response
     * @param handler handler
     */
    protected void logSuccess(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.info(ex);
    }

    /**
     * Outputs the log when HTTP status code is Redirectional (3xx).
     * <p>
     * Outputs INFO level of log.
     * </p>
     * @param ex Exception
     * @param request HTTP Servlet request
     * @param response HTTP Servlet response
     * @param handler handler
     */
    protected void logRedirection(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.info(ex);
    }

    /**
     * Outputs the log when HTTP status code is Error (4xx) related.
     * <p>
     * Outputs WARN level of log.
     * </p>
     * @param ex Exception
     * @param request HTTP Servlet request
     * @param response HTTP Servlet response
     * @param handler handler
     */
    protected void logClientError(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.warn(ex);
    }

    /**
     * Outputs the log when HTTP status code is Server Error (3xx).
     * <p>
     * Outputs Error level of log.
     * </p>
     * @param ex Exception
     * @param request HTTP Servlet request
     * @param response HTTP Servlet response
     * @param handler handler
     */
    protected void logServerError(Exception ex, HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        exceptionLogger.error(ex);
    }

    /**
     * Fetches logger object for logging exception
     * @return logger object
     */
    protected ExceptionLogger getExceptionLogger() {
        return exceptionLogger;
    }

}
