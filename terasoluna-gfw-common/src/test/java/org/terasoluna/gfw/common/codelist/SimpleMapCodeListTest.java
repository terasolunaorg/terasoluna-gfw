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

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.codelist.SimpleMapCodeList;

public class SimpleMapCodeListTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * check whether the Map returned by asMap is same as the one set through setMap also check whether getCodeListID returns
     * the same value as the one set through setBeanName
     */
    @Test
    public void testAsMap01() {
        SimpleMapCodeList codeList = new SimpleMapCodeList();

        Map<String, String> mapInput = new HashMap<String, String>();
        for (int i = 0; i < 10; i++) {
            mapInput.put(String.format("%03d", i), String.format("label%03d",
                    i));
        }

        // run
        codeList.setBeanName("CL001");
        codeList.setMap(mapInput);
        Map<String, String> mapOutput = codeList.asMap();

        // assert
        assertThat(mapOutput.size(), is(mapInput.size()));
        for (int i = 0; i < 10; i++) {
            assertThat(mapOutput.get("%03d"), is(mapInput.get("%03d")));
        }
        assertThat(codeList.getCodeListId(), is("CL001"));
    }

    /**
     * check whether the map returned by SetMap is unmodifiable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetMap01() {
        SimpleMapCodeList codeList = new SimpleMapCodeList();

        Map<String, String> mapInput = new HashMap<String, String>();
        for (int i = 0; i < 10; i++) {
            mapInput.put(String.format("%03d", i), String.format("label%03d",
                    i));
        }

        // run
        codeList.setMap(mapInput);

        // check whether the map is unmodifiableMap
        Map<String, String> mapOutput = codeList.asMap();
        mapOutput.put("111", "label111");
    }
}
