/*
 * Copyright(c) 2013 NTT DATA Corporation.
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;

/**
 * Abstract Test class of {@code org.terasoluna.gfw.common.validator.constraints}
 */
abstract public class AbstractConstraintsTest<F> {

    protected static final String MESSAGE_NOTSUPPORT_ERROR = "validator does not support this type: %s";

    protected static final String MESSAGE_INITIALIZE_ERROR = "failed to initialize validator by invalid argument";

    protected static Validator validator;

    protected F form;

    protected Set<ConstraintViolation<F>> violations;

    @BeforeClass
    public static void beforeClass() {
        Locale.setDefault(Locale.ENGLISH);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * assertion for failed to initialize.
     * @param ex validation exception.
     * @param causeType expected type of inner exception.
     */
    protected void assertFailedToInitialize(ValidationException ex,
            Class<? extends Throwable> causeType) {
        assertThat(ex.getCause(), allOf(Matchers.<Throwable> instanceOf(
                IllegalArgumentException.class), hasProperty("message", is(
                        MESSAGE_INITIALIZE_ERROR)), hasProperty("cause",
                                Matchers.<Throwable> instanceOf(causeType))));
    }

    /**
     * assertion for failed to initialize.
     * @param ex validation exception.
     * @param causeType expected type of inner exception.
     * @param message expected message of inner exception.
     */
    protected void assertFailedToInitialize(ValidationException ex,
            Class<? extends Throwable> causeType, String message) {
        assertThat(ex.getCause(), allOf(Matchers.<Throwable> instanceOf(
                IllegalArgumentException.class), hasProperty("message", is(
                        MESSAGE_INITIALIZE_ERROR)), hasProperty("cause", allOf(
                                Matchers.<Throwable> instanceOf(causeType),
                                hasProperty("message", is(message))))));
    }

    /**
     * assertion for type not support.
     * @param ex validation exception.
     * @param type expected not support type.
     */
    protected void assertTypeNotSupport(ValidationException ex, Class<?> type) {
        assertThat(ex.getCause(), allOf(Matchers.<Throwable> instanceOf(
                IllegalArgumentException.class), hasProperty("message", is(
                        String.format(MESSAGE_NOTSUPPORT_ERROR, type
                                .getName())))));
    }

}
