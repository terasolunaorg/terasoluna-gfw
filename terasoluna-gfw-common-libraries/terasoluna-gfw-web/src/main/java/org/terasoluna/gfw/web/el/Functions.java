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
package org.terasoluna.gfw.web.el;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.terasoluna.gfw.web.util.HtmlEscapeUtils;

/**
 * Class of functions of EL pattern.
 * <p>
 * Provides the following functions: <br>
 * <ul>
 * <li>Escaping HTML tag using {@code f:h}</li>
 * <li>Encoding URL using {@code f:u}</li>
 * <li>Replacing new line characters with {@code <br />
 * } using {@code f:br}</li>
 * <li>Output only the specified characters {@code f:cut}</li>
 * <li>Output the link text in {@code <a>} tag using {@code f:link}</li>
 * <li>Build query string from the parameters using {@code f:query}</li>
 * <li>Escaping JavaScript {@code f:js}</li>
 * <li>Escaping EventHandler using {@code f:hjs}</li>
 * </ul>
 * <br>
 * Refer JavaDoc of each method for information regarding how to use.<br>
 */
public final class Functions {

    /**
     * Pattern of URL for replace to the link tag.
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(http|https)://[A-Za-z0-9\\._~/:\\-?&=%;]+");

    /**
     * Pattern of line break.
     */
    private static final Pattern LINE_BREAK_PATTERN = Pattern.compile(
            "(\\r\\n|\\r|\\n)");

    /**
     * Pattern of plus character.
     */
    private static final Pattern PLUS_PATTERN = Pattern.compile("\\+");

    /**
     * line break tag string of HTML.
     */
    private static final String HTML_TAG_OF_LINE_BREAK = "<br />";

    /**
     * conversion service for format a value.
     */
    private static final FormattingConversionService CONVERSION_SERVICE = new DefaultFormattingConversionService();

    /**
     * converter from object to map
     */
    private static final ObjectToMapConverter OBJECT_TO_MAP_CONVERTER = new ObjectToMapConverter(CONVERSION_SERVICE);

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
     * @see HtmlEscapeUtils#htmlEscape(Object)
     */
    public static String h(Object input) {
        return HtmlEscapeUtils.htmlEscape(input);
    }

    /**
     * url encode the given string based on RFC 3986.<br>
     * <p>
     * url is encoded with "UTF-8".<br>
     * This method is used to encode values in "query" string. In <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>,
     * "query" part in URI is defined as follows:
     *
     * <pre>
     * <code>
     *   foo://example.com:8042/over/there?name=ferret#nose
     *   \_/   \______________/\_________/ \_________/ \__/
     *    |           |            |            |        |
     * scheme     authority       path        query   fragment
     *  </code>
     * </pre>
     *
     * and, "query" is defined as follows:
     *
     * <pre>
     * <code>
     * query         = *( pchar / "/" / "?" )
     * pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"
     * unreserved    = ALPHA / DIGIT / "-" / "." / "_" / "~"
     * sub-delims    = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
     * pct-encoded   = "%" HEXDIG HEXDIG
     *      </code>
     * </pre>
     *
     * In these characters, as a value of query parameter, <strong>"&amp;", "+" , "=" are percent-encoded</strong>.
     * <h3>sample</h3>
     * <ul>
     * <li>/ ====&gt; /</li>
     * <li>? ====&gt; ?</li>
     * <li>a ====&gt; a</li>
     * <li>0 ====&gt; 0</li>
     * <li>- ====&gt; -</li>
     * <li>. ====&gt; .</li>
     * <li>_ ====&gt; _</li>
     * <li>~ ====&gt; ~</li>
     * <li>! ====&gt; !</li>
     * <li>$ ====&gt; $</li>
     * <li>&amp; ====&gt; %26</li>
     * <li>' ====&gt; '</li>
     * <li>( ====&gt; (</li>
     * <li>) ====&gt; )</li>
     * <li>* ====&gt; *</li>
     * <li>+ ====&gt; %2B</li>
     * <li>; ====&gt; ;</li>
     * <li>=====&gt; %3D</li>
     * <li>あ ====&gt; %E3%81%82</li>
     * </ul>
     * <p>
     * Characters not listed above are percent-encoded.
     * </p>
     * @param value string to encode
     * @return encoded string based on RFC 3986. returns empty string if <code>value</code> is <code>null</code> or empty.
     * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986 3.4.Query</a>
     */
    public static String u(String value) {
        if (!StringUtils.hasLength(value)) {
            return "";
        }
        return extraEncodeQuery(UriUtils.encodeQueryParam(value, "UTF-8"));
    }

