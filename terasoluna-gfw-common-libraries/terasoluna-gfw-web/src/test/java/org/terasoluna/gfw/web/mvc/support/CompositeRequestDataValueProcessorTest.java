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
package org.terasoluna.gfw.web.mvc.support;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public class CompositeRequestDataValueProcessorTest {

    private CompositeRequestDataValueProcessor compositeRequestDataValueProcessor;

    private HttpServletRequest request = new MockHttpServletRequest();

    RequestDataValueProcessorAdaptor requestDataValueProcessor =
            mock(RequestDataValueProcessorAdaptor.class);

    @BeforeEach
    public void before() {

        compositeRequestDataValueProcessor =
                new CompositeRequestDataValueProcessor(requestDataValueProcessor);

    }

    @Test
    public void testProcessActionSameActionAndResult() {
        // Set mock behavior
        when(requestDataValueProcessor.processAction(any(HttpServletRequest.class), anyString(),
                anyString())).thenReturn("action");
        String result =
                compositeRequestDataValueProcessor.processAction(request, "action", "method");

        // assert
        assertThat(result, is("action"));
    }

    @Test
    public void testProcessActionDifferectActionAndResult() {
        // Set mock behavior
        when(requestDataValueProcessor.processAction(any(HttpServletRequest.class), anyString(),
                anyString())).thenReturn("other_action");
        String result =
                compositeRequestDataValueProcessor.processAction(request, "action", "method");

        // assert
        assertThat(result, is("other_action"));
    }

    @Test
    public void testProcessActionNoProcessors() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        String result =
                compositeRequestDataValueProcessor.processAction(request, "action", "method");

        // assert
        assertThat(result, is("action"));
    }

    @Test
    public void testProcessFormFieldValueSameValueAndResult() {
        // Set Mock behavior
        when(requestDataValueProcessor.processFormFieldValue(any(HttpServletRequest.class),
                anyString(), anyString(), anyString())).thenReturn("value");

        String result =
                compositeRequestDataValueProcessor.processFormFieldValue(request, "", "value", "");
        assertThat(result, is("value"));
    }

    @Test
    public void testProcessFormFieldValueDifferentValueAndResult() {
        // Set Mock behavior
        when(requestDataValueProcessor.processFormFieldValue(any(HttpServletRequest.class),
                anyString(), anyString(), anyString())).thenReturn("other_value");

        String result =
                compositeRequestDataValueProcessor.processFormFieldValue(request, "", "value", "");
        assertThat(result, is("other_value"));
    }

    @Test
    public void testProcessFormFieldValueNoProcessors() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        String result =
                compositeRequestDataValueProcessor.processFormFieldValue(request, "", "value", "");
        assertThat(result, is("value"));
    }

    @Test
    public void testGetExtraHiddenFieldsNullMapFromProcessorResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.getExtraHiddenFields(any(HttpServletRequest.class)))
                .thenReturn(null);

        Map<String, String> map = compositeRequestDataValueProcessor.getExtraHiddenFields(request);
        assertThat(map, is(notNullValue()));
    }

    @Test
    public void testGetExtraHiddenFieldsNotNullMapFromProcessorResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.getExtraHiddenFields(any(HttpServletRequest.class)))
                .thenReturn(new HashMap<String, String>());

        Map<String, String> map = compositeRequestDataValueProcessor.getExtraHiddenFields(request);
        assertThat(map, is(notNullValue()));
    }

    @Test
    public void testGetExtraHiddenFieldsNoProcessors() {
        // No processors assigned
        compositeRequestDataValueProcessor = new CompositeRequestDataValueProcessor();
        Map<String, String> map = compositeRequestDataValueProcessor.getExtraHiddenFields(request);
        assertThat(map, is(notNullValue()));
    }

    @Test
    public void testProcessUrlSameUrlAndResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.processUrl(any(HttpServletRequest.class), anyString()))
                .thenReturn("http://localhost:8080/test");
        String result = compositeRequestDataValueProcessor.processUrl(request,
                "http://localhost:8080/test");
        assertThat(result, is("http://localhost:8080/test"));
    }

    @Test
    public void testProcessUrlDifferentUrlAndResult() {

        // Set Mock behavior
        when(requestDataValueProcessor.processUrl(any(HttpServletRequest.class), anyString()))
                .thenReturn("http://localhost:9999/test");

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
