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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.method.HandlerMethod;

public class TransactionTokenInfoStoreTest {

    TransactionTokenInfoStore store;

    @Before
    public void setup() {
        store = new TransactionTokenInfoStore();
    }

    @Test
    public void testCreateTransactionTokenInfo01() throws Exception {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                .getDeclaredMethod("fourth", SampleForm.class, Model.class));

        TransactionTokenInfo tokenInfo = store.createTransactionTokenInfo(
                handlerMethod);
        assertNotNull(tokenInfo);

    }

    @Test
    public void testCreateTransactionTokenInfo02() throws Exception {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                .getDeclaredMethod("first", SampleForm.class, Model.class));

        TransactionTokenInfo tokenInfo = store.createTransactionTokenInfo(
                handlerMethod);
        assertNotNull(tokenInfo);

    }

    @Test
    public void testCreateTokenInfoName01() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(
                TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = mock(
                TransactionTokenCheck.class);

        when(methodAnnotation.value()).thenReturn("test");
        when(classAnnotation.value()).thenReturn("test");

        assertNotNull(store.createTokenName(classAnnotation, methodAnnotation));

    }

    @Test
    public void testCreateTokenInfoName02() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(
                TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = null;

        when(methodAnnotation.value()).thenReturn("test");

        assertNotNull(store.createTokenName(classAnnotation, methodAnnotation));

    }

    @Test
    public void testCreateTokenInfoName03() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(
                TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = null;

        when(methodAnnotation.value()).thenReturn("");

        assertNotNull(store.createTokenName(classAnnotation, methodAnnotation));

    }

    @Test
    public void testCreateTokenInfoName04() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(
                TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = null;

        when(methodAnnotation.value()).thenReturn(null);

        assertNotNull(store.createTokenName(classAnnotation, methodAnnotation));

    }

    @Test
    public void testCreateTokenInfoName05() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(
                TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = mock(
                TransactionTokenCheck.class);

        when(methodAnnotation.value()).thenReturn("test");
        when(classAnnotation.value()).thenReturn("");

        assertNotNull(store.createTokenName(classAnnotation, methodAnnotation));
    }

    @Test
    public void testCreateTokenInfoName06() throws Exception {

        TransactionTokenCheck methodAnnotation = mock(
                TransactionTokenCheck.class);
        TransactionTokenCheck classAnnotation = mock(
                TransactionTokenCheck.class);

        when(methodAnnotation.value()).thenReturn("test");
        when(classAnnotation.value()).thenReturn(null);

        assertNotNull(store.createTokenName(classAnnotation, methodAnnotation));
    }

    @Test
    public void testGetTransactionTokenInfo() throws NoSuchMethodException, SecurityException {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleController(), TransactionTokenSampleController.class
                .getDeclaredMethod("first", SampleForm.class, Model.class));

        assertNotNull(store.getTransactionTokenInfo(handlerMethod));

    }

    @Test
    public void testNamespaceCreateTransactionTokenInfo() throws Exception {

        HandlerMethod handlerMethod = new HandlerMethod(new TransactionTokenSampleNamespaceController(), TransactionTokenSampleNamespaceController.class
                .getDeclaredMethod("first"));

        TransactionTokenInfo tokenInfo = store.createTransactionTokenInfo(
                handlerMethod);
        assertThat(tokenInfo.getTokenName(), containsString(
                "testTokenAttrByNameSpace"));

    }
}
