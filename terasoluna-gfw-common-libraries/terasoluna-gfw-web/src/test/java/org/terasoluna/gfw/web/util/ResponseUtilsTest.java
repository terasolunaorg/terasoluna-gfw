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

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.springframework.mock.web.MockHttpServletResponse;

public class ResponseUtilsTest {
    MockHttpServletResponse response;

    @Before
    public void bfore() throws Exception {
        response = new MockHttpServletResponse();
    }

    @Test
    public void testSetPreventionCachingHeaders() {
        ResponseUtils.setPreventionCachingHeaders(response);
        assertThat(response.getHeader("Cache-Control"), is(
                "private,no-store,no-cache,must-revalidate"));
        assertThat(response.getHeader("Pragma"), is("no-cache"));
        assertThat(response.getHeader("Expires"), is(
                "Thu, 01 Jan 1970 00:00:00 GMT")); // Changed by SPR-11912
    }

    @Test(expected = None.class)
    public void testSetPreventionCachingHeadersWithNullResponse() {

        ResponseUtils.setPreventionCachingHeaders(null);
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<ResponseUtils> c = ResponseUtils.class
                .getDeclaredConstructor();
        c.setAccessible(true);
        assertThat(c.newInstance(), is(notNullValue()));
    }
}
