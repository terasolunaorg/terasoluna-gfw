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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.terasoluna.gfw.common.exception.ExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * Class that performs exception handling.
 */
public class SystemExceptionResolver extends SimpleMappingExceptionResolver {

    /**
     * Attribute name for storing result message in flash map.
     */
    private String resultMessagesAttribute = ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;

    /**
     * Attribute name to set exception code in request scope.
     */
    private String exceptionCodeAttribute = "exceptionCode";

    /**
     * Header name to set exception code in response header.
     */
    private String exceptionCodeHeader = "X-Exception-Code";

    /**
     * Exception code resolution object.
     */
    private ExceptionCodeResolver exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();

    /**
     * Sets the value for exception Code Attribute name.
     * <p>
     * This value is used as the attribute name to set exception code in request scope and {@code FlashMap}. Calling this method
     * overwrites the default value {@code "exceptionCode"}. <br>
     * If {@code null} or blank or space is set, then exception code will not be set in request scope and {@code FlashMap} when
     * {@link #setExceptionCode(Exception, HttpServletRequest, HttpServletResponse)} is called.
     * </p>
     * @param exceptionCodeAttribute Attribute name of the exception code to be set in request scope and {@code FlashMap}.
     */
    public void setExceptionCodeAttribute(String exceptionCodeAttribute) {
        this.exceptionCodeAttribute = exceptionCodeAttribute;
    }

    /**
     * Sets the header name to set exception code in the response header.
     * <p>
     * Calling this method overwrites the default value {@code "X-Exception-Code"}. <br>
     * If {@code null} or blank or space is set, then exception code will not be set in the response header when
     * {@link #setExceptionCode(Exception, HttpServletRequest, HttpServletResponse)} is called..
     * </p>
     * @param exceptionCodeHeader Attribute name of the exception code to be set in the response header.
     */
    public void setExceptionCodeHeader(String exceptionCodeHeader) {
        this.exceptionCodeHeader = exceptionCodeHeader;
    }

    /**
     * Sets the attribute name used for storing result message in request scope and {@code FlashMap}.
     * <p>
     * Calling this method overwrites the default value {@code "resultMessages"}. <br>
     * If {@code null} or blank or space is set, then {@code ResultMessages} will not be set in request scope and
     * {@code FlashMap}　when　{@link #setResultMessages(Exception, HttpServletRequest)} is called.
     * </p>
     * @param resultMessagesAttribute Attribute name used for storing result message in request scope and {@code FlashMap}.
     */
    public void setResultMessagesAttribute(String resultMessagesAttribute) {
        this.resultMessagesAttribute = resultMessagesAttribute;
    }

    /**
     * Sets the object for resolving exception code.
     * <p>
     * If not set, exception code will not be set in request scope and response header　when
     * {@link #setExceptionCode(Exception, HttpServletRequest, HttpServletResponse)} is called.
     * </p>
     * @param exceptionCodeResolver Exception code resolution object.
     */
    public void setExceptionCodeResolver(
            ExceptionCodeResolver exceptionCodeResolver) {
        this.exceptionCodeResolver = exceptionCodeResolver;
    }

    /**
     * Performs exception handling.
     * <p>
     * Decides the View and resolves the Model necessary for display of View.
     * </p>
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler Request handler
     * @param ex Exception
     * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView modelAndView = super.doResolveException(request, response,
                handler, ex);
        if (modelAndView == null) {
            return modelAndView;
        }

        setExceptionInfo(ex, request, response);

        return modelAndView;

    }

    /**
     * Sets the exception information
     * <p>
     * Sets exception information in {@code HttpServletRequest} and {@code HttpServletResponse}.
     * </p>
     * @param ex Exception
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    protected void setExceptionInfo(Exception ex, HttpServletRequest request,
            HttpServletResponse response) {

        setExceptionCode(ex, request, response);

        setResultMessages(ex, request);

    }

    /**
     * Sets exception code in {@code HttpServletRequest} and {@code HttpServletResponse} header.
     * <p>
     * Sets exception code in {@code HttpServletRequest} and {@code HttpServletResponse} header. If exceptionCodeAttribute is
     * {@code null} or blank or space is set, then exception code is not set.
     * </p>
     * @param ex Exception
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    protected void setExceptionCode(Exception ex, HttpServletRequest request,
            HttpServletResponse response) {

        String exceptionCode = null;
        if (exceptionCodeResolver != null && (StringUtils.hasText(
                exceptionCodeAttribute) || StringUtils.hasText(
                        exceptionCodeHeader))) {
            exceptionCode = exceptionCodeResolver.resolveExceptionCode(ex);
        }

        if (exceptionCode == null) {
            return;
        }

        if (StringUtils.hasText(exceptionCodeAttribute)) {
            request.setAttribute(exceptionCodeAttribute, exceptionCode);
            FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
            if (flashMap != null) {
                flashMap.put(exceptionCodeAttribute, exceptionCode);
            }
        }

        if (StringUtils.hasText(exceptionCodeHeader)) {
            response.setHeader(exceptionCodeHeader, exceptionCode);
        }

    }

    /**
     * Sets result message
     * <p>
     * Sets result message in {@code HttpServletRequest}({@code FlashMap})
     * </p>
     * @param ex Exception
     * @param request {@link HttpServletRequest}
     */
    protected void setResultMessages(Exception ex, HttpServletRequest request) {

        if (!StringUtils.hasText(resultMessagesAttribute)) {
            return;
        }

        if (!(ex instanceof ResultMessagesNotificationException)) {
            return;
        }

        ResultMessages resultMessages = ((ResultMessagesNotificationException) ex)
                .getResultMessages();

        request.setAttribute(resultMessagesAttribute, resultMessages);

        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        if (flashMap != null) {
            flashMap.put(resultMessagesAttribute, resultMessages);
        }

    }

}
