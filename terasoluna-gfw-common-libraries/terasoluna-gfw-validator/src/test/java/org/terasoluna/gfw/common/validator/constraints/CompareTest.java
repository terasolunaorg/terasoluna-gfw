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
package org.terasoluna.gfw.common.validator.constraints;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import java.beans.IntrospectionException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.Compare.Node;
import org.terasoluna.gfw.common.validator.constraints.Compare.Operator;
import org.terasoluna.gfw.common.validator.constraints.CompareTest.CompareTestForm;

import jakarta.validation.ValidationException;

/**
 * Test class of {@link Compare}
 */
public class CompareTest extends AbstractConstraintsTest<CompareTestForm> {

    private static final String MESSAGE_VALIDATION_ERROR = "invalid combination of %s and %s";

    @Before
    public void before() {
        form = new CompareTestForm();
    }

    /**
     * input null value. expected valid.
     * @throws Throwable
     */
    @Test
    public void testInputNull() throws Throwable {

        {
            form.setLeft(100);

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(null);
            form.setRight(100);

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(null);
            form.setRight(null);

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify left and right. expected valid if input left value is equal right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyLeftAndRight() throws Throwable {

        {
            form.setLeft(100);
            form.setRight(99);

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify operator. expected valid if input left value less than or equal right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorLessThanOrEqual() throws Throwable {

        {
            form.setLeft(101);
            form.setRight(100);

            violations = validator.validate(form, LessThanOrEqual.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, LessThanOrEqual.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(99);
            form.setRight(100);

            violations = validator.validate(form, LessThanOrEqual.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify operator. expected valid if input left value less than right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorLessThan() throws Throwable {

        {
            form.setLeft(101);
            form.setRight(100);

            violations = validator.validate(form, LessThan.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, LessThan.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(99);
            form.setRight(100);

            violations = validator.validate(form, LessThan.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify operator. expected valid if input left value equal right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorEqual() throws Throwable {

        {
            form.setLeft(101);
            form.setRight(100);

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(99);
            form.setRight(100);

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }
    }

    /**
     * specify operator. expected valid if input left value not equal right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorNotEqual() throws Throwable {

        {
            form.setLeft(101);
            form.setRight(100);

            violations = validator.validate(form, NotEqual.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, NotEqual.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(99);
            form.setRight(100);

            violations = validator.validate(form, NotEqual.class);
            assertThat(violations, is(empty()));
        }

    }

    /**
     * specify operator. expected valid if input left value less than right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorGreaterThan() throws Throwable {

        {
            form.setLeft(99);
            form.setRight(100);

            violations = validator.validate(form, GreaterThan.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, GreaterThan.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(101);
            form.setRight(100);

            violations = validator.validate(form, GreaterThan.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify operator. expected valid if input left value less than or equal right value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorGreaterThanOrEqual() throws Throwable {

        {
            form.setLeft(99);
            form.setRight(100);

            violations = validator.validate(form, GreaterThanOrEqual.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "right")))));
        }

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, GreaterThanOrEqual.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(101);
            form.setRight(100);

            violations = validator.validate(form, GreaterThanOrEqual.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify requireBoth. expected valid if input left and right are both null.
     * @throws Throwable
     */
    @Test
    public void testSpecifyRequireBothIsTrue() throws Throwable {

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, RequireBoth.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(100);
            form.setRight(null);

            violations = validator.validate(form, RequireBoth.class);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath", hasToString("left")), //
                            hasProperty("message", is(String.format(
                                    MESSAGE_VALIDATION_ERROR, "left",
                                    "right"))))));
        }

        {
            form.setLeft(null);
            form.setRight(100);

            violations = validator.validate(form, RequireBoth.class);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath", hasToString("left")), //
                            hasProperty("message", is(String.format(
                                    MESSAGE_VALIDATION_ERROR, "left",
                                    "right"))))));
        }

        {
            form.setLeft(null);
            form.setRight(null);

            violations = validator.validate(form, RequireBoth.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify requireBoth. expected valid if input either left and right is null.
     * @throws Throwable
     */
    @Test
    public void testSpecifyRequireBothIsFalse() throws Throwable {

        {
            form.setLeft(100);
            form.setRight(100);

            violations = validator.validate(form, RequireEither.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(100);
            form.setRight(null);

            violations = validator.validate(form, RequireEither.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(null);
            form.setRight(100);

            violations = validator.validate(form, RequireEither.class);
            assertThat(violations, is(empty()));
        }

        {
            form.setLeft(null);
            form.setRight(null);

            violations = validator.validate(form, RequireEither.class);
            assertThat(violations, is(empty()));
        }
    }

    /**
     * specify path. expected validation message node is left.
     * @throws Throwable
     */
    @Test
    public void testSpecifyNodeProperty() throws Throwable {

        form.setLeft(100);
        form.setRight(99);

        violations = validator.validate(form, NodeProperty.class);
        assertThat(violations, containsInAnyOrder( //
                allOf( //
                        hasProperty("propertyPath", hasToString("left")), //
                        hasProperty("message", is(String.format(
                                MESSAGE_VALIDATION_ERROR, "left", "right"))))));
    }

    /**
     * specify path. expected validation message node is root bean.
     * @throws Throwable
     */
    @Test
    public void testSpecifyPathRootBean() throws Throwable {

        form.setLeft(100);
        form.setRight(99);

        violations = validator.validate(form, PathRootBean.class);
        assertThat(violations, containsInAnyOrder( //
                allOf( //
                        hasProperty("propertyPath", hasToString(emptyString())), //
                        hasProperty("message", is(String.format(
                                MESSAGE_VALIDATION_ERROR, "left", "right"))))));
    }

    /**
     * specify left type is {@code Integer} and right type is {@code String}. expected invalid.
     * @throws Throwable
     */
    @Test
    public void testSpecifyRightTypeUnmatch() throws Throwable {

        {
            form.setLeft(100);
            form.setStringProperty("100");

            violations = validator.validate(form, TypeUnmatch.class);
            assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                    String.format(MESSAGE_VALIDATION_ERROR, "left",
                            "stringProperty")))));
        }
    }

    /**
     * specify unknown left. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message is
     * {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyUnknownLeft() throws Throwable {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(form, UnknownLeft.class));
        assertFailedToInitialize(ex, IntrospectionException.class);
    }

    /**
     * specify unknown right. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message is
     * {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyUnknownRight() throws Throwable {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(form, UnknownRight.class));
        assertFailedToInitialize(ex, IntrospectionException.class);
    }

    /**
     * specify not comparable left. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message
     * is {@code validator does not support this type: java.lang.Object}
     * @throws Throwable
     */
    @Test
    public void testSpecifyNotComparableLeft() throws Throwable {
        form.setObjectProperty(new Object());
        form.setRight(100);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> validator.validate(form, NotComparableLeft.class));
        assertTypeNotSupport(ex, Object.class);
    }

    /**
     * validate collection element values.
     */
    @Test
    public void testElementTypeTypeUse() {

        {
            form.setListProperty(Arrays.asList(new Pair(100, 100),
                    new Pair(100, 100)));

            violations = validator.validate(form);
            assertThat(violations, is(empty()));
        }

        {
            form.setListProperty(Arrays.asList(new Pair(101, 100),
                    new Pair(100, 100)));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath", hasToString(
                                    "listProperty[0].<list element>.left")), //
                            hasProperty("message", is(String.format(
                                    MESSAGE_VALIDATION_ERROR, "left",
                                    "right"))))));
        }

        {
            form.setListProperty(Arrays.asList(new Pair(100, 100),
                    new Pair(100, 101)));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath", hasToString(
                                    "listProperty[1].<list element>.left")), //
                            hasProperty("message", is(String.format(
                                    MESSAGE_VALIDATION_ERROR, "left",
                                    "right"))))));
        }

        {
            form.setListProperty(Arrays.asList(new Pair(101, 100),
                    new Pair(100, 101)));

            violations = validator.validate(form);
            assertThat(violations, containsInAnyOrder( //
                    allOf( //
                            hasProperty("propertyPath", hasToString(
                                    "listProperty[0].<list element>.left")), //
                            hasProperty("message", is(String.format(
                                    MESSAGE_VALIDATION_ERROR, "left",
                                    "right")))), //
                    allOf( //
                            hasProperty("propertyPath", hasToString(
                                    "listProperty[1].<list element>.left")), //
                            hasProperty("message", is(String.format(
                                    MESSAGE_VALIDATION_ERROR, "left",
                                    "right"))))));
        }
    }

    /**
     * Validation group operator not equal.
     */
    private static interface NotEqual {
    };

    /**
     * Validation group operator less than or equal.
     */
    private static interface LessThanOrEqual {
    };

    /**
     * Validation group operator less than.
     */
    private static interface LessThan {
    };

    /**
     * Validation group operator greater than.
     */
    private static interface GreaterThan {
    };

    /**
     * Validation group operator greater than or equal.
     */
    private static interface GreaterThanOrEqual {
    };

    /**
     * Validation group required left and right both.
     */
    private static interface RequireBoth {
    };

    /**
     * Validation group required left and right either.
     */
    private static interface RequireEither {
    };

    /**
     * Validation group node left property.
     */
    private static interface NodeProperty {
    };

    /**
     * Validation group node root bean.
     */
    private static interface PathRootBean {
    };

    /**
     * Validation group left and right type unmatch.
     */
    private static interface TypeUnmatch {
    };

    /**
     * Validation group left not found.
     */
    private static interface UnknownLeft {
    };

    /**
     * Validation group right not found.
     */
    private static interface UnknownRight {
    };

    /**
     * Validation group left not comparable.
     */
    private static interface NotComparableLeft {
    };

    @Compare(left = "left", right = "right", operator = Operator.EQUAL)
    @Compare(left = "left", right = "right", operator = Operator.NOT_EQUAL, groups = {
            NotEqual.class })
    @Compare(left = "left", right = "right", operator = Operator.GREATER_THAN_OR_EQUAL, groups = {
            GreaterThanOrEqual.class })
    @Compare(left = "left", right = "right", operator = Operator.GREATER_THAN, groups = {
            GreaterThan.class })
    @Compare(left = "left", right = "right", operator = Operator.LESS_THAN_OR_EQUAL, groups = {
            LessThanOrEqual.class })
    @Compare(left = "left", right = "right", operator = Operator.LESS_THAN, groups = {
            LessThan.class })
    @Compare(left = "left", right = "right", operator = Operator.EQUAL, requireBoth = true, groups = {
            RequireBoth.class })
    @Compare(left = "left", right = "right", operator = Operator.EQUAL, requireBoth = false, groups = {
            RequireEither.class })
    @Compare(left = "left", right = "right", operator = Operator.EQUAL, node = Node.PROPERTY, groups = {
            NodeProperty.class })
    @Compare(left = "left", right = "right", operator = Operator.EQUAL, node = Node.ROOT_BEAN, groups = {
            PathRootBean.class })
    @Compare(left = "left", right = "stringProperty", operator = Operator.EQUAL, groups = {
            TypeUnmatch.class })
    @Compare(left = "unknown", right = "right", operator = Operator.EQUAL, groups = {
            UnknownLeft.class })
    @Compare(left = "left", right = "unknown", operator = Operator.EQUAL, groups = {
            UnknownRight.class })
    @Compare(left = "objectProperty", right = "right", operator = Operator.EQUAL, groups = {
            NotComparableLeft.class })
    public static class CompareTestForm {
        private Integer left;

        private Integer right;

        private String stringProperty;

        private Object objectProperty;

        private List<@Compare(left = "left", right = "right", operator = Operator.EQUAL) Pair> listProperty;

        public Integer getLeft() {
            return left;
        }

        public void setLeft(Integer left) {
            this.left = left;
        }

        public Integer getRight() {
            return right;
        }

        public void setRight(Integer right) {
            this.right = right;
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public Object getObjectProperty() {
            return objectProperty;
        }

        public void setObjectProperty(Object objectProperty) {
            this.objectProperty = objectProperty;
        }

        public List<Pair> getListProperty() {
            return listProperty;
        }

        public void setListProperty(List<Pair> listProperty) {
            this.listProperty = listProperty;
        }
    }

    public static class Pair {
        private Integer left;

        private Integer right;

        public Pair(Integer left, Integer right) {
            this.left = left;
            this.right = right;
        }

        public Integer getLeft() {
            return left;
        }

        public void setLeft(Integer left) {
            this.left = left;
        }

        public Integer getRight() {
            return right;
        }

        public void setRight(Integer right) {
            this.right = right;
        }
    }
}
