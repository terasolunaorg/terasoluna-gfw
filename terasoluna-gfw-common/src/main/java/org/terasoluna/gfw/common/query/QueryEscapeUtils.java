/*
 * Copyright (C) 2013 terasoluna.org
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

    /**
     * Default constructor.
     */
    private QueryEscapeUtils() {
        // do nothing.
    }

    /**
     * Escape character used in the pattern string of LIKE value. Escape character is '~'.
     */
    public static final char LIKE_ESC_CHAR = '~';

    /**
     * <p>
     * Convert a search criteria string to the escaped string of LIKE condition.
     * </p>
     * <p>
     * Conversion rules are as follows:
     * </p>
     * <ol>
     * <li>Escape {@link #LIKE_ESC_CHAR} using {@link #LIKE_ESC_CHAR}.</li>
     * <li>Escape '%' and '_' and '％' and '＿' using {@link #LIKE_ESC_CHAR}.</li>
     * </ol>
     * <p>
     * For example.<br>
     * 
     * <pre>
     * <code>
     * toLikeCondition("a")    -> "a"
     * toLikeCondition("a~")   -> "a~~"
     * toLikeCondition("a%")   -> "a~%"
     * toLikeCondition("a_")   -> "a~_"
     * toLikeCondition("_a%")  -> "~_a~%"
     * toLikeCondition("a％")  -> "a~％"
     * toLikeCondition("a＿")  -> "a~＿"
     * toLikeCondition(" ")    -> " "
     * toLikeCondition("")     -> ""
     * toLikeCondition(null)   -> null
     * </code>
     * </pre>
     * 
     * </p>
     * <p>
     * return likeCondition of argument when condition is <code>null</code>.<br>
     * Returned value is not appended "%" keyword.
     * </p>
     * @param condition search criteria string.
     * @param likeCondition instance of StringBuilder for storing a converted search criteria string. <br>
     *            if arguments is null, create new instance of StringBuilder and stored a converted search criteria string.
     * @return instance of StringBuilder that are stored a converted search criteria string.
     */
    public static StringBuilder toLikeCondition(String condition,
            StringBuilder likeCondition) {
        StringBuilder storingLikeCondition = likeCondition;
        if (storingLikeCondition == null) {
            storingLikeCondition = new StringBuilder();
        }
        if (condition == null) {
            return storingLikeCondition;
        }
        for (int i = 0; i < condition.length(); i++) {
            char c = condition.charAt(i);
            if (c == LIKE_ESC_CHAR) {
                storingLikeCondition.append(LIKE_ESC_CHAR);
                storingLikeCondition.append(LIKE_ESC_CHAR);
            } else if (c == '%' || c == '_' || c == '＿' || c == '％') {
                storingLikeCondition.append(LIKE_ESC_CHAR);
                storingLikeCondition.append(c);
            } else {
                storingLikeCondition.append(c);
            }
        }
        return storingLikeCondition;
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
     */
    public static String toLikeCondition(String condition) {
        if (condition == null) {
            return null;
        }
        return toLikeCondition(condition, new StringBuilder()).toString();
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
     */
    public static String toStartingWithCondition(String condition) {
        if (condition == null) {
            return condition;
        }
        return toLikeCondition(condition, new StringBuilder()).append("%")
                .toString();
    }

    /**
     * Convert a search criteria string to the escaped string of LIKE condition ,and append "%" keyword to the backward.
     * <p>
     * Conversion rules see JavaDoc of {@link QueryEscapeUtils#toLikeCondition(String, StringBuilder)}.
     * </p>
     * <p>
     * return null when condition is <code>null</code>.<br>
     * Returned value is appended "%" keyword to the backward, if not null or blank string.
     * </p>
     * @param condition search criteria string.
     * @return converted search criteria string.
     */
    public static String toEndingWithCondition(String condition) {
        if (condition == null) {
            return condition;
        }
        return toLikeCondition(condition, new StringBuilder("%")).toString();
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
     */
    public static String toContainingCondition(String condition) {
        if (condition == null) {
            return condition;
        }
        return toLikeCondition(condition, new StringBuilder("%")).append("%")
                .toString();
    }

}
