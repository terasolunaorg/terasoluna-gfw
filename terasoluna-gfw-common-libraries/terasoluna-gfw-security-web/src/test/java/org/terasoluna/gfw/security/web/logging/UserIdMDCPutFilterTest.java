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
package org.terasoluna.gfw.security.web.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserIdMDCPutFilterTest {

    SecurityContext securityContext;

    Authentication authentication;

    User user;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @After
    public void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetMDCKey() {
        UserIdMDCPutFilter mdcPutFilter = new UserIdMDCPutFilter();

        // run
        String mdcKeyStr = mdcPutFilter.getMDCKey(request, response);

        // assert
        assertThat(mdcKeyStr, is("USER"));

    }

    @Test
    public void testGetMDCKey_SpecifyCustomKey() {
        UserIdMDCPutFilter mdcPutFilter = new UserIdMDCPutFilter();
        mdcPutFilter.setAttributeName("LOGIN_USER_ID");

        // run
        String mdcKeyStr = mdcPutFilter.getMDCKey(request, response);

        // assert
        assertThat(mdcKeyStr, is("LOGIN_USER_ID"));

    }

    @Test
    public void testGetMDCValue() {
        UserIdMDCPutFilter mdcPutFilter = new UserIdMDCPutFilter();

        // expected data
        String userName = "terasoluna@nttd.co.jp";

        // SecurityContextHolder setting start
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        user = new User(userName, "yyyy", Arrays.asList(
                new SimpleGrantedAuthority("user")));
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        // setting end

        // run
        String mdcValueStr = mdcPutFilter.getMDCValue(request, response);

        // assert
        assertThat(mdcValueStr, is(userName));

    }

    @Test
    public void testGetMDCValueNullAuthentication() {
        UserIdMDCPutFilter mdcPutFilter = new UserIdMDCPutFilter();
        String mdcValueStr = mdcPutFilter.getMDCValue(request, response);
        assertNull(mdcValueStr);
    }

    @Test
    public void testGetMDCValuePrincipalStringValue() {
        UserIdMDCPutFilter mdcPutFilter = new UserIdMDCPutFilter();

        // expected data
        String userName = "terasoluna@nttd.co.jp";

        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userName);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String mdcValueStr = mdcPutFilter.getMDCValue(request, response);
        // assert
        assertThat(mdcValueStr, is(userName));
    }

    // @Test
    // public void testCutValue() {
    // String mdcValueStr = "longString12345";
    // UserIdMDCPutFilter mdcPutFilter = new UserIdMDCPutFilter();
    // mdcPutFilter.setMaxMDCValueLength(10);
    // mdcValueStr = mdcPutFilter.cutValue(mdcValueStr);
    // // assert
    // assertThat(mdcValueStr, is("longString"));
    // }

}
