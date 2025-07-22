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
package org.terasoluna.gfw.web.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestUtilsTest {

    MockHttpServletRequest request;

    @BeforeEach
    public void before() throws Exception {
        request = new MockHttpServletRequest();
    }

    @Test
    public void testIsAjaxRequest() {
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        assertThat(RequestUtils.isAjaxRequest(request), is(true));
    }

    @Test
    public void testNotAjaxRequest() {

        assertThat(RequestUtils.isAjaxRequest(request), is(false));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<RequestUtils> c = RequestUtils.class.getDeclaredConstructor();
        c.setAccessible(true);
        assertThat(c.newInstance(), is(notNullValue()));
    }

}
