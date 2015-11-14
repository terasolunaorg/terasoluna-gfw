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

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Internal validator class of {@link Year} type.
 * @see InternalValidator
 * @see AfterValidator
 * @see BeforeValidator
 * @since 5.1.0
 */
class InternalValidatorForYear extends InternalValidator<Year> {

    /**
     * Default format.
     */
    private static final String DEFAULT_FORMAT = "uuuu";

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isGenericType(Object value) {
        return value instanceof Year;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isAfter(Year value, String date, String format) {
        return value.isAfter(parse(date, format));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isBefore(Year value, String date, String format) {
        return value.isBefore(parse(date, format));
    }

    /**
     * Parse string to {@link Year}.
     * @param date parse date
     * @param format parse format
     * @return parsed date
     * @throws IllegalArgumentException exception occurs during parse, By invalid date and format.
     */
    private Year parse(String date, String format) {
        try {

            DateTimeFormatter formatter = isEmpty(format) ? DateTimeFormatter
                    .ofPattern(DEFAULT_FORMAT) : DateTimeFormatter
                    .ofPattern(format);
            formatter = formatter.withResolverStyle(ResolverStyle.STRICT);

            return Year.parse(date, formatter);

        } catch (RuntimeException e) {
            throw reportFailedToInitialize(e);
        }
    }
}
