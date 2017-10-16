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
package org.terasoluna.gfw.common.validator.constraints;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Abstract Test class of {@code org.terasoluna.gfw.common.validator.constraints}
 */
abstract public class AbstractConstraintsTest<F> {

    protected static final String MESSAGE_NOTSUPPORT_ERROR = "validator does not support this type: %s";

    protected static final String MESSAGE_INITIALIZE_ERROR = "failed to initialize validator by invalid argument";

    protected static Validator validator;

    protected F form;

    protected Set<ConstraintViolation<F>> violations;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * set {@code ExpectedException} for failed to initialize.
     * @param cls expected inner exception.
     */
    protected void setExpectedFailedToInitialize(Class<?> cls) {
        thrown.expect(ValidationException.class);
        thrown.expectCause(allOf(Matchers.<Throwable> instanceOf(
                IllegalArgumentException.class), hasProperty("message", is(
                        MESSAGE_INITIALIZE_ERROR)), hasProperty("cause",
                                Matchers.<Throwable> instanceOf(cls))));
    }

    /**
     * set {@code ExpectedException} for type not support.
     * @param cls expected not support type.
     */
    protected void setExpectedTypeNotSupport(Class<?> cls) {
        thrown.expect(ValidationException.class);
        thrown.expectCause(allOf(Matchers.<Throwable> instanceOf(
                IllegalArgumentException.class), hasProperty("message", is(
                        String.format(MESSAGE_NOTSUPPORT_ERROR, cls
                                .getName())))));
    }

}
