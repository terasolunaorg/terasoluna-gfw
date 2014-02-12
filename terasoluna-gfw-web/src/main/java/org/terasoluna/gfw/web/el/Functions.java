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
package org.terasoluna.gfw.web.el;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.terasoluna.gfw.web.util.EscapeUtils;

/**
 * Class of functions of EL pattern.
 * <p>
 * Provides the following functions: <br>
 * <ul>
 * <li>Escaping HTML tag using {@code f:h}</li>
 * <li>Encoding URL using {@code f:u}</li>
 * <li>Replacing new line characters with {@code<br />} using {@code f:br}</li>
 * <li>Output only the specified characters {@code f:cut}</li>
 * <li>Output the link text in {@code <a>} tag using {@code f:link}</li>
 * <li>Build query string from the parameters using {@code f:query}</li>
 * <li>Escaping JavaScript {@code f:js}</li>
 * <li>Escaping EventHandler using {@code f:hjs}</li>
 * </ul>
 * <br>
 * Refer JavaDoc of each method for information regarding how to use.<br>
 * <br>
 * </p>
 */
public final class Functions {

    /**
     * Pattern of URL for replace to the link tag.
     */
    private static final Pattern URL_PATTERN = Pattern
            .compile("(http|https)://[A-Za-z0-9\\._~/:\\-?&=%;]+");

    /**
     * Pattern of line break.
     */
    private static final Pattern LINE_BREAK_PATTERN = Pattern
            .compile("(\\r\\n|\\r|\\n)");

    /**
     * line break tag string of HTML.
     */
    private static final String HTML_TAG_OF_LINE_BREAK = "<br />";

    /**
     * conversion service for format a value.
     */
    private static final FormattingConversionService CONVERSION_SERVICE = new DefaultFormattingConversionService();

    /**
     * type descriptor of string for format a value.
     */
    private static final TypeDescriptor STRING_DESC = TypeDescriptor
            .valueOf(String.class);

    /**
     * Default Constructor.
     */
    private Functions() {
        // do nothing
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
    public static String h(Object input) {
        return EscapeUtils.htmlEscape(input);
    }

    /**
     * url encode the given string.
     * <p>
     * url is encoded with "UTF-8".
     * </p>
     * @param value string to encode
     * @return encoded string. returns empty string if <code>value</code> is <code>null</code> or empty.
     * @see UriComponents#encode()
     */
    public static String u(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        UriComponents components = UriComponentsBuilder.fromUriString(value)
                .build().encode();
        return components.toString();
    }

    /**
     * convert <code>&quot;\r\n&quot;</code>,<code>&quot;\r&quot;</code>, <code>&quot;\n&quot;</code> to <code>&lt;br&gt;</code>
     * @param value string to convert
     * @return converted string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String br(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        String replacedValue = LINE_BREAK_PATTERN.matcher(value).replaceAll(
                HTML_TAG_OF_LINE_BREAK);
        return replacedValue;
    }

    /**
     * cut the given string from head to the given length.
     * @param value string to be cut
     * @param length length of cut string
     * @return cut string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String cut(String value, int length) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, l = value.length(); i < l; i++) {
            if (i >= length) {
                break;
            }
            sb.append(value.charAt(i));
        }
        return sb.toString();
    }

    /**
     * convert URL to anchor in the given string.
     * @param value string to convert
     * @return converted string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String link(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return URL_PATTERN.matcher(value).replaceAll("<a href=\"$0\">$0</a>");
    }

    /**
     * build query string from map.
     * <p>
     * query string is encoded with "UTF-8".
     * </p>
     * @param map map
     * @return query string. if map is not empty, return query string of start with "&". ex) &name1=value&name2=value&...
     */
    public static String mapToQuery(Map<String, Object> map) {
        return mapToQuery(map, null);
    }

    /**
     * build query string from map with the specified {@link BeanWrapper}.
     * <p>
     * query string is encoded with "UTF-8".
     * </p>
     * @param map map
     * @param beanWrapper beanWrapper which has the definition of each field.
     * @return query string. if map is not empty, return query string of start with "&". ex) &name1=value&name2=value&...
     */
    public static String mapToQuery(Map<String, Object> map,
            BeanWrapper beanWrapper) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
        Map<String, Object> uriVariables = new HashMap<String, Object>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String name = e.getKey();
            Object value = e.getValue();
            builder.path("&" + name + "={" + name + "}");
            TypeDescriptor sourceType;
            if (beanWrapper != null) {
                sourceType = beanWrapper.getPropertyTypeDescriptor(name);
            } else {
                sourceType = TypeDescriptor.forObject(value);
            }
            uriVariables.put(name,
                    CONVERSION_SERVICE.convert(value, sourceType, STRING_DESC));
        }
        return builder.buildAndExpand(uriVariables).encode().toString();
    }

    /**
     * build query string from map or bean.
     * <p>
     * query string is encoded with "UTF-8".
     * </p>
     * @param params map or bean
     * @return query string. returns empty string if <code>params</code> is <code>null</code> or empty string or
     *         {@link Iterable} or {@link BeanUtils#isSimpleValueType(Class)}.
     */
    @SuppressWarnings("unchecked")
    public static String query(Object params) {
        if (params == null) {
            return "";
        }
        Class<?> clazz = params.getClass();
        if (clazz.isArray() || params instanceof Iterable
                || BeanUtils.isSimpleValueType(clazz)) {
            return "";
        }

        String query;
        if (params instanceof Map) {
            query = mapToQuery((Map<String, Object>) params);
        } else {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            BeanWrapper beanWrapper = PropertyAccessorFactory
                    .forBeanPropertyAccess(params);
            PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);
            for (PropertyDescriptor pd : pds) {
                String name = pd.getName();
                if (!"class".equals(name)) {
                    Object value = beanWrapper.getPropertyValue(name);
                    map.put(name, value);
                }
            }
            query = mapToQuery(map, beanWrapper);
        }
        if (query.startsWith("&")) {
            return query.substring(1);
        } else {
            return query;
        }
    }

    /**
     * escape javascript in the given string supposed to be surrounded by single-quote.<br>
     * <p>
     * example
     * </p>
     * 
     * <pre>
     * &lt;script type="text/javascript"&gt;
     *   var message = '${f:js(message)}';
     *   ...
     * &lt;/script&gt;
     * </pre>
     * 
     * target characters to escape are following <br>
     * ' ====&gt; \'<br>
     * " ====&gt; \"<br>
     * \ ====&gt; \\<br>
     * / ====&gt; \/<br>
     * < ====&gt; \x3c<br>
     * > ====&gt; \x3e<br>
     * 0x0D ====&gt; \r<br>
     * 0x0A ====&gt; \n<br>
     * </p>
     * @param value string to escape
     * @return escaped string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String js(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
            case '\'':
                result.append("\\'");
                break;
            case '"':
                result.append("\\\"");
                break;
            case '\\':
                result.append("\\\\");
                break;
            case '/':
                result.append("\\/");
                break;
            case '<':
                result.append("\\x3c");
                break;
            case '>':
                result.append("\\x3e");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\n':
                result.append("\\n");
                break;
            default:
                result.append(ch);
                break;
            }
        }
        return result.toString();
    }

    /**
     * escape html (by {@link h}) after escape js (by {@link js})<br>
     * <p>
     * This is used to escape event handler (ex. onclick="callback('${f:hjs(xxxx)}')"). This function equals to
     * ${f:h(f:js(xxx))}.
     * </p>
     * @param value string to escape
     * @return escaped string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String hjs(String input) {
        return h(js(input));
    }
}