    /**
     * convert <code>&quot;\r\n&quot;</code>,<code>&quot;\r&quot;</code>, <code>&quot;\n&quot;</code> to <code>&lt;br&gt;</code>
     * @param value string to convert
     * @return converted string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String br(String value) {
        if (!StringUtils.hasLength(value)) {
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
        if (!StringUtils.hasLength(value)) {
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
        if (!StringUtils.hasLength(value)) {
            return "";
        }
        return URL_PATTERN.matcher(value).replaceAll("<a href=\"$0\">$0</a>");
    }

    /**
     * build query string from map.
     * <p>
     * query string is encoded with "UTF-8".
     * </p>
     * @see ObjectToMapConverter
     * @param map map
     * @return query string. if map is not empty, return query string. ex) name1=value&amp;name2=value&amp;...
     */
    public static String mapToQuery(Map<String, ?> map) {
        if (CollectionUtils.isEmpty(map)) {
            return "";
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
        for (Map.Entry<String, ?> e : map.entrySet()) {
            String name = e.getKey();
            Object value = e.getValue();
            builder.queryParam(name, value);
        }
        String query = builder.build().encode().toString();
        // remove the beginning symbol character('?') of the query string.
        return extraEncodeQuery(query.substring(1));
    }

    /**
     * build query string from map or bean.
     * <p>
     * query string is encoded with "UTF-8".
     * </p>
     * <p>
     * Note : About a {@link Map} property<br>
     * In this method, {@code null} and empty element are distinguished explicitly. Conversion rules are as follows:
     * <ul>
     * <li>If the value of a property is {@code null}, it is converted to empty string and the key is prefixed with {@code "_"}
     * .(converted to the reset parameter provided by Spring Web MVC)</li>
     * <li>If the value of a {@link Map} property is empty element, it is not converted.</li>
     * </ul>
     * <br>
     * But if this method is used after the {@code <form:form>} tag provided by Spring Web MVC, {@code null} is converted to
     * empty element during the processing of {@code <form:form>} tag. As a result, {@code null} value is not converted to a
     * reset parameter that start with {@code "_"}. In this case, this method does not guarantee the symmetry with the form
     * binding provided by Spring Web MVC.
     * @see ObjectToMapConverter
     * @param params map or bean
     * @return query string. returns empty string if <code>params</code> is <code>null</code> or empty string or
     *         {@link Iterable} or {@link BeanUtils#isSimpleValueType(Class)}.
     */
    public static String query(Object params) {
        if (params == null) {
            return "";
        }
        Class<?> clazz = params.getClass();
        if (BeanUtils.isSimpleValueType(clazz)) {
            return "";
        }
        return mapToQuery(OBJECT_TO_MAP_CONVERTER.convert(params));
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
     * &lt; ====&gt; \x3c<br>
     * &gt; ====&gt; \x3e<br>
     * 0x0D ====&gt; \r<br>
     * 0x0A ====&gt; \n<br>
     * @param value string to escape
     * @return escaped string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String js(String value) {
        if (!StringUtils.hasLength(value)) {
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
     * escape html (by {@link #h}) after escape js (by {@link #js})<br>
     * <p>
     * This is used to escape event handler (ex. onclick="callback('${f:hjs(xxxx)}')"). This function equals to
     * ${f:h(f:js(xxx))}.
     * </p>
     * @param input string to escape
     * @return escaped string. returns empty string if <code>value</code> is <code>null</code> or empty.
     */
    public static String hjs(String input) {
        return h(js(input));
    }

    /**
     * Percent-encode the "+" character in query string. This method is created for backward compatibility with spring 4.x or
     * earlier version.
     * @param query query string
     * @return encoded query string
     */
    private static String extraEncodeQuery(String query) {
        // replace "+" -> "%2B" due to Spring Framework 5.x specification change.
        return PLUS_PATTERN.matcher(query).replaceAll("%2B");
    }
}
