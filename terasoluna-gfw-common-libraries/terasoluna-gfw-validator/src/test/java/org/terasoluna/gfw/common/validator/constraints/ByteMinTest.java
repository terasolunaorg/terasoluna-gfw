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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static java.util.Comparator.comparing;

import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validation;
import javax.validation.Validator;

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
     */
    @Test
    public void testInputNull() {

        violations = validator.validate(form);
        assertThat(violations.size(), is(0));
    }

    /**
     * specify min value. expected valid if input value encoded in UTF-8 is grater than or equal min value.
     */
    @Test
    public void testSpecifyMinValue() {

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
     */
    @Test
    public void testSpecifyMinValueForStringBuilder() {

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
     */
    @Test
    public void testSpecifyCharset() {

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
     */
    @Test
    public void testSpecifyIllegalCharset() {
        setExpectedFailedToInitialize(UnsupportedCharsetException.class);

        validator.validate(form, IllegalCharset.class);
    }

    /**
     * specify not support type. expected {@code UnexpectedTypeException}
     */
    @Test
    public void testAnnotateUnexpectedType() {
        thrown.expect(UnexpectedTypeException.class);

        validator.validate(form, UnexpectedType.class);
    }

    /**
     * all values in the collection are valid.
     */
    @Test
    public void testCollectionValid() {
        form.setListProperty(Arrays.asList("ああ", "ああ"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMinTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    /**
     * first value in the collection is invalid.
     */
    @Test
    public void testCollectionFirstInvalid() {
        form.setListProperty(Arrays.asList("あaa", "ああ"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMinTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<ByteMinTestForm> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is(
                "listProperty[0].<list element>"));
        assertThat(v.getMessage(), is(String.format(MESSAGE_VALIDATION_ERROR,
                6)));
    }

    /**
     * last value in the collection is invalid.
     */
    @Test
    public void testCollectionLastInvalid() {
        form.setListProperty(Arrays.asList("ああ", "あaa"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMinTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<ByteMinTestForm> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is(
                "listProperty[1].<list element>"));
        assertThat(v.getMessage(), is(String.format(MESSAGE_VALIDATION_ERROR,
                6)));
    }

    /**
     * all values in the collection are invalid.
     */
    @Test
    public void testCollectionAllInvalid() {
        form.setListProperty(Arrays.asList("あaa", "あaa"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMinTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(2));

        Iterator<ConstraintViolation<ByteMinTestForm>> iterator = violations
                .stream().sorted(comparing(v -> v.getPropertyPath().toString()))
                .iterator();

        ConstraintViolation<ByteMinTestForm> violation = iterator.next();
        assertThat(violation.getPropertyPath().toString(), is(
                "listProperty[0].<list element>"));
        assertThat(violation.getMessage(), is(String.format(
                MESSAGE_VALIDATION_ERROR, 6)));
        violation = iterator.next();
        assertThat(violation.getPropertyPath().toString(), is(
                "listProperty[1].<list element>"));
        assertThat(violation.getMessage(), is(String.format(
                MESSAGE_VALIDATION_ERROR, 6)));
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
        @ByteMin(6)
        @ByteMin(value = 6, charset = "shift-jis", groups = {
                SpecifyCharset.class })
        @ByteMin(value = 6, charset = "illegal-charset", groups = {
                IllegalCharset.class })
        private String stringProperty;

        @ByteMin(6)
        private StringBuilder stringBuilderProperty;

        @ByteMin(value = 6, groups = { UnexpectedType.class })
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

        public List<String> getListProperty() {
            return listProperty;
        }

        public void setListProperty(List<String> listProperty) {
            this.listProperty = listProperty;
        }
    }
}
