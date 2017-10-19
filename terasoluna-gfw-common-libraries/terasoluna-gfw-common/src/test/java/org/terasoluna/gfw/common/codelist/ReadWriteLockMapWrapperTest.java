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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract class for the reloadable codelist functionality
 */
public class ReadWriteLockMapWrapperTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * test isEmpty when the map is empty
     */
    @Test
    public void testIsEmpty01() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.isEmpty(), is(true));
    }

    /**
     * test isEmpty when the map is not empty
     */
    @Test
    public void testIsEmpty02() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.isEmpty(), is(false));
    }

    /**
     * test when the map contains the key
     */
    @Test
    public void testContainsKey01() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.containsKey("key1"), is(true));
    }

    /**
     * test when the map does not contain the key
     */
    @Test
    public void testContainsKey02() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.containsKey("key2"), is(false));
    }

    /**
     * test when the map contains the value
     */
    @Test
    public void testContainsValue01() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.containsValue("value1"), is(true));
    }

    /**
     * test when the map does not contain the value
     */
    @Test
    public void testContainsValue02() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.containsValue("value2"), is(false));
    }

    /**
     * test for the case when a key value pair is to be removed form map
     */
    @Test
    public void testRemove01() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.containsValue("value1"), is(true));

        readWriteLockMap.remove("key1");
        assertThat(readWriteLockMap.containsValue("value1"), is(false));
    }

    /**
     * test for the case when a key value pair is to be put to the map
     */
    @Test
    public void testPut01() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map);
        assertThat(readWriteLockMap.containsValue("value2"), is(false));

        readWriteLockMap.put("key2", "value2");
        assertThat(readWriteLockMap.containsValue("value2"), is(true));
    }

    /**
     * test the case of putting a map of couple of values in the map
     */
    @Test
    public void testPutAll01() throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key1", "value1");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("key2", "value2");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map1);
        assertThat(readWriteLockMap.containsValue("value2"), is(false));

        readWriteLockMap.putAll(map2);
        assertThat(readWriteLockMap.containsValue("value2"), is(true));
    }

    /**
     * clear the map
     */
    @Test
    public void testClear01() throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map1);
        assertThat(readWriteLockMap.containsValue("value1"), is(true));
        assertThat(readWriteLockMap.containsValue("value2"), is(true));

        readWriteLockMap.clear();
        assertThat(readWriteLockMap.containsValue("value1"), is(false));
        assertThat(readWriteLockMap.containsValue("value2"), is(false));
    }

    /**
     * test for returning collection of values
     */
    @Test
    public void testValues01() throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map1);

        Collection<String> collection = readWriteLockMap.values();
        assertThat(collection.size(), is(2));
        assertThat(collection.contains("value1"), is(true));
        assertThat(collection.contains("value2"), is(true));
    }

    /**
     * test for returning EntrySet
     */
    @Test
    public void testEntrySet01() throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map1);

        Set<Entry<String, String>> entrySet = readWriteLockMap.entrySet();
        assertThat(entrySet.size(), is(2));
    }

    /**
     * test for Equals method
     */
    @Test
    public void testEquals01() throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("key1", "value1");
        map2.put("key2", "value2");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map1);

        assertThat(readWriteLockMap.equals(map2), is(true));
    }

    /**
     * test for HashCode method
     */
    @Test
    public void testHashCode01() throws Exception {
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("key1", "value1");
        map2.put("key2", "value2");

        ReadWriteLockMapWrapper<String, String> readWriteLockMap = new ReadWriteLockMapWrapper<String, String>(map1);

        assertThat(map2.hashCode() == readWriteLockMap.hashCode(), is(true));
        assertThat(map1.hashCode() == readWriteLockMap.hashCode(), is(true));
    }

}
