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
package org.terasoluna.gfw.common.codepoints;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.terasoluna.gfw.common.codepoints.validator.ConsistOfValidator;

/**
 * All code points in the string must be included in any {@link CodePoints} class specified by {@link #value()}.
 * @since 5.1.0
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ConsistOfValidator.class })
@Documented
public @interface ConsistOf {
    /**
     * CodePoints
     * @return codePoints
     */
    Class<? extends CodePoints>[] value();

    /**
     * Error message or message key
     * @return error message or message key
     */
    String message() default "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}";

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
     * Defines several <code>@ConsistOf</code> annotations on the same element
     * @see ConsistOf
     */
    @Target({ ElementType.METHOD, ElementType.FIELD,
            ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        /**
         * <code>@ConsistOf</code> annotations
         * @return annotations
         */
        ConsistOf[] value();
    }
}
