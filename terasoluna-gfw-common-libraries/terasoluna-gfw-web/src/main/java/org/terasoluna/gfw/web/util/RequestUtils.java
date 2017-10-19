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

import javax.servlet.http.HttpServletRequest;

/**
 * utility class about HttpRequest
 */
public final class RequestUtils {

    /**
     * Default Constructor.
     * <p>
     * do nothing.
     * </p>
     */
    private RequestUtils() {
        // do nothing.
    }

    /**
     * Returns whether the request is Ajax.
     * @param request the current request
     * @return <code>true</code> if ajax, otherwise <code>false</code>.
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
    }
}
