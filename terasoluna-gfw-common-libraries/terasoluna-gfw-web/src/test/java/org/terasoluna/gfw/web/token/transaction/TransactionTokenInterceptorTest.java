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
package org.terasoluna.gfw.web.token.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
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
    public void before() throws Exception {

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

    @Test
    public void testPreHandleIncorrectHandler() throws Exception {
        boolean result = interceptor.preHandle(request, response, null);
        assertThat(result, is(true));
    }

    @Test
    public void testPreHandleWithoutTokenValidate() throws Exception {
        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("first", SampleForm.class,
                                Model.class)));
        assertThat(result, is(true));
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
        assertThat(result, is(true));
        // Confirm that TokenContext is stored in the request
        assertThat(request.getAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME),
                is(notNullValue()));
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

        assertThat(result, is(true));

        TransactionTokenContext transactionTokenCtx = (TransactionTokenContext) request
                .getAttribute(
                        TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME);
        TransactionToken token = transactionTokenCtx.getReceivedToken();
        assertThat(token, is(notNullValue()));
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

        assertThat(result, is(true));

        result = interceptor.validateToken(inputToken, tokenStore, tokenInfo);

        assertThat(result, is(false));

    }

    @Test
    public void testValidateToken02() {

        HttpSessionTransactionTokenStore tokenStore = mock(
                HttpSessionTransactionTokenStore.class);
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");

        TransactionTokenInfo tokenInfo = new TransactionTokenInfo("tokenName1", TransactionTokenType.IN);

        // Set mock behavior
        when(tokenStore.getAndClear(any(TransactionToken.class))).thenReturn(
                "differentValue");

        boolean result = interceptor.validateToken(inputToken, tokenStore,
                tokenInfo);
        assertThat(result, is(false));
    }

    // ---------------Constructor related---------------

    @Test
    public void testNonParameterizedConstructor() {
        // Use parameterless constructor
        interceptor = new TransactionTokenInterceptor();
        assertThat(interceptor, is(notNullValue()));
    }

    @Test
    public void testIntConstructor() {
        // Use int constructor
        interceptor = new TransactionTokenInterceptor(10);
        assertThat(interceptor, is(notNullValue()));

    }

    // ------------------------------

    @Test
    public void testCreateReceivedToken() {
        request.setParameter(
                TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, "a~b~c");
        TransactionToken token = interceptor.createReceivedToken(request);
        assertThat(token.getTokenName(), is("a"));
    }

    @Test(expected = None.class)
    public void testRemoveEmptyToken() {
        // This test case should always pass
        // This will ensure that neither valid/invalid token removal generates exception
        interceptor.removeToken(new TransactionToken(""));
        interceptor.removeToken(new TransactionToken("a~b~c"));
    }

    @Test(expected = None.class)
    public void testPostHandleIncorrectHandler() throws Exception {

        interceptor.postHandle(request, response, null, null);
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
        assertThat(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken
                                .getTokenKey()), is(nullValue()));
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
        assertThat(request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME),
                is(notNullValue()));

        TransactionToken nextToken = (TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);

        // Confirm that next token is present in the session
        assertThat(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + nextToken.getTokenName() + "~" + nextToken
                                .getTokenKey()), is(notNullValue()));
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
        assertThat(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken
                                .getTokenKey()), is(notNullValue()));
        // Next token is also stored in request object
        assertThat(request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME),
                is(notNullValue()));
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
        assertThat(nextToken, is(notNullValue()));
        assertThat(nextToken.getTokenName(), is("testTokenAttr"));
        assertThat(nextToken.getTokenKey(), is("111"));
        assertThat(nextToken.getTokenValue(), is("222"));
        assertThat(tokenStore.getAndClear(nextToken), is("222"));
    }

    @Test(expected = None.class)
    public void testPostHandleWithNoneOperation() throws Exception {

        TransactionTokenContextImpl context = mock(
                TransactionTokenContextImpl.class);

        request.setAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME,
                context);

        when(context.getReserveCommand()).thenReturn(
                TransactionTokenContextImpl.ReserveCommand.NONE);

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                        .getDeclaredMethod("third", SampleForm.class,
                                Model.class)), null);

    }

    @Test(expected = None.class)
    public void testAfterCompletionWithoutException() {

        interceptor.afterCompletion(request, response, null, null);
    }

    @Test
    public void testAfterCompletionWithException() throws Exception {

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
        assertThat(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken
                                .getTokenKey()), is(notNullValue()));

        // Consider that exception has occured and call afterCompletion()
        // method
        // This call should remove the token from the store
        Exception ex = new InvalidTransactionTokenException();
        interceptor.afterCompletion(request, response, null, ex);

        // Confirm that token is removed from session
        assertThat(tokenStore.getSession().getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken
                                .getTokenKey()), is(nullValue()));

    }

    @Test(expected = None.class)
    public void testAfterCompletionWithExceptionHasNoTransactionTokenContextImpl() {

        interceptor.afterCompletion(request, response, null, new Exception());
    }
}
