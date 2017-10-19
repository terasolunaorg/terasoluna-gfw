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
package org.terasoluna.gfw.common.codelist.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;
import org.terasoluna.gfw.common.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Logger;

public class AbstractExistInCodeListValidatorTest {

    @Test
    public <T> void testIsValidIsTraceEnabledFalse() throws Exception {

        Logger logger = (Logger) LoggerFactory.getLogger(
                AbstractExistInCodeListValidator.class);

        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        ConstraintValidatorContext constraintValidatorContext = mock(
                ConstraintValidatorContext.class);

        ExistInCodeListValidator<String> existInCodeListValidator = new ExistInCodeListValidator<String>();
        ApplicationContext context = new FileSystemXmlApplicationContext("src/test/resources/org/terasoluna/gfw/common/codelist/ExistInCodeListTest-context.xml");
        existInCodeListValidator.setApplicationContext(context);
        existInCodeListValidator.initialize(new GenderCodeList());

        // test
        boolean isValid = existInCodeListValidator.isValid("Male",
                constraintValidatorContext);

        // assert
        assertThat(isValid, is(true));
        assertThat(logger.isTraceEnabled(), is(false));

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

}

class ExistInCodeListValidator<T> extends AbstractExistInCodeListValidator<T> {

    @Override
    protected String getCode(T value) {
        return "M";
    }

}

class GenderCodeList implements ExistInCodeList {

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }

    @Override
    public String codeListId() {
        return "CD_GENDER";
    }

    @Override
    public Class<?>[] groups() {
        return null;
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return null;
    }

}
