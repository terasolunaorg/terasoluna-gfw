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
package org.terasoluna.gfw.common.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class QueryEscapeUtilsTest {

    @DataPoints
    public static TestData[] dataSet = {
            /* input, expectedToLikeCondition, expectedToLikeConditionWithFullWidth */
            new TestData("a", "a", "a") /* normal */,
            new TestData("a~", "a~~", "a~~") /* escape character */,
            new TestData("a%", "a~%", "a~%") /* % wildcard */,
            new TestData("a_", "a~_", "a~_") /* _ wildcard */,
            new TestData("_a%", "~_a~%", "~_a~%") /* _ and % */,
            new TestData("a％", "a％" /* not escaped */, "a~％" /* escaped */) /* full-width ~ wildcard */,
            new TestData("a＿", "a＿" /* not escaped */, "a~＿" /* escaped */) /* full-width _ wildcard */,
            new TestData(" ", " ", " ") /* blank */,
            new TestData("", "", "") /* empty */,
            new TestData(null, null, null) /* null */,
            new TestData("a~%_％＿b", "a~~~%~_％＿b", "a~~~%~_~％~＿b") /* complex */
    };

    /**
     * test {@link QueryEscapeUtils#toLikeCondition(String)}
     */
    @Theory
    public void testToLikeCondition(TestData param) {
        String actual = QueryEscapeUtils.toLikeCondition(param.input);
        String expected = param.expectedToLikeCondition;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#withFullWidth()} {@link LikeConditionEscape#toLikeCondition(String)}
     */
    @Theory
    public void testToLikeConditionWithFullWidth(TestData param) {
        String actual = QueryEscapeUtils.withFullWidth().toLikeCondition(
                param.input);
        String expected = param.expectedToLikeConditionWithFullWidth;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#toLikeCondition(String, StringBuilder)}
     */
    @Theory
    public void testToLikeConditionStringBuilder(TestData param) {
        StringBuilder actual = QueryEscapeUtils.toLikeCondition(param.input,
                new StringBuilder());
        StringBuilder expected = param.expectedToLikeConditionStringBuilder;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual.toString(), is(expected.toString()));
    }

    /**
     * test {@link QueryEscapeUtils#withFullWidth()} {@link LikeConditionEscape#toLikeCondition(String, StringBuilder)}
     */
    @Theory
    public void testToLikeConditionWithFullWidthStringBuilder(TestData param) {
        StringBuilder actual = QueryEscapeUtils.withFullWidth().toLikeCondition(
                param.input, new StringBuilder());
        StringBuilder expected = param.expectedToLikeConditionWithFullWidthStringBuilder;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual.toString(), is(expected.toString()));
    }

    /**
     * test {@link QueryEscapeUtils#toStartingWithCondition(String)}
     */
    @Theory
    public void testToStartingWithCondition(TestData param) {
        String actual = QueryEscapeUtils.toStartingWithCondition(param.input);
        String expected = param.expectedStartingWithCondition;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#withFullWidth()} {@link LikeConditionEscape#toStartingWithCondition(String)}
     */
    @Theory
    public void testToStartingWithConditionWithFullWidth(TestData param) {
        String actual = QueryEscapeUtils.withFullWidth()
                .toStartingWithCondition(param.input);
        String expected = param.expectedStartingWithConditionWithFullWidth;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#toEndingWithCondition(String)}
     */
    @Theory
    public void testToEndingWithCondition(TestData param) {
        String actual = QueryEscapeUtils.toEndingWithCondition(param.input);
        String expected = param.expectedEndingWithCondition;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#withFullWidth()} {@link LikeConditionEscape#toEndingWithCondition(String)}
     */
    @Theory
    public void testToEndingWithConditionWithFullWidth(TestData param) {
        String actual = QueryEscapeUtils.withFullWidth().toEndingWithCondition(
                param.input);
        String expected = param.expectedEndingWithConditionWithFullWidth;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#toContainingCondition(String)}
     */
    @Theory
    public void testToContainingCondition(TestData param) {
        String actual = QueryEscapeUtils.toContainingCondition(param.input);
        String expected = param.expectedContainingCondition;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    /**
     * test {@link QueryEscapeUtils#withFullWidth()} {@link LikeConditionEscape#toContainingCondition(String)}
     */
    @Theory
    public void testToContainingWithFullWidth(TestData param) {
        String actual = QueryEscapeUtils.withFullWidth().toContainingCondition(
                param.input);
        String expected = param.expectedContainingConditionWithFullWidth;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;
        assertThat(errorMessage, actual, is(expected));
    }

    @Theory
    public void testToLikeConditionIsNull(TestData param) {
        // set up
        StringBuilder actual = QueryEscapeUtils.toLikeCondition(param.input,
                null);
        StringBuilder expected = param.expectedToLikeConditionIsNull;
        String errorMessage = "Input:" + param.input + ", Expected: " + expected
                + ", Actual: " + actual;

        // assert
        assertThat(errorMessage, actual.toString(), is(expected.toString()));
    }

    @Test
    public void testQueryEscapeUtils() throws Exception {
        // set up
        Constructor<QueryEscapeUtils> constructor = QueryEscapeUtils.class
                .getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());

        constructor.setAccessible(false);
    }

    static class TestData {
        final String input;

        final String expectedToLikeCondition;

        final String expectedToLikeConditionWithFullWidth;

        final StringBuilder expectedToLikeConditionStringBuilder;

        final StringBuilder expectedToLikeConditionWithFullWidthStringBuilder;

        final String expectedStartingWithCondition;

        final String expectedStartingWithConditionWithFullWidth;

        final String expectedEndingWithCondition;

        final String expectedEndingWithConditionWithFullWidth;

        final String expectedContainingCondition;

        final String expectedContainingConditionWithFullWidth;

        private final StringBuilder expectedToLikeConditionIsNull;

        public TestData(String input, String expectedToLikeCondition,
                String expectedToLikeConditionWithFullWidth) {
            this.input = input;
            this.expectedToLikeCondition = expectedToLikeCondition;
            this.expectedToLikeConditionWithFullWidth = expectedToLikeConditionWithFullWidth;
            this.expectedToLikeConditionStringBuilder = (expectedToLikeCondition == null)
                    ? new StringBuilder()
                    : new StringBuilder(expectedToLikeCondition);
            this.expectedToLikeConditionWithFullWidthStringBuilder = (expectedToLikeConditionWithFullWidth == null)
                    ? new StringBuilder()
                    : new StringBuilder(expectedToLikeConditionWithFullWidth);
            this.expectedStartingWithCondition = (expectedToLikeCondition == null)
                    ? null : expectedToLikeCondition + "%";
            this.expectedStartingWithConditionWithFullWidth = (expectedToLikeConditionWithFullWidth == null)
                    ? null : expectedToLikeConditionWithFullWidth + "%";
            this.expectedEndingWithCondition = (expectedToLikeCondition == null)
                    ? null : "%" + expectedToLikeCondition;
            this.expectedEndingWithConditionWithFullWidth = (expectedToLikeConditionWithFullWidth == null)
                    ? null : "%" + expectedToLikeConditionWithFullWidth;
            this.expectedContainingCondition = (expectedToLikeCondition == null)
                    ? null : "%" + expectedToLikeCondition + "%";
            this.expectedContainingConditionWithFullWidth = (expectedToLikeConditionWithFullWidth == null)
                    ? null : "%" + expectedToLikeConditionWithFullWidth + "%";
            this.expectedToLikeConditionIsNull = (expectedToLikeCondition == null)
                    ? new StringBuilder()
                    : new StringBuilder(expectedToLikeCondition);
        }
    }
}
