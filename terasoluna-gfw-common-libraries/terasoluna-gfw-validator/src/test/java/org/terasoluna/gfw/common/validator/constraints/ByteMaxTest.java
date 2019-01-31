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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.ByteMaxTest.ByteMaxTestForm;

/**
 * Test class of {@link ByteMax}
 */
public class ByteMaxTest extends AbstractConstraintsTest<ByteMaxTestForm> {

    private static final String MESSAGE_VALIDATION_ERROR = "must be less than or equal to %d bytes";

    @Before
    public void before() {
        form = new ByteMaxTestForm();
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
     * specify max value. expected valid if input value encoded in UTF-8 is grater than or equal max value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyMaxValue() throws Throwable {

        {
            form.setStringProperty("ああa");

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
     * specify max value for StringBuilder(CharSequence).
     */
    @Test
    public void testSpecifyMaxValueForStringBuilder() throws Throwable {

        {
            form.setStringBuilderProperty(new StringBuilder("ああa"));

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
     * specify charset. expected valid if input value encoded in specified charset is grater than or equal max value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyCharset() throws Throwable {

        {
            form.setStringProperty("あああa");

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
     * all values in the collection are valid.
     * @throws Throwable
     */
    @Test
    public void testCollectionValid() throws Throwable {
        form.setListProperty(Arrays.asList("ああ", "ああ"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMaxTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    /**
     * first value in the collection is invalid.
     * @throws Throwable
     */
    @Test
    public void testCollectionFirstInvalid() throws Throwable {
        form.setListProperty(Arrays.asList("ああa", "ああ"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMaxTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<ByteMaxTestForm> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is(
                "listProperty[0].<list element>"));
        assertThat(v.getMessage(), is(String.format(MESSAGE_VALIDATION_ERROR,
                6)));
    }

    /**
     * last value in the collection is invalid.
     * @throws Throwable
     */
    @Test
    public void testCollectionLastInvalid() throws Throwable {
        form.setListProperty(Arrays.asList("ああ", "ああa"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMaxTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<ByteMaxTestForm> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is(
                "listProperty[1].<list element>"));
        assertThat(v.getMessage(), is(String.format(MESSAGE_VALIDATION_ERROR,
                6)));
    }

    /**
     * all values in the collection are invalid.
     * @throws Throwable
     */
    @Test
    public void testCollectionAllInvalid() throws Throwable {
        form.setListProperty(Arrays.asList("ああa", "ああa"));
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<ByteMaxTestForm>> violations = validator
                .validate(form);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(2));

        Iterator<ConstraintViolation<ByteMaxTestForm>> iterator = violations
                .stream().sorted(comparing(v -> v.getPropertyPath().toString()))
                .iterator();

        ConstraintViolation<ByteMaxTestForm> violation = iterator.next();
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

    public class ByteMaxTestForm {
        @ByteMax(6)
        @ByteMax(value = 6, charset = "shift-jis", groups = {
                SpecifyCharset.class })
        @ByteMax(value = 6, charset = "illegal-charset", groups = {
                IllegalCharset.class })
        private String stringProperty;

        @ByteMax(6)
        private StringBuilder stringBuilderProperty;

        @ByteMax(value = 6, groups = { UnexpectedType.class })
        private Integer intProperty;

        private List<@ByteMax(6) String> listProperty;

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
