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
package org.terasoluna.gfw.web.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.Enumeration;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.SystemException;
import org.terasoluna.gfw.common.message.ResultMessages;

public class SystemExceptionResolverTest {
    private SystemExceptionResolver testTarget;

    /**
     * mock instance of http servlet requset.
     */
    private MockHttpServletRequest mockRequest;

    /**
     * mock instance of http servlet response.
     */
    private MockHttpServletResponse mockResponse;

    /**
     * setup all test case.
     */
    @Before
    public void setup() {

        // create test target.
        this.testTarget = new SystemExceptionResolver();

        this.mockRequest = new MockHttpServletRequest();
        this.mockResponse = new MockHttpServletResponse();
    }

    @Test
    public void testSetExceptionCode_exceptionCodeResolver_is_null() {

        testTarget.setExceptionCodeResolver(null);

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(mockResponse.getHeaderNames().isEmpty(), is(true));

    }

    @Test
    public void testSetExceptionCode_exception_code_is_null() {

        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        SystemException occurException = new SystemException(null, "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        Enumeration<String> attributeNames = mockRequest.getAttributeNames();
        assertThat(attributeNames.nextElement().equals(
                DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE), is(true));
        assertThat(attributeNames.hasMoreElements(), is(false));
        assertThat(mockResponse.getHeaderNames().isEmpty(), is(true));
        assertThat(flashMap.isEmpty(), is(true));

    }

    @Test
    public void testSetExceptionCode_exception_code_is_notnull() {

        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttribute("exceptionCode").toString(), is(
                "code001"));
        assertThat(mockResponse.getHeader("X-Exception-Code"), is("code001"));

        assertThat((String) flashMap.get("exceptionCode"), is("code001"));

    }

    @Test
    public void testSetExceptionCode_exceptionCodeAttribute_and_exceptionCodeHeader_is_null() {

        testTarget.setExceptionCodeAttribute(null);
        testTarget.setExceptionCodeHeader(null);

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(mockResponse.getHeaderNames().isEmpty(), is(true));

    }

    @Test
    public void testSetExceptionCode_exceptionCodeAttribute_and_exceptionCodeHeader_is_blank() {

        testTarget.setExceptionCodeAttribute("");
        testTarget.setExceptionCodeHeader("");

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(mockResponse.getHeaderNames().isEmpty(), is(true));

    }

    @Test
    public void testSetExceptionCode_exceptionCodeAttribute_is_null() {

        testTarget.setExceptionCodeAttribute(null);

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(mockResponse.getHeader("X-Exception-Code"), is("code001"));

    }

