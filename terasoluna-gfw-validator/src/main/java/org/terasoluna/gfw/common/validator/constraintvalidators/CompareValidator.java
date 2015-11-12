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

import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.getProperty;
import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.reportUnexpectedType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.terasoluna.gfw.common.validator.constraints.Compare;
import org.terasoluna.gfw.common.validator.constraints.Compare.Operator;

/**
 * Constraint validator class of {@link Compare} annotation.
 * <p>
 * Validate the result of comparing the two properties specified, must match the specified operator.
 * </p>
 * @since 5.1.0
 * @see ConstraintValidator
 * @see Compare
 */
public class CompareValidator implements ConstraintValidator<Compare, Object> {

    /**
     * Property name of comparison source.
     */
    private String source;

    /**
     * Property name of comparison destination.
     */
    private String destination;

    /**
     * Operator used in the comparison.
     */
    private Operator operator;

    /**
     * Validation message.
     */
    private String message;

    /**
     * Initialize validator.
     * @param constraintAnnotation annotation instance for a given constraint declaration
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(Compare constraintAnnotation) {
        source = constraintAnnotation.source();
        destination = constraintAnnotation.destination();
        operator = constraintAnnotation.operator();
        message = constraintAnnotation.message();
    }

    /**
     * Validate execute.
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if result to comparing {@code source} and {@code destination} is expected {@code operator}, or any
     *         property is null. otherwise {@code false}.
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object sourceValue = getProperty(value, source);
        Object destinationValue = getProperty(value, destination);

        if (sourceValue == null || destinationValue == null) {
            return true;
        }

        if (!assertComparable(sourceValue, destinationValue)) {
            constructValidationMessage(context);
            return false;
        }

        if (isCompareValid(sourceValue, destinationValue)) {
            return true;
        } else {
            constructValidationMessage(context);
            return false;
        }
    }

    /**
     * Assert source value and destination value are able to {@code Comparable#compareTo()}.
     * @param sourceValue comparison source
     * @param destinationValue comparison destination
     * @return {@code true} if source value is {@code Comparable}, and destionation value is able to cast to source value.
     *         otherwise {@code false}.
     * @throws IllegalArgumentException type of {@code sourceValue} is not {@code Comparable}.
     */
    private boolean assertComparable(Object sourceValue, Object destinationValue) {
        if (!(sourceValue instanceof Comparable)) {
            throw reportUnexpectedType(sourceValue);
        }

        return sourceValue.getClass().isAssignableFrom(
                destinationValue.getClass());
    }

    /**
     * Compare objects.
     * @param sourceValue comparison source
     * @param destinationValue comparison destination
     * @return {@code true} if result to comparing {@code source} and {@code destination} is expected {@code operator}.
     *         otherwise {@code false}.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean isCompareValid(Object sourceValue, Object destinationValue) {
        int result = ((Comparable) sourceValue).compareTo(destinationValue);

        for (int operatorValue : operator.value()) {
            if (result == operatorValue) {
                return true;
            }
        }
        return false;
    }

    /**
     * Construct validation message.
     * @param context constraint validation context
     */
    private void constructValidationMessage(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(
                source).addConstraintViolation();
    }
}
