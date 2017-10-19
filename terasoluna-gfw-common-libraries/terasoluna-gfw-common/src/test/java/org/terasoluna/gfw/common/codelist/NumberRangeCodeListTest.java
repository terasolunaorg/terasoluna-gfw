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
package org.terasoluna.gfw.common.codelist;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Initializes codelist information with a range of numbers
 */
public class NumberRangeCodeListTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Tests the following : 1) the codelist is initialized with the range of numbers (contents & size) 2) the order of the
     * numbers in the codelist in ascending
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet01() throws Exception {
        Map<String, String> mapTest = new LinkedHashMap<String, String>();
        mapTest.put("001", "label001");
        mapTest.put("002", "label002");
        mapTest.put("003", "label003");

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();

        // check that the codelist is initialized with range of numbers
        assertThat(mapResult.size(), is(mapTest.size()));
        for (String key : mapResult.keySet()) {
            assertThat(mapResult.get(key), is(mapTest.get(key)));
        }

        // check the order of range of numbers
        assertThat(mapResult.equals(mapTest), is(true));

    }

    /**
     * Check that the codelist is unmodifiable
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void TestAfterPropertiesSet02() throws Exception {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();

        mapResult.put("111", "111");

    }

    /**
     * Tests the following : 1) the codelist is initialized with the range of numbers (contents & size) 2) the order of the
     * numbers in the codelist in descending
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet03() throws Exception {
        Map<String, String> mapTest = new LinkedHashMap<String, String>();
        mapTest.put("003", "label003");
        mapTest.put("002", "label002");
        mapTest.put("001", "label001");
        List<String> testList = new ArrayList<String>();
        testList.addAll(mapTest.values());

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(3);
        numberRangeCodeList.setTo(1);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();
        List<String> resultList = new ArrayList<String>();
        resultList.addAll(mapResult.values());

        // check the order of range of numbers
        assertThat(mapResult, is(mapTest));
        assertThat(resultList, is(testList));
    }

    /**
     * checks the condition when from and to are equal
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet04() throws Exception {
        Map<String, String> mapTest = new LinkedHashMap<String, String>();
        mapTest.put("003", "label003");
        List<String> testList = new ArrayList<String>();
        testList.addAll(mapTest.values());

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(3);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();
        List<String> resultList = new ArrayList<String>();
        resultList.addAll(mapResult.values());

        // check the order of range of numbers
        assertThat(mapResult, is(mapTest));
        assertThat(resultList, is(testList));
    }

    /**
     * Tests the following : 1) the interval of separation between two consecutive numbers in ascending order
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet05() throws Exception {
        Map<String, String> mapTest = new LinkedHashMap<String, String>();
        mapTest.put("001", "label001");
        mapTest.put("003", "label003");
        mapTest.put("005", "label005");
        mapTest.put("007", "label007");
        List<String> testList = new ArrayList<String>();
        testList.addAll(mapTest.values());

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(8);

        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(2);

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();
        List<String> resultList = new ArrayList<String>();
        resultList.addAll(mapResult.values());

        // check the order of range of numbers
        assertThat(mapResult, is(mapTest));
        assertThat(resultList, is(testList));
    }

    /**
     * Tests the following : 1) the interval of separation between two consecutive numbers in descending order
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet06() throws Exception {
        Map<String, String> mapTest = new LinkedHashMap<String, String>();
        mapTest.put("008", "label008");
        mapTest.put("006", "label006");
        mapTest.put("004", "label004");
        mapTest.put("002", "label002");
        List<String> testList = new ArrayList<String>();
        testList.addAll(mapTest.values());

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(8);
        numberRangeCodeList.setTo(1);

        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(2);

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();
        List<String> resultList = new ArrayList<String>();
        resultList.addAll(mapResult.values());

        // check the order of range of numbers
        assertThat(mapResult, is(mapTest));
        assertThat(resultList, is(testList));
    }

    /**
     * Tests the following : 1) if negative value is set to interval issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet07() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("interval should be greater than 0");

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(-2);

        numberRangeCodeList.afterPropertiesSet();
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_valueFormat_isNull() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("valueFormat must not be empty");

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat(null);
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(1);

        numberRangeCodeList.afterPropertiesSet();
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_valueFormat_isEmpty() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("valueFormat must not be empty");

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("");
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(1);

        numberRangeCodeList.afterPropertiesSet();
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_labelFormat_isNull() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("labelFormat must not be empty");

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("%s");
        numberRangeCodeList.setLabelFormat(null);
        numberRangeCodeList.setInterval(1);

        numberRangeCodeList.afterPropertiesSet();
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_labelFormat_isEmpty() {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("labelFormat must not be empty");

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("%s");
        numberRangeCodeList.setLabelFormat("");
        numberRangeCodeList.setInterval(1);

        numberRangeCodeList.afterPropertiesSet();
    }
}
