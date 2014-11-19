/*
 * Copyright (C) 2013-2014 terasoluna.org
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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;
import org.terasoluna.gfw.web.el.Functions;

public class FunctionsTest {

    @Test
    public void testH() {
        assertThat(Functions.h(null), is(""));
        assertThat(Functions.h(""), is(""));
        assertThat(Functions.h("<a href=\"\">"),
                is("&lt;a href=&quot;&quot;&gt;"));
        assertThat(Functions.h("<a href=''>"), is("&lt;a href=&#39;&#39;&gt;"));
        assertThat(Functions.h("&lt;"), is("&amp;lt;"));
        assertThat(Functions.h(new boolean[] { true, false }),
                is("[true, false]"));
        assertThat(Functions.h(new int[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new short[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new long[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new byte[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(Functions.h(new double[] { 1, 2, 3 }), is("[1.0, 2.0, 3.0]"));
        assertThat(Functions.h(new float[] { 1, 2, 3 }), is("[1.0, 2.0, 3.0]"));
        assertThat(Functions.h(new char[] { 'a', 'b', 'c' }), is("[a, b, c]"));
        assertThat(Functions.h(new String[] { "a", "b", "c" }), is("[a, b, c]"));
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
        assertThat(Functions.u("あいうえお"),
                is("%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A"));
        assertThat(Functions.u("http://localhost:8080/spring"),
                is("http%3A%2F%2Flocalhost%3A8080%2Fspring"));
        assertThat(Functions.u("name1=hoge&name2=hoge2"),
                is("name1%3Dhoge%26name2%3Dhoge2"));
    }

    @Test
    public void testU_Colon(){
        assertThat(Functions.u(":"), is("%3A"));
    }

    @Test
    public void testU_Slash(){
        assertThat(Functions.u("/"), is("%2F"));
    }

    @Test
    public void testU_Hyphen(){
        assertThat(Functions.u("-"), is("-"));
    }

    @Test
    public void testU_UnderScore(){
        assertThat(Functions.u("_"), is("_"));
    }

    @Test
    public void testU_Tiled(){
        assertThat(Functions.u("~"), is("%7E"));
    }

    @Test
    public void testU_Question(){
        assertThat(Functions.u("?"), is("%3F"));
    }

    @Test
    public void testU_Sharp(){
        assertThat(Functions.u("#"), is("%23"));
    }

    @Test
    public void testU_Bracket(){
        assertThat(Functions.u("["), is("%5B"));
        assertThat(Functions.u("]"), is("%5D"));
        assertThat(Functions.u("("), is("%28"));
        assertThat(Functions.u(")"), is("%29"));
    }

    @Test
    public void testU_AtMark(){
        assertThat(Functions.u("@"), is("%40"));
    }

    @Test
    public void testU_ExclamationMark(){
        assertThat(Functions.u("!"), is("%21"));
    }

    @Test
    public void testU_Dollar(){
        assertThat(Functions.u("$"), is("%24"));
    }

    @Test
    public void testU_Ampersand(){
        assertThat(Functions.u("&"), is("%26"));
    }

    @Test
    public void testU_SingleQuotes(){
        assertThat(Functions.u("'"), is("%27"));
    }

    @Test
    public void testU_Asterisk(){
        assertThat(Functions.u("*"), is("*"));
    }

    @Test
    public void testU_PlusSign(){
        assertThat(Functions.u("+"), is("%2B"));
    }

    @Test
    public void testU_Comma(){
        assertThat(Functions.u(","), is("%2C"));
    }
    
    @Test
    public void testU_SemiColon(){
        assertThat(Functions.u(";"), is("%3B"));
    }
    
    @Test
    public void testU_Equal(){
        assertThat(Functions.u("="), is("%3D"));
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
        assertThat(Functions.br("<br />\r\r<br />"),
                is("<br /><br /><br /><br />"));
    }

    @Test
    public void testBr_LfOnly() {
        assertThat(Functions.br("\n"), is("<br />"));
        assertThat(Functions.br("a\n"), is("a<br />"));
        assertThat(Functions.br("\na"), is("<br />a"));
        assertThat(Functions.br("\n\n"), is("<br /><br />"));
        assertThat(Functions.br("<br />\n\n<br />"),
                is("<br /><br /><br /><br />"));
    }

    @Test
    public void testBr_CrLfOnly() {
        assertThat(Functions.br("\r\n"), is("<br />"));
        assertThat(Functions.br("a\r\n"), is("a<br />"));
        assertThat(Functions.br("\r\na"), is("<br />a"));
        assertThat(Functions.br("\r\n\r\n"), is("<br /><br />"));
        assertThat(Functions.br("<br />\r\n\r\n<br />"),
                is("<br /><br /><br /><br />"));
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
        assertThat(
                Functions.link("go to http://www.google.com"),
                is("go to <a href=\"http://www.google.com\">http://www.google.com</a>"));
        assertThat(
                Functions.link("go to https://www.google.com"),
                is("go to <a href=\"https://www.google.com\">https://www.google.com</a>"));
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
        assertThat(
                query,
                is("name=Ichiro%20Suzuki&ja=%E3%81%99%E3%81%9A%E3%81%8D%20%E3%81%84%E3%81%A1%E3%82%8D%E3%81%86&arr=xxx,yyy"));
        assertThat(Functions.query(new LinkedHashMap<String, Object>()), is(""));
    }

    @Test
    public void testQuery03() {
        Person p = new Person();
        p.setName("すずき いちろう");
        p.setAge(10);
        p.setDate(new DateTime().withDate(2000, 1, 1).toDate());
        p.setList(Arrays.asList("a", "b", "あ"));
        String query = Functions.query(p);
        assertThat(
                query,
                is("age=10&date=2000-01-01&list=a,b,%E3%81%82&name=%E3%81%99%E3%81%9A%E3%81%8D%20%E3%81%84%E3%81%A1%E3%82%8D%E3%81%86"));
        assertThat(Functions.query(new Person()), is("age=&date=&list=&name="));
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

    @Test
    public void testMapToQuery_map_isNull() {
        assertThat(Functions.mapToQuery(null, null), is(""));
    }

}

class Person {
    private String name;

    @DateTimeFormat(pattern = "yyy-MM-dd")
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
