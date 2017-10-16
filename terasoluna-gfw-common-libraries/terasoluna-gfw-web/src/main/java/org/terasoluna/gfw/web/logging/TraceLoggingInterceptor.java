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
package org.terasoluna.gfw.web.logging;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor for log output of performance measurement<br>
 * <p>
 * Interceptor which performs log output for the purpose of measurement of logic execution performance. <br>
 * Displays logic execution time from start to end in nano seconds.<br>
 * <p>
 * If end time is more than nano-seconds defined in {@code warnHandlingNanos} (default is 3000000000), then warning log is
 * output.<br>
 * <p>
 * Below is usage example
 * <h3>Example of bean definition which should be loaded by {@code org.springframework.web.servlet.DispatcherServlet}</h3>
 * 
 * <pre>
 *  &lt;mvc:interceptors&gt;
 *      ....
 *      &lt;mvc:interceptor&gt;
 *          &lt;mvc:mapping path="/**" /&gt;
 *              <strong style=color:red>&lt;bean class="org.terasoluna.gfw.web.logging.TraceLoggingInterceptor"&gt;</strong>
 *              <strong style=color:red>&lt;property name="warnHandlingNanos" value="5000000000" /&gt;</strong>
 *          &lt;/bean&gt;
 *      &lt;/mvc:interceptor&gt;
 *      ....
 *  &lt;/mvc:interceptors&gt;
 * </pre>
 * 
 * Set {@link TraceLoggingInterceptor} as definition of interceptor class.<br>
 * By specifying nano-seconds in value of {@code warnHandlingNanos}, timing of warning log output can be changed.<br>
 */
public class TraceLoggingInterceptor extends HandlerInterceptorAdapter {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(
            TraceLoggingInterceptor.class);

    /**
     * Attribute name of start time
     */
    private static final String START_ATTR = TraceLoggingInterceptor.class
            .getName() + ".startTime";

    /**
     * Attribute name of time elapsed.
     */
    private static final String HANDLING_ATTR = TraceLoggingInterceptor.class
            .getName() + ".handlingTime";

    /**
     * Default nano-seconds as after which warning log is to be output
     */
    private static final long DEFAULT_WARN_NANOS = TimeUnit.SECONDS.toNanos(3);

    /**
     * nano-seconds as after which warning log is to be output
     */
    private long warnHandlingNanos = DEFAULT_WARN_NANOS;

    /**
     * Logic to output start log
     * <p>
     * Outputs the start log and sets start time (in nano-seconds) in {@code HttpServletRequest}
     * </p>
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (logger.isTraceEnabled()) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method m = handlerMethod.getMethod();
            logger.trace("[START CONTROLLER] {}.{}({})", new Object[] { m
                    .getDeclaringClass().getSimpleName(), m.getName(),
                    buildMethodParams(handlerMethod) });
        }
        long startTime = System.nanoTime();
        request.setAttribute(START_ATTR, startTime);
        return true;
    }

    /**
     * Logic to output end log
     * <p>
     * Outputs the end log.<br>
     * Outputs warning log if difference of time between start time and end time is more than the nano-seconds value<br>
     * set as warning log output timing.
     * </p>
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {

        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        long startTime = 0;
        if (request.getAttribute(START_ATTR) != null) {
            startTime = ((Long) request.getAttribute(START_ATTR)).longValue();
        }
        long handlingTime = System.nanoTime() - startTime;
        request.removeAttribute(START_ATTR);
        request.setAttribute(HANDLING_ATTR, handlingTime);
        String formattedHandlingTime = String.format("%1$,3d", handlingTime);

        boolean isWarnHandling = handlingTime > warnHandlingNanos;

        if (!isEnabledLogLevel(isWarnHandling)) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method m = handlerMethod.getMethod();
        Object view = null;
        Map<String, Object> model = null;
        if (modelAndView != null) {
            view = modelAndView.getView();
            model = modelAndView.getModel();
            if (view == null) {
                view = modelAndView.getViewName();
            }
        }

        logger.trace("[END CONTROLLER  ] {}.{}({})-> view={}, model={}",
                new Object[] { m.getDeclaringClass().getSimpleName(), m
                        .getName(), buildMethodParams(handlerMethod), view,
                        model });
        String handlingTimeMessage = "[HANDLING TIME   ] {}.{}({})-> {} ns";
        if (isWarnHandling) {
            logger.warn(handlingTimeMessage + " > {}", new Object[] { m
                    .getDeclaringClass().getSimpleName(), m.getName(),
                    buildMethodParams(handlerMethod), formattedHandlingTime,
                    warnHandlingNanos });
        } else {
            logger.trace(handlingTimeMessage, new Object[] { m
                    .getDeclaringClass().getSimpleName(), m.getName(),
                    buildMethodParams(handlerMethod), formattedHandlingTime });
        }
    }

    /**
     * check whether warn is enabled if isWarnHandling, or trace is enabled
     * @param isWarnHandling
     * @return false if isWarnHandling and logger.warn is disabled, or logger.trace is disabled
     */
    private boolean isEnabledLogLevel(boolean isWarnHandling) {
        if (isWarnHandling) {
            if (!logger.isWarnEnabled()) {
                return false;
            }
        } else if (!logger.isTraceEnabled()) {
            return false;
        }
        return true;
    }

    /**
     * convert parameter names of the method into a String value<br>
     * <br>
     * @param handlerMethod Target methods of interceptor
     * @return String parameter string
     */
    protected static String buildMethodParams(HandlerMethod handlerMethod) {
        MethodParameter[] params = handlerMethod.getMethodParameters();
        List<String> lst = new ArrayList<String>(params.length);
        for (MethodParameter p : params) {
            lst.add(p.getParameterType().getSimpleName());
        }
        return StringUtils.collectionToCommaDelimitedString(lst);
    }

    /**
     * Set {@code warnHandlingNanos} <br>
     * Setter method for the value of warning log output timing in nano-seconds
     * @param warnHandlingNanos the value of warning log output timing in nano-seconds
     */
    public void setWarnHandlingNanos(long warnHandlingNanos) {
        this.warnHandlingNanos = warnHandlingNanos;
    }
}
