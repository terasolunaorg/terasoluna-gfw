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
package org.terasoluna.gfw.web.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.terasoluna.gfw.web.logback.LogLevelChangeUtil;
import com.google.common.collect.ImmutableList;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

@SpringJUnitConfig(locations = "classpath:/test-context.xml")
public class TraceLoggingInterceptorTest {

    private TraceLoggingInterceptor interceptor;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private Map<String, Method> methods;

    private TraceLoggingInterceptorController controller;

    private ModelAndView model;

    private Logger logger = (Logger) LoggerFactory.getLogger(TraceLoggingInterceptor.class);

    @SuppressWarnings("unchecked")
    private Appender<ILoggingEvent> mockAppender = mock(Appender.class);

    @BeforeEach
    public void before() throws Exception {
        // prepare request object
        request = new MockHttpServletRequest();

        // prepare response object
        response = new MockHttpServletResponse();

        controller = new TraceLoggingInterceptorController();
        methods = new HashMap<>();
        for (Method method : controller.getClass().getDeclaredMethods()) {
            methods.put(method.getName(), method);
        }

        model = mock(ModelAndView.class);
        interceptor = new TraceLoggingInterceptor();

        when(mockAppender.getName()).thenReturn("MOCK");
        logger.addAppender(mockAppender);
    }

    /**
     * PreHandleHttpServletRequestHttpServletResponseObject<br>
     * Log output
     * @throws Exception
     * @throws NoSuchMethodException
     */
    @Test
    public void testPreHandle_LogOutput() throws Exception {

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller,
                TraceLoggingInterceptorController.class.getMethod("createForm"));

        // run
        interceptor.preHandle(request, response, paramHandler);

        // expected
        Long startTime =
                (Long) request.getAttribute(TraceLoggingInterceptor.class.getName() + ".startTime");
        String expectedLogStr = "[START CONTROLLER] TraceLoggingInterceptorController.createForm()";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.TRACE), 1);
        assertThat(startTime).isNotNull();
    }

    /**
     * PreHandleHttpServletRequestHttpServletResponseObject<br>
     * parameter not handlermethod
     */
    @Test
    public void testPreHandle_ParamNotHandler() {

        // parameter create
        Object paramHandler = new Object();

        // run
        interceptor.preHandle(request, response, paramHandler);

        // expected
        Long startTime =
                (Long) request.getAttribute(TraceLoggingInterceptor.class.getName() + ".startTime");

        // assert
        assertThat(startTime).isNull();
        verify(mockAppender, times(0)).doAppend(any());
    }

    @Test
    public void testPreHandleIsTraceEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // parameter create
        Object paramHandler = new Object();

        // run
        interceptor.preHandle(request, response, paramHandler);

        // expected
        Long startTime =
                (Long) request.getAttribute(TraceLoggingInterceptor.class.getName() + ".startTime");

        // assert
        assertThat(startTime).isNull();
        verify(mockAppender, times(0)).doAppend(any());
        assertThat(logger.isDebugEnabled()).isFalse();

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * Normal Log Output
     */
    @Test
    public void testPostHandle_NormalLogOutput() throws Exception {

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller,
                TraceLoggingInterceptorController.class.getMethod("createForm"));
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);

        // run
        interceptor.postHandle(request, response, paramHandler, model);

        // expected
        verifyLogging(
                "[END CONTROLLER  ] TraceLoggingInterceptorController.createForm()-> view=null, model={}",
                ImmutableList.of(Level.TRACE), 1);
        verifyLogging("[HANDLING TIME   ] TraceLoggingInterceptorController.createForm()->",
                ImmutableList.of(Level.TRACE), 1);
    }

    /**
     * Normal Log Output
     */
    @Test
    public void testPostHandle_NormalLogOutput_ViewNotNull() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("second"));

        // mocketd
        View view = mock(View.class);
        when(model.getView()).thenReturn(view);

        // run
        interceptor.postHandle(request, response, paramHandler, model);

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.second(SampleForm,Model)->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.TRACE), 2);

    }

    /**
     * Normal Log Output
     */
    @Test
    public void testPostHandle_NormalLogOutput_ModelNull() {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("createForm"));

        // run
        interceptor.postHandle(request, response, paramHandler, null);

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.TRACE), 2);

    }

    /**
     * Warn Log Output
     */
    @Test
    public void testPostHandle_WarnlLogOutput() throws Exception {
        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("first"));

        // run
        interceptor.setWarnHandlingNanos(0L);
        interceptor.postHandle(request, response, paramHandler, model);

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.first(SampleForm,Model)->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.TRACE, Level.WARN), 2);
    }

    /**
     * Warn Log Output
     */
    @Test
    public void testPostHandle_WarnlLogNotOutput() throws Exception {
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.ERROR);

        // parameter create
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("first"));

        // run
        interceptor.setWarnHandlingNanos(0L);
        interceptor.postHandle(request, response, paramHandler, model);

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.first(SampleForm,Model)->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.TRACE, Level.WARN), 0);

        // reset log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * not set startTime
     */
    @Test
    public void testPostHandle_NotSetStartTime() {
        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("createForm"));

        // run
        interceptor.postHandle(request, response, paramHandler, model);

        // expected
        String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.WARN), 1);
    }

    /**
     * not handler
     */
    @Test
    public void testPostHandle_NotHandler() {
        // parameter create
        Object paramHandler = new Object();

        // run
        interceptor.postHandle(request, response, paramHandler, model);

        // expected
        final String expectedLogStr = "TraceLoggingInterceptorController.createForm()->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.TRACE), 0);
    }

    @Test
    public void testPostHandleNullStartAttr() throws Exception {
        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("createForm"));
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", null);

        // run
        interceptor.postHandle(request, response, paramHandler, null);

        // expected
        String expectedLogStr =
                "[HANDLING TIME   ] TraceLoggingInterceptorController.createForm()->";

        // assert
        verifyLogging(expectedLogStr, ImmutableList.of(Level.WARN), 1);
    }

    @Test
    public void testIsEnabledLogLevelIsWarnEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("createForm"));
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);

        // run
        interceptor.postHandle(request, response, paramHandler, model);

        // assert
        assertThat(logger.isDebugEnabled()).isFalse();

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    @Test
    public void testIsEnabledLogLevelIsTraceEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // parameter create
        HandlerMethod paramHandler = new HandlerMethod(controller, methods.get("createForm"));
        long startTime = System.nanoTime();
        request.setAttribute(TraceLoggingInterceptor.class.getName() + ".startTime", startTime);

        // run
        interceptor.postHandle(request, response, paramHandler, model);

        // assert
        assertThat(logger.isDebugEnabled()).isFalse();

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

    /**
     * verify logging.
     * @param expectedLogMessage expected log message.
     * @param expectedLogLevel expected log level.
     * @param expectedCallCount expected call count
     */
    private void verifyLogging(final String expectedLogMessage, final List<Level> expectedLogLevel,
            final int expectedCallCount) {
        verify(mockAppender, times(expectedCallCount))
                .doAppend(argThat(argument -> expectedLogLevel.contains(argument.getLevel())
                        && argument.getFormattedMessage().contains(expectedLogMessage)));
    }
}
