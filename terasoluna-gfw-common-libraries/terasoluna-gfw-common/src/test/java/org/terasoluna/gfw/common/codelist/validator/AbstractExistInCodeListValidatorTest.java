/*
 * Copyright (C) 2013-2016 NTT DATA Corporation
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
package org.terasoluna.gfw.common.codelist.validator;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import org.junit.Test;
import org.terasoluna.gfw.common.logback.ChangingLogbackFile;

public class AbstractExistInCodeListValidatorTest extends ChangingLogbackFile {

    @Test
    public <T> void testIsValidIsTraceEnabled() throws Exception {
        // set up
        setLogger(AbstractExistInCodeListValidator.class);
        before();

        T object = (T) mock(Object.class);
        ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);
        ExistInCodeListValidator<T> existInCodeListValidator = new ExistInCodeListValidator<T>();

        try {
            existInCodeListValidator.isValid(object, constraintValidatorContext);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }

        // assert
        assertFalse(logger.isTraceEnabled());

        after();
    }

}

class ExistInCodeListValidator<T> extends AbstractExistInCodeListValidator<T> {

    @Override
    protected String getCode(T value) {
        return "code";
    }

}