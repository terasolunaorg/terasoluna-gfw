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

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@code HandlerMethodArgumentResolver} to handke {@code TransactionTokenContext}<br>
 */
public class TransactionTokenContextHandlerMethodArgumentResolver implements
                                                                  HandlerMethodArgumentResolver {

    /**
     * check whether the given parameter type is {@link TransactionTokenContext}.
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
     */
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return TransactionTokenContext.class.equals(parameter
                .getParameterType());
    }

    /**
     * resolve {@link TransactionTokenContext} object
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter,
     *      org.springframework.web.method.support.ModelAndViewContainer,
     *      org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        return webRequest.getAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME,
                RequestAttributes.SCOPE_REQUEST);
    }

}
