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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;
import org.terasoluna.gfw.web.token.TokenStringGenerator;

/**
 * Implementation of {@link TransactionTokenStore} interface which uses HTTP session to store the token <br>
 */
public class HttpSessionTransactionTokenStore implements TransactionTokenStore {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(
            HttpSessionTransactionTokenStore.class);

    /**
     * attribute name of token holder in the session scope
     */
    public static final String TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX = HttpSessionTransactionTokenStore.class
            .getName() + "_TOKEN_";

    /**
     * default token size per token name
     */
    public static final int NO_OF_TOKENS_PER_TOKEN_NAME = 10;

    /**
     * default retry count to generate token
     */
    public static final int DEFAULT_RETRY_CREATE_TOKEN_NAME = 10;

    /** Allowed number of transactionTokens for each tokenName */
    private final int transactionTokensPerTokenName;

    /** Number of retries for creating a sessionAttributeName from a given tokenName */
    private final int retryCreateTokenName;

    /** generator for token string */
    private final TokenStringGenerator generator;

    /**
     * Default constructor <br>
     * <p>
     * By default, number of tokenKeys per tokenName is set to 10. And number of retries to create a tokenName is set to 10.
     */
    public HttpSessionTransactionTokenStore() {
        this(new TokenStringGenerator(), NO_OF_TOKENS_PER_TOKEN_NAME,
                DEFAULT_RETRY_CREATE_TOKEN_NAME);
    }

    /**
     * Constructor. Takes transactionTokensPerTokenName as an argument <br>
     * <p>
     * transactionTokensPerTokenName indicates the number of transactionTokens that can be stored in the session for each <br>
     * tokenName at a time.
     * </p>
     * @param transactionTokenSizePerTokenName Allowed number of tokens for each tokenName(must be greater than 0)
     * @throws IllegalArgumentException sizePerTokenName is (less than or equals 0)
     */
    public HttpSessionTransactionTokenStore(
            int transactionTokenSizePerTokenName) {
        this(new TokenStringGenerator(), transactionTokenSizePerTokenName,
                DEFAULT_RETRY_CREATE_TOKEN_NAME);
    }

    /**
     * Constructor. Takes transactionTokensPerTokenName and retryCreateTokenName as an argument <br>
     * <p>
     * transactionTokensPerTokenName indicates the number of transactionTokens that can be stored in the session for each <br>
     * tokenName at a time.<br>
     * <br>
     * retryCreateTokenName indicates the number of times retrys are done to create a token name
     * </p>
     * @param transactionTokenSizePerTokenName Allowed number of tokens for each tokenName(must be greater than 0)
     * @param retryCreateTokenName Number of retries for creating tokenName(must be greater than 0)
     * @throws IllegalArgumentException sizePerTokenName is (less than or equals 0) or retryCreateTokenName is (less than or
     *             equals 0)
     */
    public HttpSessionTransactionTokenStore(
            int transactionTokenSizePerTokenName, int retryCreateTokenName) {
        this(new TokenStringGenerator(), transactionTokenSizePerTokenName,
                retryCreateTokenName);
    }

    /**
     * Constructor. Enables customization of the token store <br>
     * @param generator {@link TokenStringGenerator} instance (must not be null)
     * @param transactionTokenSizePerTokenName Allowed number of tokens for each tokenName(must be greater than 0)
     * @param retryCreateTokenName Number of retries for creating tokenName(must be greater than 0)
     * @throws IllegalArgumentException generator is null or sizePerTokenName is (less than or equals 0) or retryCreateTokenName
     *             is (less than or equals 0)
     */
    public HttpSessionTransactionTokenStore(
            final TokenStringGenerator generator,
            final int transactionTokenSizePerTokenName,
            final int retryCreateTokenName) {
        if (generator == null) {
            throw new IllegalArgumentException("generator must not be null");
        }
        if (transactionTokenSizePerTokenName <= 0) {
            throw new IllegalArgumentException("transactionTokenSizePerTokenName must be greater than 0");
        }
        if (retryCreateTokenName <= 0) {
            throw new IllegalArgumentException("retryCreateTokenName must be greater than 0");
        }
        this.generator = generator;
        this.transactionTokensPerTokenName = transactionTokenSizePerTokenName;
        this.retryCreateTokenName = retryCreateTokenName;
    }

    /**
     * Fetches the value stored in session corresponding to the {@link TransactionToken} received as argument to this method. <br>
     * <p>
     * This value corresponding to the same transactionToken instance can be fetched only once. Once the value is fetched, its
     * value is cleared from the session. For all further invocations to this method for the same transactionToken instance,
     * <code>null</code> will be returned.
     * @see org.terasoluna.gfw.web.token.transaction.TransactionTokenStore#getAndClear(org.terasoluna.gfw.web.token.transaction.TransactionToken)
     * @throws IllegalArgumentException generator is null
     */
    @Override
    public String getAndClear(TransactionToken token) {
        String key = createSessionAttributeName(token);
        HttpSession session = getSession();
        Object mutex = getMutex(session);
        TokenHolder tokenHolder;
        synchronized (mutex) {
            tokenHolder = (TokenHolder) session.getAttribute(key);
            if (tokenHolder != null) {
                session.setAttribute(key, new TokenHolder(null, System
                        .currentTimeMillis()));
            }
        }
        if (tokenHolder != null) {
            return tokenHolder.getToken();
        }

        return null;
    }

