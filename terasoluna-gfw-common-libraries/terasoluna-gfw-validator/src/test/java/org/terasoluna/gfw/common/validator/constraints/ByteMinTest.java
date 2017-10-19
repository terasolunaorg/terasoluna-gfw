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
package org.terasoluna.gfw.common.validator.constraints;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.UnsupportedCharsetException;

import javax.validation.UnexpectedTypeException;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.ByteMinTest.ByteMinTestForm;

/**
 * Test class of {@link ByteMin}
 */
public class ByteMinTest extends AbstractConstraintsTest<ByteMinTestForm> {

    private static final String MESSAGE_VALIDATION_ERROR = "must be greater than or equal to %d bytes";

    @Before
    public void before() {
        form = new ByteMinTestForm();
    }

    /**
     * input null value. expected valid.
     * @throws Throwable
     */
    @Test
    public void testInputNull() throws Throwable {

        violations = validator.validate(form);
        assertThat(violations.size(), is(0));
    }

    /**
     * specify min value. expected valid if input value encoded in UTF-8 is grater than or equal min value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyMinValue() throws Throwable {

        {
            form.setStringProperty("あaa");

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, 6)));
        }

        {
            form.setStringProperty("ああ");

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify min value for StringBuilder(CharSequence).
     * @throws Throwable
     */
    @Test
    public void testSpecifyMinValueForStringBuilder() throws Throwable {

        {
            form.setStringBuilderProperty(new StringBuilder("あaa"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, 6)));
        }

        {
            form.setStringBuilderProperty(new StringBuilder("ああ"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify charset. expected valid if input value encoded in specified charset is grater than or equal min value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyCharset() throws Throwable {

        {
            form.setStringProperty("ああa");

            violations = validator.validate(form, SpecifyCharset.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, 6)));
        }

        {
            form.setStringProperty("あああ");

            violations = validator.validate(form, SpecifyCharset.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify illegal charset. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message is
     * {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalCharset() throws Throwable {
        setExpectedFailedToInitialize(UnsupportedCharsetException.class);

        validator.validate(form, IllegalCharset.class);
    }

    /**
     * specify not support type. expected {@code UnexpectedTypeException}
     * @throws Throwable
     */
    @Test
    public void testAnnotateUnexpectedType() throws Throwable {
        thrown.expect(UnexpectedTypeException.class);

        validator.validate(form, UnexpectedType.class);
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
     * Validation group unexpected type.
     */
    private static interface UnexpectedType {
    };

    public class ByteMinTestForm {
        @ByteMin.List({ @ByteMin(6),
                @ByteMin(value = 6, charset = "shift-jis", groups = {
                        SpecifyCharset.class }),
                @ByteMin(value = 6, charset = "illegal-charset", groups = {
                        IllegalCharset.class }) })
        private String stringProperty;

        @ByteMin(6)
        private StringBuilder stringBuilderProperty;

        @ByteMin(value = 6, groups = { UnexpectedType.class })
        private Integer intProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public StringBuilder getStringBuilderProperty() {
            return stringBuilderProperty;
        }

        public void setStringBuilderProperty(
                StringBuilder stringBuilderProperty) {
            this.stringBuilderProperty = stringBuilderProperty;
        }

        public Integer getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(Integer intProperty) {
            this.intProperty = intProperty;
        }
    }
}
