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

/**
 * Utility about escaping of query.<br>
 * <p>
 * Supported query is JPQL and SQL.
 * </p>
 */
public final class QueryEscapeUtils {
    private static final LikeConditionEscape WITH_FULL_WIDTH = LikeConditionEscape
            .withFullWidthWildcardsEscape();

    private static final LikeConditionEscape WITHOUT_FULL_WIDTH = LikeConditionEscape
            .withoutFullWidthWildcardsEscape();

    /**
     * Default constructor.
     */
    private QueryEscapeUtils() {
        // do nothing.
    }

    /**
     * <p>
     * Convert a search criteria string to the escaped string of LIKE condition.
     * </p>
     * <p>
     * Conversion rules are as follows:
     * </p>
     * <ol>
     * <li>Escape {@link LikeConditionEscape#LIKE_ESC_CHAR} using {@link LikeConditionEscape#LIKE_ESC_CHAR}.</li>
     * <li>Escape '%' and '_' using {@link LikeConditionEscape#LIKE_ESC_CHAR}.</li>
     * </ol>
     * <p>
     * For example.<br>
     *
     * <pre>
     * <code>
     * 
     * toLikeCondition("a")    -&gt; "a"
     * toLikeCondition("a~")   -&gt; "a~~"
     * toLikeCondition("a%")   -&gt; "a~%"
     * toLikeCondition("a_")   -&gt; "a~_"
     * toLikeCondition("_a%")  -&gt; "~_a~%"
     * toLikeCondition("a％")  -&gt; "a％" (does not escape!)
     * toLikeCondition("a＿")  -&gt; "a＿" (does not escape!)
     * toLikeCondition(" ")    -&gt; " "
     * toLikeCondition("")     -&gt; ""
     * toLikeCondition(null)   -&gt; null
     * </code>
     * </pre>
     * <p>
     * return likeCondition of argument when condition is <code>null</code>.<br>
     * Returned value is not appended "%" keyword.<br>
     * This method does not escape full-width wildcards since 1.0.2. <strong>If you escape '％' and '＿', use
     * <code>QueryEscapeUtils.withFullWidth().toLikeCondition(condition)</code></strong>
     * </p>
     * @param condition search criteria string.
     * @param likeCondition instance of StringBuilder for storing a converted search criteria string. <br>
     *            if arguments is null, create new instance of StringBuilder and stored a converted search criteria string.
     * @return instance of StringBuilder that are stored a converted search criteria string.
     * @see LikeConditionEscape#toLikeCondition(String, StringBuilder)
     */
    public static StringBuilder toLikeCondition(String condition,
            StringBuilder likeCondition) {
        return WITHOUT_FULL_WIDTH.toLikeCondition(condition, likeCondition);
    }

    /**
     * Convert a search criteria string to the escaped string of LIKE condition
     * <p>
     * Conversion rules see JavaDoc of {@link QueryEscapeUtils#toLikeCondition(String, StringBuilder)}.
     * </p>
     * <p>
     * return null when condition is <code>null</code>.<br>
     * Returned value is appended "%" keyword to the front, if not null or blank string.
     * </p>
     * @param condition search criteria string.
     * @return converted search criteria string.
     * @see LikeConditionEscape#toLikeCondition(String)
     */
    public static String toLikeCondition(String condition) {
        return WITHOUT_FULL_WIDTH.toLikeCondition(condition);
    }

    /**
     * Convert a search criteria string to the escaped string of LIKE condition ,and append "%" keyword to the front.
     * <p>
     * Conversion rules see JavaDoc of {@link QueryEscapeUtils#toLikeCondition(String, StringBuilder)}.
     * </p>
     * <p>
     * return null when condition is <code>null</code>.<br>
     * Returned value is appended "%" keyword to the front, if not null or blank string.
     * </p>
     * @param condition search criteria string.
     * @return converted search criteria string.
     * @see LikeConditionEscape#toStartingWithCondition(String)
     */
    public static String toStartingWithCondition(String condition) {
        return WITHOUT_FULL_WIDTH.toStartingWithCondition(condition);
    }

    /**
     * Convert a search criteria string to the escaped string of LIKE condition ,and append "%" keyword to the backward.
     * <p>
     * Conversion rules see JavaDoc of {@link LikeConditionEscape#toLikeCondition(String, StringBuilder)}.
     * </p>
     * <p>
     * return null when condition is <code>null</code>.<br>
     * Returned value is appended "%" keyword to the backward, if not null or blank string.
     * </p>
     * @param condition search criteria string.
     * @return converted search criteria string.
     * @see LikeConditionEscape#toEndingWithCondition(String)
     */
    public static String toEndingWithCondition(String condition) {
        return WITHOUT_FULL_WIDTH.toEndingWithCondition(condition);
    }

    /**
     * Convert a search criteria string to the escaped string of LIKE condition ,and append "%" keyword to the back and forth.
     * <p>
     * Conversion rules see JavaDoc of {@link QueryEscapeUtils#toLikeCondition(String, StringBuilder)}.
     * </p>
     * <p>
     * return null when condition is <code>null</code>.<br>
     * Returned value is appended "%" keyword to the back and forth, if not null or blank string.
     * </p>
     * @param condition search criteria string.
     * @return converted search criteria string.
     * @see LikeConditionEscape#toContainingCondition(String)
     */
    public static String toContainingCondition(String condition) {
        return WITHOUT_FULL_WIDTH.toContainingCondition(condition);
    }

    /**
     * <p>
     * Returns {@link LikeConditionEscape} object to convert a search criteria string to the escaped string of LIKE condition.
     * </p>
     * <p>
     * Conversion rules are as follows:
     * </p>
     * <ol>
     * <li>Escape {@link LikeConditionEscape#LIKE_ESC_CHAR} using {@link LikeConditionEscape#LIKE_ESC_CHAR}.</li>
     * <li>Escape '%' and '_' and '％' and '＿' using {@link LikeConditionEscape#LIKE_ESC_CHAR}.</li>
     * </ol>
     * <p>
     * For example.<br>
     *
     * <pre>
     * <code>
     * withFullWidth().toLikeCondition("a")    -&gt; "a"
     * withFullWidth().toLikeCondition("a~")   -&gt; "a~~"
     * withFullWidth().toLikeCondition("a%")   -&gt; "a~%"
     * withFullWidth().toLikeCondition("a_")   -&gt; "a~_"
     * withFullWidth().toLikeCondition("_a%")  -&gt; "~_a~%"
     * withFullWidth().toLikeCondition("a％")  -&gt; "a~％" (escape!)
     * withFullWidth().toLikeCondition("a＿")  -&gt; "a~＿" (escape!)
     * withFullWidth().toLikeCondition(" ")    -&gt; " "
     * withFullWidth().toLikeCondition("")     -&gt; ""
     * withFullWidth().toLikeCondition(null)   -&gt; null
     * </code>
     * </pre>
     * @return LikeConditionEscape that escape full-width wildcards.
     * @since 1.0.2
     */
    public static LikeConditionEscape withFullWidth() {
        return WITH_FULL_WIDTH;
    }
}
