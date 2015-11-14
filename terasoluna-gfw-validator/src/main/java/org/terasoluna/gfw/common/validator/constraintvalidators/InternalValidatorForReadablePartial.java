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

import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;

/**
 * Internal validator class of {@link ReadablePartial} type.
 * @see InternalValidator
 * @see AfterValidator
 * @see BeforeValidator
 * @since 5.1.0
 */
class InternalValidatorForReadablePartial extends
                                         InternalValidator<ReadablePartial> {

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isGenericType(Object value) {
        return value instanceof ReadablePartial;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isAfter(ReadablePartial value, String date, String format) {
        ReadableInstant instant = parse(date, format);
        return value.toDateTime(instant).isAfter(instant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isBefore(ReadablePartial value, String date, String format) {
        ReadableInstant instant = parse(date, format);
        return value.toDateTime(instant).isBefore(instant);
    }

    /**
     * Parse string to {@link ReadableInstant}.
     * @param date parse date
     * @param format parse format
     * @return parsed date
     * @throws IllegalArgumentException exception occurs during parse, By invalid date and format.
     */
    private ReadableInstant parse(String date, String format) {
        try {

            return isEmpty(format) ? DateTime.parse(date) : DateTimeFormat
                    .forPattern(format).parseDateTime(date);

        } catch (RuntimeException e) {
            throw reportFailedToInitialize(e);
        }
    }
}
