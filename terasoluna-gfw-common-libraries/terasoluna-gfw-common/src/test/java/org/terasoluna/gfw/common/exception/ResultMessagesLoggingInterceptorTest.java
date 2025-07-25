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
package org.terasoluna.gfw.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.terasoluna.gfw.common.exception.test.TestFacade;
import org.terasoluna.gfw.common.exception.test.TestRepository;
import org.terasoluna.gfw.common.exception.test.TestService;
import org.terasoluna.gfw.common.message.ResultMessages;
import jakarta.inject.Inject;

/**
 * TestCase class of ResultMessagesLoggingInterceptor.
 */
@SpringJUnitConfig(locations = {"classpath:test-context.xml",
        "classpath:org/terasoluna/gfw/common/exception/ResultMessagesLoggingInterceptorTest.xml"})
public class ResultMessagesLoggingInterceptorTest extends ApplicationObjectSupport {

    /**
     * test target object.
     */
    @Inject
    private ResultMessagesLoggingInterceptor testTarget;

    /**
     * Mock object of ExceptionLogger.
     */
    private ExceptionLogger mockExceptionLogger;

    /**
     * Mock object of MethodInvocation.
     */
    private MethodInvocation mockMethodInvocation;

    /**
     * Setup.
     * <p>
     * <ol>
     * <li>setup mock object.</li>
     * </ol>
     * </p>
     * @throws Exception
     */
    @BeforeEach
    public void before() throws Exception {
        mockMethodInvocation = mock(MethodInvocation.class);
        mockExceptionLogger = mock(ExceptionLogger.class);
        testTarget.setExceptionLogger(mockExceptionLogger);
        testTarget.afterPropertiesSet();
    }

