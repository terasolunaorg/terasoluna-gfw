/*
 * Copyright(c) 2013 NTT DATA Corporation.
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
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.terasoluna.gfw.web.token.TokenStringGenerator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * Test class for HttpSessionTransactionTokenStore
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class HttpSessionTransactionTokenStoreTest {

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConfigurableApplicationContext context;

    HttpSessionTransactionTokenStore store;

    @SuppressWarnings("unchecked")
    private Appender<ILoggingEvent> mockAppender = mock(Appender.class);

    @Before
    public void setUp() throws Exception {
        // prepare request object
        request = new MockHttpServletRequest();

        // prepare response object
        response = new MockHttpServletResponse();

        // set ServletRequestAttributes to RequestContextHolder
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        // mock logger
        Logger logger = (Logger) LoggerFactory.getLogger(
                HttpSessionTransactionTokenStore.class);
        logger.addAppender(mockAppender);
    }

    /**
     * tokenHolder is null
     */
    @Test
    public void testGetAndClear01() {
        // setup parameters
        HttpSession session = new MockHttpSession();
        request.setSession(session);
        TransactionToken token = new TransactionToken("TransactionToken");

        // prepare store instance
        store = new HttpSessionTransactionTokenStore();

        // run
        String actuals = store.getAndClear(token);

        // assert
        assertThat(actuals, is(nullValue()));
    }

    /**
     * tokenHolder is not null
     */
    @Test
    public void testGetAndClear02() {
        // setup parameters
        HttpSession session = new MockHttpSession();
        TransactionToken token = new TransactionToken("tokenName", "tokenKey", "tokenValue");
        session.setAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + token.getTokenName() + token.getTokenKey(), token);
        request.setSession(session);

        // prepare store instance
        store = new HttpSessionTransactionTokenStore();
        store.store(token);

        // run
        String actuals = store.getAndClear(token);

        // assert
        assertThat(actuals, is(token.getTokenValue()));
        assertThat(session.getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + token.getTokenName() + token.getTokenKey()), is(
                                notNullValue()));
    }

    @Test
    public void testGetAndClear_tokenIsNull() {
        store = new HttpSessionTransactionTokenStore();

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    store.getAndClear(null);
                });
        assertThat(e.getMessage(), is("token must not be null"));
    }

    @Test
    public void testRemove() {
        // setup parameters
        HttpSession session = new MockHttpSession();
        request.setSession(session);
        TransactionToken token = new TransactionToken("tokenName", "tokenKey", "tokenValue");

        // prepare store instance
        store = new HttpSessionTransactionTokenStore();
        store.store(token);

        // run
        store.remove(token);

        // assert
        assertThat(session.getAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + token.getTokenName() + token.getTokenKey()), is(
                                nullValue()));
    }

    @Test
    public void testRemove_tokenIsNull() throws Exception {
        store = new HttpSessionTransactionTokenStore();

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    store.remove(null);
                });
        assertThat(e.getMessage(), is("token must not be null"));
    }

    @Test
    public void testRemoveIllegalState() {
        // setup parameters
        HttpSession session = mock(MockHttpSession.class);
        doThrow(IllegalStateException.class).when(session).removeAttribute(
                anyString());
        request.setSession(session);
        TransactionToken token = new TransactionToken("tokenName", "tokenKey", "tokenValue");

        // prepare store instance
        store = new HttpSessionTransactionTokenStore();
        store.store(token);

        // run
        store.remove(token);

        // assert
        verify(mockAppender).doAppend(argThat(argument -> argument.getLevel()
                .equals(Level.DEBUG)));
    }

    /**
     * create a new Token key (stored token is as it is the same as sizePerTokenName)
     */
    @Test
    public void testCreateAndReserveTokenKey_storedToken_is_as_same_as_sizePerTokenName() throws InterruptedException {
        // prepare store instance
        store = new HttpSessionTransactionTokenStore(4, 4);

        // setup parameters
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        TransactionToken tokenA = new TransactionToken("tokenName", "tokenKeyA", "tokenValueA");
        store.store(tokenA);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenB = new TransactionToken("tokenName", "tokenKeyB", "tokenValueB");
        store.store(tokenB);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenC = new TransactionToken("tokenName", "tokenKeyC", "tokenValueC");
        store.store(tokenC);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenD = new TransactionToken("tokenName", "tokenKeyD", "tokenValueD");
        store.store(tokenD);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token1 = new TransactionToken("tokenName1", "tokenKey1", "tokenValue1");
        store.store(token1);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token2 = new TransactionToken("tokenName2", "tokenKey2", "tokenValue2");
        store.store(token2);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token3 = new TransactionToken("tokenName3", "tokenKey3", "tokenValue3");
        store.store(token3);

        // run
        String actual = store.createAndReserveTokenKey(tokenA.getTokenName());

        // assert
        assertThat(actual, is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenA)), is(nullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenB)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenC)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenD)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token1)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token2)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token3)), is(notNullValue()));
    }

    /**
     * create a new Token key (stored token is as it is the same as sizePerTokenName)
     */
    @Test
    public void testCreateAndReserveTokenKey_storedToken_is_greater_than_sizePerTokenName() throws InterruptedException {
        // prepare store instance
        store = new HttpSessionTransactionTokenStore(4, 4);

        // setup parameters
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        TransactionToken tokenA = new TransactionToken("tokenName", "tokenKeyA", "tokenValueA");
        store.store(tokenA);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenB = new TransactionToken("tokenName", "tokenKeyB", "tokenValueB");
        store.store(tokenB);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenC = new TransactionToken("tokenName", "tokenKeyC", "tokenValueC");
        store.store(tokenC);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenD = new TransactionToken("tokenName", "tokenKeyD", "tokenValueE");
        store.store(tokenD);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenE = new TransactionToken("tokenName", "tokenKeyE", "tokenValueD");
        store.store(tokenE);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token1 = new TransactionToken("tokenName1", "tokenKey1", "tokenValue1");
        store.store(token1);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token2 = new TransactionToken("tokenName2", "tokenKey2", "tokenValue2");
        store.store(token2);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token3 = new TransactionToken("tokenName3", "tokenKey3", "tokenValue3");
        store.store(token3);

        // run
        String actual = store.createAndReserveTokenKey(tokenA.getTokenName());

        // assert
        assertThat(actual, is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenA)), is(nullValue())); // check
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenB)), is(nullValue())); // check
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenC)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenD)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenE)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token1)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token2)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token3)), is(notNullValue()));
    }

    /**
     * create a new Token key (stored token is as it is the same as sizePerTokenName)
     */
    @Test
    public void testCreateAndReserveTokenKey_storedToken_is_greater_than_sizePerTokenName2() throws InterruptedException {
        // prepare store instance
        store = new HttpSessionTransactionTokenStore(4, 4);

        // setup parameters
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        TransactionToken tokenA = new TransactionToken("tokenName", "tokenKeyA", "tokenValueA");
        store.store(tokenA);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenB = new TransactionToken("tokenName", "tokenKeyB", "tokenValueB");
        store.store(tokenB);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenC = new TransactionToken("tokenName", "tokenKeyC", "tokenValueC");
        store.store(tokenC);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenD = new TransactionToken("tokenName", "tokenKeyD", "tokenValueD");
        store.store(tokenD);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenE = new TransactionToken("tokenName", "tokenKeyE", "tokenValueE");
        store.store(tokenE);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken tokenF = new TransactionToken("tokenName", "tokenKeyF", "tokenValueF");
        store.store(tokenF);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token1 = new TransactionToken("tokenName1", "tokenKey1", "tokenValue1");
        store.store(token1);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token2 = new TransactionToken("tokenName2", "tokenKey2", "tokenValue2");
        store.store(token2);
        TimeUnit.MILLISECONDS.sleep(1);
        TransactionToken token3 = new TransactionToken("tokenName3", "tokenKey3", "tokenValue3");
        store.store(token3);

        // run
        String actual = store.createAndReserveTokenKey(tokenA.getTokenName());

        // assert
        assertThat(actual, is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenA)), is(nullValue())); // check
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenB)), is(nullValue())); // check
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenC)), is(nullValue())); // check
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenD)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenE)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenF)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token1)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token2)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token3)), is(notNullValue()));
    }

    /**
     * create a new Token key (stored token is less than sizePerTokenName)
     */
    @Test
    public void testCreateAndReserveTokenKey_storedToken_isLessThan_sizePerTokenName() {
        // prepare store instance
        store = new HttpSessionTransactionTokenStore(5);

        // setup parameters
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        TransactionToken tokenA = new TransactionToken("tokenName", "tokenKeyA", "tokenValueA");
        store.store(tokenA);
        TransactionToken tokenB = new TransactionToken("tokenName", "tokenKeyB", "tokenValueB");
        store.store(tokenB);
        TransactionToken tokenC = new TransactionToken("tokenName", "tokenKeyC", "tokenValueC");
        store.store(tokenC);
        TransactionToken token1 = new TransactionToken("tokenName1", "tokenKey1", "tokenValue1");
        store.store(token1);
        TransactionToken token2 = new TransactionToken("tokenName2", "tokenKey2", "tokenValue2");
        store.store(token2);
        TransactionToken token3 = new TransactionToken("tokenName3", "tokenKey3", "tokenValue3");
        store.store(token3);

        // run
        String actual = store.createAndReserveTokenKey(tokenA.getTokenName());

        // assert
        assertThat(actual, is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenA)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenB)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                tokenC)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token1)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token2)), is(notNullValue()));
        assertThat(session.getAttribute(store.createSessionAttributeName(
                token3)), is(notNullValue()));
    }

    @Test
    public void testCreateAndReserveTokenKey_generate_failed() {
        // prepare store instance
        store = new HttpSessionTransactionTokenStore(new TokenStringGenerator() {
            @Override
            public String generate(String seed) {
                // return always same value
                return "xxxxx";
            }
        }, 5, 5);

        // setup parameters
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        session.setAttribute(
                HttpSessionTransactionTokenStore.TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                        + "foo" + TransactionToken.TOKEN_STRING_SEPARATOR
                        + "xxxxx", "already in!");

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> {
                    store.createAndReserveTokenKey("foo");
                });
        assertThat(e.getMessage(), is(
                "token key generation failed within retry count 5"));
    }

    @Test
    public void testStore_token_isNull() {
        store = new HttpSessionTransactionTokenStore(5);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    store.store(null);
                });
        assertThat(e.getMessage(), is("token must not be null"));
    }

    @Test
    public void testConstructor_generator_isNull() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    new HttpSessionTransactionTokenStore(null, 10, 10);
                });
        assertThat(e.getMessage(), is("generator must not be null"));
    }

    @Test
    public void testConstructor_transactionTokensPerTokenName_isZero() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    new HttpSessionTransactionTokenStore(new TokenStringGenerator(), 0, 1);
                });
        assertThat(e.getMessage(), is(
                "transactionTokenSizePerTokenName must be greater than 0"));
    }

    @Test
    public void testConstructor_transactionTokensPerTokenName_isNegative() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    new HttpSessionTransactionTokenStore(new TokenStringGenerator(), -1, 0);
                });
        assertThat(e.getMessage(), is(
                "transactionTokenSizePerTokenName must be greater than 0"));
    }

    @Test
    public void testConstructor_retryCreateTokenName_isZero() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    new HttpSessionTransactionTokenStore(new TokenStringGenerator(), 1, 0);
                });
        assertThat(e.getMessage(), is(
                "retryCreateTokenName must be greater than 0"));
    }

    @Test
    public void testConstructor_retryCreateTokenName_isNegative() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    new HttpSessionTransactionTokenStore(new TokenStringGenerator(), 1, -1);
                });
        assertThat(e.getMessage(), is(
                "retryCreateTokenName must be greater than 0"));
    }
}
