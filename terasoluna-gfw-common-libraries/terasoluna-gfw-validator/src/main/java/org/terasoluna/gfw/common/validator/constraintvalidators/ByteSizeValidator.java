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
package org.terasoluna.gfw.common.validator.constraintvalidators;

import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.reportFailedToInitialize;

import java.nio.charset.Charset;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.terasoluna.gfw.common.validator.constraints.ByteSize;

/**
 * Constraint validator class of {@link ByteSize} annotation.
 * <p>
 * Validate the {@link CharSequence}({@link String}, {@link StringBuilder}, etc ...) whose byte length must be between the
 * specified minimum and maximum. Determine the byte length By encoding the string in the specified charset.
 * </p>
 * @since 5.4.0
 * @see ConstraintValidator
 * @see ByteSize
 */
public class ByteSizeValidator implements
                               ConstraintValidator<ByteSize, CharSequence> {

    /**
     * The charset used in parse to a string.
     */
    private Charset charset;

    /**
     * Byte length must be higher or equal to.
     */
    private long min;

    /**
     * Byte length must be lower or equal to.
     */
    private long max;

    /**
     * Initialize validator.
     * @param constraintAnnotation annotation instance for a given constraint declaration
     * @throws IllegalArgumentException failed to get a charset by name, or min and max are invalid.
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(ByteSize constraintAnnotation) {
        try {
            charset = Charset.forName(constraintAnnotation.charset());
        } catch (IllegalArgumentException e) {
            throw reportFailedToInitialize(e);
        }
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        if (min < 0) {
            throw reportFailedToInitialize(new IllegalArgumentException("min["
                    + min + "] must not be negative value."));
        }
        if (max < 0) {
            throw reportFailedToInitialize(new IllegalArgumentException("max["
                    + max + "] must not be negative value."));
        }
        if (max < min) {
            throw reportFailedToInitialize(new IllegalArgumentException("max["
                    + max + "] must be higher or equal to min[" + min + "]."));
        }
    }

    /**
     * Validate execute.
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if {@code value} length is between the specified minimum and maximum, or null. otherwise {@code false}.
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(CharSequence value,
            ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        long byteLength = value.toString().getBytes(charset).length;
        return min <= byteLength && byteLength <= max;
    }
}
