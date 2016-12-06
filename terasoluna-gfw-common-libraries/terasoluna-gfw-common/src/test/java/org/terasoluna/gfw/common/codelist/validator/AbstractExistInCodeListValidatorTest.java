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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import javax.validation.ConstraintValidatorContext;

import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.common.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Logger;

public class AbstractExistInCodeListValidatorTest {

    @Test
    public <T> void testIsValidIsTraceEnabledFalse() throws Exception {

        Logger logger = (Logger) LoggerFactory
                .getLogger(AbstractExistInCodeListValidator.class);

        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        T object = (T) mock(Object.class);
        ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);
        ExistInCodeListValidator<T> existInCodeListValidator = new ExistInCodeListValidator<T>();

        existInCodeListValidator.isValid(object, constraintValidatorContext);

        // assert
        assertThat(logger.isTraceEnabled(), is(false));

        // init log level
        LogLevelChangeUtil.clearProperty();
    }

}

class ExistInCodeListValidator<T> extends AbstractExistInCodeListValidator<T> {

    @Override
    protected String getCode(T value) {
        return "code";
    }

}