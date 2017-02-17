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

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * The class to store(cache) the TransactionTokenInfo instances
 */
public class TransactionTokenInfoStore {

    /**
     * Cache to stores the TransactionTokenInfo object
     */
    private ConcurrentMap<Method, TransactionTokenInfo> tokenInfoCache = new ConcurrentHashMap<Method, TransactionTokenInfo>();

    /**
     * Global token name. Assigned when no name is defined in method annotation or class annotation
     */
    private String globalTokenName = "globalToken";

    /**
     * No parameter constructor
     */
    public TransactionTokenInfoStore() {
        // do nothing.
    }

    /**
     * Returns <code>TransactionTokenInfo</code> instance fetched from <code>TransactionTokenCache</code> based to the
     * handlerMethod.<br>
     * <p>
     * If there is no <code>TransactionTokenInfo</code> corresponding to the handlerMethod passed as argument, a new
     * TransactionTokenInfo is returned as well as put in the cache. <br>
     * @param handlerMethod handler method
     * @return TransactionTokenInfo
     */
    public TransactionTokenInfo getTransactionTokenInfo(
            final HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        TransactionTokenInfo info = tokenInfoCache.get(method);
        if (info == null) {
            synchronized (tokenInfoCache) {
                info = tokenInfoCache.get(method);
                if (info == null) {
                    info = createTransactionTokenInfo(handlerMethod);
                    tokenInfoCache.put(method, info);
                }
            }
        }
        return info;
    }

    /**
     * Returns a new <code>TransactionTokenInfo<code> based on the annotation information received from handlerMethod <br>
     * <p>
     * Method Annotation given priority over Class Annotation
     * @param handlerMethod
     * @return TransactionTokenInfo
     */
    TransactionTokenInfo createTransactionTokenInfo(
            final HandlerMethod handlerMethod) {

        TransactionTokenCheck methodAnnotation = handlerMethod
                .getMethodAnnotation(TransactionTokenCheck.class);

        TransactionTokenCheck classAnnotation = AnnotationUtils.findAnnotation(
                handlerMethod.getBeanType(), TransactionTokenCheck.class);

        if (methodAnnotation == null) {
            return new TransactionTokenInfo(null, TransactionTokenType.NONE);
        }

        String tokenName = createTokenName(classAnnotation, methodAnnotation);

        return new TransactionTokenInfo(tokenName, methodAnnotation.type());
    }

    /**
     * create token name (= namespace) from class level {@link TransactionTokenCheck} annotation and method level
     * {@link TransactionTokenCheck} annotation.<br>
     * If class level and method level annotation are not set, returns 'globalToken' as global token name.
     * @param classAnnotation class level annotation
     * @param methodAnnotation method level annotation
     * @return token name
     */
    String createTokenName(final TransactionTokenCheck classAnnotation,
            final TransactionTokenCheck methodAnnotation) {
        String methodTokenName = methodAnnotation.value();
        String classTokenName = (classAnnotation == null) ? ""
                : classAnnotation.value();

        StringBuilder tokenNameStringBuilder = new StringBuilder();
        if (classTokenName != null && !classTokenName.isEmpty()) {
            tokenNameStringBuilder.append(classTokenName);
        }
        if (methodTokenName != null && !methodTokenName.isEmpty()) {
            if (tokenNameStringBuilder.length() != 0) {
                tokenNameStringBuilder.append("/");
            }
            tokenNameStringBuilder.append(methodTokenName);
        }
        if (tokenNameStringBuilder.length() == 0) {
            tokenNameStringBuilder.append(globalTokenName);
        }

        return tokenNameStringBuilder.toString();
    }

}
