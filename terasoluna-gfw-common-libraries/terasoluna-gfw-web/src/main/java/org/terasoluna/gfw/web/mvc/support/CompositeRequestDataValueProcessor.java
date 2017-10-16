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
package org.terasoluna.gfw.web.mvc.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * Concrete implementation class for {@link RequestDataValueProcessor}, used when <br>
 * multiple implementations of {@link RequestDataValueProcessor} are to be applied.<br>
 * <p>
 * This class is like a list of other {@link RequestDataValueProcessor} implementations.
 * </p>
 */
public class CompositeRequestDataValueProcessor implements
                                                RequestDataValueProcessor {

    /**
     * List of {@link RequestDataValueProcessor}
     */
    private final List<RequestDataValueProcessor> processors;

    /**
     * Reversed list of {@link RequestDataValueProcessor}
     */
    private final List<RequestDataValueProcessor> reversedProcessors;

    /**
     * Helper for invoke the {@code processAction()} method of {@link RequestDataValueProcessor}.
     * @since 1.0.2
     */
    private final ProcessActionInvocationHelper processActionInvocationHelper;

    /**
     * Constructor<br>
     * <p>
     * Sets and initializes a list of {@link RequestDataValueProcessor}
     * </p>
     * @param processors List of {@link RequestDataValueProcessor}
     */
    public CompositeRequestDataValueProcessor(
            RequestDataValueProcessor... processors) {

        this.processors = Collections.unmodifiableList(Arrays.asList(
                processors));
        List<RequestDataValueProcessor> reverse = Arrays.asList(processors);
        Collections.reverse(reverse);
        this.reversedProcessors = Collections.unmodifiableList(reverse);
        this.processActionInvocationHelper = new ProcessActionInvocationHelper();
    }

    /**
     * Calls the {@code processAction()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds. This method is for compatibility with Spring 3.
     * @param request the current request
     * @param action action of form tag. must not be null.
     * @return the action to use, possibly modified
     */
    public String processAction(HttpServletRequest request, String action) {

        String result = action;
        for (RequestDataValueProcessor processor : processors) {
            result = processActionInvocationHelper.invokeProcessAction(
                    processor, request, action, null);
            if (!action.equals(result)) {
                break;
            }
        }

        return result;
    }

    /**
     * Calls the {@code processAction()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds. This method is for compatibility with Spring 4 or higher.
     * @param request the current request
     * @param action action of form tag. must not be null.
     * @param method http method of form tag.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processAction(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, java.lang.String)
     * @since 1.0.2
     */
    @Override
    public String processAction(HttpServletRequest request, String action,
            String method) {

        String result = action;
        for (RequestDataValueProcessor processor : processors) {
            result = processActionInvocationHelper.invokeProcessAction(
                    processor, request, action, method);
            if (!action.equals(result)) {
                break;
            }
        }

        return result;
    }

    /**
     * Calls the {@code processFormFieldValue()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @param name the form field name
     * @param value the form field value.must not be null.
     * @param type the form field type ("text", "hidden", etc.)
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processFormFieldValue(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String processFormFieldValue(HttpServletRequest request, String name,
            String value, String type) {

        String result = value;
        for (RequestDataValueProcessor processor : processors) {
            result = processor.processFormFieldValue(request, name, value,
                    type);
            if (!value.equals(result)) {
                break;
            }
        }

        return result;
    }

    /**
     * Calls the {@code getExtraHiddenFields()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#getExtraHiddenFields(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, String> getExtraHiddenFields(
            HttpServletRequest request) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (RequestDataValueProcessor processor : reversedProcessors) {
            Map<String, String> map = processor.getExtraHiddenFields(request);
            if (map != null) {
                result.putAll(map);
            }
        }
        return result;
    }

    /**
     * Calls the {@code processUrl()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @param url the URL value.must not be null.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processUrl(javax.servlet.http.HttpServletRequest,
     *      java.lang.String)
     */
    @Override
    public String processUrl(HttpServletRequest request, String url) {
        String result = url;
        for (RequestDataValueProcessor processor : processors) {
            result = processor.processUrl(request, url);
            if (!url.equals(result)) {
                break;
            }
        }
        return result;
    }

}
