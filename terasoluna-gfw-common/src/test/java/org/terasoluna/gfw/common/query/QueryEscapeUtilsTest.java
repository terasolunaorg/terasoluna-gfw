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
package org.terasoluna.gfw.common.query;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

public class QueryEscapeUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    /**
     * test specify value.
     */
    @Test
    public void testToLikeConditionVersionOfStringBuilderSpecifyValue() {
        // prepare input
        StringBuilder likeCondition = new StringBuilder();
        String input = "_a%";

        // execute test
        StringBuilder result = QueryEscapeUtils.toLikeCondition(input,
                likeCondition);

        // check
        assertThat(result, is(likeCondition));
        assertThat(result.toString(), is("~_a~%"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToLikeConditionVersionOfStringBuilderNull() {
        // prepare input
        StringBuilder likeCondition = new StringBuilder();
        String input = null;

        // execute test
        StringBuilder result = QueryEscapeUtils.toLikeCondition(input,
                likeCondition);

        // check
        assertThat(result, is(likeCondition));
        assertThat(result.length(), is(0));

    }

    /**
     * test no escaped string
     */
    @Test
    public void testToLikeConditionNoEscape() throws Exception {
        // prepare input
        String input = "a";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("a"));
    }

    /**
     * test % is escaped with ~%%
     */
    @Test
    public void testToLikeConditionPercentIsEscaped() throws Exception {
        // prepare input
        String input = "a%";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("a~%"));
    }

    /**
     * test _ is escaped
     */
    @Test
    public void testToLikeConditionUnderscoreIsEscaped() throws Exception {
        // prepare input
        String input = "a_";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("a~_"));
    }

    /**
     * test ~ is escaped with ~~%
     */
    @Test
    public void testToLikeConditionTildeIsEscaped() throws Exception {
        // prepare input
        String input = "a~";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("a~~"));
    }

    /**
     * test both _ and % are escaped
     */
    @Test
    public void testToLikeConditionUnderScoreAndPercentIsEscaped() throws Exception {
        // prepare input
        String input = "_a%";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("~_a~%"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToLikeConditionNullReturnsNull() throws Exception {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertNull(result);
    }

    /**
     * test empty string
     */
    @Test
    public void testToLikeConditionEmpty() throws Exception {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is(""));
    }

    /**
     * test space string
     */
    @Test
    public void testToLikeConditionSpace() throws Exception {
        // prepare input
        String input = " ";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is(" "));
    }

    /**
     * test full width ％ are escaped
     */
    @Test
    public void testToLikeConditionFullWidthPercentIsEscaped() throws Exception {
        // prepare input
        String input = "a％";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("a％"));
    }

    /**
     * test full width ＿ are escaped
     */
    @Test
    public void testToLikeConditionFullWidthUnderscoreIsEscaped() throws Exception {
        // prepare input
        String input = "a＿";

        // execute test
        String result = QueryEscapeUtils.toLikeCondition(input);

        // check
        assertThat(result, is("a＿"));
    }

    /**
     * test private constructor.
     */
    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<QueryEscapeUtils> c = QueryEscapeUtils.class
                .getDeclaredConstructor();
        assertThat(c.isAccessible(), is(false));
        c.setAccessible(true);
        assertNotNull(c.newInstance());
        c.setAccessible(false);
    }

    /**
     * test null returns null
     */
    @Test
    public void testToStartingWithConditionNull() {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.toStartingWithCondition(input);

        // check
        assertNull(result);
    }

    /**
     * test empty string returns empty string
     */
    @Test
    public void testToStartingWithConditionEmpty() {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.toStartingWithCondition(input);

        // check
        assertThat(result, is("%"));
    }

    /**
     * test any string are escaped and appended keyword
     */
    @Test
    public void testToStartingWithConditionAny() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        String result = QueryEscapeUtils.toStartingWithCondition(input);

        // check
        assertThat(result, is("a~~~%~_％＿b%"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToEndingWithConditionNull() {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.toEndingWithCondition(input);

        // check
        assertNull(result);
    }

    /**
     * test empty string returns empty string
     */
    @Test
    public void testToEndingWithConditionEmpty() {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.toEndingWithCondition(input);

        // check
        assertThat(result, is("%"));
    }

    /**
     * test any string are escaped and appended keyword
     */
    @Test
    public void testToEndingWithConditionAny() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        String result = QueryEscapeUtils.toEndingWithCondition(input);

        // check
        assertThat(result, is("%a~~~%~_％＿b"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToContainingConditionNull() {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.toContainingCondition(input);

        // check
        assertNull(result);
    }

    /**
     * test empty string returns empty string
     */
    @Test
    public void testToContainingConditionEmpty() {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.toContainingCondition(input);

        // check
        assertThat(result, is("%%"));
    }

    /**
     * test any string are escaped and appended keyword
     */
    @Test
    public void testToContainingConditionAny() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        String result = QueryEscapeUtils.toContainingCondition(input);

        // check
        assertThat(result, is("%a~~~%~_％＿b%"));
    }

    /**
     * StringBuilder For Storing is null.
     */
    @Test
    public void testToLikeCondition_StringBuilderForStoring_isNull() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        StringBuilder result = QueryEscapeUtils.toLikeCondition(input, null);

        // check
        assertThat(result.toString(), is("a~~~%~_％＿b"));
    }

    /**
     * conditionString and StringBuilder For Storing is null.
     */
    @Test
    public void testToLikeCondition_conditionString_and_StringBuilderForStoring_isNull() {
        // prepare input
        String input = null;

        // execute test
        StringBuilder result = QueryEscapeUtils.toLikeCondition(input, null);

        // check
        assertThat(result.toString(), is(""));
    }

    /**
     * test specify value (with Full-Width wildcards).
     */
    @Test
    public void testToLikeConditionVersionOfStringBuilderSpecifyValue_WithFullWidth() {
        // prepare input
        StringBuilder likeCondition = new StringBuilder();
        String input = "_a%";

        // execute test
        StringBuilder result = QueryEscapeUtils.withFullWidth()
                .toLikeCondition(input, likeCondition);

        // check
        assertThat(result, is(likeCondition));
        assertThat(result.toString(), is("~_a~%"));
    }

    /**
     * test null returns null (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionVersionOfStringBuilderNull_WithFullWidth() {
        // prepare input
        StringBuilder likeCondition = new StringBuilder();
        String input = null;

        // execute test
        StringBuilder result = QueryEscapeUtils.withFullWidth()
                .toLikeCondition(input, likeCondition);

        // check
        assertThat(result, is(likeCondition));
        assertThat(result.length(), is(0));

    }

    /**
     * test no escaped string (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionNoEscape_WithFullWidth() throws Exception {
        // prepare input
        String input = "a";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("a"));
    }

    /**
     * test % is escaped with ~%% (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionPercentIsEscaped_WithFullWidth() throws Exception {
        // prepare input
        String input = "a%";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("a~%"));
    }

    /**
     * test _ is escaped (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionUnderscoreIsEscaped_WithFullWidth() throws Exception {
        // prepare input
        String input = "a_";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("a~_"));
    }

    /**
     * test ~ is escaped with ~~% (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionTildeIsEscaped_WithFullWidth() throws Exception {
        // prepare input
        String input = "a~";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("a~~"));
    }

    /**
     * test both _ and % are escaped (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionUnderScoreAndPercentIsEscaped_WithFullWidth() throws Exception {
        // prepare input
        String input = "_a%";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("~_a~%"));
    }

    /**
     * test null returns null (with Full-Width wildcards)
     */
    @Test
    public void testToLikeConditionNullReturnsNull_WithFullWidth() throws Exception {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertNull(result);
    }

    /**
     * test empty string
     */
    @Test
    public void testToLikeConditionEmpty_WithFullWidth() throws Exception {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is(""));
    }

    /**
     * test space string
     */
    @Test
    public void testToLikeConditionSpace_WithFullWidth() throws Exception {
        // prepare input
        String input = " ";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is(" "));
    }

    /**
     * test full width ％ are escaped
     */
    @Test
    public void testToLikeConditionFullWidthPercentIsEscaped_WithFullWidth() throws Exception {
        // prepare input
        String input = "a％";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("a~％"));
    }

    /**
     * test full width ＿ are escaped
     */
    @Test
    public void testToLikeConditionFullWidthUnderscoreIsEscaped_WithFullWidth() throws Exception {
        // prepare input
        String input = "a＿";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toLikeCondition(input);

        // check
        assertThat(result, is("a~＿"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToStartingWithConditionNull_WithFullWidth() {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.withFullWidth()
                .toStartingWithCondition(input);

        // check
        assertNull(result);
    }

    /**
     * test empty string returns empty string
     */
    @Test
    public void testToStartingWithConditionEmpty_WithFullWidth() {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.withFullWidth()
                .toStartingWithCondition(input);

        // check
        assertThat(result, is("%"));
    }

    /**
     * test any string are escaped and appended keyword
     */
    @Test
    public void testToStartingWithConditionAny_WithFullWidth() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        String result = QueryEscapeUtils.withFullWidth()
                .toStartingWithCondition(input);

        // check
        assertThat(result, is("a~~~%~_~％~＿b%"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToEndingWithConditionNull_WithFullWidth() {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toEndingWithCondition(
                input);

        // check
        assertNull(result);
    }

    /**
     * test empty string returns empty string
     */
    @Test
    public void testToEndingWithConditionEmpty_WithFullWidth() {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toEndingWithCondition(
                input);

        // check
        assertThat(result, is("%"));
    }

    /**
     * test any string are escaped and appended keyword
     */
    @Test
    public void testToEndingWithConditionAny_WithFullWidth() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toEndingWithCondition(
                input);

        // check
        assertThat(result, is("%a~~~%~_~％~＿b"));
    }

    /**
     * test null returns null
     */
    @Test
    public void testToContainingConditionNull_WithFullWidth() {
        // prepare input
        String input = null;

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toContainingCondition(
                input);

        // check
        assertNull(result);
    }

    /**
     * test empty string returns empty string
     */
    @Test
    public void testToContainingConditionEmpty_WithFullWidth() {
        // prepare input
        String input = "";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toContainingCondition(
                input);

        // check
        assertThat(result, is("%%"));
    }

    /**
     * test any string are escaped and appended keyword
     */
    @Test
    public void testToContainingConditionAny_WithFullWidth() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        String result = QueryEscapeUtils.withFullWidth().toContainingCondition(
                input);

        // check
        assertThat(result, is("%a~~~%~_~％~＿b%"));
    }

    /**
     * StringBuilder For Storing is null.
     */
    @Test
    public void testToLikeCondition_StringBuilderForStoring_isNull_WithFullWidth() {
        // prepare input
        String input = "a~%_％＿b";

        // execute test
        StringBuilder result = QueryEscapeUtils.withFullWidth()
                .toLikeCondition(input, null);

        // check
        assertThat(result.toString(), is("a~~~%~_~％~＿b"));
    }

    /**
     * conditionString and StringBuilder For Storing is null.
     */
    @Test
    public void testToLikeCondition_conditionString_and_StringBuilderForStoring_isNull_WithFullWidth() {
        // prepare input
        String input = null;

        // execute test
        StringBuilder result = QueryEscapeUtils.withFullWidth()
                .toLikeCondition(input, null);

        // check
        assertThat(result.toString(), is(""));
    }
}
