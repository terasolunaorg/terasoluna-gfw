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
package org.terasoluna.gfw.common.validator.constraints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.ByteMaxTest.ByteMaxTestForm;
import org.terasoluna.gfw.common.validator.constraints.ByteMinTest.ByteMinTestForm;
import org.terasoluna.gfw.common.validator.constraints.ByteSizeTest.ByteSizeTestForm;
import org.terasoluna.gfw.common.validator.constraints.CompareTest.CompareTestForm;

/**
 * Test class of {@code ContributorValidationMessages_ja.properties}
 */
public class ContributorValidationMessagesJaTest {

    private static Validator validator;

    private static Locale originalLocale;

    @BeforeClass
    public static void beforeClass() {
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.JAPANESE);

        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterClass
    public static void afterClass() {
        Locale.setDefault(originalLocale);
    }

    /**
     * expect {@link ByteMin} japanese message.
     */
    @Test
    public void testMessageByteMin() {

        ByteMinTest.ByteMinTestForm form = new ByteMinTest.ByteMinTestForm();
        form.setStringProperty("あaa");

        Set<ConstraintViolation<ByteMinTestForm>> violations = validator.validate(form);
        assertThat(violations, containsInAnyOrder(
                hasProperty("message", is(String.format("%d バイト以上のサイズにしてください", 6)))));
    }

    /**
     * expect {@link ByteMax} japanese message.
     */
    @Test
    public void testMessageByteMax() {

        ByteMaxTest.ByteMaxTestForm form = new ByteMaxTest.ByteMaxTestForm();
        form.setStringProperty("ああa");

        Set<ConstraintViolation<ByteMaxTestForm>> violations = validator.validate(form);
        assertThat(violations, containsInAnyOrder(
                hasProperty("message", is(String.format("%d バイト以下のサイズにしてください", 6)))));
    }

    /**
     * expect {@link ByteSize} japanese message.
     */
    @Test
    public void testMessageByteSize() {

        ByteSizeTest.ByteSizeTestForm form = new ByteSizeTest.ByteSizeTestForm();
        form.setStringProperty("aa");

        Set<ConstraintViolation<ByteSizeTestForm>> violations = validator.validate(form);
        assertThat(violations, containsInAnyOrder(
                hasProperty("message", is(String.format("%d から %d バイトの間のサイズにしてください", 3, 6)))));
    }

    /**
     * expect {@link Compare} japanese message.
     */
    @Test
    public void testMessageCompare() {

        CompareTest.CompareTestForm form = new CompareTest.CompareTestForm();
        form.setLeft(100);
        form.setRight(99);

        Set<ConstraintViolation<CompareTestForm>> violations = validator.validate(form);
        assertThat(violations, containsInAnyOrder(hasProperty("message",
                is(String.format("正しくない %s と %s の組合せです", "left", "right")))));
    }

}
