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
package org.terasoluna.gfw.common.exception;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.terasoluna.gfw.common.exception.annotation.ResourceMustBeFound;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

/**
 * Interceptor throws {@link ResourceNotFoundException} if resource not found.
 * <p>
 * Determine whether {@link ResourceNotFoundException} is thrown by {@link ResourceMustBeFound} assigned to method.
 * </p>
 *
 * <h2>Example of the Settings</h2><br>
 * <strong>[xxx-domain.xml]</strong><br>
 * 
 * <pre>
 * &lt;bean id=&quot;resourceNotFoundExceptionThrowingInterceptor&quot;
 *   class=&quot;org.terasoluna.gfw.common.exception.ResourceNotFoundExceptionThrowingInterceptor&quot; /&gt;
 * &lt;aop:config&gt;
 *   &lt;aop:advisor advice-ref=&quot;resourceNotFoundExceptionThrowingInterceptor&quot;
 *     pointcut=&quot;execution(* com.abc.xyz.domain.repository..*(..))&quot; /&gt;
 * &lt;/aop:config&gt;
 * </pre>
 * 
 * @see ResourceMustBeFound
 * @since 5.6.0
 * @author Atsushi Yoshikawa
 */
public class ResourceNotFoundExceptionThrowingInterceptor implements
                                                          MethodInterceptor {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(
            ResourceNotFoundExceptionThrowingInterceptor.class);

    /**
     * Throws {@link ResourceNotFoundException} if resource not found.
     * <p>
     * Determine whether {@link ResourceNotFoundException} is thrown by {@link ResourceMustBeFound} assigned to method.
     * </p>
     * 
     * @param invocation invocation target
     * @return returned object from invocation target
     * @throws Throwable if occur exception in invocation target
     * @see MethodInterceptor#invoke(MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = invocation.proceed();
        ResourceMustBeFound annotation = AnnotatedElementUtils
                .findMergedAnnotation(invocation.getMethod(),
                        ResourceMustBeFound.class);

        if (annotation == null || !annotation.notFoundIs().isNotFound(result)) {
            return result;
        }

        throw new ResourceNotFoundException(buildResultMessages(invocation,
                annotation));
    }

    /**
     * Build {@link ResultMessages} from {@link ResourceMustBeFound} and target arguments.
     *
     * @param invocation invocation target
     * @param annotation {@link ResourceMustBeFound} of invocation target
     * @return built {@link ResultMessages}
     */
    protected ResultMessages buildResultMessages(
            final MethodInvocation invocation,
            final ResourceMustBeFound annotation) {

        StandardResultMessageType type = annotation.type();
        String code = annotation.code();
        Object[] args = obtainMessageArgs(invocation, annotation);
        String text = annotation.text();

        ResultMessages messages = new ResultMessages(type);
        if (!code.isEmpty()) {
            messages.add(ResultMessage.fromCode(code, args));
        } else if (!text.isEmpty()) {
            messages.add(ResultMessage.fromText(text));
        } else {
            logger.warn(
                    "provided a ResultMessages without any message from [{}]",
                    invocation.getMethod().toGenericString());
        }

        return messages;
    }

    /**
     * Obtain arguments for {@link ResultMessage#fromCode(String, Object...)}.
     *
     * @param invocation invocation target
     * @param annotation {@link ResourceMustBeFound} of invocation target
     * @return method arguments of invocation target
     */
    protected Object[] obtainMessageArgs(final MethodInvocation invocation,
            final ResourceMustBeFound annotation) {

        return invocation.getArguments();
    }
}
