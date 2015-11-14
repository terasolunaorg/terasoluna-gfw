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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Utility methods for constraint validators and internal validators.
 * @since 5.1.0
 */
class ConstraintValidatorsUtils {

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
     * @return {@code true} if string is null or length is {@code 0}. otherwise {@code false}.
     */
    static boolean isEmpty(String value) {
        return (value == null || value.length() == 0);
    }

    /**
     * Get property in object by name.
     * @param value parent object
     * @param propertyName property name
     * @return property object. if failed to get, return null.
     */
    static Object getProperty(Object value, String propertyName) {
        if (value == null || isEmpty(propertyName)) {
            return null;
        }

        try {
            PropertyDescriptor property = new PropertyDescriptor(propertyName, value
                    .getClass());
            Method getter = property.getReadMethod();
            return getter.invoke(value, (Object[]) null);
        } catch (Exception e) {
            throw reportFailedToInitialize(e);
        }
    }

    /**
     * Check the class is present by name.
     * @param className class name
     * @return {@code true} if the class is present. otherwise {@code false}.
     */
    static boolean isClassPresent(String className) {
        if (isEmpty(className)) {
            return false;
        }

        try {
            forName(className, getDefaultClassLoader());
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Get class loader.
     * @return class loader
     */
    private static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable e) {
            // cannot access context class loader for current thread.
        }
        if (classLoader == null) {
            try {
                classLoader = ConstraintValidatorsUtils.class.getClassLoader();
            } catch (Throwable e) {
                // cannot access this class load for this class.
            }
            if (classLoader == null) {
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable e) {
                    // cannot access system class loader.
                }
            }
        }
        return classLoader;
    }

    /**
     * Get the class from class loader.
     * @param className class name
     * @param classLoader class loader
     * @return acquired class
     * @throws ClassNotFoundException class is not exist in class loader
     */
    private static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {

        return classLoader != null ? classLoader.loadClass(className) : Class
                .forName(className);
    }

}
