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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.terasoluna.gfw.web.token.TokenStringGenerator;

@SpringJUnitConfig(locations = {"classpath:test-context.xml"})
public class TransactionTokenInterceptorTest {

    @Autowired
    ConfigurableApplicationContext context;

    TransactionTokenInterceptor interceptor;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @BeforeEach
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
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testPreHandleWithoutTokenValidate() throws Exception {
        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("first",
                                SampleForm.class, Model.class)));
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testPreHandleWithTokenValidate() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("third",
                                SampleForm.class, Model.class)));

        // Confirm return value
        assertThat(result).isEqualTo(true);
        // Confirm that TokenContext is stored in the request
        assertThat(request.getAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME)).isNotNull();
    }

    @Test
    public void testPreHandleWithTokenValidateFail() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        assertThrows(InvalidTransactionTokenException.class, () -> {
            interceptor.preHandle(request, response,
                    new HandlerMethod(new TransactionTokenSampleController(),
                            TransactionTokenSampleController.class.getDeclaredMethod("third",
                                    SampleForm.class, Model.class)));
        });
    }

    @Test
    public void testPreHandleTokenIsSentInHiddenFieldAtBeginPhase() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");
        tokenStore.store(inputToken);

        assertThat(tokenStore.getSession().getAttribute(
                tokenStore.createSessionAttributeName(inputToken))).isNotNull();

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "tokenName1~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("first",
                                SampleForm.class, Model.class)));

        // check if inputToken is removed
        assertThat(tokenStore.getSession()
                .getAttribute(tokenStore.createSessionAttributeName(inputToken))).isNull();
    }

    @Test
    public void testPreHandleValidTokenOnCheck() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        assertThat(tokenStore.getSession().getAttribute(
                tokenStore.createSessionAttributeName(inputToken))).isNotNull();

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        boolean result = interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("fifth",
                                SampleForm.class, Model.class)));

        assertThat(result).isEqualTo(true);

        TransactionTokenContext transactionTokenCtx = (TransactionTokenContext) request
                .getAttribute(TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME);
        TransactionToken token = transactionTokenCtx.getReceivedToken();
        assertThat(token).isNotNull();
        assertThat(token.getTokenName()).isEqualTo("testTokenAttr");
        assertThat(token.getTokenKey()).isEqualTo("111");
        assertThat(token.getTokenValue()).isEqualTo("222");
    }

    @Test
    public void testValidateToken01() {
        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");
        tokenStore.store(inputToken);

        TransactionTokenInfo tokenInfo =
                new TransactionTokenInfo("tokenName1", TransactionTokenType.IN);

        boolean result = interceptor.validateToken(inputToken, tokenStore, tokenInfo);

        assertThat(result).isEqualTo(true);

        result = interceptor.validateToken(inputToken, tokenStore, tokenInfo);

        assertThat(result).isEqualTo(false);

    }

    @Test
    public void testValidateToken02() {

        HttpSessionTransactionTokenStore tokenStore = mock(HttpSessionTransactionTokenStore.class);
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");

        TransactionTokenInfo tokenInfo =
                new TransactionTokenInfo("tokenName1", TransactionTokenType.IN);

        // Set mock behavior
        when(tokenStore.getAndClear(any(TransactionToken.class))).thenReturn("differentValue");

        boolean result = interceptor.validateToken(inputToken, tokenStore, tokenInfo);
        assertThat(result).isEqualTo(false);
    }

    // ---------------Constructor related---------------

    @Test
    public void testNonParameterizedConstructor() {
        // Use parameterless constructor
        interceptor = new TransactionTokenInterceptor();
        assertThat(interceptor).isNotNull();
    }

    @Test
    public void testIntConstructor() {
        // Use int constructor
        interceptor = new TransactionTokenInterceptor(10);
        assertThat(interceptor).isNotNull();

    }

    // ------------------------------

    @Test
    public void testCreateReceivedToken() {
        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, "a~b~c");
        TransactionToken token = interceptor.createReceivedToken(request);
        assertThat(token.getTokenName()).isEqualTo("a");
    }

    @Test
    public void testRemoveEmptyToken() {
        // This test case should always pass
        // This will ensure that neither valid/invalid token removal generates exception
        assertDoesNotThrow(() -> {
            interceptor.removeToken(new TransactionToken(""));
            interceptor.removeToken(new TransactionToken("a~b~c"));
        });

    }

    @Test
    public void testPostHandleIncorrectHandler() throws Exception {
        assertDoesNotThrow(() -> {
            interceptor.postHandle(request, response, null, null);
        });
    }

    @Test
    public void testPostHandleWithRemoveToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("third",
                                SampleForm.class, Model.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("third",
                                SampleForm.class, Model.class)),
                null);

        // Confirm that token is removed from session
        assertThat(tokenStore.getSession()
                .getAttribute(HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken.getTokenKey())).isNull();
    }

    @Test
    public void testPostHandleWithCreateToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("tokenName1", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "tokenName1~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("first",
                                SampleForm.class, Model.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("third",
                                SampleForm.class, Model.class)),
                null);

        // Next token is stored in request object
        assertThat(request.getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME)).isNotNull();

        TransactionToken nextToken = (TransactionToken) request
                .getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);

        // Confirm that next token is present in the session
        assertThat(tokenStore.getSession()
                .getAttribute(HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + nextToken.getTokenName() + "~" + nextToken.getTokenKey())).isNotNull();
    }

    @Test
    public void testPostHandleWithUpdateToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("second",
                                SampleForm.class, Model.class, TransactionTokenContext.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("third",
                                SampleForm.class, Model.class)),
                null);

        // Confirm that token is still present in the session
        assertThat(tokenStore.getSession()
                .getAttribute(HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken.getTokenKey())).isNotNull();
        // Next token is also stored in request object
        assertThat(request.getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME)).isNotNull();
    }

    @Test
    public void testPostHandleWithKeepToken() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("fifth",
                                SampleForm.class, Model.class)));

        interceptor.postHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("fifth",
                                SampleForm.class, Model.class)),
                null);

        TransactionToken nextToken = (TransactionToken) request
                .getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);
        assertThat(nextToken).isNotNull();
        assertThat(nextToken.getTokenName()).isEqualTo("testTokenAttr");
        assertThat(nextToken.getTokenKey()).isEqualTo("111");
        assertThat(nextToken.getTokenValue()).isEqualTo("222");
        assertThat(tokenStore.getAndClear(nextToken)).isEqualTo("222");
    }

    @Test
    public void testPostHandleWithNoneOperation() throws Exception {

        TransactionTokenContextImpl context = mock(TransactionTokenContextImpl.class);

        request.setAttribute(TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME,
                context);

        when(context.getReserveCommand())
                .thenReturn(TransactionTokenContextImpl.ReserveCommand.NONE);

        assertDoesNotThrow(() -> {
            interceptor.postHandle(request, response,
                    new HandlerMethod(new TransactionTokenSampleController(),
                            TransactionTokenSampleController.class.getDeclaredMethod("third",
                                    SampleForm.class, Model.class)),
                    null);
        });
    }

    @Test
    public void testAfterCompletionWithoutException() {
        assertDoesNotThrow(() -> {
            interceptor.afterCompletion(request, response, null, null);
        });
    }

    @Test
    public void testAfterCompletionWithException() throws Exception {

        HttpSessionTransactionTokenStore tokenStore = new HttpSessionTransactionTokenStore();
        TransactionToken inputToken = new TransactionToken("testTokenAttr", "111", "222");
        tokenStore.store(inputToken);

        request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                "testTokenAttr~111~222");

        interceptor = new TransactionTokenInterceptor(new TokenStringGenerator(),
                new TransactionTokenInfoStore(), tokenStore);

        interceptor.preHandle(request, response,
                new HandlerMethod(new TransactionTokenSampleController(),
                        TransactionTokenSampleController.class.getDeclaredMethod("third",
                                SampleForm.class, Model.class)));

        // Confirm that token is stored in session
        assertThat(tokenStore.getSession()
                .getAttribute(HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken.getTokenKey())).isNotNull();

        // Consider that exception has occured and call afterCompletion()
        // method
        // This call should remove the token from the store
        Exception ex = new InvalidTransactionTokenException();
        interceptor.afterCompletion(request, response, null, ex);

        // Confirm that token is removed from session
        assertThat(tokenStore.getSession()
                .getAttribute(HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + inputToken.getTokenName() + "~" + inputToken.getTokenKey())).isNull();

    }

    @Test
    public void testAfterCompletionWithExceptionHasNoTransactionTokenContextImpl() {
        assertDoesNotThrow(() -> {
            interceptor.afterCompletion(request, response, null, new Exception());
        });
    }
}
