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

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestDataValueProcessorAdaptorTest {

    RequestDataValueProcessorAdaptor requestDataValueProcessorAdaptor = new RequestDataValueProcessorAdaptor();

    HttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void testProcessAction01() {
        String action = requestDataValueProcessorAdaptor.processAction(request,
                "action");
        assertThat(action, is("action"));
    }

    @Test
    public void testProcessAction02() {
        // set up
        String action = requestDataValueProcessorAdaptor.processAction(request,
                "action", "method");

        // assert
        assertThat(action, is("action"));
    }

    @Test
    public void testProcessFormFieldValue() {
        String value = requestDataValueProcessorAdaptor.processFormFieldValue(
                request, "", "value", "");
        assertThat(value, is("value"));
    }

    @Test
    public void testGetExtraHiddenFields() {
        Map<String, String> map = requestDataValueProcessorAdaptor
                .getExtraHiddenFields(request);
        assertNull(map);
    }

    @Test
    public void testProcessUrl() {
        String url = requestDataValueProcessorAdaptor.processUrl(request,
                "http://localhost:8080/test");
        assertThat(url, is("http://localhost:8080/test"));
    }

}
