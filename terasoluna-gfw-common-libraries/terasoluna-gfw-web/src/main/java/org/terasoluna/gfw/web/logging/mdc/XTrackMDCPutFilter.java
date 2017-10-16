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
package org.terasoluna.gfw.web.logging.mdc;

import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Set random value per request to MDC and HTTP Response Header and HTTP Request Attribute (request scope). <br>
 * <p>
 * default attribute name is "X-Track". You can change this name by configure. The value of X-Track is retrieved from HTTP
 * Request Header (same attribute name).<br>
 * If the attibute is not set in HTTP Request Header, this filter creates random value as X-Track and use it.
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
public class XTrackMDCPutFilter extends AbstractMDCPutFilter {

    /**
     * pattern for replace uuid
     */
    private static final Pattern UUID_REPLACE_PATTERN = Pattern.compile("-");

    /**
     * attribute name to set MDC and HTTP Response Header
     */
    private String attributeName = "X-Track";

    /**
     * set attribute name to set MDC and HTTP Response Header<br>
     * @param attributeName attribute name
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * <p>
     * get attribute name to set MDC and HTTP Response Header. (default: X-Track)
     * </p>
     * @see org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter#getMDCKey(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String getMDCKey(HttpServletRequest request,
            HttpServletResponse response) {
        return attributeName;
    }

    /**
     * Create track ID (X-Track)<br>
     * <p>
     * returns 32-length random HEX string.
     * </p>
     * @return X-Track
     */
    protected String createXTrack() {
        String uuid = UUID.randomUUID().toString();
        String xTrack = UUID_REPLACE_PATTERN.matcher(uuid).replaceAll("");
        return xTrack;
    }

    /**
     * <p>
     * return track ID (X-Track) and set it HTTP Response Header and HTTP Request Attribute (request scope).<br>
     * If track ID is set in HTTP Request Header, then use it. Unless track ID is created by {@link #createXTrack()}.
     * </p>
     * @see org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter#getMDCValue(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String getMDCValue(HttpServletRequest request,
            HttpServletResponse response) {
        String xTrack = request.getHeader(attributeName);
        if (xTrack == null) {
            xTrack = createXTrack();
        }
        String cutXTrack = cutValue(xTrack);
        response.setHeader(attributeName, cutXTrack);
        request.setAttribute(attributeName, cutXTrack);
        return cutXTrack;
    }

}
