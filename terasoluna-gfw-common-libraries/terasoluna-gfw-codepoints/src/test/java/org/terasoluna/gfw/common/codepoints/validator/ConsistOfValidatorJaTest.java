/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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
package org.terasoluna.gfw.common.codepoints.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsistOfValidatorJaTest {

    private Validator validator;

    private Locale originalLocale;

    public ConsistOfValidatorJaTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Before
    public void before() {
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.JAPANESE);
    }

    @After
    public void after() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testIsValid_message_japanese() throws Exception {
        Name_Simple name = new Name_Simple("abc", "GHI");

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, containsInAnyOrder( //
                allOf( //
                        hasProperty("propertyPath", hasToString("firstName")), //
                        hasProperty("message", is("指定されたコードポイントで構成されていません")))));
    }

}
