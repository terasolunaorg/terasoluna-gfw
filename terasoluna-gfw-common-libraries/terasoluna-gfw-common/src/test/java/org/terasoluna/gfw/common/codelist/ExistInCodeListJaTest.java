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
package org.terasoluna.gfw.common.codelist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringJUnitConfig(locations = "ExistInCodeListTest-context.xml")
public class ExistInCodeListJaTest {

    @Inject
    Validator validator;

    private Locale originalLocale;

    @BeforeEach
    public void before() {
        ((LocalValidatorFactoryBean) validator).afterPropertiesSet();
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.JAPANESE);
    }

    @AfterEach
    public void after() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void test_messageJapanese() {
        Customer c = new Customer();
        c.gender = 'G';
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result).containsExactlyInAnyOrder(hasProperty("message", is("CD_GENDER にありません")));
    }

    private class Customer {
        @ExistInCodeList(codeListId = "CD_GENDER")
        public Character gender;

        @ExistInCodeList(codeListId = "CD_LANG")
        public String lang;
    }
}
