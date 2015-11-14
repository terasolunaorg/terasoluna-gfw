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

/**
 * Abstract class of internal validator.
 * @param <T> validate target type
 * @since 5.1.0
 */
abstract class InternalValidator<T> {

    /**
     * Check object type is validation target.
     * @param value object to validate
     * @return {@code true} if object type is validator's target. otherwise {@code false}
     */
    abstract boolean isGenericType(Object value);

    /**
     * Validate object value is after(not allow equal) the specified date time.
     * @param value object to validate
     * @param date compare destination date time
     * @param format format string used in parse to a date time
     * @return {@code true} if {@code value} is after(not allow equal) the specified date time. otherwise {@code false}.
     */
    abstract boolean isAfter(T value, String date, String format);

    /**
     * Validate object value is before(not allow equal) the specified date time.
     * @param value object to validate
     * @param date compare destination date time
     * @param format format string used in parse to a date time
     * @return {@code true} if {@code value} is before(not allow equal) the specified date time. otherwise {@code false}.
     */
    abstract boolean isBefore(T value, String date, String format);
}
