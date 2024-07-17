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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;

import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.ByteMinTest.ByteMinTestForm;

/**
 * Test class of {@link ByteMin}
 */
public class ByteMinTest extends AbstractConstraintsTest<ByteMinTestForm> {

    private static final String MESSAGE_VALIDATION_ERROR =
            "must be greater than or equal to %d bytes";

    @Before
    public void before() {
        form = new ByteMinTestForm();
    }

    /**
     * input null value. expected valid.
     */
    @Test
    public void testInputNull() {

        violations = validator.validate(form);
        assertThat(violations, is(empty()));
    }

    /**
     * specify min value. expected valid if input value encoded in UTF-8 is grater than or equal min
     * value.
     */
    @Test
    public void testSpecifyMinValue() {

        {
            form.setStringProperty("あaa");

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder(
                    hasProperty("message", is(String.format(MESSAGE_VALIDATION_ERROR, 6)))));
        }

        {
            form.setStringProperty("ああ");

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify min value for StringBuilder(CharSequence).
     */
    @Test
    public void testSpecifyMinValueForStringBuilder() {

        {
            form.setStringBuilderProperty(new StringBuilder("あaa"));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder(
                    hasProperty("message", is(String.format(MESSAGE_VALIDATION_ERROR, 6)))));
        }

        {
            form.setStringBuilderProperty(new StringBuilder("ああ"));

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify charset. expected valid if input value encoded in specified charset is grater than or
     * equal min value.
     */
    @Test
    public void testSpecifyCharset() {

        {
            form.setStringProperty("ああa");

            violations = validator.validate(form, SpecifyCharset.class);
            assertThat(violations, containsInAnyOrder(
                    hasProperty("message", is(String.format(MESSAGE_VALIDATION_ERROR, 6)))));
        }

        {
            form.setStringProperty("あああ");

            violations = validator.validate(form, SpecifyCharset.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify illegal charset. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is
     * {@code failed to initialize validator by invalid argument}.
     */
    @Test
    public void testSpecifyIllegalCharset() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(form, IllegalCharset.class));
        assertFailedToInitialize(ex, UnsupportedCharsetException.class);
    }

    /**
     * specify negative value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is
     * {@code failed to initialize validator by invalid argument} and nested by
     * {@code IllegalArgumentException} that message is
     * {@code value[-1] must not be negative value.}.
     */
    @Test
    public void testSpecifyNegativeValue() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(form, NegativeValue.class));
        assertFailedToInitialize(ex, IllegalArgumentException.class,
                "value[-1] must not be negative value.");
    }

    /**
     * specify not support type. expected {@code UnexpectedTypeException}
     */
    @Test
    public void testAnnotateUnexpectedType() {
        assertThrows(UnexpectedTypeException.class,
                () -> validator.validate(form, UnexpectedType.class));
    }

    /**
     * validate collection element values.
     */
    @Test
    public void testElementTypeTypeUse() {

        {
            form.setListProperty(Arrays.asList("ああ", "ああ"));

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }

        {
            form.setListProperty(Arrays.asList("あaa", "ああ"));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath",
                                    hasToString("listProperty[0].<list element>")), //
                            hasProperty("message",
                                    is(String.format(MESSAGE_VALIDATION_ERROR, 6))))));
        }

        {
            form.setListProperty(Arrays.asList("ああ", "あaa"));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath",
                                    hasToString("listProperty[1].<list element>")), //
                            hasProperty("message",
                                    is(String.format(MESSAGE_VALIDATION_ERROR, 6))))));
        }

        {
            form.setListProperty(Arrays.asList("あaa", "あaa"));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath",
                                    hasToString("listProperty[0].<list element>")), //
                            hasProperty("message", is(String.format(MESSAGE_VALIDATION_ERROR, 6)))), //
                    allOf( //
                            hasProperty("propertyPath",
                                    hasToString("listProperty[1].<list element>")), //
                            hasProperty("message",
                                    is(String.format(MESSAGE_VALIDATION_ERROR, 6))))));
        }
    }

    /**
     * Validation group encoding shift-jis.
     */
    private static interface SpecifyCharset {
    };

    /**
     * Validation group encoding unsupported.
     */
    private static interface IllegalCharset {
    };

    /**
     * Validation group value negative.
     */
    private static interface NegativeValue {
    };

    /**
     * Validation group unexpected type.
     */
    private static interface UnexpectedType {
    };

    public static class ByteMinTestForm {
        @ByteMin(6)
        @ByteMin(value = 6, charset = "shift-jis", groups = {SpecifyCharset.class})
        @ByteMin(value = 6, charset = "illegal-charset", groups = {IllegalCharset.class})
        @ByteMin(value = -1, groups = {NegativeValue.class})
        private String stringProperty;

        @ByteMin(6)
        private StringBuilder stringBuilderProperty;

        @ByteMin(value = 6, groups = {UnexpectedType.class})
        private Integer intProperty;

        private List<@ByteMin(6) String> listProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public StringBuilder getStringBuilderProperty() {
            return stringBuilderProperty;
        }

        public void setStringBuilderProperty(StringBuilder stringBuilderProperty) {
            this.stringBuilderProperty = stringBuilderProperty;
        }

        public Integer getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(Integer intProperty) {
            this.intProperty = intProperty;
        }

        public List<String> getListProperty() {
            return listProperty;
        }

        public void setListProperty(List<String> listProperty) {
            this.listProperty = listProperty;
        }
    }
}