    /**
     * [invoke] Case of not occur exception.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>call target method.</li>
     * <li>log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvoke_not_occur_exception() throws Throwable {

        // do setup for test case.
        // nothing.

        // do test.
        testTarget.invoke(mockMethodInvocation);

        // do assert.
        verify(mockMethodInvocation, times(1)).proceed();
        verify(mockExceptionLogger, never()).warn((Exception) any());

    }

    @Test
    public void testInvokeOccurBusinessexception01() throws Throwable {
        // do setup for test case.
        BusinessException occurException = new BusinessException("testing businessexception");
        when(mockMethodInvocation.proceed()).thenThrow(occurException);

        BusinessException e = assertThrows(BusinessException.class, () -> {
            // do test.
            testTarget.invoke(mockMethodInvocation);
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, times(1)).warn(occurException);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));

    }

    /**
     * [invoke] Case of occur BusinessException.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>throws BusinessException.</li>
     * <li>BusinessException log is output.</li>
     * <li>unnecessary log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvokeOccurBusinessexception02() throws Throwable {
        // do setup for test case.
        BusinessException occurException =
                new BusinessException(ResultMessages.error().add("e.cm.xxx1", "args1", "args2"));
        when(mockMethodInvocation.proceed()).thenThrow(occurException);

        BusinessException e = assertThrows(BusinessException.class, () -> {
            // do test.
            testTarget.invoke(mockMethodInvocation);
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, times(1)).warn(occurException);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));

    }

    @Test
    public void testInvokeOccurBusinessexception03() throws Throwable {
        // do setup for test case.
        BusinessException occurException = new BusinessException(
                ResultMessages.error().add("e.cm.xxx1", "args1", "args2"), null);
        when(mockMethodInvocation.proceed()).thenThrow(occurException);

        BusinessException e = assertThrows(BusinessException.class, () -> {
            // do test.
            testTarget.invoke(mockMethodInvocation);
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, times(1)).warn(occurException);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));

    }

    @Test
    public void testInvokeOccurResourcenotfoundexceptionInNestedMethod01() throws Throwable {
        // do setup for test case.
        ResourceNotFoundException occurException =
                new ResourceNotFoundException("testing resourcenotfoundexception");

        TestService service = getApplicationContext().getBean(TestService.class);
        service.setResultMessagesNotificationException(occurException);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            // do test.
            TestFacade facade = getApplicationContext().getBean(TestFacade.class);
            facade.getMessage();
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, times(1)).warn(occurException);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));

    }

    /**
     * [invoke] Case of occur BusinessException in nested method.
     * <p>
     * {@link TestFacade#getMessage()} => {@link TestService#getMessage()}(nested method) => ...<br>
     * *) TestFacade and TestService are interception target.
     * </p>
     * <p>
     * [Expected Result]
     * <ol>
     * <li>throws BusinessException.</li>
     * <li>BusinessException log is output & not duplicate.</li>
     * <li>unnecessary log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvokeOccurResourcenotfoundexceptionInNestedMethod02() throws Throwable {
        // do setup for test case.
        ResourceNotFoundException occurException = new ResourceNotFoundException(
                ResultMessages.error().add("e.cm.xxx1", "args1", "args2"));

        TestService service = getApplicationContext().getBean(TestService.class);
        service.setResultMessagesNotificationException(occurException);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            // do test.
            TestFacade facade = getApplicationContext().getBean(TestFacade.class);
            facade.getMessage();
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, times(1)).warn(occurException);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));

    }

    @Test
    public void testInvokeOccurResourcenotfoundexceptionInNestedMethod03() throws Throwable {
        // do setup for test case.
        ResourceNotFoundException occurException = new ResourceNotFoundException(
                ResultMessages.error().add("e.cm.xxx1", "args1", "args2"), null);

        TestService service = getApplicationContext().getBean(TestService.class);
        service.setResultMessagesNotificationException(occurException);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            // do test.
            TestFacade facade = getApplicationContext().getBean(TestFacade.class);
            facade.getMessage();
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, times(1)).warn(occurException);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));

    }

    /**
     * [invoke] Case of occur other exception(not BusinessException).
     * <p>
     * [Expected Result]
     * <ol>
     * <li>throws exception.</li>
     * <li>exception log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvoke_occur_other_exception() throws Throwable {

        // do setup for test case.
        NullPointerException occurException =
                new NullPointerException("testInvoke_occur_other_exception");
        when(mockMethodInvocation.proceed()).thenThrow(occurException);

        NullPointerException e = assertThrows(NullPointerException.class, () -> {
            // do test.
            testTarget.invoke(mockMethodInvocation);
        });
        // do assert.
        assertThat(e, is(occurException));
        verify(mockExceptionLogger, never()).warn(any(Exception.class));

    }

    /**
     * [invoke] Case of intercept two times(multi times) on same thread(+other stack).
     * <p>
     * [Expected Result]
     * <ol>
     * <li>throws exception.</li>
     * <li>BusinessException log is output in both interception.(verify ThreadLocal variable is
     * removed.)</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvoke_multi_times_on_same_thread() throws Throwable {
        // do setup for test case.
        // nothing.

        BusinessException occurException =
                new BusinessException(ResultMessages.error().add("e.cm.xxx1", "args1", "args2"));
        when(mockMethodInvocation.proceed()).thenThrow(occurException);

        // do test.
        // 1st intercept.
        BusinessException e = assertThrows(BusinessException.class, () -> {
            testTarget.invoke(mockMethodInvocation);
        });
        assertThat(e, is(occurException));
        // 2nd intercept.
        e = assertThrows(BusinessException.class, () -> {
            testTarget.invoke(mockMethodInvocation);
        });
        assertThat(e, is(occurException));

        // do assert.
        verify(mockExceptionLogger, times(2)).warn(occurException);
        verify(mockExceptionLogger, times(2)).warn(any(Exception.class));

    }

    /**
     * [invoke] Case of not occur exception in a multit-hreaded environment.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>method's return value is expected.</li>
     * <li>log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvoke_multi_thread_not_occur_exception() throws Throwable {

        // do setup for test case.
        final TestFacade facade = getApplicationContext().getBean(TestFacade.class);
        final Map<Thread, String> actualMessage = new HashMap<Thread, String>();

        // setup for thread1.
        Thread thread1 = new Thread(() -> {
            facade.setSleepTime(Long.valueOf(2));
            actualMessage.put(Thread.currentThread(), facade.getMessage());
        });

        // setup for thread2.
        Thread thread2 = new Thread(() -> {
            facade.setSleepTime(Long.valueOf(0));
            actualMessage.put(Thread.currentThread(), facade.getMessage());
        });

        // do test.
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        thread2.start();

        // wait thread finish.
        thread1.join();
        thread2.join();

        // do assert.
        String expectedBaseMessage =
                getApplicationContext().getBean(TestRepository.class).toString() + ":";
        assertThat(actualMessage, hasEntry(thread1, expectedBaseMessage + thread1.getId()));
        assertThat(actualMessage, hasEntry(thread2, expectedBaseMessage + thread2.getId()));

        verify(mockExceptionLogger, never()).warn(any(Exception.class));
    }

    /**
     * [invoke] Case of occur BusinessException on both thread in a multit-hreaded environment.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>throws BusinessException on both thread.</li>
     * <li>BusinessException log is output & not duplicate on both thread.</li>
     * <li>unnecessary log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvoke_multi_thread_occur_businessexception_both_thread() throws Throwable {

        // do setup for test case.
        final TestFacade facade = getApplicationContext().getBean(TestFacade.class);
        final TestService service = getApplicationContext().getBean(TestService.class);
        final Map<Thread, BusinessException> actualBusinessException =
                new HashMap<Thread, BusinessException>();

        // setup for thread1.
        final BusinessException occurExceptionForThread1 =
                new BusinessException(ResultMessages.error().add("e.cm.thread1"));
        Thread thread1 = new Thread(() -> {
            service.setResultMessagesNotificationException(occurExceptionForThread1);
            facade.setSleepTime(Long.valueOf(2));
            try {
                facade.getMessage();
            } catch (BusinessException e) {
                actualBusinessException.put(Thread.currentThread(), e);
            }
        });

        // setup for thread2.
        final BusinessException occurExceptionForThread2 =
                new BusinessException(ResultMessages.error().add("e.cm.thread2"));
        Thread thread2 = new Thread(() -> {
            service.setResultMessagesNotificationException(occurExceptionForThread2);
            facade.setSleepTime(Long.valueOf(0));
            try {
                facade.getMessage();
            } catch (BusinessException e) {
                actualBusinessException.put(Thread.currentThread(), e);
            }
        });

        // do test.
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        thread2.start();

        // wait thread finish.
        thread1.join();
        thread2.join();

        // do assert
        assertThat(actualBusinessException, hasEntry(thread1, occurExceptionForThread1));
        assertThat(actualBusinessException, hasEntry(thread2, occurExceptionForThread2));

        verify(mockExceptionLogger, times(1)).warn(occurExceptionForThread1);
        verify(mockExceptionLogger, times(1)).warn(occurExceptionForThread2);
        verify(mockExceptionLogger, times(2)).warn(any(Exception.class));
    }

    /**
     * [invoke] Case of occur BusinessException on one thread in a multit-hreaded environment.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>throws BusinessException on one thread.</li>
     * <li>BusinessException log is output & not duplicate on one thread.</li>
     * <li>unnecessary log is not output.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testInvoke_multi_thread_occur_businessexception_one_thread() throws Throwable {

        // do setup for test case.
        final TestFacade facade = getApplicationContext().getBean(TestFacade.class);
        final TestService service = getApplicationContext().getBean(TestService.class);
        final Map<Thread, BusinessException> actualBusinessException =
                new HashMap<Thread, BusinessException>();

        // setup for thread1.
        Thread thread1 = new Thread(() -> {
            facade.setSleepTime(Long.valueOf(2));
            try {
                facade.getMessage();
            } catch (BusinessException e) {
                actualBusinessException.put(Thread.currentThread(), e);
            }
        });

        // setup for thread2.
        final BusinessException occurExceptionForThread2 =
                new BusinessException(ResultMessages.error().add("e.cm.thread2"));
        Thread thread2 = new Thread(() -> {
            service.setResultMessagesNotificationException(occurExceptionForThread2);
            facade.setSleepTime(Long.valueOf(0));
            try {
                facade.getMessage();
            } catch (BusinessException e) {
                actualBusinessException.put(Thread.currentThread(), e);
            }
        });

        // do test.
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        thread2.start();

        // wait thread finish.
        thread1.join();
        thread2.join();

        // do assert
        assertThat(actualBusinessException, not(hasKey(thread1)));
        assertThat(actualBusinessException, hasEntry(thread2, occurExceptionForThread2));

        verify(mockExceptionLogger, times(1)).warn(occurExceptionForThread2);
        verify(mockExceptionLogger, times(1)).warn(any(Exception.class));
    }

    /**
     * [getExceptionLogger] call getExceptionLogger.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>Returned instance is same instance that is set to target.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testGetExceptionLogger() throws Throwable {
        assertThat(testTarget.getExceptionLogger(), is(mockExceptionLogger));
    }

}
