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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestUtilsTest {

    MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
    }

    @Test
    public void testIsAjaxRequest() {
        request.addHeader("X-Requested-With", "XMLHttpRequest");

        assertTrue(RequestUtils.isAjaxRequest(request));
    }

    @Test
    public void testNotAjaxRequest() {

        assertFalse(RequestUtils.isAjaxRequest(request));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<RequestUtils> c = RequestUtils.class
                .getDeclaredConstructor();
        c.setAccessible(true);
        assertNotNull(c.newInstance());
    }

}
