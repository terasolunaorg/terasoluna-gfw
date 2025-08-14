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
package org.terasoluna.gfw.common.codelist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Initializes codelist information with a range of numbers
 */
public class NumberRangeCodeListTest {

    /**
     * Tests the following : 1) the codelist is initialized with the range of numbers (contents &
     * size) 2) the order of the numbers in the codelist in ascending
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
        assertThat(mapResult).isEqualTo(mapTest);

        // check the order of range of numbers
        assertThat(mapResult.equals(mapTest)).isEqualTo(true);

    }

    /**
     * Check that the codelist is unmodifiable
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet02() throws Exception {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");

        numberRangeCodeList.afterPropertiesSet();
        Map<String, String> mapResult = numberRangeCodeList.asMap();

        assertThrows(UnsupportedOperationException.class, () -> {
            mapResult.put("111", "111");
        });
    }

    /**
     * Tests the following : 1) the codelist is initialized with the range of numbers (contents &
     * size) 2) the order of the numbers in the codelist in descending
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
        assertThat(mapResult).isEqualTo(mapTest);
        assertThat(resultList).isEqualTo(testList);
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
        assertThat(mapResult).isEqualTo(mapTest);
        assertThat(resultList).isEqualTo(testList);
    }

    /**
     * Tests the following : 1) the interval of separation between two consecutive numbers in
     * ascending order
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
        assertThat(mapResult).isEqualTo(mapTest);
        assertThat(resultList).isEqualTo(testList);
    }

    /**
     * Tests the following : 1) the interval of separation between two consecutive numbers in
     * descending order
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
        assertThat(mapResult).isEqualTo(mapTest);
        assertThat(resultList).isEqualTo(testList);
    }

    /**
     * Tests the following : 1) if negative value is set to interval issues/6 added message
     * assertion
     */
    @Test
    public void TestAfterPropertiesSet07() {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("00%s");
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(-2);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> numberRangeCodeList.afterPropertiesSet());
        assertThat(ex.getMessage()).isEqualTo("interval should be greater than 0");
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_valueFormat_isNull() {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat(null);
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(1);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> numberRangeCodeList.afterPropertiesSet());
        assertThat(ex.getMessage()).isEqualTo("valueFormat must not be empty");
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_valueFormat_isEmpty() {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("");
        numberRangeCodeList.setLabelFormat("label00%s");
        numberRangeCodeList.setInterval(1);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> numberRangeCodeList.afterPropertiesSet());
        assertThat(ex.getMessage()).isEqualTo("valueFormat must not be empty");
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_labelFormat_isNull() {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("%s");
        numberRangeCodeList.setLabelFormat(null);
        numberRangeCodeList.setInterval(1);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> numberRangeCodeList.afterPropertiesSet());
        assertThat(ex.getMessage()).isEqualTo("labelFormat must not be empty");
    }

    /*
     * issues/6 added message assertion
     */
    @Test
    public void TestAfterPropertiesSet_labelFormat_isEmpty() {

        NumberRangeCodeList numberRangeCodeList = new NumberRangeCodeList();
        numberRangeCodeList.setFrom(1);
        numberRangeCodeList.setTo(3);
        numberRangeCodeList.setValueFormat("%s");
        numberRangeCodeList.setLabelFormat("");
        numberRangeCodeList.setInterval(1);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> numberRangeCodeList.afterPropertiesSet());
        assertThat(ex.getMessage()).isEqualTo("labelFormat must not be empty");
    }
}
