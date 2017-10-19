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
package org.terasoluna.gfw.web.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class ResponseUtilsTest {
    MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
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

    @Test
    public void testSetPreventionCachingHeadersWithNullResponse() {
        try {
            ResponseUtils.setPreventionCachingHeaders(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<ResponseUtils> c = ResponseUtils.class
                .getDeclaredConstructor();
        c.setAccessible(true);
        assertNotNull(c.newInstance());
    }
}
