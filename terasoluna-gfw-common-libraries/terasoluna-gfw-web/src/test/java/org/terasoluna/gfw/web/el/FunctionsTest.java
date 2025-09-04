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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

public class FunctionsTest {

    /**
     * test private constructor.
     */
    @Test
    public void testFunctions() throws Exception {
        // set up
        Constructor<Functions> constructor = Functions.class.getDeclaredConstructor();
        assertThat(constructor.canAccess(null)).isFalse();
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance()).isNotNull();

        constructor.setAccessible(false);
    }

    @Test
    public void testH() {
        assertThat(Functions.h(null)).isEmpty();
        assertThat(Functions.h("")).isEmpty();
        assertThat(Functions.h("<a href=\"\">")).isEqualTo("&lt;a href=&quot;&quot;&gt;");
        assertThat(Functions.h("<a href=''>")).isEqualTo("&lt;a href=&#39;&#39;&gt;");
        assertThat(Functions.h("&lt;")).isEqualTo("&amp;lt;");
        assertThat(Functions.h(new boolean[] {true, false})).isEqualTo("[true, false]");
        assertThat(Functions.h(new int[] {1, 2, 3})).isEqualTo("[1, 2, 3]");
        assertThat(Functions.h(new short[] {1, 2, 3})).isEqualTo("[1, 2, 3]");
        assertThat(Functions.h(new long[] {1, 2, 3})).isEqualTo("[1, 2, 3]");
        assertThat(Functions.h(new byte[] {1, 2, 3})).isEqualTo("[1, 2, 3]");
        assertThat(Functions.h(new double[] {1, 2, 3})).isEqualTo("[1.0, 2.0, 3.0]");
        assertThat(Functions.h(new float[] {1, 2, 3})).isEqualTo("[1.0, 2.0, 3.0]");
        assertThat(Functions.h(new char[] {'a', 'b', 'c'})).isEqualTo("[a, b, c]");
        assertThat(Functions.h(new String[] {"a", "b", "c"})).isEqualTo("[a, b, c]");
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH01() {
        // setup input parameters
        Object input = null;

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEmpty();
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH02() {
        // setup input parameters
        String[] input = {"1", "a", "A"};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((String[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH03() {
        // setup input parameters
        boolean[] input = {true, false, true};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((boolean[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH04() {
        // setup input parameters
        char[] input = {'1', 'a', 'A'};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((char[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH05() {
        // setup input parameters
        int[] input = {1, 5, 9};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((int[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH06() {
        // setup input parameters
        long[] input = {1L, 3L, 5L};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((long[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH07() {
        // setup input parameters
        byte[] input = new byte[] {0, 2, 4};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((byte[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH08() {
        // setup input parameters
        short[] input = new short[] {3, 6, 9};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((short[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH09() {
        // setup input parameters
        float[] input = {3F, 6F, 9.5F};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((float[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH10() {
        // setup input parameters
        double[] input = {1.1, 2.2, 3.3};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((double[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH11() {
        // setup input parameters
        Object[] input = {1.1, true, "ABC"};

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(Arrays.toString((Object[]) input));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH12() {
        // setup input parameters
        Object input = "12345abcde&<>\"\'";
        Object expct = "12345abcde&amp;&lt;&gt;&quot;&#39;";

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(expct);
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH13() {
        // setup input parameters
        Object input = "&<>\"\'";
        Object expct = "&amp;&lt;&gt;&quot;&#39;";

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(expct);
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH14() {
        // setup input parameters
        Object input = "a>1";
        Object expct = "a&gt;1";

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result).isEqualTo(expct);
    }

    @Test
    public void testU() {
        assertThat(Functions.u(null)).isEmpty();
        assertThat(Functions.u("")).isEmpty();
        assertThat(Functions.u("あいうえお")).isEqualTo("%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A");
        assertThat(Functions.u("http://localhost:8080/spring"))
                .isEqualTo("http://localhost:8080/spring");
        assertThat(Functions.u("http://localhost:8080/あいうえお"))
                .isEqualTo("http://localhost:8080/%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A");
    }

    @Test
    public void testU_NoEncodingSymb() {
        assertThat(Functions.u("/?:@-._~!$'()*,;")).isEqualTo("/?:@-._~!$'()*,;");
    }

    @Test
    public void testU_ALPHA() {
        assertThat(Functions.u("a")).isEqualTo("a");
    }

    @Test
    public void testU_DIGIT() {
        assertThat(Functions.u("0")).isEqualTo("0");
    }

    @Test
    public void testU_EncodingDelimiter() {
        assertThat(Functions.u("+")).isEqualTo("%2B");
        assertThat(Functions.u("=")).isEqualTo("%3D");
        assertThat(Functions.u("&")).isEqualTo("%26");
    }

    @Test
    public void testU_EncodingChar() {
        assertThat(Functions.u("%")).isEqualTo("%25");
        assertThat(Functions.u("あ")).isEqualTo("%E3%81%82");
        assertThat(Functions.u("\n")).isEqualTo("%0A");
    }

    @Test
    public void testU_Space() {
        assertThat(Functions.u(" ")).isEqualTo("%20");
    }

    @Test
    public void testUAndQuery_NoEncodingSymb() {
        String inputStr = "/?:@-._~!$'()*,;";
        String matcher = "name=" + Functions.u(inputStr);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual).isEqualTo(matcher);
    }

    @Test
    public void testUAndQuery_ALPHA() {
        String inputStr = "a";
        String matcher = "name=" + Functions.u(inputStr);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual).isEqualTo(matcher);
    }

    @Test
    public void testUAndQuery_DIGIT() {
        String inputStr = "0";
        String matcher = "name=" + Functions.u(inputStr);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual).isEqualTo(matcher);
    }

    @Test
    public void testUAndQuery_EncodingDelimiter() {
        String[] inputStr = {"+", "&", "="};
        for (String str : inputStr) {
            String matcher = "name=" + Functions.u(str);
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name", str);
            String actual = Functions.query(map);
            assertThat(actual).isEqualTo(matcher);
        }
    }

    @Test
    public void testUAndQuery_EncodingChar() {
        String[] inputStr = {"%", "あ", "\n"};
        for (String str : inputStr) {
            String matcher = "name=" + Functions.u(str);
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name", str);
            String actual = Functions.query(map);
            assertThat(actual).isEqualTo(matcher);
        }
    }

    @Test
    public void testUAndQuery_Space() {
        String inputStr = "spr ing";
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String matcher = "name=" + Functions.u(inputStr);
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual).isEqualTo(matcher);
    }

    @Test
    public void testBr() {
        assertThat(Functions.br(null)).isEmpty();
        assertThat(Functions.br("")).isEmpty();
        assertThat(Functions.br("abcde\nabcdf")).isEqualTo("abcde<br />abcdf");
    }

    @Test
    public void testBr_CrOnly() {
        assertThat(Functions.br("\r")).isEqualTo("<br />");
        assertThat(Functions.br("a\r")).isEqualTo("a<br />");
        assertThat(Functions.br("\ra")).isEqualTo("<br />a");
        assertThat(Functions.br("\r\r")).isEqualTo("<br /><br />");
        assertThat(Functions.br("<br />\r\r<br />")).isEqualTo("<br /><br /><br /><br />");
    }

    @Test
    public void testBr_LfOnly() {
        assertThat(Functions.br("\n")).isEqualTo("<br />");
        assertThat(Functions.br("a\n")).isEqualTo("a<br />");
        assertThat(Functions.br("\na")).isEqualTo("<br />a");
        assertThat(Functions.br("\n\n")).isEqualTo("<br /><br />");
        assertThat(Functions.br("<br />\n\n<br />")).isEqualTo("<br /><br /><br /><br />");
    }

    @Test
    public void testBr_CrLfOnly() {
        assertThat(Functions.br("\r\n")).isEqualTo("<br />");
        assertThat(Functions.br("a\r\n")).isEqualTo("a<br />");
        assertThat(Functions.br("\r\na")).isEqualTo("<br />a");
        assertThat(Functions.br("\r\n\r\n")).isEqualTo("<br /><br />");
        assertThat(Functions.br("<br />\r\n\r\n<br />")).isEqualTo("<br /><br /><br /><br />");
    }

    @Test
    public void testBr_multi() {
        // \r -> \n -> \r\n
        assertThat(Functions.br("a\rb\nc\r\nd")).isEqualTo("a<br />b<br />c<br />d");
        // \n -> \r\n -> \r
        assertThat(Functions.br("a\nb\r\nc\rd")).isEqualTo("a<br />b<br />c<br />d");
        // \r\n -> \r -> \n
        assertThat(Functions.br("a\r\nb\rc\nd")).isEqualTo("a<br />b<br />c<br />d");
    }

    @Test
    public void testCut() {
        assertThat(Functions.cut(null, 1)).isEmpty();
        assertThat(Functions.cut("", 1)).isEmpty();
        assertThat(Functions.cut("abcdef", 3)).isEqualTo("abc");
        assertThat(Functions.cut("abcdef", 7)).isEqualTo("abcdef");
        assertThat(Functions.cut("abcdef", 0)).isEmpty();
        assertThat(Functions.cut("abcdef", -1)).isEmpty();
    }

    @Test
    public void testLink() {
        assertThat(Functions.link(null)).isEmpty();
        assertThat(Functions.link("")).isEmpty();
        assertThat(Functions.link("go to http://www.google.com"))
                .isEqualTo("go to <a href=\"http://www.google.com\">http://www.google.com</a>");
        assertThat(Functions.link("go to https://www.google.com"))
                .isEqualTo("go to <a href=\"https://www.google.com\">https://www.google.com</a>");

        // Schemes not supported
        assertThat(Functions.link("ftp://test.com/test.txt")).isEqualTo("ftp://test.com/test.txt");
        assertThat(Functions.link("file:///etc/hosts")).isEqualTo("file:///etc/hosts");

        // Reserved Characters gen-delims (":" / "/" / "?" / "#" / "[" / "]" / "@")
        assertThat(Functions.link("http://user@[::1]:8000/?/#"))
                .isEqualTo("<a href=\"http://user@[::1]:8000/?/#\">http://user@[::1]:8000/?/#</a>");

        // Reserved Characters sub-delims ("!" / "$" / "&" / "'" / "(" / ")"
        // / "*" / "+" / "," / ";" / "=")
        assertThat(Functions.link("http://test.com/!$*+/'aaa'/(bbb)//?a=1&b=2,c=3;d=4")).isEqualTo(
                "<a href=\"http://test.com/!$*+/'aaa'/(bbb)//?a=1&b=2,c=3;d=4\">http://test.com/!$*+/'aaa'/(bbb)//?a=1&b=2,c=3;d=4</a>");

        // Unreserved Characters (ALPHA / DIGIT / "-" / "." / "_" / "~")
        assertThat(Functions.link("http://test.com:8000/~user/web-aaa/web_bbb/")).isEqualTo(
                "<a href=\"http://test.com:8000/~user/web-aaa/web_bbb/\">http://test.com:8000/~user/web-aaa/web_bbb/</a>");

        // Percent-Encoding ("%" HEXDIG HEXDIG)
        assertThat(Functions.link("http://test.com/%E3%81%82"))
                .isEqualTo("<a href=\"http://test.com/%E3%81%82\">http://test.com/%E3%81%82</a>");

        // Other Characters
        assertThat(Functions.link("http://test.com/\""))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>\"");
        assertThat(Functions.link("http://test.com/<"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a><");
        assertThat(Functions.link("http://test.com/>"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>>");
        assertThat(Functions.link("http://test.com/\\"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>\\");
        assertThat(Functions.link("http://test.com/^"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>^");
        assertThat(Functions.link("http://test.com/`"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>`");
        assertThat(Functions.link("http://test.com/{"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>{");
        assertThat(Functions.link("http://test.com/}"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>}");
        assertThat(Functions.link("http://test.com/|"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>|");
        assertThat(Functions.link("http://test.com/ｱ"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>ｱ");
        assertThat(Functions.link("http://test.com/あ"))
                .isEqualTo("<a href=\"http://test.com/\">http://test.com/</a>あ");
    }

    @Test
    public void testQuery01() throws Exception {
        assertThat(Functions.query(null)).isEmpty();
        assertThat(Functions.query("")).isEmpty();
        assertThat(Functions.query("aaaa")).isEmpty();
        assertThat(Functions.query(1)).isEmpty();
        assertThat(Functions.query(1.0)).isEmpty();
        assertThat(Functions.query(1L)).isEmpty();
        assertThat(Functions.query(new Date())).isEmpty();
        assertThat(Functions.query(Locale.getDefault())).isEmpty();
        assertThat(Functions.query(new URL("http://google.com"))).isEmpty();
        assertThat(Functions.query(new Object[] {"1", "2", "3"})).isEmpty();
        assertThat(Functions.query(new int[] {1, 2, 3})).isEmpty();
        assertThat(Functions.query(Arrays.asList("a", "b", "c"))).isEmpty();
    }

    @Test
    public void testQuery02() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", "Ichiro Suzuki");
        map.put("ja", "すずき いちろう");
        map.put("arr", new Object[] {"xxx", "yyy"});
        String query = Functions.query(map);
        // Spec has been changed between 5.0.0 and 5.0.1
        // 5.0.0 ... arr=xxx,yyy
        // 5.0.1 ... arr[0]=xxx&arr[1]=yyy
        // Either can be populated as { "xxx", "yyy" }
        assertThat(query).isEqualTo(
                "name=Ichiro%20Suzuki&ja=%E3%81%99%E3%81%9A%E3%81%8D%20%E3%81%84%E3%81%A1%E3%82%8D%E3%81%86&arr%5B0%5D=xxx&arr%5B1%5D=yyy");
        assertThat(Functions.query(new LinkedHashMap<String, Object>())).isEmpty();
    }

    @Test
    public void testQuery03() throws Exception {
        Person p = new Person();
        p.setName("すずき いちろう");
        p.setAge(10);
        p.setList(Arrays.asList("a", "b", "あ"));
        p.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));
        String query = Functions.query(p);
        // Spec has been changed between 5.0.0 and 5.0.1
        // 5.0.0 ... list=a,b,あ
        // 5.0.1 ... list[0]=a&list[1]=b&list[2]=あ
        // Either can be populated as { "a", "b", "あ" }
        assertThat(query).isEqualTo(
                "age=10&date=2000-01-01&list%5B0%5D=a&list%5B1%5D=b&list%5B2%5D=%E3%81%82&name=%E3%81%99%E3%81%9A%E3%81%8D%20%E3%81%84%E3%81%A1%E3%82%8D%E3%81%86");
        // Spec has been changed betwee 5.0.0 and 5.0.1
        // 5.0.0 ... age=&date=&list=&name=
        // 5.0.1 ... _age=&_date=&_list=&_name=
        assertThat(Functions.query(new Person())).isEqualTo("_age=&_date=&_list=&_name="); // null
        // property
        // should show
        // reset
        // parameter
        // that start
        // with "_"
    }

    @Test
    public void testQuery04_url_encoding_for_queryString() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // Specify a characters(#[]) that cannot use in the query on RFC3986.
        // See http://tools.ietf.org/html/rfc3986#section-3.4.
        map.put("key#1", "value#1");
        map.put("key[2", "value[2");
        map.put("key]3", "value]3");
        // Specify a characters(&=+) that must be escaped in the query on general web server.
        map.put("key&4", "value&4");
        map.put("key=5", "value=5");
        map.put("key+6", "value+6");

        String actualQuery = Functions.query(map);

        StringBuilder expectedQuery = new StringBuilder();
        expectedQuery.append("key%231=value%231"); // #
        expectedQuery.append("&").append("key%5B2=value%5B2"); // [
        expectedQuery.append("&").append("key%5D3=value%5D3"); // ]
        expectedQuery.append("&").append("key%264=value%264"); // &
        expectedQuery.append("&").append("key%3D5=value%3D5"); // =
        expectedQuery.append("&").append("key%2B6=value%2B6"); // +

        assertThat(actualQuery).isEqualTo(expectedQuery.toString());
    }

    @Test
    public void testQuery5_NestedFields() {
        Person p1 = new Person();
        p1.setName("山田");
        p1.setAge(20);
        p1.setDate(
                Date.from(LocalDateTime.of(2001, 1, 1, 0, 0, 0).toInstant(ZoneOffset.ofHours(9))));
        Person p2 = new Person();
        p2.setName("鈴木");
        p2.setAge(30);
        p2.setDate(
                Date.from(LocalDateTime.of(1991, 1, 1, 0, 0, 0).toInstant(ZoneOffset.ofHours(9))));
        MeetingRegisterForm form = new MeetingRegisterForm();
        form.setMeetingId(10);
        form.setParticipants(Arrays.asList(p1, p2));
        String q = Functions.query(form);
        assertThat(q).isEqualTo("meetingId=10" + "&participants%5B0%5D.age=20"
                + "&participants%5B0%5D.date=2001-01-01" + "&_participants%5B0%5D.list="
                + "&participants%5B0%5D.name=%E5%B1%B1%E7%94%B0" + "&participants%5B1%5D.age=30"
                + "&participants%5B1%5D.date=1991-01-01" + "&_participants%5B1%5D.list="
                + "&participants%5B1%5D.name=%E9%88%B4%E6%9C%A8");
    }

    @Test
    public void testJs() {
        assertThat(Functions.js(null)).isEmpty();
        assertThat(Functions.js("")).isEmpty();
        assertThat(Functions.js("a")).isEqualTo("a");
        assertThat(Functions.js("'")).isEqualTo("\\'");
        assertThat(Functions.js("\"")).isEqualTo("\\\"");
        assertThat(Functions.js("\\")).isEqualTo("\\\\");
        assertThat(Functions.js("/")).isEqualTo("\\/");
        assertThat(Functions.js("<")).isEqualTo("\\x3c");
        assertThat(Functions.js(">")).isEqualTo("\\x3e");
        assertThat(Functions.js("\r")).isEqualTo("\\r");
        assertThat(Functions.js("\n")).isEqualTo("\\n");
        assertThat(Functions.js("b")).isEqualTo("b");
    }

    @Test
    public void testHjs() {
        assertThat(Functions.hjs(null)).isEmpty();
        assertThat(Functions.hjs("")).isEmpty();
        assertThat(Functions.hjs("a")).isEqualTo("a");
        assertThat(Functions.hjs("'")).isEqualTo("\\&#39;");
        assertThat(Functions.hjs("\"")).isEqualTo("\\&quot;");
        assertThat(Functions.hjs("\\")).isEqualTo("\\\\");
        assertThat(Functions.hjs("/")).isEqualTo("\\/");
        assertThat(Functions.hjs("<")).isEqualTo("\\x3c");
        assertThat(Functions.hjs(">")).isEqualTo("\\x3e");
        assertThat(Functions.hjs("\r")).isEqualTo("\\r");
        assertThat(Functions.hjs("\n")).isEqualTo("\\n");
        assertThat(Functions.hjs("b")).isEqualTo("b");
    }

}


class Person {
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Integer age;

    private List<String> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}


class MeetingRegisterForm {
    private Integer meetingId;

    private List<Person> participants;

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }
}
