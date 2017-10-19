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
package org.terasoluna.gfw.web.util;

import javax.servlet.http.HttpServletResponse;

/**
 * utility class about HttpResponse
 */
public final class ResponseUtils {

    /**
     * header name of "Pragma".
     */
    private static final String HEADER_PRAGMA = "Pragma";

    /**
     * header name of "Expires".
     */
    private static final String HEADER_EXPIRES = "Expires";

    /**
     * header name of "Cache-Control".
     */
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";

    /**
     * Default Constructor.
     * <p>
     * do nothing.
     * </p>
     */
    private ResponseUtils() {
        // do nothing.
    }

    /**
     * set http headers to prevent response caching.
     * <table border=1>
     * <caption>http headers</caption>
     * <tr>
     * <th>Target</th>
     * <th>Attribute Name</th>
     * <th>Value</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>Header</td>
     * <td>Cache-Control</td>
     * <td>private, no-store, no-cache, must-revalidate</td>
     * <td>http://www.ipa.go.jp/security/awareness/vendor/programmingv2/contents/ 405.html</td>
     * </tr>
     * <tr>
     * <td>Header</td>
     * <td>Pragma</td>
     * <td>no-cache</td>
     * <td>for HTTP 1.0</td>
     * </tr>
     * <tr>
     * <td>DateHeader</td>
     * <td>Expires</td>
     * <td>1</td>
     * <td>expires soon</td>
     * </tr>
     * </table>
     * <p>
     * Nothing happens when <code>response</code> is null.
     * </p>
     * @param response HttpServletResponse to prevent caching
     */
    public static void setPreventionCachingHeaders(
            HttpServletResponse response) {
        if (response != null) {
            response.setHeader(HEADER_PRAGMA, "no-cache");
            response.setDateHeader(HEADER_EXPIRES, 1L);
            response.setHeader(HEADER_CACHE_CONTROL,
                    "private,no-store,no-cache,must-revalidate");
        }
    }
}
