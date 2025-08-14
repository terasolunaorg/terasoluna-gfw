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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Test class for TransactionTokenContextHandlerMethodArgumentResolver
 */
public class TransactionTokenContextHandlerMethodArgumentResolverTest {

    /**
     * case of supportsParameter returns true
     */
    @Test
    public void testSupportsParameter01() {
        TransactionTokenContextHandlerMethodArgumentResolver resolver =
                new TransactionTokenContextHandlerMethodArgumentResolver();
        MethodParameter parameter = mock(MethodParameter.class);

        Mockito.<Class<?>>when(parameter.getParameterType())
                .thenReturn(TransactionTokenContext.class);

        boolean result = resolver.supportsParameter(parameter);

        assertThat(result).isEqualTo(true);
    }

    /**
     * case of supportsParameter returns false
     */
    @Test
    public void testSupportsParameter02() {
        TransactionTokenContextHandlerMethodArgumentResolver resolver =
                new TransactionTokenContextHandlerMethodArgumentResolver();
        MethodParameter parameter = mock(MethodParameter.class);

        Mockito.<Class<?>>when(parameter.getParameterType()).thenReturn(Object.class);

        boolean result = resolver.supportsParameter(parameter);

        assertThat(result).isEqualTo(false);
    }

    /**
     * case of resolveArgument
     */
    @Test
    public void testResolveArgument01() {
        TransactionTokenContextHandlerMethodArgumentResolver resolver =
                new TransactionTokenContextHandlerMethodArgumentResolver();
        MethodParameter parameter = mock(MethodParameter.class);
        ModelAndViewContainer mavContainer = mock(ModelAndViewContainer.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        WebDataBinderFactory binderFactory = mock(WebDataBinderFactory.class);

        String str = new String("testResolveArgument01");
        when(webRequest.getAttribute(
                TransactionTokenInterceptor.TOKEN_CONTEXT_REQUEST_ATTRIBUTE_NAME,
                RequestAttributes.SCOPE_REQUEST)).thenReturn(str);

        Object result = null;
        result = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        assertThat(result).isInstanceOf(String.class);
    }
}
