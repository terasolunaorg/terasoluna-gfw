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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.terasoluna.gfw.web.token.TokenStringGenerator;

/**
 * {@code HandlerInterceptor} implementation class that introduces TransactionTokenCheck functionality for each incoming HTTP
 * Request.
 */
public class TransactionTokenInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(
            TransactionTokenInterceptor.class);

    /**
     * attribute name of {@link TransactionTokenContext} in the request scope
     */
    public static final String TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME = TransactionTokenInterceptor.class
            .getName() + ".TOKEN_CONTEXT";

    /**
     * attribute name of next {@link TransactionToken} in the request scope
     */
    public static final String NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME = TransactionTokenInterceptor.class
            .getName() + ".NEXT_TOKEN";

    /**
     * request parameter of token value to check
     */
    public static final String TOKEN_REQUEST_PARAMETER = "_TRANSACTION_TOKEN";

    /**
     * invalid token constant
     */
    private static final TransactionToken INVALID_TOKEN = new TransactionToken(null, null, null) {
        /**
         * serial version UID.
         */
        private static final long serialVersionUID = 674844591801033738L;

        @Override
        public boolean valid() {
            return false;
        }
    };

    /**
     * Store for TransactionTokenInfo objects
     */
    private final TransactionTokenInfoStore tokenInfoStore;

    /**
     * Store for TransactionToken objects
     */
    private final TransactionTokenStore tokenStore;

    /**
     * Token string generator
     */
    private final TokenStringGenerator generator;

    /**
     * Constructor that takes sizePerTokenName.
     * <p>
     * sizePerTokenName is the number of tokenKeys that are allowed to be generated per tokenName.<br>
     * {@link TokenStringGenerator} is used to generate token string.<br>
     * {@link TransactionTokenInfoStore} is used to store {@link TransactionTokenInfo}.<br>
     * {@link HttpSessionTransactionTokenStore} is used to store transaction tokens. Default size per tokenName(namespace) is
     * used.
     * </p>
     * @see HttpSessionTransactionTokenStore#HttpSessionTransactionTokenStore()
     */
    public TransactionTokenInterceptor() {
        this(new TokenStringGenerator(), new TransactionTokenInfoStore(),
                new HttpSessionTransactionTokenStore());
    }

    /**
     * Constructor that takes sizePerTokenName.
     * <p>
     * sizePerTokenName is the number of tokenKeys that are allowed to be generated per tokenName.<br>
     * {@link TokenStringGenerator} is used to generate token string.<br>
     * {@link TransactionTokenInfoStore} is used to store {@link TransactionTokenInfo}.<br>
     * {@link HttpSessionTransactionTokenStore} is used to store transaction tokens. The size per tokenName(namespace) is given.
     * </p>
     * @param sizePerTokenName size per tokenName(must be greater than 0)
     * @see HttpSessionTransactionTokenStore#HttpSessionTransactionTokenStore(int)
     */
    public TransactionTokenInterceptor(int sizePerTokenName) {
        this(new TokenStringGenerator(), new TransactionTokenInfoStore(),
                new HttpSessionTransactionTokenStore(sizePerTokenName));
    }

    /**
     * Constructor that takes tokenStringGenerator, transactionTokenInfoStore and transactionTokenStore as parameters
     * @param generator token string generator
     * @param tokenInfoStore store for {@link TransactionTokenInfo}
     * @param tokenStore store for {@link TransactionToken}
     */
    public TransactionTokenInterceptor(TokenStringGenerator generator,
            TransactionTokenInfoStore tokenInfoStore,
            TransactionTokenStore tokenStore) {
        this.generator = generator;
        this.tokenInfoStore = tokenInfoStore;
        this.tokenStore = tokenStore;
    }

    /**
     * Validates the token received from request. <br>
     * <p>
     * If token check passes, sets context information of the token in the "TransactionTokenInterceptor.TOKEN_CONTEXT" request
     * attribute and returns true.
     * <p>
     * This method expects the handler argument to be an instance of <code>HandlerMethod</code> class. If the handler is not an
     * instance of <code>HandlerMethod</code> class, the method returns true without executing the validation. <br>
     * @throws InvalidTransactionTokenException in case of Transaction Token Validation error.<br>
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(final HttpServletRequest request,
            final HttpServletResponse response, final Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        logger.trace("preHandle");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        TransactionTokenInfo tokenInfo = tokenInfoStore.getTransactionTokenInfo(
                handlerMethod);

        TransactionToken receivedToken = INVALID_TOKEN;
        if (tokenInfo.needValidate()) {
            receivedToken = createReceivedToken(request);
            if (!validateToken(receivedToken, tokenStore, tokenInfo)) {
                processTransactionTokenError(receivedToken);
            }
        }
        if (tokenInfo.getTransactionTokenType() == TransactionTokenType.BEGIN) {
            // This logic is added later to remove existing token sent in the request in case of Transaction BEGIN
            // When transactions BEGIN, the transaction token sent from the request are usually those which are generated
            // due to input errors. (Since method with BEGIN may be called in spite of input errors, and token will be generated
            // in the intercepter postHandle method execution.

            // This logic is added here to minimize the impact to any other existing logic

            String tokenStr = request.getParameter(TOKEN_REQUEST_PARAMETER);
            if (null != tokenStr) {
                removeToken(new TransactionToken(tokenStr));
            }
        }

        TransactionTokenContextImpl tokenContext = new TransactionTokenContextImpl(tokenInfo, receivedToken);

        request.setAttribute(TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME,
                tokenContext);

        return true;
    }

    protected void processTransactionTokenError(
            TransactionToken receivedToken) {
        removeToken(receivedToken);
        throw new InvalidTransactionTokenException();
    }

    /**
     * Retrieves the value of transactionToken fetched from a request parameter
     * @param request
     * @return currentToken transactionToken received from the request
     */
    TransactionToken createReceivedToken(final HttpServletRequest request) {
        String tokenStr = request.getParameter(TOKEN_REQUEST_PARAMETER);
        TransactionToken currentToken = new TransactionToken(tokenStr);
        return currentToken;
    }

    /**
     * Validates the instance of receivedToken. <br>
     * <p>
     * Returns false if the instance of receivedToken is not valid or token namespace of the target method and passed token does
     * not match. <br>
     * Returns true if <br>
     * a) it is saved in tokenStore AND <br>
     * b) Value of receivedToken is same as the value of token stored in tokenStore <br>
     * Returns false otherwise <br>
     * <p>
     * Take note that once a token is fetched from tokenStore, its value is cleared in tokenStore. Hence this method can return
     * true only once. All further invocations to this method for the same receivedToken will return false.
     * @param receivedToken
     * @param tokenStore
     * @param tokenInfo
     * @return if valid token, return true
     */
    boolean validateToken(final TransactionToken receivedToken,
            final TransactionTokenStore tokenStore,
            final TransactionTokenInfo tokenInfo) {

        if (receivedToken.valid() && receivedToken.getTokenName().equals(
                tokenInfo.getTokenName())) {

            String storedToken = tokenStore.getAndClear(receivedToken);
            if (storedToken != null && storedToken.equals(receivedToken
                    .getTokenValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Based on context information from the request attribute named <code>TransactionTokenInterceptor.TOKEN_CONTEXT</code>,
     * creates, updates or keeps the token stored with the request attribute <code>TransactionTokenInterceptor.NEXT_TOKEN</code>
     * and also in the <code>TransactionTokenStore</code> or removes the token from <code>TransactionTokenStore</code>
     * <p>
     * modelAndView is not used in the implementation
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance examination
     * @param modelAndView the <code>ModelAndView</code> that the handler returned (can also be <code>null</code>)
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(final HttpServletRequest request,
            final HttpServletResponse response, final Object handler,
            final ModelAndView modelAndView) {

        logger.trace("postHandle");

        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        TransactionTokenContextImpl tokenContext = (TransactionTokenContextImpl) request
                .getAttribute(TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME);

        switch (tokenContext.getReserveCommand()) {
        case CREATE_TOKEN:
            createToken(request, request.getSession(true), tokenContext
                    .getTokenInfo(), generator, tokenStore);
            break;
        case UPDATE_TOKEN:
            updateToken(request, request.getSession(true), tokenContext
                    .getReceivedToken(), tokenContext.getTokenInfo(), generator,
                    tokenStore);
            break;
        case REMOVE_TOKEN:
            removeToken(tokenContext.getReceivedToken());
            break;
        case KEEP_TOKEN:
            keepToken(request, tokenContext.getReceivedToken(), tokenContext
                    .getTokenInfo(), tokenStore);
            break;
        default:
            // noop
            break;
        }

    }

    /**
     * If exception occurred during request processing, the token is removed from request as well as
     * <code>TransactionTokenStore</code>
     * <p>
     * Token Context is fetched from the request attribute named <code>TransactionTokenInterceptor.TOKEN_CONTEXT</code>
     * Arguments <code>response</code> and <code>handler</code> are not used in this implementation
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(final HttpServletRequest request,
            final HttpServletResponse response, final Object handler,
            final Exception ex) {
        logger.trace("afterCompletion");

        if (ex != null) {
            TransactionTokenContextImpl tokenContext = (TransactionTokenContextImpl) request
                    .getAttribute(TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME);
            if (tokenContext != null) {
                TransactionToken token = tokenContext.getReceivedToken();
                removeToken(token);
            }
        }

    }

    /**
     * Updates the value of existing token in <code>TransactionTokenStore</code> as well as in the request
     * <p>
     * Updated <code>TransactionToken</code> instance is set to request attribute
     * <code>TransactionTokenInterceptor.NEXT_TOKEN</code>.
     * <p>
     * @param request
     * @param session
     * @param receivedToken
     * @param tokenInfo
     * @param generator
     * @param tokenStore
     */
    void updateToken(HttpServletRequest request, HttpSession session,
            TransactionToken receivedToken, TransactionTokenInfo tokenInfo,
            TokenStringGenerator generator, TransactionTokenStore tokenStore) {
        TransactionToken nextToken = new TransactionToken(tokenInfo
                .getTokenName(), receivedToken.getTokenKey(), generator
                        .generate(session.getId()));
        tokenStore.store(nextToken);
        request.setAttribute(NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME, nextToken);
    }

    /**
     * Creates a new <code>TransactionToken</code> <br>
     * <p>
     * Generated <code>TransactionToken</code> instance is stored in <code>TransactionTokenStore</code> and also set to request
     * attribute <code>TransactionTokenInterceptor.NEXT_TOKEN</code>.
     * <p>
     * @param request
     * @param session
     * @param tokenInfo TransactionTokenInfo
     * @param generator TokenStringGenerator
     * @param tokenStore TransactionTokenStore
     */
    void createToken(HttpServletRequest request, HttpSession session,
            TransactionTokenInfo tokenInfo, TokenStringGenerator generator,
            TransactionTokenStore tokenStore) {
        TransactionToken nextToken;
        synchronized (WebUtils.getSessionMutex(session)) {
            String tokenKey = tokenStore.createAndReserveTokenKey(tokenInfo
                    .getTokenName());
            nextToken = new TransactionToken(tokenInfo
                    .getTokenName(), tokenKey, generator.generate(session
                            .getId()));
            tokenStore.store(nextToken);
        }
        request.setAttribute(NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME, nextToken);
    }

    /**
     * Removes the receivedToken received as parameter to this method, from the tokenStore
     * @param receivedToken
     */
    void removeToken(TransactionToken receivedToken) {
        if (receivedToken.valid()) {
            tokenStore.remove(receivedToken);
        }
    }

    /**
     * Set the receivedToken to the attributes named <code>TransactionTokenInterceptor.NEXT_TOKEN</code> of request. And
     * receivedToken stored in <code>TransactionTokenStore</code> without updates.
     * @param request current HTTP request
     * @param receivedToken {@link TransactionToken} got from the current HTTP request
     * @param tokenInfo meta-information about a TransactionToken
     * @param tokenStore store for {@link TransactionToken}
     */
    void keepToken(HttpServletRequest request, TransactionToken receivedToken,
            TransactionTokenInfo tokenInfo, TransactionTokenStore tokenStore) {
        tokenStore.store(receivedToken);
        request.setAttribute(NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME, receivedToken);
    }
}
