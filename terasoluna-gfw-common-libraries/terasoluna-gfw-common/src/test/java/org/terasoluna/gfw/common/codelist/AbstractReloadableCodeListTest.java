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
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.common.logback.LogLevelChangeUtil;

import ch.qos.logback.classic.Logger;

/**
 * Abstract class for the reloadable codelist functionality
 */
public class AbstractReloadableCodeListTest {

    private Logger logger = (Logger) LoggerFactory.getLogger(
            AbstractReloadableCodeList.class);

    /**
     * In case LazyInit is set to false
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet01() throws Exception {
        // create expected values
        Map<String, String> mapExpectedFirstFetch = new HashMap<String, String>();
        mapExpectedFirstFetch.put("001", "firstRetrieve001");
        mapExpectedFirstFetch.put("002", "firstRetrieve002");
        mapExpectedFirstFetch.put("003", "firstRetrieve003");

        Map<String, String> mapExpectedSecondFetch = new HashMap<String, String>();
        mapExpectedSecondFetch.put("001", "secondRetrieve001");
        mapExpectedSecondFetch.put("002", "secondRetrieve002");
        mapExpectedSecondFetch.put("003", "secondRetrieve003");

        // create target
        AbstractReloadableCodeList reloadableCodeList = new ExtendedReloadableCodelist();

        // fetch codelist map for the first time
        Map<String, String> mapResult1 = reloadableCodeList.asMap();

        // assert
        assertThat(mapResult1.size(), is(mapExpectedFirstFetch.size()));
        for (String key : mapResult1.keySet()) {
            assertThat(mapResult1.get(key), is(mapExpectedFirstFetch.get(key)));
        }

        // fetch codelist map for the first time
        reloadableCodeList.afterPropertiesSet();

        // fetch codelist map again immediately
        Map<String, String> mapResult2 = reloadableCodeList.asMap();
        assertThat(mapResult2.size(), is(mapExpectedSecondFetch.size()));
        for (String key : mapResult2.keySet()) {
            assertThat(mapResult2.get(key), is(mapExpectedSecondFetch.get(
                    key)));
        }
    }

    /**
     * In case LazyInit is set to true
     * @throws Exception
     */
    @Test
    public void TestAfterPropertiesSet02() throws Exception {
        // create expected values
        Map<String, String> mapExpectedFirstFetch = new HashMap<String, String>();
        mapExpectedFirstFetch.put("001", "firstRetrieve001");
        mapExpectedFirstFetch.put("002", "firstRetrieve002");
        mapExpectedFirstFetch.put("003", "firstRetrieve003");

        Map<String, String> mapExpectedSecondFetch = new HashMap<String, String>();
        mapExpectedSecondFetch.put("001", "secondRetrieve001");
        mapExpectedSecondFetch.put("002", "secondRetrieve002");
        mapExpectedSecondFetch.put("003", "secondRetrieve003");

        // create target
        AbstractReloadableCodeList abstractReloadableCodeList = new ExtendedReloadableCodelist();

        abstractReloadableCodeList.setLazyInit(true);

        // fetch codelist map for the first time
        Map<String, String> mapResult1 = abstractReloadableCodeList.asMap();

        // assert
        assertThat(mapResult1.size(), is(mapExpectedFirstFetch.size()));
        for (String key : mapResult1.keySet()) {
            assertThat(mapResult1.get(key), is(mapExpectedFirstFetch.get(key)));
        }

        // fetch codelist map for the first time
        abstractReloadableCodeList.afterPropertiesSet();

        // fetch codelist map again immediately
        Map<String, String> mapResult2 = abstractReloadableCodeList.asMap();
        assertThat(mapResult2.size(), is(mapExpectedFirstFetch.size()));
        for (String key : mapResult2.keySet()) {
            assertThat(mapResult2.get(key), is(mapExpectedFirstFetch.get(key)));
        }
    }

    @Test
    public void testRefreshIsDebugEnabledFalse() throws Exception {
        // set up
        LogLevelChangeUtil.setLogLevel(LogLevelChangeUtil.LogLevel.INFO);

        // create expected values
        Map<String, String> mapExpectedFirstFetch = new HashMap<String, String>();
        mapExpectedFirstFetch.put("001", "firstRetrieve001");
        mapExpectedFirstFetch.put("002", "firstRetrieve002");
        mapExpectedFirstFetch.put("003", "firstRetrieve003");

        Map<String, String> mapExpectedSecondFetch = new HashMap<String, String>();
        mapExpectedSecondFetch.put("001", "secondRetrieve001");
        mapExpectedSecondFetch.put("002", "secondRetrieve002");
        mapExpectedSecondFetch.put("003", "secondRetrieve003");

        // create target
        AbstractReloadableCodeList reloadableCodeList = new ExtendedReloadableCodelist();

        // fetch codelist map for the first time
        Map<String, String> mapResult1 = reloadableCodeList.asMap();

        // assert
        assertThat(mapResult1.size(), is(mapExpectedFirstFetch.size()));
        for (String key : mapResult1.keySet()) {
            assertThat(mapResult1.get(key), is(mapExpectedFirstFetch.get(key)));
        }
        assertThat(logger.isDebugEnabled(), is(false));

        // fetch codelist map for the first time
        reloadableCodeList.afterPropertiesSet();

        // fetch codelist map again immediately
        Map<String, String> mapResult2 = reloadableCodeList.asMap();
        assertThat(mapResult2.size(), is(mapExpectedSecondFetch.size()));
        for (String key : mapResult2.keySet()) {
            assertThat(mapResult2.get(key), is(mapExpectedSecondFetch.get(
                    key)));
        }

        // init log level
        LogLevelChangeUtil.resetLogLevel();
    }

}

class ExtendedReloadableCodelist extends AbstractReloadableCodeList {
    private int i = 0;

    @Override
    protected Map<String, String> retrieveMap() {
        return delegateRetrieveMap(i++);
    }

    private Map<String, String> delegateRetrieveMap(int i) {
        Map<String, String> map = new HashMap<String, String>();
        if (i == 0) {
            map.put("001", "firstRetrieve001");
            map.put("002", "firstRetrieve002");
            map.put("003", "firstRetrieve003");
        } else {
            map.put("001", "secondRetrieve001");
            map.put("002", "secondRetrieve002");
            map.put("003", "secondRetrieve003");
        }
        return map;
    }

}
