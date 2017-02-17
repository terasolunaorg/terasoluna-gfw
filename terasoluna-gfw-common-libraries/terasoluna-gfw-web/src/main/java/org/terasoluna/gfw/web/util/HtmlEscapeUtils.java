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
package org.terasoluna.gfw.web.util;

import java.util.Arrays;

import org.springframework.web.util.HtmlUtils;

/**
 * utility class about HTML Escape processing
 */
public final class HtmlEscapeUtils {

    /**
     * Default Constructor.
     * <p>
     * do nothing.
     * </p>
     */
    private HtmlEscapeUtils() {
        // do nothing.
    }

    /**
     * escape html tags in the given string.
     * <p>
     * target characters to escape are following <br>
     * &lt; ====&gt; &amp;lt;<br>
     * &gt; ====&gt; &amp;gt;<br>
     * &amp; ====&gt; &amp;amp;<br>
     * " ====&gt; &amp;quot;<br>
     * ' ====&gt; &amp;#39;<br>
     * </p>
     * @param input string to escape
     * @return escaped string. returns empty string if <code>value</code> is <code>null</code> or empty string.
     * @see HtmlUtils#htmlEscape(String)
     */
    public static String htmlEscape(Object input) {
        if (input == null) {
            return "";
        }
        String str;
        if (input.getClass().isArray()) {
            str = convertArraysToString(input);
        } else {
            str = input.toString();
        }

        return HtmlUtils.htmlEscape(str);
    }

    /**
     * converts input array object to String
     * @param input
     * @return converted String value
     */
    private static String convertArraysToString(Object input) {
        Class<?> clazz = input.getClass().getComponentType();
        String str;
        if (clazz == String.class) {
            str = Arrays.toString((Object[]) input);
        } else if (clazz == boolean.class) {
            str = Arrays.toString((boolean[]) input);
        } else if (clazz == char.class) {
            str = Arrays.toString((char[]) input);
        } else if (clazz == int.class) {
            str = Arrays.toString((int[]) input);
        } else if (clazz == long.class) {
            str = Arrays.toString((long[]) input);
        } else if (clazz == byte.class) {
            str = Arrays.toString((byte[]) input);
        } else if (clazz == short.class) {
            str = Arrays.toString((short[]) input);
        } else if (clazz == float.class) {
            str = Arrays.toString((float[]) input);
        } else if (clazz == double.class) {
            str = Arrays.toString((double[]) input);
        } else {
            str = Arrays.toString((Object[]) input);
        }
        return str;
    }
}
