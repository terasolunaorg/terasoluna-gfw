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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Utility methods for constraint validators and internal validators.
 * @since 5.1.0
 */
class ConstraintValidatorsUtils {

    /**
     * Default constructor
     */
    private ConstraintValidatorsUtils() {
        // do nothing for Default constructor
    }

    /**
     * Wrap the exception in {@link IllegalArgumentException} with initialize error messages.
     * @param cause caused throwable
     * @return wrapped exception
     */
    static IllegalArgumentException reportFailedToInitialize(Throwable cause) {
        return new IllegalArgumentException("failed to initialize validator by invalid argument", cause);
    }

    /**
     * Wrap the exception in {@link IllegalArgumentException} with unexpected type messages.
     * @param value validation target value
     * @return wrapped exception
     */
    static IllegalArgumentException reportUnexpectedType(Object value) {
        return new IllegalArgumentException(String.format(
                "validator does not support this type: %s", value.getClass()
                        .getName()));
    }

    /**
     * Check string is null or empty.
     * @param value string to check
     * @return {@code true} if string is null or empty. otherwise {@code false}.
     */
    static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Get property value in bean by name.
     * @param bean bean which holds a specified property
     * @param propertyName property name
     * @return property value. if failed to get, return null.
     */
    static Object getPropertyValue(Object bean, String propertyName) {
        if (bean == null || isEmpty(propertyName)) {
            return null;
        }

        try {
            PropertyDescriptor property = new PropertyDescriptor(propertyName, bean
                    .getClass());
            Method getter = property.getReadMethod();
            return getter.invoke(bean);
        } catch (Exception e) {
            throw reportFailedToInitialize(e);
        }
    }
}
