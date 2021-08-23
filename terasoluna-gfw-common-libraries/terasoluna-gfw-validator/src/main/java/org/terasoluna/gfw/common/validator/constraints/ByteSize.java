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

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ValidationException;

import org.terasoluna.gfw.common.validator.constraints.ByteSize.List;
import org.terasoluna.gfw.common.validator.constraintvalidators.ByteSizeValidator;

/**
 * The annotated element must be a {@link CharSequence}({@link String}, {@link StringBuilder}, etc ...) whose byte length must
 * be between the specified minimum and maximum.
 * <p>
 * This is an annotation combining the functions {@link ByteMin} and {@link ByteMax}. Compared to using two annotations, the
 * advantage is that overhead can be reduced by getting byte length at a time.
 * </p>
 * <p>
 * Supported types are:
 * </p>
 * <ul>
 * <li>{@code CharSequence}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid. Determine the byte length By encoding the string in the specified
 * {@link ByteSize#charset()}. If not specify, encode with charset {@code "UTF-8"}. An {@link IllegalArgumentException}(wrapped
 * in {@link ValidationException}) is thrown if specify {@link ByteSize#charset()} that can not be used or specify
 * {@link ByteSize#min()} or {@link ByteSize#max()} that is negative or specify {@link ByteSize#max()} that lower than
 * {@link ByteSize#min()} value.
 * </p>
 * @since 5.4.2
 * @see ByteSizeValidator
 * @see ByteMin
 * @see ByteMax
 */
@Documented
@Constraint(validatedBy = { ByteSizeValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface ByteSize {

    /**
     * Error message or message key
     * @return error message or message key
     */
    String message() default "{org.terasoluna.gfw.common.validator.constraints.ByteSize.message}";

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
    long min() default 0;

    /**
     * @return value the element's byte length must be lower or equal to
     */
    long max() default Long.MAX_VALUE;

    /**
     * @return the charset name used in parse to a string
     */
    String charset() default "UTF-8";

    /**
     * Defines several {@link ByteSize} annotations on the same element.
     * @see ByteSize
     * @since 5.4.2
     */
    @Documented
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER,
            TYPE_USE })
    @Retention(RUNTIME)
    @interface List {
        /**
         * <code>@ByteSize</code> annotations
         * @return annotations
         */
        ByteSize[] value();
    }
}
