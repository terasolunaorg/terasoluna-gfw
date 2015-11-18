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

import org.terasoluna.gfw.common.validator.constraintvalidators.CompareValidator;

/**
 * The annotated element, the result of comparing the two properties specified, must match the specified operator. Operator can
 * be specified to {@link Operator}
 * <p>
 * Supported specified property types are:
 * </p>
 * <ul>
 * <li>{@code Comparable}</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid. If any of the specified two properties is {@code null}, are considered valid. If
 * specify two properties of different types, are considered invalid. If specify a property not {@link Comparable}, it is thrown
 * {@link IllegalArgumentException}(wrapped in {@link ValidationException}).
 * </p>
 * @since 5.1.0
 * @see CompareValidator
 * @see Operator
 */
@Documented
@Constraint(validatedBy = { CompareValidator.class })
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Compare {

    String message() default "{org.terasoluna.gfw.common.validator.constraints.Compare.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return property name of comparison source
     */
    String source();

    /**
     * @return property name of comparison destination
     */
    String destination();

    /**
     * @return operator used in the comparison
     */
    Operator operator();

    /**
     * @return property path of bind validation message
     */
    Path path() default Path.SOURCE;

    /**
     * Defines several {@link Compare} annotations on the same element.
     * @see Compare
     * @since 5.1.0
     */
    @Documented
    @Target({ METHOD, FIELD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @interface List {
        Compare[] value();
    }

    /**
     * The operator used in the {@link Compare} comparison.
     * @since 5.1.0
     */
    enum Operator {

        /**
         * Source must be less than or equal destination.
         */
        LESS_THAN_OR_EQUAL(CompareStrategy.EQ, CompareStrategy.LT),

        /**
         * Source must be less than destination.
         */
        LESS_THAN(CompareStrategy.LT),

        /**
         * Source must be equal destination.
         */
        EQUAL(CompareStrategy.EQ),

        /**
         * Source must be grater than destination.
         */
        GRATER_THAN(CompareStrategy.GT),

        /**
         * Source must be grater than or equal destination.
         */
        GRATER_THAN_OR_EQUAL(CompareStrategy.EQ, CompareStrategy.GT);

        /**
         * strategies to assert result of {@code Comparable#compareTo(Object)} as the expected.
         */
        private final CompareStrategy[] strategies;

        /**
         * Constructor.
         * @param strategies to assert result of {@code Comparable#compareTo(Object)} as the expected
         */
        private Operator(CompareStrategy... strategies) {
            this.strategies = strategies;
        }

        /**
         * Assert result of {@code Comparable#compareTo(Object)} as the expected.
         * @param value result of {@code Comparable#compareTo(Object)}
         * @return {@code true} if result of {@code Comparable#compareTo(Object)} as the expected in any of the
         *         {@code CompareStrategy#isExpected(int)}, otherwise {@code false}.
         */
        public boolean isExpected(int value) {
            for (CompareStrategy strategy : strategies) {
                if (strategy.isExpected(value)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Strategy to assert result of {@code Comparable#compareTo(Object)} as the expected.
         * @since 5.1.0
         */
        private enum CompareStrategy {

            /**
             * Expected equals ZERO.
             */
            EQ {
                /**
                 * {@inheritDoc}
                 */
                @Override
                protected boolean isExpected(int value) {
                    return value == 0;
                }
            },

            /**
             * Expected grater than ZERO.
             */
            GT {
                /**
                 * {@inheritDoc}
                 */
                @Override
                protected boolean isExpected(int value) {
                    return value > 0;
                }
            },

            /**
             * Expected less than ZERO.
             */
            LT {
                /**
                 * {@inheritDoc}
                 */
                @Override
                protected boolean isExpected(int value) {
                    return value < 0;
                }
            };

            /**
             * Assert result of {@code Comparable#compareTo(Object)} as the expected.
             * @param value result of {@code Comparable#compareTo(Object)}
             * @return {@code true} if result of {@code Comparable#compareTo(Object)} as the expected, otherwise {@code false}.
             */
            protected abstract boolean isExpected(int value);
        }
    }

    /**
     * The property path of bind validation message.
     * @since 5.1.0
     */
    enum Path {

        /**
         * Bind validation message to property specified {@code Compare#source()}.
         */
        SOURCE,

        /**
         * Bind validation message to property specified {@code Compare#destination()}.
         */
        DESTINATION,

        /**
         * Bind validation message to root bean {@code Compare} annotated.
         */
        ROOT_BEAN
    }
}
