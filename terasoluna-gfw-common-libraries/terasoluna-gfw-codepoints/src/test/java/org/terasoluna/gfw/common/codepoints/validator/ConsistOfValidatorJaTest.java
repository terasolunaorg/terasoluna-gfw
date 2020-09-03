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
package org.terasoluna.gfw.common.codepoints.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Test;

public class ConsistOfValidatorJaTest {

    public ConsistOfValidatorJaTest() {
        Locale.setDefault(Locale.JAPANESE);
    }

    @Test
    public void testIsValid_message_japanese() throws Exception {
        Name_Simple name = new Name_Simple("abc", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Simple> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("firstName"));
        assertThat(v.getMessage(), is("指定されたコードポイントで構成されていません"));
    }

}
