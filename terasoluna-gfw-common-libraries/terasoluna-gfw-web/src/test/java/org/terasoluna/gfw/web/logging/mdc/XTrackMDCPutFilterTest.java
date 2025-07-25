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
package org.terasoluna.gfw.web.logging.mdc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import jakarta.servlet.ServletException;

public class XTrackMDCPutFilterTest {
    private XTrackMDCPutFilter xTrackMDCPutFilter;

    private MockFilterConfig mockFilterConfig;

    @BeforeEach
    public void before() throws Exception {
        xTrackMDCPutFilter = new XTrackMDCPutFilter();
        mockFilterConfig = new MockFilterConfig();
    }

    @Test
    public void testGetMDCKey_default() throws ServletException {
        xTrackMDCPutFilter.init(mockFilterConfig);
        assertThat(xTrackMDCPutFilter.getMDCKey(null, null), is("X-Track"));
    }

    @Test
    public void testGetMDCKey_changed_by_initParam() throws ServletException {
        mockFilterConfig.addInitParameter("attributeName", "X-Hoge");
        xTrackMDCPutFilter.init(mockFilterConfig);
        assertThat(xTrackMDCPutFilter.getMDCKey(null, null), is("X-Hoge"));
    }

    @Test
    public void testGetMDCValue_default_attributeName() throws ServletException {
        xTrackMDCPutFilter.init(mockFilterConfig);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String xTrack = xTrackMDCPutFilter.getMDCValue(request, response);
        assertThat(xTrack, matchesPattern("^[a-f0-9]{32}$"));
        assertThat(response.getHeader("X-Track"), is(xTrack));
        assertThat(request.getAttribute("X-Track"), is(xTrack));
    }

    @Test
    public void testGetMDCValue_changed_by_initParam() throws ServletException {
        mockFilterConfig.addInitParameter("attributeName", "X-Hoge");
        xTrackMDCPutFilter.init(mockFilterConfig);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String xTrack = xTrackMDCPutFilter.getMDCValue(request, response);
        assertThat(xTrack, matchesPattern("^[a-f0-9]{32}$"));
        assertThat(response.getHeader("X-Hoge"), is(xTrack));
        assertThat(request.getAttribute("X-Hoge"), is(xTrack));
    }

    @Test
    public void testGetMDCValue_default_attributeName_set_in_http_request()
            throws ServletException {
        xTrackMDCPutFilter.init(mockFilterConfig);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("X-Track", "hoge");
        String xTrack = xTrackMDCPutFilter.getMDCValue(request, response);
        assertThat(xTrack, is("hoge"));
        assertThat(response.getHeader("X-Track"), is("hoge"));
        assertThat(request.getAttribute("X-Track"), is("hoge"));
    }

    @Test
    public void testGetMDCValue_changed_by_initParam_set_in_http_request() throws ServletException {
        mockFilterConfig.addInitParameter("attributeName", "X-Hoge");
        xTrackMDCPutFilter.init(mockFilterConfig);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("X-Hoge", "12345678901234567890123456789012");
        String xTrack = xTrackMDCPutFilter.getMDCValue(request, response);
        assertThat(xTrack, is("12345678901234567890123456789012"));
        assertThat(response.getHeader("X-Hoge"), is("12345678901234567890123456789012"));
        assertThat(request.getAttribute("X-Hoge"), is("12345678901234567890123456789012"));
    }

    @Test
    public void testGetMDCValue_default_attributeName_set_in_http_request_too_long_length()
            throws ServletException {
        xTrackMDCPutFilter.init(mockFilterConfig);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("X-Track", "12345678901234567890123456789012345678901234567890");
        String xTrack = xTrackMDCPutFilter.getMDCValue(request, response);
        assertThat(xTrack, is("12345678901234567890123456789012"));
        assertThat(response.getHeader("X-Track"), is("12345678901234567890123456789012"));
        assertThat(request.getAttribute("X-Track"), is("12345678901234567890123456789012"));
    }
}
