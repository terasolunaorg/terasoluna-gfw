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
package org.terasoluna.gfw.common.exception.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Optional;
import java.util.stream.BaseStream;

import org.springframework.core.annotation.AliasFor;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

/**
 * Annotate method that the resource must be found.
 * <p>
 * Throws {@link ResourceNotFoundException} if the resource cannot be found.
 * </p>
 * 
 * @since 5.6.0
 * @author Atsushi Yoshikawa
 */
@Documented
@Target({ METHOD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ResourceMustBeFound {

    /**
     * Alias for {@link #code()}.
     * 
     * @return message code
     */
    @AliasFor("code")
    String value() default "";

    /**
     * Message code of {@link ResultMessage}.
     * <ul>
     * <li>It takes precedence over {@link #text()}.
     * <li>Message placeholder (as <code>{0}</code>) is automatically completed with the method arguments.
     * </ul>
     * 
     * @return message code
     */
    @AliasFor("value")
    String code() default "";

    /**
     * Message text of {@link ResultMessage}.
     * 
     * @return message text
     */
    String text() default "";

    /**
     * Message type of {@link ResultMessages}.
     * 
     * @return message type
     */
    StandardResultMessageType type() default StandardResultMessageType.ERROR;

    /**
     * Which conditions determine that the resource cannot be found.
     * 
     * @return condition
     */
    NotFoundCondition notFoundIs() default NotFoundCondition.EMPTY;

    /**
     * Candidates for which conditions determine that the resource cannot be found.
     * 
     * @since 5.6.0
     * @author Atsushi Yoshikawa
     */
    enum NotFoundCondition {
        /**
         * Null is the resource cannot be found.
         */
        NULL {
            @Override
            public boolean isNotFound(Object target) {
                Object value = unwrapOptional(target);
                return value == null;
            }
        },
        /**
         * Null and Empty Array,Iterable,Map,Stream are the resource cannot be found.
         */
        EMPTY {
            @Override
            public boolean isNotFound(Object target) {
                Object value = unwrapOptional(target);
                return value == null //
                        || (value.getClass().isArray()
                                && ((Object[]) value).length == 0) // empty array.
                        || (value instanceof Iterable<?>
                                && !((Iterable<?>) value).iterator().hasNext()) // empty iterable.
                        || (value instanceof Map<?, ?> && ((Map<?, ?>) value)
                                .isEmpty()) // empty map.
                        || (value instanceof BaseStream<?, ?>
                                && !((BaseStream<?, ?>) value).iterator()
                                        .hasNext()) // empty stream.
                ;
            }
        };

        /**
         * Whether or not the resource cannot be found.
         * 
         * @param target validation target
         * @return {@code true} : resource cannot be found.
         */
        public abstract boolean isNotFound(Object target);

        /**
         * Unwrap optional value.
         * 
         * @param target validation target
         * @return unwraped value or {@code null}
         */
        protected Object unwrapOptional(Object target) {
            return target instanceof Optional<?> ? ((Optional<?>) target)
                    .orElse(null) : target;
        }
    }
}
