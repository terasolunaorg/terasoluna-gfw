/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.terasoluna.gfw.common.validator.constraints.ByteMin.List;
import org.terasoluna.gfw.common.validator.constraintvalidators.ByteMinValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ValidationException;

/**
 * The annotated element must be a {@link CharSequence}({@link String}, {@link StringBuilder}, etc
 * ...) whose byte length must be higher or equal to the specified minimum.
 * <p>
 * If you want to specify not only a minimum length but also a maximum length, it is recommended to
 * use {@link ByteSize}.
 * </p>
 * <p>
 * Supported types are:
 * </p>
 * <ul>
 * <li>{@code CharSequence}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid. Determine the byte length By encoding the string in
 * the specified {@link ByteMin#charset()}. If not specify, encode with charset {@code "UTF-8"}. An
 * {@link IllegalArgumentException}(wrapped in {@link ValidationException}) is thrown if specify
 * {@link ByteMin#charset()} that can not be used or specify {@link ByteMin#value()} that is
 * negative value.
 * </p>
 * @since 5.1.0
 * @see ByteMinValidator
 * @see ByteSize
 */
@Documented
@Constraint(validatedBy = {ByteMinValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface ByteMin {

    /**
     * Error message or message key
     * @return error message or message key
     */
    String message() default "{org.terasoluna.gfw.common.validator.constraints.ByteMin.message}";

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
     * @return value the element's byte length must be higher or equal to
     */
    long value();

    /**
     * @return the charset name used in parse to a string
     */
    String charset() default "UTF-8";

    /**
     * Defines several {@link ByteMin} annotations on the same element.
     * @see ByteMin
     * @since 5.1.0
     */
    @Documented
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @interface List {
        /**
         * <code>@ByteMin</code> annotations
         * @return annotations
         */
        ByteMin[] value();
    }
}
