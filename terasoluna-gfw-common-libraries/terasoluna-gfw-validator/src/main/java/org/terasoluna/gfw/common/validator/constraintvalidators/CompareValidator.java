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

import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.getPropertyValue;
import static org.terasoluna.gfw.common.validator.constraintvalidators.ConstraintValidatorsUtils.reportUnexpectedType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.terasoluna.gfw.common.validator.constraints.Compare;
import org.terasoluna.gfw.common.validator.constraints.Compare.Node;
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
     * Name of property to become left side of comparison.
     */
    private String left;

    /**
     * Name of property to become right side of comparison.
     */
    private String right;

    /**
     * Operator used in the comparison.
     */
    private Operator operator;

    /**
     * Configure how to treat {@code null} value in given parameters. If true, it is regarded as valid only when both left and
     * right are {@code null}. It is regarded as invalid when either is {@code null}. If false, both cases are regarded as
     * valid.
     */
    private boolean requireBoth;

    /**
     * Node of bind validation message
     */
    private Node node;

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
        left = constraintAnnotation.left();
        right = constraintAnnotation.right();
        operator = constraintAnnotation.operator();
        requireBoth = constraintAnnotation.requireBoth();
        node = constraintAnnotation.node();
        message = constraintAnnotation.message();
    }

    /**
     * Validate execute.
     * @param bean bean to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if result to comparing {@code left} and {@code right} is expected {@code operator}, or is match
     *         conditions that are described in the {@link CompareValidator#requireBoth} . otherwise {@code false}.
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        Object leftValue = getPropertyValue(bean, left);
        Object rightValue = getPropertyValue(bean, right);

        if (leftValue == null || rightValue == null) {
            if (requireBoth && !(leftValue == null && rightValue == null)) {
                constructValidationMessage(context);
                return false;
            }
            return true;
        }

        if (!assertComparable(leftValue, rightValue)) {
            constructValidationMessage(context);
            return false;
        }

        if (!isCompareValid(leftValue, rightValue)) {
            constructValidationMessage(context);
            return false;
        }

        return true;
    }

    /**
     * Assert left value and right value are able to {@code Comparable#compareTo()}.
     * @param leftValue value to become left side of comparison
     * @param rightValue value to become right side of comparison
     * @return {@code true} if left value is {@code Comparable}, and right value is able to cast to left value. otherwise
     *         {@code false}.
     * @throws IllegalArgumentException type of {@code leftValue} is not {@code Comparable}.
     */
    private boolean assertComparable(Object leftValue, Object rightValue) {
        if (!(leftValue instanceof Comparable)) {
            throw reportUnexpectedType(leftValue);
        }

        return leftValue.getClass().isAssignableFrom(rightValue.getClass());
    }

    /**
     * Compare objects by {@code Comparable#compareTo()}.
     * @param leftValue value to become left side of comparison
     * @param rightValue value to become right side of comparison
     * @return {@code true} if comparison result as the expected to specified {@code operator}, otherwise {@code false}.
     */
    private boolean isCompareValid(Object leftValue, Object rightValue) {
        @SuppressWarnings({ "rawtypes", "unchecked" })
        int result = ((Comparable) leftValue).compareTo(rightValue);
        return operator.isExpected(result);
    }

    /**
     * Construct validation message when selected {@link Node#PROPERTY}.
     * @param context constraint validation context
     */
    private void constructValidationMessage(
            ConstraintValidatorContext context) {
        if (node == Node.ROOT_BEAN) {
            return;
        }

        context.buildConstraintViolationWithTemplate(message).addPropertyNode(
                left).addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
