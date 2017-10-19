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
package org.terasoluna.gfw.web.token.transaction;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.terasoluna.gfw.web.mvc.support.RequestDataValueProcessorAdaptor;

/**
 * A {@code RequestDataValueProcessor} implementation class which returns a map containing the {@link TransactionToken} received
 * in the request. <br>
 */
public class TransactionTokenRequestDataValueProcessor extends
                                                       RequestDataValueProcessorAdaptor {

    /**
     * Returns a map containing the {@link TransactionToken} received in the request. <br>
     * Request attribute containing the token string is {@link TransactionTokenInterceptor#NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME}
     * @see org.terasoluna.gfw.web.mvc.support.RequestDataValueProcessorAdaptor#getExtraHiddenFields(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, String> getExtraHiddenFields(
            HttpServletRequest request) {
        TransactionToken nextToken = (TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);
        if (nextToken != null) {
            Map<String, String> map = new HashMap<String, String>(2);
            map.put(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER,
                    nextToken.getTokenString());
            return map;
        }
        return null;
    }

}
