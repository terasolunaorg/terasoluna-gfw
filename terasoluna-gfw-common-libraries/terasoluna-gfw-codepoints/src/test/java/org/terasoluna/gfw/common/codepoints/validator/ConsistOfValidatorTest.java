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

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ConsistOfValidatorTest {

    private Validator validator;

    private static Locale originalLocale;

    public ConsistOfValidatorTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeAll
    public static void beforeClass() {
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterAll
    public static void afterClass() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testIsValid_all_valid() throws Exception {
        Name_Simple name = new Name_Simple("ABC", "GHI");

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_all_null() throws Exception {
        Name_Simple name = new Name_Simple();

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_firstName_is_invalid() throws Exception {
        Name_Simple name = new Name_Simple("abc", "GHI");

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(name);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage).containsExactlyInAnyOrder(
                        tuple("firstName", "not consist of specified code points"));
    }

    @Test
    public void testIsValid_lastName_is_invalid() throws Exception {
        Name_Simple name = new Name_Simple("ABC", "ghi");

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(name);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage).containsExactlyInAnyOrder(
                        tuple("lastName", "not consist of specified code points"));
    }

    @Test
    public void testIsValid_both_are_invalid() throws Exception {
        Name_Simple name = new Name_Simple("abc", "ghi");

        Set<ConstraintViolation<Name_Simple>> violations = validator.validate(name);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage).containsExactlyInAnyOrder(
                        tuple("firstName", "not consist of specified code points"),
                        tuple("lastName", "not consist of specified code points"));
    }

    @Test
    public void testIsValid_multi_all_valid() throws Exception {
        Name_Multi name = new Name_Multi("ABCGHI", "DEFJKL");

        Set<ConstraintViolation<Name_Multi>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_multi_firstName_is_invalid() throws Exception {
        Name_Multi name = new Name_Multi("ABCDEFGHIJKLMN", "ABCDEFGHIJKL");

        Set<ConstraintViolation<Name_Multi>> violations = validator.validate(name);

        assertThat(violations).hasSize(1);
    }

    @Test
    public void testIsValid_composite_all_valid() throws Exception {
        Name_Composite name = new Name_Composite("ABCGHI", "DEFJKL");

        Set<ConstraintViolation<Name_Composite>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_composite_firstName_is_invalid() throws Exception {
        Name_Composite name = new Name_Composite("ABCDEFGHIJKLMN", "ABCDEFGHIJKL");

        Set<ConstraintViolation<Name_Composite>> violations = validator.validate(name);

        assertThat(violations).hasSize(1);
    }

    @Test
    public void testIsValid_getter_all_valid() throws Exception {
        Name_Getter name = new Name_Getter("ABC", "GHI");

        Set<ConstraintViolation<Name_Getter>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_getter_firstName_is_invalid() throws Exception {
        Name_Getter name = new Name_Getter("abc", "GHI");

        Set<ConstraintViolation<Name_Getter>> violations = validator.validate(name);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage).containsExactlyInAnyOrder(
                        tuple("firstName", "not consist of specified code points"));
    }

    @Test
    public void testIsValid_getter_lastName_is_invalid() throws Exception {
        Name_Getter name = new Name_Getter("ABC", "ghi");

        Set<ConstraintViolation<Name_Getter>> violations = validator.validate(name);

        assertThat(violations).extracting(violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage).containsExactlyInAnyOrder(
                        tuple("lastName", "not consist of specified code points"));
    }

    @Test
    public void testIsValid_parameter_all_valid() throws Exception {

        Set<ConstraintViolation<Name_ConstructorParameter>> violations =
                validator.forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(String.class, String.class),
                        new Object[] {"ABC", "GHI"});

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_parameter_firstName_is_invalid() throws Exception {

        Set<ConstraintViolation<Name_ConstructorParameter>> violations =
                validator.forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(String.class, String.class),
                        new Object[] {"abc", "GHI"});

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("Name_ConstructorParameter.arg0",
                        "not consist of specified code points"));
    }

    @Test
    public void testIsValid_parameter_lastName_is_invalid() throws Exception {

        Set<ConstraintViolation<Name_ConstructorParameter>> violations =
                validator.forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(String.class, String.class),
                        new Object[] {"ABC", "ghi"});

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("Name_ConstructorParameter.arg1",
                        "not consist of specified code points"));
    }

    @Test
    public void testIsValid_annoattion_all_valid() throws Exception {

        Set<ConstraintViolation<Name_ConstructorParameter>> violations =
                validator.forExecutables().validateConstructorParameters(
                        Name_ConstructorParameter.class.getConstructor(String.class, String.class),
                        new Object[] {"ABC", "GHI"});

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_annotation_all_valid() throws Exception {
        Name_Annotation name = new Name_Annotation("ABC", "GHI");

        Set<ConstraintViolation<Name_Annotation>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_annotation_firstName_is_invalid() throws Exception {
        Name_Annotation name = new Name_Annotation("ＡＢＣ", "GHI");

        Set<ConstraintViolation<Name_Annotation>> violations = validator.validate(name);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("firstName", "not ascii printable!"));
    }

    @Test
    public void testIsValid_annotation_lastName_is_invalid() throws Exception {
        Name_Annotation name = new Name_Annotation("ABC", "ＧＨＩ");

        Set<ConstraintViolation<Name_Annotation>> violations = validator.validate(name);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("lastName", "not ascii printable!"));
    }

    @Test
    public void testIsValid_collection_all_valid() throws Exception {
        Name_Collection name =
                new Name_Collection(Arrays.asList("ABC", "ABC"), Arrays.asList("GHI", "GHI"));

        Set<ConstraintViolation<Name_Collection>> violations = validator.validate(name);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testIsValid_collection_firstNames_is_invalid() throws Exception {
        Name_Collection name =
                new Name_Collection(Arrays.asList("GHI", "ABC"), Arrays.asList("GHI", "GHI"));

        Set<ConstraintViolation<Name_Collection>> violations = validator.validate(name);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("firstNames[0].<list element>",
                        "not consist of specified code points"));
    }

    @Test
    public void testIsValid_collection_lastNames_is_invalid() throws Exception {
        Name_Collection name =
                new Name_Collection(Arrays.asList("ABC", "ABC"), Arrays.asList("ABC", "GHI"));

        Set<ConstraintViolation<Name_Collection>> violations = validator.validate(name);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("lastNames[0].<list element>",
                        "not consist of specified code points"));
    }

    @Test
    public void testIsValid_collection_all_is_invalid() throws Exception {
        Name_Collection name =
                new Name_Collection(Arrays.asList("GHI", "ABC"), Arrays.asList("GHI", "ABC"));

        Set<ConstraintViolation<Name_Collection>> violations = validator.validate(name);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        tuple("firstNames[0].<list element>",
                                "not consist of specified code points"),
                        tuple("lastNames[1].<list element>",
                                "not consist of specified code points"));
    }

}
