/*
 * Copyright(c) 2013 NTT DATA Corporation.
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Set random value(track ID) per request to MDC and HTTP Response Header and HTTP Request Attribute (request scope). <br>
 * <p>
 * The value of track ID is retrieved from HTTP Request Header.<br>
 * If the attribute is not set in HTTP Request Header, this filter creates random value as track ID and use it.
 *
 * <p>
 * An attribute name of track ID is "X-Track" by default.
 * Also you can change to an any name as follow:
 *
 * <pre>
 * in web.xml
 * <code>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;attributeName&lt;/param-name&gt;
 *     &lt;param-value&gt;Tracking-Id&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </code>
 * </pre>
 *
 * <p>
 * A random value is the 32 length HEX string(trimmed the '-' character from version 4 uuid string) by default.
 * Also you can change to use a version 4 uuid string(not trimmed the '-' character) as follow:
 *
 * <pre>
 * in web.xml
 * <code>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;useV4Uuid&lt;/param-name&gt;
 *     &lt;param-value&gt;true&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </code>
 * </pre>
 *
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
     * Whether use the version 4 uuid string when a track ID will create
     */
    private boolean useV4Uuid;

    /**
     * Whether there was specified the {@code maxMDCValueLength} by user
     */
    private boolean specifiedMaxMDCValueLength;

    /**
     * set attribute name to set MDC and HTTP Response Header<br>
     * @param attributeName attribute name
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * set whether use the version 4 uuid string when a track ID will create.
     * <p>
     *   Default is {@code false}.
     * </p>
     * @param useV4Uuid If use the version 4 uuid string, set to {@code true}
     */
    public void setUseV4Uuid(boolean useV4Uuid) {
        this.useV4Uuid = useV4Uuid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxMDCValueLength(int maxMDCValueLength) {
        super.setMaxMDCValueLength(maxMDCValueLength);
        this.specifiedMaxMDCValueLength = true;
    }

    /**
     * If the {@code useV4Uuid} is {@code true} and the {@code maxMDCValueLength } not present,
     * set the {@code maxMDCValueLength} to {@code 36}(length of version 4 uuid string).
     */
    @Override
    protected void initFilterBean() {
        if (!specifiedMaxMDCValueLength && useV4Uuid) {
            setMaxMDCValueLength(36);
        }
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
     * returns 32-length random HEX string or version 4 uuid string.
     * </p>
     * @return X-Track
     */
    protected String createXTrack() {
        String uuid = UUID.randomUUID().toString();
        String xTrack = useV4Uuid ? uuid
                : UUID_REPLACE_PATTERN.matcher(uuid).replaceAll("");
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