    /**
     * Removes the session attribute corresponding to the transactionToken instance passed as argument to this method
     * @see org.terasoluna.gfw.web.token.transaction.TransactionTokenStore#remove(org.terasoluna.gfw.web.token.transaction.TransactionToken)
     */
    @Override
    public void remove(TransactionToken token) {
        String key = createSessionAttributeName(token);
        HttpSession session = getSession();
        Object mutex = getMutex(session);
        synchronized (mutex) {
            try {
                session.removeAttribute(key);
            } catch (IllegalStateException e) {
                logger.debug("session is already invalidated.", e);
            }
        }
    }

    /**
     * Creates a new Token key and reserve it in the HttpSession<br>
     * removes oldeset token if token size is greater than or equals {@link #transactionTokensPerTokenName} in the same
     * namespace.
     * @see org.terasoluna.gfw.web.token.transaction.TransactionTokenStore#createAndReserveTokenKey(java.lang.String)
     */
    @Override
    public String createAndReserveTokenKey(String tokenName) {
        String tokenNamePrefix = TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX
                + tokenName;
        Set<String> sessionAttributeNames = new HashSet<String>();
        HttpSession session = getSession();
        Object mutex = getMutex(session);
        String tokenKey = null;
        synchronized (mutex) {
            Enumeration<String> tokenNameEnumeration = session
                    .getAttributeNames();
            while (tokenNameEnumeration.hasMoreElements()) {
                String name = tokenNameEnumeration.nextElement();
                // fetch the sessionKeyPrefix (session key with only Token prefix and namespace name) and compare
                if (tokenNamePrefix.equals(name.split(
                        TransactionToken.TOKEN_STRING_SEPARATOR)[0])) {
                    sessionAttributeNames.add(name);
                }
            }

            for (int i = 0, max = sessionAttributeNames.size(); i < max; i++) {
                // do not use while loop to avoid infinite loop
                if (sessionAttributeNames
                        .size() >= transactionTokensPerTokenName) {
                    removeOldTokenName(sessionAttributeNames, session);
                } else {
                    break;
                }
            }

            for (int i = 0; i < retryCreateTokenName; i++) {
                String str = generator.generate(session.getId());
                String name = tokenNamePrefix
                        + TransactionToken.TOKEN_STRING_SEPARATOR + str;
                if (!sessionAttributeNames.contains(name)) {
                    tokenKey = str;
                    break;
                }
            }
        }
        if (tokenKey == null) {
            throw new IllegalStateException("token key generation failed within retry count "
                    + retryCreateTokenName);
        }

        return tokenKey;
    }

    /**
     * removes old token name from session
     * @param sessionAttributeNames set of token names
     * @param session HttpSession
     */
    private void removeOldTokenName(Set<String> sessionAttributeNames,
            HttpSession session) {
        String oldestTokenName = null;
        TokenHolder oldestTokenHolder = new TokenHolder(null, Long.MAX_VALUE);
        for (String name : sessionAttributeNames) {
            TokenHolder tokenHolder = (TokenHolder) session.getAttribute(name);
            if (tokenHolder.getTimestamp() < oldestTokenHolder.getTimestamp()) {
                oldestTokenName = name;
                oldestTokenHolder = tokenHolder;
            }
        }
        session.removeAttribute(oldestTokenName);
        sessionAttributeNames.remove(oldestTokenName);
    }

    /**
     * Stores the token represented by <code>TransactionToken</code> into HTTP session <br>
     * <p>
     * The session attribute name to store it is prefixed with <code>HttpSessionTransactionTokenStore_TOKEN_</code>.
     * @see org.terasoluna.gfw.web.token.transaction.TransactionTokenStore#store(org.terasoluna.gfw.web.token.transaction.TransactionToken)
     */
    @Override
    public void store(TransactionToken token) {
        String sessionAttributeKey = createSessionAttributeName(token);
        HttpSession session = getSession();
        Object mutex = getMutex(session);
        synchronized (mutex) {
            session.setAttribute(sessionAttributeKey, new TokenHolder(token
                    .getTokenValue(), System.currentTimeMillis()));
        }
    }

    /**
     * Returns mutex from the given session<br>
     * @param session
     * @return mutex object
     */
    Object getMutex(HttpSession session) {
        return WebUtils.getSessionMutex(session);
    }

    /**
     * Returns {@link HttpSession} from request context<br>
     * @return http session object
     */
    HttpSession getSession() {
        return getRequest().getSession(true);
    }

    /**
     * Returns {@link HttpServletRequest} from request context<br>
     * @return http request in this context
     */
    HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
    }

    /**
     * Creates attribute name to store in http session from token
     * @param token
     * @return attribute name
     */
    String createSessionAttributeName(TransactionToken token) {
        if (token == null) {
            throw new IllegalArgumentException("token must not be null");
        }
        return TOKEN_HOLDER_SESSION_ATTRIBUTE_PREFIX + token.getTokenName()
                + TransactionToken.TOKEN_STRING_SEPARATOR + token.getTokenKey();
    }

    /**
     * Object to hold token
     */
    private static final class TokenHolder implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * string representation for token object
         */
        private final String token;

        /**
         * timestamp when token were stored
         */
        private final long timestamp;

        /**
         * Constructor<br>
         * @param token string representation for token object
         * @param timestamp timestamp when token were stored
         */
        public TokenHolder(final String token, final long timestamp) {
            this.token = token;
            this.timestamp = timestamp;
        }

        /**
         * Returns string representation for token object<br>
         * @return string representation for token object
         */
        public String getToken() {
            return token;
        }

        /**
         * Returns timestamp when token were stored
         * @return timestamp
         */
        public long getTimestamp() {
            return timestamp;
        }

    }
}
