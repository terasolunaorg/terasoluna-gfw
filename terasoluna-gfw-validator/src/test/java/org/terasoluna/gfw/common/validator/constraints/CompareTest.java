/*
 * Copyright (C) 2013-2015 terasoluna.org
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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.beans.IntrospectionException;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import javax.validation.Path.PropertyNode;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.Compare.Operator;
import org.terasoluna.gfw.common.validator.constraints.Compare.Path;
import org.terasoluna.gfw.common.validator.constraints.CompareTest.CompareTestForm;

import lombok.Data;

/**
 * Test class of {@link Compare}
 */
public class CompareTest extends AbstractConstraintsTest<CompareTestForm> {

    private static final String MESSAGE_VALIDATION_ERROR = "not match '%s' and '%s'";

    @Before
    public void before() {
        form = new CompareTestForm();
    }

    /**
     * input null value. expected valid if only input null each source and destination.
     * @throws Throwable
     */
    @Test
    public void testInputNull() throws Throwable {

        {
            form.setSource(100);
            form.setDestination(null);

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(null);
            form.setDestination(100);

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(null);
            form.setDestination(null);

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify source and destination. expected valid if input source value is equal destination value.
     * @throws Throwable
     */
    @Test
    public void testSpecifySourceAndDestination() throws Throwable {

        {
            form.setSource(100);
            form.setDestination(99);

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(100);
            form.setDestination(100);

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify operator. expected valid if input source value less than or equal destination value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorLessThanOrEqual() throws Throwable {

        {
            form.setSource(101);
            form.setDestination(100);

            violations = validator.validate(form, LessThanOrEqual.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(100);
            form.setDestination(100);

            violations = validator.validate(form, LessThanOrEqual.class);
            assertThat(violations.size(), is(0));
        }

        {
            form.setSource(99);
            form.setDestination(100);

            violations = validator.validate(form, LessThanOrEqual.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify operator. expected valid if input source value less than destination value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorLessThan() throws Throwable {

        {
            form.setSource(101);
            form.setDestination(100);

            violations = validator.validate(form, LessThan.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(100);
            form.setDestination(100);

            violations = validator.validate(form, LessThan.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(99);
            form.setDestination(100);

            violations = validator.validate(form, LessThan.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify operator. expected valid if input source value equal destination value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorEqual() throws Throwable {

        {
            form.setSource(101);
            form.setDestination(100);

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(100);
            form.setDestination(100);

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }

        {
            form.setSource(99);
            form.setDestination(100);

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }
    }

    /**
     * specify operator. expected valid if input source value less than destination value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorGraterThan() throws Throwable {

        {
            form.setSource(99);
            form.setDestination(100);

            violations = validator.validate(form, GraterThan.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(100);
            form.setDestination(100);

            violations = validator.validate(form, GraterThan.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(101);
            form.setDestination(100);

            violations = validator.validate(form, GraterThan.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify operator. expected valid if input source value less than or equal destination value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyOperatorGraterThanOrEqual() throws Throwable {

        {
            form.setSource(99);
            form.setDestination(100);

            violations = validator.validate(form, GraterThanOrEqual.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source", "destination")));
        }

        {
            form.setSource(100);
            form.setDestination(100);

            violations = validator.validate(form, GraterThanOrEqual.class);
            assertThat(violations.size(), is(0));
        }

        {
            form.setSource(101);
            form.setDestination(100);

            violations = validator.validate(form, GraterThanOrEqual.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * specify path. expected validation message node is source.
     * @throws Throwable
     */
    @Test
    public void testSpecifyPathSource() throws Throwable {

        form.setSource(100);
        form.setDestination(99);

        violations = validator.validate(form, PathSource.class);
        assertThat(violations.size(), is(1));
        for (ConstraintViolation<CompareTestForm> violation : violations) {
            assertThat(violation.getMessage(), is(String.format(
                    MESSAGE_VALIDATION_ERROR, "source", "destination")));
            for (Node node : violation.getPropertyPath()) {
                assertThat(node, instanceOf(PropertyNode.class));
                assertThat(node.getName(), is("source"));
            }
        }
    }

    /**
     * specify path. expected validation message node is destination.
     * @throws Throwable
     */
    @Test
    public void testSpecifyPathDestination() throws Throwable {

        form.setSource(100);
        form.setDestination(99);

        violations = validator.validate(form, PathDestination.class);
        assertThat(violations.size(), is(1));
        for (ConstraintViolation<CompareTestForm> violation : violations) {
            assertThat(violation.getMessage(), is(String.format(
                    MESSAGE_VALIDATION_ERROR, "source", "destination")));
            for (Node node : violation.getPropertyPath()) {
                assertThat(node, instanceOf(PropertyNode.class));
                assertThat(node.getName(), is("destination"));
            }
        }
    }

    /**
     * specify path. expected validation message node is root bean.
     * @throws Throwable
     */
    @Test
    public void testSpecifyPathRootBean() throws Throwable {

        form.setSource(100);
        form.setDestination(99);

        violations = validator.validate(form, PathRootBean.class);
        assertThat(violations.size(), is(1));
        for (ConstraintViolation<CompareTestForm> violation : violations) {
            assertThat(violation.getMessage(), is(String.format(
                    MESSAGE_VALIDATION_ERROR, "source", "destination")));
            for (Node node : violation.getPropertyPath()) {
                assertThat(node, instanceOf(PropertyNode.class));
                assertThat(node.getName(), nullValue());
            }
        }
    }

    /**
     * specify source type is {@code Integer} and destination type is {@code String}. expected invalid.
     * @throws Throwable
     */
    @Test
    public void testSpecifyDestinationTypeUnmatch() throws Throwable {

        {
            form.setSource(100);
            form.setStringProperty("100");

            violations = validator.validate(form, TypeUnmatch.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "source",
                            "stringProperty")));
        }
    }

    /**
     * specify unknown source. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message is
     * {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyUnknownSource() throws Throwable {
        setExpectedFailedToInitialize(IntrospectionException.class);

        validator.validate(form, UnknownSource.class);
    }

    /**
     * specify unknown destination. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message
     * is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyUnknownDestination() throws Throwable {
        setExpectedFailedToInitialize(IntrospectionException.class);

        validator.validate(form, UnknownDestination.class);
    }

    /**
     * specify not comparable source. expected {@code ValidationException} caused by {@code IllegalArgumentException} that
     * message is {@code validator does not support this type: java.lang.Object}
     * @throws Throwable
     */
    @Test
    public void testSpecifyNotComparableSource() throws Throwable {
        setExpectedTypeNotSupport(Object.class);

        form.setObjectProperty(new Object());
        form.setDestination(100);

        validator.validate(form, NotComparableSource.class);
    }

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
     * Validation group operator grater than.
     */
    private static interface GraterThan {
    };

    /**
     * Validation group operator grater than or equal.
     */
    private static interface GraterThanOrEqual {
    };

    /**
     * Validation group path source.
     */
    private static interface PathSource {
    };

    /**
     * Validation group path destination.
     */
    private static interface PathDestination {
    };

    /**
     * Validation group path root bean.
     */
    private static interface PathRootBean {
    };

    /**
     * Validation group source and destination type unmatch.
     */
    private static interface TypeUnmatch {
    };

    /**
     * Validation group source not found.
     */
    private static interface UnknownSource {
    };

    /**
     * Validation group destination not found.
     */
    private static interface UnknownDestination {
    };

    /**
     * Validation group source not comparable.
     */
    private static interface NotComparableSource {
    };

    @Data
    @Compare.List({
            @Compare(source = "source", destination = "destination", operator = Operator.EQUAL),
            @Compare(source = "source", destination = "destination", operator = Operator.GRATER_THAN_OR_EQUAL, groups = { GraterThanOrEqual.class }),
            @Compare(source = "source", destination = "destination", operator = Operator.GRATER_THAN, groups = { GraterThan.class }),
            @Compare(source = "source", destination = "destination", operator = Operator.LESS_THAN_OR_EQUAL, groups = { LessThanOrEqual.class }),
            @Compare(source = "source", destination = "destination", operator = Operator.LESS_THAN, groups = { LessThan.class }),
            @Compare(source = "source", destination = "destination", operator = Operator.EQUAL, path = Path.SOURCE, groups = { PathSource.class }),
            @Compare(source = "source", destination = "destination", operator = Operator.EQUAL, path = Path.DESTINATION, groups = { PathDestination.class }),
            @Compare(source = "source", destination = "destination", operator = Operator.EQUAL, path = Path.ROOT_BEAN, groups = { PathRootBean.class }),
            @Compare(source = "source", destination = "stringProperty", operator = Operator.EQUAL, groups = { TypeUnmatch.class }),
            @Compare(source = "unknown", destination = "destination", operator = Operator.EQUAL, groups = { UnknownSource.class }),
            @Compare(source = "source", destination = "unknown", operator = Operator.EQUAL, groups = { UnknownDestination.class }),
            @Compare(source = "objectProperty", destination = "destination", operator = Operator.EQUAL, groups = { NotComparableSource.class }) })
    public class CompareTestForm {
        private Integer source;

        private Integer destination;

        private String stringProperty;

        private Object objectProperty;
    }
}
