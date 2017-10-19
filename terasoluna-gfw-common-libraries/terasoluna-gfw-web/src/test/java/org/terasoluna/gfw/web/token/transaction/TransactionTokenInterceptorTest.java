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
package org.terasoluna.gfw.web.token.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.terasoluna.gfw.web.token.TokenStringGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class TransactionTokenInterceptorTest {

    @Autowired
    ConfigurableApplicationContext context;

    TransactionTokenInterceptor interceptor;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {

        // prepare request object
        request = new MockHttpServletRequest();
        request.setRequestURI("/token/first");
        request.setMethod("GET");

        // prepare response object
        response = new MockHttpServletResponse();

        // set ServletRequestAttributes to RequestContextHolder
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        // prepare intercepter instance
        interceptor = new TransactionTokenInterceptor();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPreHandleIncorrectHandler() throws Exception {
        boolean result = interceptor.preHandle(request, response, null);
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithoutTokenValidate() throws Exception {
        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("first", SampleForm.class,
                                Model.class)));
        assertTrue(result);
    }

    @Test
    public void testPreHandleWithTokenValidate() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)));

        // Confirm return value
        assertTrue(result);
        // Confirm that TokenContext is stored in the request
        assertNotNull(request.getAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME));
    }

    @Test(expected = InvalidTransactionTokenException.class)
    public void testPreHandleWithTokenValidateFail() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)));

    }

    @Test
    public void testPreHandleTokenIsSentInHiddenFieldAtBeginPhase() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");
        tokenStore.store(inputToken);

        assertThat(tokenStore.getSession().getAttribute(tokenStore
                .createSessionAttributeName(inputToken)), is(notNullValue()));

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "tokenName1~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("first", SampleForm.class,
                                Model.class)));

        // check if inputToken is removed
        assertThat(tokenStore.getSession().getAttribute(tokenStore
                .createSessionAttributeName(inputToken)), is(nullValue()));
    }

    @Test
    public void testPreHandleValidTokenOnCheck() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        assertThat(tokenStore.getSession().getAttribute(tokenStore
                .createSessionAttributeName(inputToken)), is(notNullValue()));

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("fifth", SampleForm.class,
                                Model.class)));

        assertTrue(result);

        TransactionTokenContext transactionTokenCtx = (TransactionTokenContext) request
                .getAttribute(
                        TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME);
        TransactionToken token = transactionTokenCtx.getReceivedToken();
        assertNotNull(token);
        assertThat(token.getTokenName(), is("testTokenAttr"));
        assertThat(token.getTokenKey(), is("111"));
        assertThat(token.getTokenValue(), is("222"));
    }

    @Test
    public void testValidateToken01() {
        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");
        tokenStore.store(inputToken);

        TransactionTokenInfo tokenInfo = new TransactionTokenInfo("tokenName1", TransactionTokenType.IN);

        boolean result = interceptor.validateToken(inputToken, tokenStore,
                tokenInfo);

        assertTrue(result);

        result = interceptor.validateToken(inputToken, tokenStore, tokenInfo);

        assertFalse(result);

    }

    @Test
    public void testValidateToken02() {

        HttpSessionTransactionTokenStore tokenStore = mock(
                HttpSessionTransactionTokenStore.class);
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");

        TransactionTokenInfo tokenInfo = new TransactionTokenInfo("tokenName1", TransactionTokenType.IN);

        // Set mock behavior
        when(tokenStore.getAndClear((TransactionToken) anyObject())).thenReturn(
                "differentValue");

        boolean result = interceptor.validateToken(inputToken, tokenStore,
                tokenInfo);
        assertFalse(result);
    }

    // ---------------Constructor related---------------

    @Test
    public void testNonParameterizedConstructor() {
        // Use parameterless constructor
        interceptor = new TransactionTokenInterceptor();
        assertNotNull(interceptor);
    }

    @Test
    public void testIntConstructor() {
        // Use int constructor
        interceptor = new TransactionTokenInterceptor(10);
        assertNotNull(interceptor);

    }

    // ------------------------------

    @Test
    public void testCreateReceivedToken() {
        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, "a~b~c");
        TransactionToken token = interceptor.createReceivedToken(request);
        assertThat(token.getTokenName(), is("a"));
    }

    @Test
    public void testRemoveEmptyToken() {
        // This test case should always pass
        // This will ensure that neither valid/invalid token removal generates
        // exception
        try {
            interceptor.removeToken(new TransactionToken(""));
            interceptor.removeToken(new TransactionToken("a~b~c"));
        } catch (Exception ex) {
            fail();
        }

    }

    @Test
    public void testPostHandleIncorrectHandler() throws Exception {

        try {
            interceptor.postHandle(request, response, null, null);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void testPostHandleWithRemoveToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)), null);

        // Confirm that token is removed from session
        assertNull(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken
                                .getTokenKey()));
    }

    @Test
    public void testPostHandleWithCreateToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "tokenName1~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("first", SampleForm.class,
                                Model.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)), null);

        // Next token is stored in request object
        assertNotNull(request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME));

        TransactionToken nextToken = (TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);

        // Confirm that next token is present in the session
        assertNotNull(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + nextToken.getTokenName() + "~" + nextToken
                                .getTokenKey()));
    }

    @Test
    public void testPostHandleWithUpdateToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("second", SampleForm.class,
                                Model.class, TransactionTokenContext.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)), null);

        // Confirm that token is still present in the session
        assertNotNull(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken
                                .getTokenKey()));
        // Next token is also stored in request object
        assertNotNull(request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME));
    }

    @Test
    public void testPostHandleWithKeepToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("fifth", SampleForm.class,
                                Model.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("fifth", SampleForm.class,
                                Model.class)), null);

        TransactionToken nextToken = (TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);
        assertNotNull(nextToken);
        assertThat(nextToken.getTokenName(), is("testTokenAttr"));
        assertThat(nextToken.getTokenKey(), is("111"));
        assertThat(nextToken.getTokenValue(), is("222"));
        assertThat(tokenStore.getAndClear(nextToken), is("222"));
    }

    @Test
    public void testPostHandleWithNoneOperation() throws Exception {

        TransactionTokenContextImpl context = mock(
                TransactionTokenContextImpl.class);

        request.setAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME,
                context);

        when(context.getReserveCommand()).thenReturn(
                TransactionTokenContextImpl.ReserveCommand.NONE);

        try {
            interceptor.postHandle(request, response,
                    new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                            .getDeclaredMethod("third", SampleForm.class,
                                    Model.class)), null);
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    public void testAfterCompletionWithoutException() {
        try {
            interceptor.afterCompletion(request, response, null, null);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAfterCompletionWithException() {
        try {

            HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
            TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
            tokenStore.store(inputToken);

            request.setParameter(
                    TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                    "testTokenAttr~111~222");

            interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(), new TransactionTokenInfoStore(), tokenStore);

            interceptor.preHandle(request, response,
                    new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                            .getDeclaredMethod("third", SampleForm.class,
                                    Model.class)));

            // Confirm that token is stored in session
            assertNotNull(tokenStore.getSession().getAttribute(
                    HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                            + inputToken.getTokenName() + "~" + inputToken
                                    .getTokenKey()));

            // Consider that exception has occured and call afterCompletion()
            // method
            // This call should remove the token from the store
            Exception ex = new InvalidTransactionTokenException();
            interceptor.afterCompletion(request, response, null, ex);

            // Confirm that token is removed from session
            assertNull(tokenStore.getSession().getAttribute(
                    HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                            + inputToken.getTokenName() + "~" + inputToken
                                    .getTokenKey()));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAfterCompletionWithExceptionHasNoTransactionTokenContextImpl() {
        try {
            interceptor.afterCompletion(request, response, null,
                    new Exception());
        } catch (Exception e) {
            fail();
        }
    }

    /*
     * @Test public void testCreateTokenSynchronization() throws Exception { int size = 2000; Thread arrThreads[] = new
     * Thread[size]; for (int i = 0; i <size ; i++) { Thread thread = new Thread(new Runnable() {
     * @Override public void run() { try { interceptor.createToken(request, session1, tokenInfo1, generator1, tokenStore1); }
     * catch (Exception ex) { ex.printStackTrace(); } } }, "Thread_" + (i+1)); arrThreads[i] = thread; } for (Thread thread :
     * arrThreads) { try { thread.start(); thread.join(); } catch (Exception e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } } }
     */
}
