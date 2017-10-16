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
package org.terasoluna.gfw.common.codepoints.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Test;

public class ConsistOfValidatorTest {

    @Test
    public void testIsValid_all_valid() throws Exception {
        Name_Simple name = new Name_Simple("ABC", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_all_null() throws Exception {
        Name_Simple name = new Name_Simple();
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_firstName_is_invalid() throws Exception {
        Name_Simple name = new Name_Simple("abc", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Simple> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("firstName"));
        assertThat(v.getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_lastName_is_invalid() throws Exception {
        Name_Simple name = new Name_Simple("ABC", "ghi");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Simple> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("lastName"));
        assertThat(v.getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_both_are_invalid() throws Exception {
        Name_Simple name = new Name_Simple("abc", "ghi");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(2));

        Iterator<ConstraintViolation<Name_Simple>> iterator = violations
                .iterator();
        List<ConstraintViolation<Name_Simple>> lst = new ArrayList<ConstraintViolation<Name_Simple>>(2);
        lst.add(iterator.next());
        lst.add(iterator.next());
        Collections.sort(lst,
                new Comparator<ConstraintViolation<Name_Simple>>() {
                    @Override
                    public int compare(ConstraintViolation<Name_Simple> o1,
                            ConstraintViolation<Name_Simple> o2) {
                        return o1.getPropertyPath().toString().compareTo(o2
                                .getPropertyPath().toString());
                    }
                });

        assertThat(lst.get(0).getPropertyPath().toString(), is("firstName"));
        assertThat(lst.get(0).getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
        assertThat(lst.get(1).getPropertyPath().toString(), is("lastName"));
        assertThat(lst.get(1).getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_multi_all_valid() throws Exception {
        Name_Multi name = new Name_Multi("ABCGHI", "DEFJKL");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Multi>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_multi_firstName_is_invalid() throws Exception {
        Name_Multi name = new Name_Multi("ABCDEFGHIJKLMN", "ABCDEFGHIJKL");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Multi>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));
    }

    @Test
    public void testIsValid_composite_all_valid() throws Exception {
        Name_Composite name = new Name_Composite("ABCGHI", "DEFJKL");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Composite>> violations = validator
                .validate(name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_composite_firstName_is_invalid() throws Exception {
        Name_Composite name = new Name_Composite("ABCDEFGHIJKLMN", "ABCDEFGHIJKL");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Composite>> violations = validator
                .validate(name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));
    }

    @Test
    public void testIsValid_getter_all_valid() throws Exception {
        Name_Getter name = new Name_Getter("ABC", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Getter>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_getter_firstName_is_invalid() throws Exception {
        Name_Getter name = new Name_Getter("abc", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Getter>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Getter> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("firstName"));
        assertThat(v.getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_getter_lastName_is_invalid() throws Exception {
        Name_Getter name = new Name_Getter("ABC", "ghi");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Getter>> violations = validator.validate(
                name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Getter> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("lastName"));
        assertThat(v.getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_parameter_all_valid() throws Exception {
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_ConstructorParameter>> violations = validator
                .forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(
                                String.class, String.class), new Object[] {
                                        "ABC", "GHI" });

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_parameter_firstName_is_invalid() throws Exception {
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_ConstructorParameter>> violations = validator
                .forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(
                                String.class, String.class), new Object[] {
                                        "abc", "GHI" });

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_ConstructorParameter> v = violations.iterator()
                .next();
        assertThat(v.getPropertyPath().toString(), is(
                "Name_ConstructorParameter.arg0"));
        assertThat(v.getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_parameter_lastName_is_invalid() throws Exception {
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_ConstructorParameter>> violations = validator
                .forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(
                                String.class, String.class), new Object[] {
                                        "ABC", "ghi" });

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_ConstructorParameter> v = violations.iterator()
                .next();
        assertThat(v.getPropertyPath().toString(), is(
                "Name_ConstructorParameter.arg1"));
        assertThat(v.getMessage(), is(
                "{org.terasoluna.gfw.common.codepoints.ConsistOf.message}"));
    }

    @Test
    public void testIsValid_annoattion_all_valid() throws Exception {
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_ConstructorParameter>> violations = validator
                .forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(
                                String.class, String.class), new Object[] {
                                        "ABC", "GHI" });

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_annotation_all_valid() throws Exception {
        Name_Annotation name = new Name_Annotation("ABC", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Annotation>> violations = validator
                .validate(name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testIsValid_annotation_firstName_is_invalid() throws Exception {
        Name_Annotation name = new Name_Annotation("ＡＢＣ", "GHI");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Annotation>> violations = validator
                .validate(name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Annotation> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("firstName"));
        assertThat(v.getMessage(), is("not ascii printable!"));
    }

    @Test
    public void testIsValid_annotation_lastName_is_invalid() throws Exception {
        Name_Annotation name = new Name_Annotation("ABC", "ＧＨＩ");
        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Name_Annotation>> violations = validator
                .validate(name);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));

        ConstraintViolation<Name_Annotation> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("lastName"));
        assertThat(v.getMessage(), is("not ascii printable!"));
    }
}
