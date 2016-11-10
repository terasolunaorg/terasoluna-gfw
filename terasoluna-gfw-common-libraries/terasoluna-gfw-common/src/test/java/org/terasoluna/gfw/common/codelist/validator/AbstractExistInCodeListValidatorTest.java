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

import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Context;

public class AbstractExistInCodeListValidatorTest {

    private final String LOGBACK_UNIT_TEST_FILE_PATH = "src/test/resources/logback-unittest.xml";

    private final String LOGBACK_DEFAULT_FILE_PATH = "src/test/resources/logback.xml";

    private String logbackUnitTestFilePath = LOGBACK_UNIT_TEST_FILE_PATH;

    private String logbackDefaultFilePath = LOGBACK_DEFAULT_FILE_PATH;

    private Logger logger;

    private Context context;

    private JoranConfigurator configurator;

    @Before
    public void setUp() throws Exception {

        logger = (Logger) LoggerFactory.getLogger(AbstractExistInCodeListValidator.class);
        context = logger.getLoggerContext();
        configurator = new JoranConfigurator();
    }

    @Test
    public <T> void testIsValidIsTraceEnabled() throws Exception {
        //Change in the logback setting file
        configurator.setContext(context);
        ((LoggerContext) context).reset();
        configurator.doConfigure(logbackUnitTestFilePath);

        // set up
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

        //Change in the logback setting file
        ((LoggerContext) context).reset();
        configurator.doConfigure(logbackDefaultFilePath);
    }

}

class ExistInCodeListValidator<T> extends AbstractExistInCodeListValidator<T> {

    @Override
    protected String getCode(T value) {
        return "code";
    }

}