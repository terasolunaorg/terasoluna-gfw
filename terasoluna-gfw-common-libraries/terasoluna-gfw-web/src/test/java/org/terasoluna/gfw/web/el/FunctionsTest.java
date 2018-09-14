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
package org.terasoluna.gfw.web.el;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.format.annotation.DateTimeFormat;

public class FunctionsTest {

    /**
     * test private constructor.
     */
    @Test
    public void testFunctions() throws Exception {
        // set up
        Constructor<Functions> constructor = Functions.class
                .getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());

        constructor.setAccessible(false);
    }

    @Test
    public void testH() {
        assertThat(Functions.h(null), is(""));
        assertThat(Functions.h(""), is(""));
        assertThat(Functions.h("<a href=\"\">"), is(
                "&lt;a href=&quot;&quot;&gt;"));
        assertThat(Functions.h("<a href=''>"), is("&lt;a href=&#39;&#39;&gt;"));
        assertThat(Functions.h("&lt;"), is("&amp;lt;"));
        assertThat(Functions.h(new boolean[] { true, false }), is(
                "[true, false]"));
        assertThat(Functions.h(new int[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new short[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new long[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new byte[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new double[] { 1, 2, 3 }), is(
                "[1.0, 2.0, 3.0]"));
        assertThat(Functions.h(new float[] { 1, 2, 3 }), is("[1.0, 2.0, 3.0]"));
        assertThat(Functions.h(new char[] { 'a', 'b', 'c' }), is("[a, b, c]"));
        assertThat(Functions.h(new String[] { "a", "b", "c" }), is(
                "[a, b, c]"));
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
        assertThat(result, is(""));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH02() {
        // setup input parameters
        String[] input = { "1", "a", "A" };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((String[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH03() {
        // setup input parameters
        boolean[] input = { true, false, true };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((boolean[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH04() {
        // setup input parameters
        char[] input = { '1', 'a', 'A' };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((char[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH05() {
        // setup input parameters
        int[] input = { 1, 5, 9 };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((int[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH06() {
        // setup input parameters
        long[] input = { 1L, 3L, 5L };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((long[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH07() {
        // setup input parameters
        byte[] input = new byte[] { 0, 2, 4 };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((byte[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH08() {
        // setup input parameters
        short[] input = new short[] { 3, 6, 9 };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((short[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH09() {
        // setup input parameters
        float[] input = { 3F, 6F, 9.5F };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((float[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH10() {
        // setup input parameters
        double[] input = { 1.1, 2.2, 3.3 };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((double[]) input)));
    }

    /**
     * Test method for {@link org.terasoluna.gfw.web.el.Functions#h(java.lang.Object)}.
     */
    @Test
    public void testH11() {
        // setup input parameters
        Object[] input = { 1.1, true, "ABC" };

        // run
        String result = Functions.h(input);

        // assert
        assertThat(result, is(Arrays.toString((Object[]) input)));
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
        assertThat(result, is(expct));
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
        assertThat(result, is(expct));
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
        assertThat(result, is(expct));
    }

    @Test
    public void testU() {
        assertThat(Functions.u(null), is(""));
        assertThat(Functions.u(""), is(""));
        assertThat(Functions.u("あいうえお"), is(
                "%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A"));
        assertThat(Functions.u("http://localhost:8080/spring"), is(
                "http://localhost:8080/spring"));
        assertThat(Functions.u("http://localhost:8080/あいうえお"), is(
                "http://localhost:8080/%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A"));
    }

    @Test
    public void testU_NoEncodingSymb() {
        assertThat(Functions.u("/?:@-._~!$'()*,;"), is("/?:@-._~!$'()*,;"));
    }

    @Test
    public void testU_ALPHA() {
        assertThat(Functions.u("a"), is("a"));
    }

    @Test
    public void testU_DIGIT() {
        assertThat(Functions.u("0"), is("0"));
    }

    @Test
    public void testU_EncodingDelimiter() {
        assertThat(Functions.u("+"), is("%2B"));
        assertThat(Functions.u("="), is("%3D"));
        assertThat(Functions.u("&"), is("%26"));
    }

    @Test
    public void testU_EncodingChar() {
        assertThat(Functions.u("%"), is("%25"));
        assertThat(Functions.u("あ"), is("%E3%81%82"));
        assertThat(Functions.u("\n"), is("%0A"));
    }

    @Test
    public void testU_Space() {
        assertThat(Functions.u(" "), is("%20"));
    }

    @Test
    public void testUAndQuery_NoEncodingSymb() {
        String inputStr = "/?:@-._~!$'()*,;";
        String matcher = "name=" + Functions.u(inputStr);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual, is(matcher));
    }

    @Test
    public void testUAndQuery_ALPHA() {
        String inputStr = "a";
        String matcher = "name=" + Functions.u(inputStr);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual, is(matcher));
    }

    @Test
    public void testUAndQuery_DIGIT() {
        String inputStr = "0";
        String matcher = "name=" + Functions.u(inputStr);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual, is(matcher));
    }

    @Test
    public void testUAndQuery_EncodingDelimiter() {
        String[] inputStr = { "+", "&", "=" };
        for (String str : inputStr) {
            String matcher = "name=" + Functions.u(str);
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name", str);
            String actual = Functions.query(map);
            assertThat(actual, is(matcher));
        }
    }

    @Test
    public void testUAndQuery_EncodingChar() {
        String[] inputStr = { "%", "あ", "\n" };
        for (String str : inputStr) {
            String matcher = "name=" + Functions.u(str);
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name", str);
            String actual = Functions.query(map);
            assertThat(actual, is(matcher));
        }
    }

    @Test
    public void testUAndQuery_Space() {
        String inputStr = "spr ing";
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String matcher = "name=" + Functions.u(inputStr);
        map.put("name", inputStr);
        String actual = Functions.query(map);
        assertThat(actual, is(matcher));
    }

    @Test
    public void testBr() {
        assertThat(Functions.br(null), is(""));
        assertThat(Functions.br(""), is(""));
        assertThat(Functions.br("abcde\nabcdf"), is("abcde<br />abcdf"));
    }

    @Test
    public void testBr_CrOnly() {
        assertThat(Functions.br("\r"), is("<br />"));
        assertThat(Functions.br("a\r"), is("a<br />"));
        assertThat(Functions.br("\ra"), is("<br />a"));
        assertThat(Functions.br("\r\r"), is("<br /><br />"));
        assertThat(Functions.br("<br />\r\r<br />"), is(
                "<br /><br /><br /><br />"));
    }

    @Test
    public void testBr_LfOnly() {
        assertThat(Functions.br("\n"), is("<br />"));
        assertThat(Functions.br("a\n"), is("a<br />"));
        assertThat(Functions.br("\na"), is("<br />a"));
        assertThat(Functions.br("\n\n"), is("<br /><br />"));
        assertThat(Functions.br("<br />\n\n<br />"), is(
                "<br /><br /><br /><br />"));
    }

    @Test
    public void testBr_CrLfOnly() {
        assertThat(Functions.br("\r\n"), is("<br />"));
        assertThat(Functions.br("a\r\n"), is("a<br />"));
        assertThat(Functions.br("\r\na"), is("<br />a"));
        assertThat(Functions.br("\r\n\r\n"), is("<br /><br />"));
        assertThat(Functions.br("<br />\r\n\r\n<br />"), is(
                "<br /><br /><br /><br />"));
    }

    @Test
    public void testBr_multi() {
        // \r -> \n -> \r\n
        assertThat(Functions.br("a\rb\nc\r\nd"), is("a<br />b<br />c<br />d"));
        // \n -> \r\n -> \r
        assertThat(Functions.br("a\nb\r\nc\rd"), is("a<br />b<br />c<br />d"));
        // \r\n -> \r -> \n
        assertThat(Functions.br("a\r\nb\rc\nd"), is("a<br />b<br />c<br />d"));
    }

    @Test
    public void testCut() {
        assertThat(Functions.cut(null, 1), is(""));
        assertThat(Functions.cut("", 1), is(""));
        assertThat(Functions.cut("abcdef", 3), is("abc"));
        assertThat(Functions.cut("abcdef", 7), is("abcdef"));
        assertThat(Functions.cut("abcdef", 0), is(""));
        assertThat(Functions.cut("abcdef", -1), is(""));
    }

    @Test
    public void testLink() {
        assertThat(Functions.link(null), is(""));
        assertThat(Functions.link(""), is(""));
        assertThat(Functions.link("go to http://www.google.com"), is(
                "go to <a href=\"http://www.google.com\">http://www.google.com</a>"));
        assertThat(Functions.link("go to https://www.google.com"), is(
                "go to <a href=\"https://www.google.com\">https://www.google.com</a>"));
    }

    @Test
    public void testQuery01() throws Exception {
        assertThat(Functions.query(null), is(""));
        assertThat(Functions.query(""), is(""));
        assertThat(Functions.query("aaaa"), is(""));
        assertThat(Functions.query(1), is(""));
        assertThat(Functions.query(1.0), is(""));
        assertThat(Functions.query(1L), is(""));
        assertThat(Functions.query(new Date()), is(""));
        assertThat(Functions.query(Locale.getDefault()), is(""));
        assertThat(Functions.query(new URL("http://google.com")), is(""));
        assertThat(Functions.query(new Object[] { "1", "2", "3" }), is(""));
        assertThat(Functions.query(new int[] { 1, 2, 3 }), is(""));
        assertThat(Functions.query(Arrays.asList("a", "b", "c")), is(""));
    }

    @Test
    public void testQuery02() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", "Ichiro Suzuki");
        map.put("ja", "すずき いちろう");
        map.put("arr", new Object[] { "xxx", "yyy" });
        String query = Functions.query(map);
        // Spec has been changed between 5.0.0 and 5.0.1
        // 5.0.0 ... arr=xxx,yyy
        // 5.0.1 ... arr[0]=xxx&arr[1]=yyy
        // Either can be populated as { "xxx", "yyy" }
        assertThat(query, is(
                "name=Ichiro%20Suzuki&ja=%E3%81%99%E3%81%9A%E3%81%8D%20%E3%81%84%E3%81%A1%E3%82%8D%E3%81%86&arr%5B0%5D=xxx&arr%5B1%5D=yyy"));
        assertThat(Functions.query(new LinkedHashMap<String, Object>()), is(
                ""));
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
        assertThat(query, is(
                "age=10&date=2000-01-01&list%5B0%5D=a&list%5B1%5D=b&list%5B2%5D=%E3%81%82&name=%E3%81%99%E3%81%9A%E3%81%8D%20%E3%81%84%E3%81%A1%E3%82%8D%E3%81%86"));
        // Spec has been changed betwee 5.0.0 and 5.0.1
        // 5.0.0 ... age=&date=&list=&name=
        // 5.0.1 ... _age=&_date=&_list=&_name=
        assertThat(Functions.query(new Person()), is(
                "_age=&_date=&_list=&_name=")); // null property should show reset parameter that start with "_"
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

        assertThat(actualQuery, is(expectedQuery.toString()));
    }

    @Test
    public void testQuery5_NestedFields() {
        Person p1 = new Person();
        p1.setName("山田");
        p1.setAge(20);
        p1.setDate(new LocalDate(2001, 1, 1).toDate());
        Person p2 = new Person();
        p2.setName("鈴木");
        p2.setAge(30);
        p2.setDate(new LocalDate(1991, 1, 1).toDate());
        MeetingRegisterForm form = new MeetingRegisterForm();
        form.setMeetingId(10);
        form.setParticipants(Arrays.asList(p1, p2));
        String q = Functions.query(form);
        assertThat(q, is("meetingId=10" + "&participants%5B0%5D.age=20"
                + "&participants%5B0%5D.date=2001-01-01"
                + "&_participants%5B0%5D.list="
                + "&participants%5B0%5D.name=%E5%B1%B1%E7%94%B0"
                + "&participants%5B1%5D.age=30"
                + "&participants%5B1%5D.date=1991-01-01"
                + "&_participants%5B1%5D.list="
                + "&participants%5B1%5D.name=%E9%88%B4%E6%9C%A8"));
    }

    @Test
    public void test_Deprecated_mapToQuery() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", "Ichiro Suzuki");
        map.put("age", 10);
        map.put("list", Arrays.asList("xxx", "yyy"));
        assertThat(Functions.mapToQuery(map, null), is(
                "name=Ichiro%20Suzuki&age=10&list=xxx,yyy"));
        assertThat(Functions.mapToQuery(map, new BeanWrapperImpl(new Person())),
                is("name=Ichiro%20Suzuki&age=10&list=xxx,yyy"));

    }

    @Test
    public void testDeprecatedMapToQueryIsNull() {
        assertThat(Functions.mapToQuery(null, null), is(""));
    }

    @Test
    public void testDeprecatedMapToQueryIsEmpty() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        assertThat(Functions.mapToQuery(map, null), is(""));
    }

    @Test
    public void testJs() {
        assertThat(Functions.js(null), is(""));
        assertThat(Functions.js(""), is(""));
        assertThat(Functions.js("a"), is("a"));
        assertThat(Functions.js("'"), is("\\'"));
        assertThat(Functions.js("\""), is("\\\""));
        assertThat(Functions.js("\\"), is("\\\\"));
        assertThat(Functions.js("/"), is("\\/"));
        assertThat(Functions.js("<"), is("\\x3c"));
        assertThat(Functions.js(">"), is("\\x3e"));
        assertThat(Functions.js("\r"), is("\\r"));
        assertThat(Functions.js("\n"), is("\\n"));
        assertThat(Functions.js("b"), is("b"));
    }

    @Test
    public void testHjs() {
        assertThat(Functions.hjs(null), is(""));
        assertThat(Functions.hjs(""), is(""));
        assertThat(Functions.hjs("a"), is("a"));
        assertThat(Functions.hjs("'"), is("\\&#39;"));
        assertThat(Functions.hjs("\""), is("\\&quot;"));
        assertThat(Functions.hjs("\\"), is("\\\\"));
        assertThat(Functions.hjs("/"), is("\\/"));
        assertThat(Functions.hjs("<"), is("\\x3c"));
        assertThat(Functions.hjs(">"), is("\\x3e"));
        assertThat(Functions.hjs("\r"), is("\\r"));
        assertThat(Functions.hjs("\n"), is("\\n"));
        assertThat(Functions.hjs("b"), is("b"));
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
