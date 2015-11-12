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
package org.terasoluna.gfw.common.validator.constraintvalidators;

import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.isEmpty;
import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.reportFailedToInitialize;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Internal validator class of {@link ChronoLocalDateTime} type.
 * @see InternalValidator
 * @see AfterValidator
 * @see BeforeValidator
 */
class InternalValidatorForChronoLocalDateTime
                                             extends
                                             InternalValidator<ChronoLocalDateTime<?>> {

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isGenericType(Object value) {
        return value instanceof ChronoLocalDateTime<?>;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isAfter(ChronoLocalDateTime<?> value, String date, String format) {
        return value.isAfter(parse(date, format));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isBefore(java.time.chrono.ChronoLocalDateTime<?> value,
            String date, String format) {
        return value.isBefore(parse(date, format));
    };

    /**
     * Parse string to {@link ChronoLocalDateTime}.
     * @param date parse date
     * @param format parse format
     * @return parsed date
     * @throws IllegalArgumentException exception occurs during parse, By invalid date and format.
     */
    private ChronoLocalDateTime<?> parse(String date, String format) {
        try {

            DateTimeFormatter formatter = isEmpty(format) ? DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    : DateTimeFormatter.ofPattern(format);
            formatter = formatter.withResolverStyle(ResolverStyle.STRICT);

            return LocalDateTime.parse(date, formatter);

        } catch (RuntimeException e) {
            throw reportFailedToInitialize(e);
        }
    }
}
