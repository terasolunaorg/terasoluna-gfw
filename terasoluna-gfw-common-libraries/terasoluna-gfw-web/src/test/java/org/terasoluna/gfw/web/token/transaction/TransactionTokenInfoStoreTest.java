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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.method.HandlerMethod;

public class TransactionTokenInfoStoreTest {

    TransactionTokenInfoStore store;

    @BeforeEach
    public void before() {
        store = new TransactionTokenInfoStore();
    }

    @Test
    public void testCreateTransactionTokenInfo01() throws Exception {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleController(),
                TransactionTokenSampleController.class.getDeclaredMethod("fourth", SampleForm.class,
                        Model.class));

        TransactionTokenInfo tokenInfo = store.createTransactionTokenInfo(handlerMethod);
        assertThat(tokenInfo).isNotNull();

    }

    @Test
    public void testCreateTransactionTokenInfo02() throws Exception {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleController(),
                TransactionTokenSampleController.class.getDeclaredMethod("first", SampleForm.class,
                        Model.class));

        TransactionTokenInfo tokenInfo = store.createTransactionTokenInfo(handlerMethod);
        assertThat(tokenInfo).isNotNull();

    }

    @Test
    public void testCreateTokenInfoName01() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = mock(TransactionTokenCheck.class);

        when(methodAnnotation.value()).thenReturn("test");
        when(classAnnotation.value()).thenReturn("test");

        assertThat(store.createTokenName(classAnnotation, methodAnnotation)).isNotNull();

    }

    @Test
    public void testCreateTokenInfoName02() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = null;

        when(methodAnnotation.value()).thenReturn("test");

        assertThat(store.createTokenName(classAnnotation, methodAnnotation)).isNotNull();

    }

    @Test
    public void testCreateTokenInfoName03() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = null;

        when(methodAnnotation.value()).thenReturn("");

        assertThat(store.createTokenName(classAnnotation, methodAnnotation)).isNotNull();

    }

    @Test
    public void testCreateTokenInfoName04() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = null;

        when(methodAnnotation.value()).thenReturn(null);

        assertThat(store.createTokenName(classAnnotation, methodAnnotation)).isNotNull();

    }

    @Test
    public void testCreateTokenInfoName05() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = mock(TransactionTokenCheck.class);

        when(methodAnnotation.value()).thenReturn("test");
        when(classAnnotation.value()).thenReturn("");

        assertThat(store.createTokenName(classAnnotation, methodAnnotation)).isNotNull();
    }

    @Test
    public void testCreateTokenInfoName06() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = mock(TransactionTokenCheck.class);

        when(methodAnnotation.value()).thenReturn("test");
        when(classAnnotation.value()).thenReturn(null);

        assertThat(store.createTokenName(classAnnotation, methodAnnotation)).isNotNull();
    }

    @Test
    public void testGetTransactionTokenInfo() throws NoSuchMethodException, SecurityException {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleController(),
                TransactionTokenSampleController.class.getDeclaredMethod("first", SampleForm.class,
                        Model.class));

        assertThat(store.getTransactionTokenInfo(handlerMethod)).isNotNull();

    }

    @Test
    public void testNamespaceCreateTransactionTokenInfo() throws Exception {

        HandlerMethod handlerMethod =
                new HandlerMethod(new TransactionTokenSampleNamespaceController(),
                        TransactionTokenSampleNamespaceController.class.getDeclaredMethod("first"));

        TransactionTokenInfo tokenInfo = store.createTransactionTokenInfo(handlerMethod);
        assertThat(tokenInfo.getTokenName()).contains("testTokenAttrByNameSpace");

    }
}
