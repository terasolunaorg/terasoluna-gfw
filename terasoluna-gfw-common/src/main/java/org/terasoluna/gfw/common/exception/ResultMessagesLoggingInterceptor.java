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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

/**
 * Interceptor class for logging {@link org.terasoluna.gfw.common.message.ResultMessages}.
 * <p>
 * Please treat as an object of singleton, because this class is thread-safe.
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
 * <strong>[xxx-domain.xml]</strong><br>
 * inject bean of custom ExceptionLogger.
 * 
 * <pre>
 * &lt;bean id=&quot;resultMessagesLoggingInterceptor&quot;
 *   class=&quot;org.terasoluna.gfw.common.exception.ResultMessagesLoggingInterceptor&quot;&gt;
 *   &lt;property name=&quot;exceptionLogger&quot; ref=&quot;exceptionLogger&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 */
public class ResultMessagesLoggingInterceptor implements MethodInterceptor,
                                              InitializingBean {

    /**
     * Starting point of interception in thread.
     */
    private final ThreadLocal<MethodInvocation> startingPoint = new ThreadLocal<MethodInvocation>();

    /**
     * Exception logger.
     */
    private ExceptionLogger exceptionLogger = null;

    /**
     * Inject any exception logger.
     * <p>
     * if not inject, use default exception logger. <br>
     * </p>
     * @param exceptionLogger any exception logger.
     */
    public void setExceptionLogger(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    /**
     * If ResultMessagesNotificationException occurred, logging {@link org.terasoluna.gfw.common.message.ResultMessages}.
     * <p>
     * Detail Specification:
     * <ol>
     * <li>if other exception occurred , not logging exception.</li>
     * <li>if intercepted on multi point, logging do in starting point of interception.(for prevent duplicate log output)</li>
     * </ol>
     * @param invocation invocation object of intercepted target's method.
     * @return returned object from intercepted target's method.
     * @throws Throwable if occur exception in invocation target's method.
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(
            MethodInvocation invocation) throws Throwable, ResultMessagesNotificationException {

        // set starting point.
        if (startingPoint.get() == null) {
            startingPoint.set(invocation);
        }

        try {

            // invoke method.
            return invocation.proceed();

        } catch (ResultMessagesNotificationException e) {

            // output log.
            if (isStartingPoint(invocation)) {
                logResultMessagesNotificationException(e);
            }
            throw e;

        } finally {

            // clear starting point.
            if (isStartingPoint(invocation)) {
                startingPoint.remove();
            }

        }
    }

    /**
     * Initializes the ResultMessagesLoggingInterceptor.
     * <p>
     * If exception logger object is not set, use {@link org.terasoluna.gfw.common.exception.ExceptionLogger}.
     * </p>
     * <p>
     * default exception logger's name is 'org.terasoluna.gfw.common.exception.ResultMessagesLoggingInterceptor'<br>
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
     * Is the starting point of interception in thread ?
     * @param invocation invocation object of intercepted target's method.
     * @return if starting point of interception in thread, return true.
     */
    protected boolean isStartingPoint(MethodInvocation invocation) {
        return startingPoint.get() == invocation;
    }

    /**
     * logging ResultMessagesNotificationException.
     * <p>
     * default log level is 'WARN'.
     * </p>
     * @param e ResultMessagesNotificationException
     */
    protected void logResultMessagesNotificationException(
            ResultMessagesNotificationException e) {
        exceptionLogger.warn(e);
    }

    /**
     * Fetches logger object for logging exception
     * @return logger object
     */
    protected ExceptionLogger getExceptionLogger() {
        return exceptionLogger;
    }

}
