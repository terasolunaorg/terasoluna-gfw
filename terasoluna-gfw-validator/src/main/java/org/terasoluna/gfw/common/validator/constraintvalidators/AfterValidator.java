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

import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.isClassPresent;
import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.reportUnexpectedType;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.terasoluna.gfw.common.validator.constraints.After;

/**
 * Constraint validator class of {@link After} annotation.
 * <p>
 * Validate the object whose value must be a date time whose value must be after(not allow equal) the specified date time.
 * </p>
 * @since 5.1.0
 * @see ConstraintValidator
 * @see After
 */
public class AfterValidator implements ConstraintValidator<After, Object> {

    /**
     * Class name of Java8 Date and Time API used to check the class is present.
     */
    private static final String CHRONO_CLASS_NAME = "java.time.chrono.ChronoLocalDateTime";

    /**
     * Class name of JodaTime API used to check the class is present.
     */
    private static final String JODA_TIME_CLASS_NAME = "org.joda.time.ReadableInstant";

    /**
     * Internal validators.
     */
    private Set<InternalValidator<?>> validators = new HashSet<InternalValidator<?>>();

    /**
     * The Compare destination date time.
     */
    private String date;

    /**
     * The Format string used in parse to a date time.
     */
    private String format;

    /**
     * Constructor.
     * <p>
     * Register available internal validators.
     * </p>
     */
    public AfterValidator() {
        validators.add(new InternalValidatorForDate());
        validators.add(new InternalValidatorForCalendar());
        if (isClassPresent(CHRONO_CLASS_NAME)) {
            validators.add(new InternalValidatorForChronoLocalDate());
            validators.add(new InternalValidatorForChronoLocalDateTime());
            validators.add(new InternalValidatorForChronoZonedDateTime());
            validators.add(new InternalValidatorForLocalTime());
            validators.add(new InternalValidatorForOffsetDateTime());
            validators.add(new InternalValidatorForOffsetTime());
            validators.add(new InternalValidatorForYear());
            validators.add(new InternalValidatorForYearMonth());
        }
        if (isClassPresent(JODA_TIME_CLASS_NAME)) {
            validators.add(new InternalValidatorForReadableInstant());
            validators.add(new InternalValidatorForReadablePartial());
        }
    }

    /**
     * Initialize validator.
     * @param constraintAnnotation annotation instance for a given constraint declaration
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(After constraintAnnotation) {
        date = constraintAnnotation.value();
        format = constraintAnnotation.format();
    }

    /**
     * Validate execute.
     * <p>
     * Delegete the validation {@link InternalValidator} by {@code value}'s type.
     * </p>
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if {@code value} is after(not allow equal) the specified date time, or null. otherwise {@code false}
     *         .
     * @throws IllegalArgumentException {@code value} type is not match any type of internal validator target.
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (InternalValidator validator : validators) {
            if (validator.isGenericType(value)) {
                return validator.isAfter(value, date, format);
            }
        }

        throw reportUnexpectedType(value);
    }
}
