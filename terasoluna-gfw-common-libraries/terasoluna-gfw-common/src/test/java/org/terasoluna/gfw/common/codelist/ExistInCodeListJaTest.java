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
package org.terasoluna.gfw.common.codelist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("ExistInCodeListTest-context.xml")
public class ExistInCodeListJaTest {

    @Inject
    Validator validator;

    public ExistInCodeListJaTest() {
        Locale.setDefault(Locale.JAPANESE);
    }

    @Before
    public void setUp() throws Exception {
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_messageJapanese() {
        Customer c = new Customer();
        c.gender = 'G';
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.size(), is(1));
        assertThat(((ConstraintViolation<Customer>) result.toArray()[0])
                .getMessage(), is("CD_GENDER にありません"));
    }

    private class Customer {
        @ExistInCodeList(codeListId = "CD_GENDER")
        public Character gender;

        @ExistInCodeList(codeListId = "CD_LANG")
        public String lang;
    }
}
