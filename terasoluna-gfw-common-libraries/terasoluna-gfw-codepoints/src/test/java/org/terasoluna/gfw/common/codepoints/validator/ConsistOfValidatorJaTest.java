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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ConsistOfValidatorJaTest {

    private Validator validator;

    private Locale originalLocale;

    public ConsistOfValidatorJaTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void before() {
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.JAPANESE);
    }

    @AfterEach
    public void after() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testIsValid_message_japanese() throws Exception {
        Name_Simple name = new Name_Simple("abc", "GHI");

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(name);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("firstName", "指定されたコードポイントで構成されていません"));
    }

}