    @Test
    public void testSetExceptionCode_exceptionCodeAttribute_is_blank() {

        testTarget.setExceptionCodeAttribute("");

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttributeNames().hasMoreElements(), is(
                false));
        assertThat(mockResponse.getHeader("X-Exception-Code"), is("code001"));

    }

    @Test
    public void testSetExceptionCode_exceptionCodeHeader_is_null() {

        testTarget.setExceptionCodeHeader(null);

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttribute("exceptionCode").toString(), is(
                "code001"));
        assertThat(mockResponse.getHeaderNames().isEmpty(), is(true));

    }

    @Test
    public void testSetExceptionCode_exceptionCodeHeader_is_blank() {

        testTarget.setExceptionCodeHeader("");

        SystemException occurException = new SystemException("code001", "message");

        testTarget.setExceptionCode(occurException, mockRequest, mockResponse);

        assertThat(mockRequest.getAttribute("exceptionCode").toString(), is(
                "code001"));
        assertThat(mockResponse.getHeaderNames().isEmpty(), is(true));

    }

    @Test
    public void testSetResultMessages_BusinessException_flashMap_is_null() {

        ResultMessages resultMessages = ResultMessages.error().add("code01");
        BusinessException occurException = new BusinessException(resultMessages);

        testTarget.setResultMessages(occurException, mockRequest);

        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(mockRequest);

        assertThat(flashMap, is(nullValue()));

    }

    @Test
    public void testSetResultMessages_BusinessException_flashMap_is_notnull() {

        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        ResultMessages resultMessages = ResultMessages.error().add("code01");
        BusinessException occurException = new BusinessException(resultMessages);

        testTarget.setResultMessages(occurException, mockRequest);

        assertSame(resultMessages, flashMap.get(
                ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME));

    }

    @Test
    public void testSetResultMessages_not_BusinessException() {

        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        SystemException occurException = new SystemException("code01", "message");

        testTarget.setResultMessages(occurException, mockRequest);

        assertThat(flashMap.isEmpty(), is(true));

    }

    @Test
    public void testSetResultMessages_bussinessExceptionMessagesAttribute_is_null() {

        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        testTarget.setResultMessagesAttribute(null);

        ResultMessages resultMessages = ResultMessages.error().add("code01");
        BusinessException occurException = new BusinessException(resultMessages);

        testTarget.setResultMessages(occurException, mockRequest);

        assertThat(flashMap.isEmpty(), is(true));

    }

    @Test
    public void testSetResultMessages_bussinessExceptionMessagesAttribute_is_blank() {

        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        testTarget.setResultMessagesAttribute("");

        ResultMessages resultMessages = ResultMessages.error().add("code01");
        BusinessException occurException = new BusinessException(resultMessages);

        testTarget.setResultMessages(occurException, mockRequest);

        assertThat(flashMap.isEmpty(), is(true));

    }

    @Test
    public void testSetExceptionInfo() {

        // do setup.
        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        SimpleMappingExceptionCodeResolver exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
        exceptionCodeResolver.setDefaultExceptionCode("defaultCode");
        testTarget.setExceptionCodeResolver(exceptionCodeResolver);

        ResultMessages resultMessages = ResultMessages.error().add("code001");
        BusinessException occurException = new BusinessException(resultMessages);

        // do test.
        testTarget.setExceptionInfo(occurException, mockRequest, mockResponse);

        // do assert.
        assertThat(mockRequest.getAttribute("exceptionCode").toString(), is(
                "defaultCode"));
        assertThat(mockResponse.getHeader("X-Exception-Code"), is(
                "defaultCode"));
        assertSame(resultMessages, flashMap.get(
                ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME));

    }

    @Test
    public void testDoResolveException_super_return_null() {

        // do setup.

        // do test.
        ModelAndView actualModleAndView = testTarget.doResolveException(
                mockRequest, mockResponse, null, new Exception());

        // do assert.
        assertThat(actualModleAndView, is(nullValue()));
    }

    @Test
    public void testDoResolveException_super_return_notnull() throws Exception {

        // do setup.
        ResultMessages resultMessages = ResultMessages.error().add("code001");
        BusinessException occurException = new BusinessException(resultMessages);

        // setup locale.
        Locale locale = Locale.ITALIAN;
        mockRequest.addPreferredLocale(locale);

        // setup FlashMap.
        FlashMap flashMap = new FlashMap();
        mockRequest.setAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE,
                flashMap);

        // setup exception resolver.
        // setup default.
        testTarget.setDefaultErrorView("defaultErrorView");
        testTarget.setDefaultStatusCode(444);

        // setup exception code resolver.
        SimpleMappingExceptionCodeResolver exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
        exceptionCodeResolver.setDefaultExceptionCode("defaultExceptionCode");
        testTarget.setExceptionCodeResolver(exceptionCodeResolver);

        // do test.
        ModelAndView actualModleAndView = testTarget.doResolveException(
                mockRequest, mockResponse, null, occurException);

        // do assert.
        assertThat(mockResponse.getStatus(), is(444));
        assertThat((Integer) mockRequest.getAttribute(
                WebUtils.ERROR_STATUS_CODE_ATTRIBUTE), is(444));

        assertSame(occurException, actualModleAndView.getModel().get(
                SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE));

        assertThat(mockRequest.getAttribute("exceptionCode").toString(), is(
                "defaultExceptionCode"));
        assertThat(mockResponse.getHeader("X-Exception-Code"), is(
                "defaultExceptionCode"));

        assertSame(resultMessages, flashMap.get(
                ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME));

    }

    public class TestAjaxController {
        public void method1(String arg) {

        }

    }

}
