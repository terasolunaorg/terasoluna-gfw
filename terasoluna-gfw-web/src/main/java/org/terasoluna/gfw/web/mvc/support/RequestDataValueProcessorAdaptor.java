/*
 * Copyright (C) 2013-2014 terasoluna.org
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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * Base class for custom implementations of {@link RequestDataValueProcessor} interface. <br>
 */
public class RequestDataValueProcessorAdaptor implements
                                             RequestDataValueProcessor {

    /**
     * returns the action passed as argument as it is. This method is for compatibility with Spring 3.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processAction(javax.servlet.http.HttpServletRequest,
     *      java.lang.String)
     */
    public String processAction(HttpServletRequest request, String action) {
        return action;
    }

    /**
     * returns the action passed as argument as it is. This method is for compatibility with Spring 4.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processAction(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, java.lang.String)
     */
    public String processAction(HttpServletRequest request, String action, String method) {
        return action;
    }

    /**
     * returns the value passed as argument as it is.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processFormFieldValue(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String processFormFieldValue(HttpServletRequest request,
            String name, String value, String type) {
        return value;
    }

    /**
     * returns null.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#getExtraHiddenFields(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        return null;
    }

    /**
     * Returns the url passed as argument as it is.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processUrl(javax.servlet.http.HttpServletRequest,
     *      java.lang.String)
     */
    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return url;
    }

}
