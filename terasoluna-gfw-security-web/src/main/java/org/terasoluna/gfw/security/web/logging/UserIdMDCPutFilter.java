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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter;

/**
 * class that stores the authentication user name in {@code MDC} and also returns the same.
 * <p>
 * default attribute name is "USER". You can change this name by configure.<br>
 * 
 * <pre>
 * <code>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;attributeName&lt;/param-name&gt;
 *     &lt;param-value&gt;XXXX&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </code>
 * </pre>
 * 
 * in web.xml
 */
public class UserIdMDCPutFilter extends AbstractMDCPutFilter {

    /**
     * attribute name to set MDC
     */
    private String attributeName = "USER";

    /**
     * set attribute name to set MDC<br>
     * @param attibuteName attribute name
     */
    public void setAttributeName(String attibuteName) {
        this.attributeName = attibuteName;
    }

    /**
     * fetches the key used for storing authentication user name in {@code MDC}
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return Key used for storing authentication user name in {@code MDC}
     * @see org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter#getMDCKey(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected String getMDCKey(HttpServletRequest request,
            HttpServletResponse response) {
        return attributeName;
    }

    /**
     * fetches the username which has been authenticated
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return username
     * @see org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter#getMDCValue(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected String getMDCValue(HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
            return principal.toString();
        }

        return null;
    }

}
