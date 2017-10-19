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
package org.terasoluna.gfw.security.web.redirect;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

@SuppressWarnings("deprecation")
public class RedirectAuthenticationHandlerTest {

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    Authentication auth;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setContextPath("/foo");
        response = new MockHttpServletResponse();
        auth = mock(Authentication.class);
    }

    /**
     * Redirect attribute name was changed
     */
    @Test
    public void testOnAuthenticationSuccess_SetConstParam() throws Exception {
        String changeAttrName = "hoge";

        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.setTargetUrlParameter(changeAttrName);
        redireHandler.afterPropertiesSet();

        // redirectURI is parameter and expected data
        String redirectURI = "/foo/test/terasoluna?";
        request.setParameter(changeAttrName, redirectURI);

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(redirectURI));
    }

    @Test
    public void testOnAuthenticationSuccessTargetUrlParameterIsNull() throws Exception {
        // set up
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.afterPropertiesSet();

        // expected data
        String expectedRedirectURL = "/foo/";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    /**
     * redirected in the name of the default attributes
     */
    @Test
    public void testOnAuthenticationSuccess_SetRedirectTo() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.afterPropertiesSet();

        // redirectURI is parameter and expected data
        String redirectURI = "/foo/test/terasoluna?";
        request.setParameter("redirectTo", redirectURI);

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(redirectURI));
    }

    /**
     * parameter is not available, the redirect in the default path
     */
    @Test
    public void testOnAuthenticationSuccess_NotSetRedirectTo() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.afterPropertiesSet();
        // expected data
        String expectedRedirectURL = "/foo/";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    /**
     * parameter is blank, the redirect in the default path
     */
    @Test
    public void testOnAuthenticationSuccess_SetRedirectToBlank() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.afterPropertiesSet();

        // set Blank URI
        String redirectURI = "";
        request.setParameter("redirectTo", redirectURI);

        // expected data
        String expectedRedirectURL = "/foo/";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    /**
     * parameter is blank, the redirect in the default path
     */
    @Test
    public void testOnAuthenticationSuccess_SetAbsolutePath() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.afterPropertiesSet();

        // set Blank URI
        String redirectURI = "http://localhost/foo/bar";
        request.setParameter("redirectTo", redirectURI);

        // expected data
        String expectedRedirectURL = "bar";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    @Test
    public void testOnAuthenticationSuccess_SetNullRedirectToRedirectStrategy() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        // redireHandler.afterPropertiesSet(); do not initialize

        // set Blank URI
        String redirectURI = "http://localhost/foo/bar";
        request.setParameter("redirectTo", redirectURI);

        // expected data
        String expectedRedirectURL = "http://localhost/foo/bar";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    @Test
    public void testOnAuthenticationSuccess_SetNullRedirectToRedirectStrategyAndSetContextRelative() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.setContextRelative(true);
        redireHandler.setRedirectStrategy(redirectStrategy);

        // set Blank URI
        String redirectURI = "http://localhost/foo/bar";
        request.setParameter("redirectTo", redirectURI);

        // expected data
        String expectedRedirectURL = "bar";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    /**
     * test if default path is returned when illegal redirect path is set
     */
    @Test
    public void testOnAuthenticationSuccess_SetIllegalUrlWhenUseDefaultRedirectToRedirectStrategy() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        // redireHandler.afterPropertiesSet(); do not initialize

        // set Blank URI
        String redirectURI = "http://google.com";
        request.setParameter("redirectTo", redirectURI);

        // expected data
        String expectedRedirectURL = "http://google.com";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }

    @Test
    public void testOnAuthenticationSuccess_SetCustomeRedirectToRedirectStrategy() throws Exception {
        RedirectAuthenticationHandler redireHandler = new RedirectAuthenticationHandler();
        redireHandler.setRedirectToRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest request,
                    HttpServletResponse response,
                    String url) throws IOException {
                response.sendRedirect("http://google.com");
            }
        });
        redireHandler.afterPropertiesSet();

        // set Blank URI
        String redirectURI = "/foo/bar";
        request.setParameter("redirectTo", redirectURI);

        // expected data
        String expectedRedirectURL = "http://google.com";

        // run
        redireHandler.onAuthenticationSuccess(request, response, auth);

        // assert
        assertThat(response.getRedirectedUrl(), is(expectedRedirectURL));
    }
}
