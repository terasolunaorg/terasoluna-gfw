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
package org.terasoluna.gfw.web.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.terasoluna.gfw.common.exception.ExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;
import org.terasoluna.gfw.common.message.ResultMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
     * Classes to be excluded
     */
    @Nullable
    private Class<?>[] excludedExceptions;

    /**
     * Whether to check for cause when checking for excludedExceptions.
     */
    private boolean checkCause = false;

    /**
     * Whether to check subclasses when checking for excludedExceptions.
     */
    private boolean checkSubClass = false;

    /**
     * Sets the value for exception Code Attribute name.
     * <p>
     * This value is used as the attribute name to set exception code in request scope and
     * {@code FlashMap}. Calling this method overwrites the default value {@code "exceptionCode"}.
     * <br>
     * If {@code null} or blank or space is set, then exception code will not be set in request
     * scope and {@code FlashMap} when
     * {@link #setExceptionCode(Exception, HttpServletRequest, HttpServletResponse)} is called.
     * </p>
     * @param exceptionCodeAttribute Attribute name of the exception code to be set in request scope
     *        and {@code FlashMap}.
     */
    public void setExceptionCodeAttribute(String exceptionCodeAttribute) {
        this.exceptionCodeAttribute = exceptionCodeAttribute;
    }

    /**
     * Sets the header name to set exception code in the response header.
     * <p>
     * Calling this method overwrites the default value {@code "X-Exception-Code"}. <br>
     * If {@code null} or blank or space is set, then exception code will not be set in the response
     * header when {@link #setExceptionCode(Exception, HttpServletRequest, HttpServletResponse)} is
     * called..
     * </p>
     * @param exceptionCodeHeader Attribute name of the exception code to be set in the response
     *        header.
     */
    public void setExceptionCodeHeader(String exceptionCodeHeader) {
        this.exceptionCodeHeader = exceptionCodeHeader;
    }

    /**
     * Sets the attribute name used for storing result message in request scope and
     * {@code FlashMap}.
     * <p>
     * Calling this method overwrites the default value {@code "resultMessages"}. <br>
     * If {@code null} or blank or space is set, then {@code ResultMessages} will not be set in
     * request scope and {@code FlashMap} when
     * {@link #setResultMessages(Exception, HttpServletRequest)} is called.
     * </p>
     * @param resultMessagesAttribute Attribute name used for storing result message in request
     *        scope and {@code FlashMap}.
     */
    public void setResultMessagesAttribute(String resultMessagesAttribute) {
        this.resultMessagesAttribute = resultMessagesAttribute;
    }

    /**
     * Sets the object for resolving exception code.
     * <p>
     * If not set, exception code will not be set in request scope and response header when
     * {@link #setExceptionCode(Exception, HttpServletRequest, HttpServletResponse)} is called.
     * </p>
     * @param exceptionCodeResolver Exception code resolution object.
     */
    public void setExceptionCodeResolver(ExceptionCodeResolver exceptionCodeResolver) {
        this.exceptionCodeResolver = exceptionCodeResolver;
    }

    /**
     * Sets classes to be excluded.
     * <p>
     * Set one or more classes to be excluded from the exception mappings. Excluded classes are
     * checked first and if one of them equals the actual class, the class will remain unresolved.
     * </p>
     * @param excludedExceptions One or more excluded class types.
     */
    @Override
    public void setExcludedExceptions(Class<?>... excludedExceptions) {
        this.excludedExceptions = excludedExceptions;
        // The process of using excludedExceptions in the super class is overridden, so there is no
        // need to call the setter of
        // the super.
        // super.setExcludedExceptions(excludedExceptions);
    }

    /**
     * Sets whether to check for cause when checking for excludedExceptions.
     * <p>
     * If set to true, causes are also subject to exclusion settings. Default value is false.
     * </p>
     * @param checkCause Whether to check for cause.
     */
    public void setCheckCause(boolean checkCause) {
        this.checkCause = checkCause;
    }

    /**
     * Sets whether to check subclasses when checking for excludedExceptions.
     * <p>
     * If set to true, the instance type is compared when checking for exclusion settings.
     * Therefore, subclasses of errors that are set to be excluded are also excluded. Default value
     * is false.
     * </p>
     * @param checkSubClass Whether to check subclasses.
     */
    public void setCheckSubClass(boolean checkSubClass) {
        this.checkSubClass = checkSubClass;
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
    @Nullable
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
        if (modelAndView == null) {
            return modelAndView;
        }

        setExceptionInfo(ex, request, response);

        return modelAndView;

    }

    /**
     * Check {@link #setExcludedExceptions(Class[]) "excludedExecptions"} and call the parent class
     * determineViewName.
     * <p>
     * When {@code checkCause} is true, check if causes are also eligible for exclusion.
     * </p>
     * @param ex Exception
     * @param request {@link HttpServletRequest}
     * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver#determineViewName(Exception
     *      ex, HttpServletRequest request)
     */
    @Nullable
    @Override
    protected String determineViewName(Exception ex, HttpServletRequest request) {

        if (this.excludedExceptions != null) {
            if (checkExcludedExceptions(ex)) {
                return null;
            }

            if (this.checkCause) {
                Throwable cause = ex.getCause();
                while (cause != null) {
                    if (checkExcludedExceptions(cause)) {
                        return null;
                    }
                    cause = cause.getCause();
                }
            }
        }

        return super.determineViewName(ex, request);

    }

    /**
     * Checks if the specified class is an excluded class.
     * <p>
     * When {@code checkInstanceType} is true, the given class is also excluded if it is a subclass
     * of {@code excludedExceptions}.
     * <p>
     * @param ex Exception
     */
    private boolean checkExcludedExceptions(Throwable ex) {
        for (Class<?> excludedException : this.excludedExceptions) {
            if ((this.checkSubClass && excludedException.isInstance(ex))
                    || (!this.checkSubClass && excludedException.equals(ex.getClass()))) {
                return true;
            }
        }
        return false;
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
     * Sets exception code in {@code HttpServletRequest} and {@code HttpServletResponse} header. If
     * exceptionCodeAttribute is {@code null} or blank or space is set, then exception code is not
     * set.
     * </p>
     * @param ex Exception
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    protected void setExceptionCode(Exception ex, HttpServletRequest request,
            HttpServletResponse response) {

        String exceptionCode = null;
        if (exceptionCodeResolver != null && (StringUtils.hasText(exceptionCodeAttribute)
                || StringUtils.hasText(exceptionCodeHeader))) {
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

        ResultMessages resultMessages =
                ((ResultMessagesNotificationException) ex).getResultMessages();

        request.setAttribute(resultMessagesAttribute, resultMessages);

        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        if (flashMap != null) {
            flashMap.put(resultMessagesAttribute, resultMessages);
        }

    }

}
