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
package org.terasoluna.gfw.common.validator.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ValidationException;

import org.terasoluna.gfw.common.validator.constraintvalidators.ByteMaxValidator;

/**
 * The annotated element must be a {@link CharSequence}({@link String}, {@link StringBuilder}, etc ...) whose byte length must
 * be lower or equal to the specified maximum.
 * <p>
 * Supported types are:
 * </p>
 * <ul>
 * <li>{@code String}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid. Determine the byte length By encoding the string in the specified
 * {@link ByteMax#charset()}. If not specify, encode with charset {@code "UTF-8"}. If specify a charset that can not be used, it
 * is thrown {@link IllegalArgumentException}(wrapped in {@link ValidationException}).
 * </p>
 * @since 5.1.0
 * @see ByteMaxValidator
 */
@Documented
@Constraint(validatedBy = { ByteMaxValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface ByteMax {

    /**
     * Error message or message key
     * @return error message or message key
     */
    String message() default "{org.terasoluna.gfw.common.validator.constraints.ByteMax.message}";

    /**
     * Constraint groups
     * @return constraint groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return value the element's byte length must be lower or equal to
     */
    long value();

    /**
     * @return the charset name used in parse to a string
     */
    String charset() default "UTF-8";

    /**
     * Defines several {@link ByteMax} annotations on the same element.
     * @see ByteMax
     * @since 5.1.0
     */
    @Documented
    @Target({ METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @interface List {
        /**
         * <code>@ByteMax</code> annotations
         * @return annotations
         */
        ByteMax[] value();
    }
}
