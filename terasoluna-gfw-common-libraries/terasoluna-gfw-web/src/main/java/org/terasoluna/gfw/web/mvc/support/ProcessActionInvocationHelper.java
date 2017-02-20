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

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * Helper class for invoke the {@code processAction()} method of {@link RequestDataValueProcessor}.
 * <p>
 * This class support the both with Spring3's(legacy) signature and Spring4's signature.
 * </p>
 * @since 1.0.2
 */
class ProcessActionInvocationHelper {

    /**
     * Method of {@link RequestDataValueProcessor#processAction} on the runtime environment.
     */
    private final Method processActionMethod;

    /**
     * Flag that indicate whether legacy signature.
     */
    private final boolean isLegacySignature;

    /**
     * Default constructor.
     */
    ProcessActionInvocationHelper() {
        // Check Spring4's signature
        Method targetProcessActionMethod = ReflectionUtils.findMethod(
                RequestDataValueProcessor.class, "processAction",
                HttpServletRequest.class, String.class, String.class);
        boolean isLegacySignatureTmp = false;

        if (targetProcessActionMethod == null) {
            // Check Spring3's signature
            targetProcessActionMethod = ReflectionUtils.findMethod(
                    RequestDataValueProcessor.class, "processAction",
                    HttpServletRequest.class, String.class);
            isLegacySignatureTmp = true;
        }
        if (targetProcessActionMethod == null) {
            throw new IllegalStateException("'processActionMethod' is not found. Should never get here!");
        }
        this.processActionMethod = targetProcessActionMethod;
        this.isLegacySignature = isLegacySignatureTmp;
    }

    /**
     * Invoke the {@code processAction()} method of specified {@link RequestDataValueProcessor}.
     * @param requestDataValueProcessor {@link RequestDataValueProcessor} of invocation target
     * @param request current request
     * @param action action path of from
     * @param method http method of from
     * @return action that returned from specified {@link RequestDataValueProcessor}
     */
    String invokeProcessAction(
            RequestDataValueProcessor requestDataValueProcessor,
            HttpServletRequest request, String action, String method) {
        if (isLegacySignature) {
            return (String) ReflectionUtils.invokeMethod(
                    this.processActionMethod, requestDataValueProcessor,
                    request, action);
        } else {
            return (String) ReflectionUtils.invokeMethod(
                    this.processActionMethod, requestDataValueProcessor,
                    request, action, method);
        }
    }
}
