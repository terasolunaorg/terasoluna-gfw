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
package org.terasoluna.gfw.web.mvc.support;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class CompositeRequestDataValueProcessorTest {

    private CompositeRequestDataValueProcessor compositeRequestDataValueProcessor;

    private HttpServletRequest request = new MockHttpServletRequest();

    RequestDataValueProcessorAdaptor requestDataValueProcessor = mock(
            RequestDataValueProcessorAdaptor.class);

    @Before
    public void setup() {

        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor(requestDataValueProcessor);

    }

    @Test
    public void testProcessActionSameActionAndResult01() {
        // Set mock behavior
        // for Spring3
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString())).thenReturn(
                        "action");
        // for Spring4
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString(), anyString()))
                        .thenReturn("action");
        String result = compositeRequestDataValueProcessor.processAction(
                request, "action");
        assertThat(result, is("action"));
    }

    @Test
    public void testProcessActionDifferectActionAndResult01() {
        // Set mock behavior
        // for Spring3
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString())).thenReturn(
                        "other_action");
        // for Spring4
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString(), anyString()))
                        .thenReturn("other_action");
        String result = compositeRequestDataValueProcessor.processAction(
                request, "action");
        assertThat(result, is("other_action"));
    }

    @Test
    public void testProcessActionNoProcessors01() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        String result = compositeRequestDataValueProcessor.processAction(
                request, "action");
        assertThat(result, is("action"));
    }

    @Test
    public void testProcessActionSameActionAndResult02() {
        // Set mock behavior
        // for Spring3
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString())).thenReturn(
                        "action");
        // for Spring4
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString(), anyString()))
                        .thenReturn("action");
        String result = compositeRequestDataValueProcessor.processAction(
                request, "action", "method");

        // assert
        assertThat(result, is("action"));
    }

    @Test
    public void testProcessActionDifferectActionAndResult02() {
        // Set mock behavior
        // for Spring3
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString())).thenReturn(
                        "other_action");
        // for Spring4
        when(requestDataValueProcessor.processAction(
                (HttpServletRequest) (anyObject()), anyString(), anyString()))
                        .thenReturn("other_action");
        String result = compositeRequestDataValueProcessor.processAction(
                request, "action", "method");

        // assert
        assertThat(result, is("other_action"));
    }

    @Test
    public void testProcessActionNoProcessors02() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        String result = compositeRequestDataValueProcessor.processAction(
                request, "action", "method");

        // assert
        assertThat(result, is("action"));
    }

    @Test
    public void testProcessFormFieldValueSameValueAndResult() {
        // Set Mock behavior
        when(requestDataValueProcessor.processFormFieldValue(
                (HttpServletRequest) (anyObject()), anyString(), anyString(),
                anyString())).thenReturn("value");

        String result = compositeRequestDataValueProcessor
                .processFormFieldValue(request, "", "value", "");
        assertThat(result, is("value"));
    }

    @Test
    public void testProcessFormFieldValueDifferentValueAndResult() {
        // Set Mock behavior
        when(requestDataValueProcessor.processFormFieldValue(
                (HttpServletRequest) (anyObject()), anyString(), anyString(),
                anyString())).thenReturn("other_value");

        String result = compositeRequestDataValueProcessor
                .processFormFieldValue(request, "", "value", "");
        assertThat(result, is("other_value"));
    }

    @Test
    public void testProcessFormFieldValueNoProcessors() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        String result = compositeRequestDataValueProcessor
                .processFormFieldValue(request, "", "value", "");
        assertThat(result, is("value"));
    }

    @Test
    public void testGetExtraHiddenFieldsNullMapFromProcessorResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.getExtraHiddenFields(
                (HttpServletRequest) (anyObject()))).thenReturn(null);

        Map<String, String> map = compositeRequestDataValueProcessor
                .getExtraHiddenFields(request);
        assertNotNull(map);
    }

    @Test
    public void testGetExtraHiddenFieldsNotNullMapFromProcessorResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.getExtraHiddenFields(
                (HttpServletRequest) (anyObject()))).thenReturn(
                        new HashMap<String, String>());

        Map<String, String> map = compositeRequestDataValueProcessor
                .getExtraHiddenFields(request);
        assertNotNull(map);
    }

    @Test
    public void testGetExtraHiddenFieldsNoProcessors() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        Map<String, String> map = compositeRequestDataValueProcessor
                .getExtraHiddenFields(request);
        assertNotNull(map);
    }

    @Test
    public void testProcessUrlSameUrlAndResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.processUrl(
                (HttpServletRequest) (anyObject()), anyString())).thenReturn(
                        "http://localhost:8080/test");
        String result = compositeRequestDataValueProcessor.processUrl(request,
                "http://localhost:8080/test");
        assertThat(result, is("http://localhost:8080/test"));
    }

    @Test
    public void testProcessUrlDifferentUrlAndResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.processUrl(
                (HttpServletRequest) (anyObject()), anyString())).thenReturn(
                        "http://localhost:9999/test");

        String result = compositeRequestDataValueProcessor.processUrl(request,
                "http://localhost:8080/test");
        assertThat(result, is("http://localhost:9999/test"));
    }

    @Test
    public void testProcessUrlNoProcessors() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        String result = compositeRequestDataValueProcessor.processUrl(request,
                "http://localhost:8080/test");
        assertThat(result, is("http://localhost:8080/test"));
    }

}
