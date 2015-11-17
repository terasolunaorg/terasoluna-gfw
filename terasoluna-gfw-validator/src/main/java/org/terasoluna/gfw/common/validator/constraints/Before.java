/*
 * Copyright (C) 2013-2015 terasoluna.org
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

import org.terasoluna.gfw.common.validator.constraintvalidators.BeforeValidator;

/**
 * The annotated element must be a date time whose value must be before(not allow equal) the specified date time.
 * <p>
 * Supported types are:
 * </p>
 * <ul>
 * <li>{@code Date} : default format is {@code "yyyy-MM-dd"}</li>
 * <li>{@code Calendar} : default format is {@code "yyyy-MM-dd"}</li>
 * <li>{@code ChronoLocalDate} : default format is {@code DateTimeFormatter.ISO_LOCAL_DATE}</li>
 * <li>{@code ChronoLocalDateTime} : default format is {@code DateTimeFormatter.ISO_LOCAL_DATE_TIME}</li>
 * <li>{@code ChronoZonedDateTime} : default format is {@code DateTimeFormatter.ISO_ZONED_DATE_TIME}</li>
 * <li>{@code LocalTime} : default format is {@code DateTimeFormatter.ISO_LOCAL_TIME}</li>
 * <li>{@code OffsetDateTime} : default format is {@code DateTimeFormatter.ISO_OFFSET_DATE_TIME}</li>
 * <li>{@code OffsetTime} : default format is {@code DateTimeFormatter.ISO_OFFSET_TIME}</li>
 * <li>{@code Year} : default format is {@code "uuuu"}</li>
 * <li>{@code YearMonth} : default format is {@code "uuuu-MM"}</li>
 * <li>{@code ReadableInstant}</li>
 * <li>{@code ReadablePartial}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid. If specify a {@link After#value()} and {@link After#format()} that can not parse
 * to date time, and If elements type is not supported, it is thrown {@link IllegalArgumentException}(wrapped in
 * {@link ValidationException}).
 * </p>
 * <p>
 * {@code Java8 Date and Time API} and {@code Joda-Time} is optional dependency library. When {@code Java8 Date and Time API}
 * and {@code Joda-Time} exists , this annotation to perform the validation of those types
 * </p>
 * @since 5.1.0
 * @see BeforeValidator
 */
@Documented
@Constraint(validatedBy = { BeforeValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Before {

    String message() default "{org.terasoluna.gfw.common.validator.constraints.Before.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return value the compare destination date time
     */
    String value();

    /**
     * @return the format string used in parse to a date time
     */
    String format() default "";

    /**
     * Defines several {@link Before} annotations on the same element.
     * @see Before
     * @since 5.1.0
     */
    @Documented
    @Target({ METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @interface List {
        Before[] value();
    }
}
