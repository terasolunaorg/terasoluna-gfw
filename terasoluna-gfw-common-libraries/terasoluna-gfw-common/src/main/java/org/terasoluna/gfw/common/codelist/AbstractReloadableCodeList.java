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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Abstract implementation of reloadable {@link CodeList}
 */
public abstract class AbstractReloadableCodeList extends AbstractCodeList
                                                 implements ReloadableCodeList,
                                                 InitializingBean {

    /**
     * {@link CodeList} must be thread safe.
     */
    private final ReadWriteLockMapWrapper<String, String> cachedMap = new ReadWriteLockMapWrapper<String, String>(new LinkedHashMap<String, String>());

    /**
     * Codelist information visible externally. It is unmodifiable and thread safe.
     */
    private volatile Map<String, String> exposedMap = null;

    /**
     * Lazy initialization flag
     */
    private boolean lazyInit = false;

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Returns the codelist as an Immutable Thread-safe Map instance.
     * @return Map codelist in the form of an Immutable Thread-safe Map
     * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
     */
    @Override
    public final Map<String, String> asMap() {

        // If exposedMap is null, that means it is called for the first time
        // and lazyInit must be set to true
        if (exposedMap == null) {
            refresh();
        }
        return exposedMap;
    }

    /**
     * Flag that determines whether the codelist information needs to be eager fetched. <br>
     * @param lazyInit flag
     */
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Reloads the codelist.
     * @see org.terasoluna.gfw.common.codelist.ReloadableCodeList#refresh()
     */
    @Override
    public final void refresh() {
        if (logger.isDebugEnabled()) {
            logger.debug("refresh codelist codeListId={}", getCodeListId());
        }
        synchronized (cachedMap) {
            cachedMap.clearAndPutAll(retrieveMap());
            exposedMap = Collections.unmodifiableMap(cachedMap);
        }
    }

    /**
     * This method is called after the properties of the codelist are set.
     * <p>
     * Checks the lazyInit flag to determine whether the <br>
     * codelist should be refreshed after the properties are set.<br>
     * If lazyInit flag is set to true, the codelist is not refreshed immediately. <br>
     * If it is set to false, it is refreshed (values re-loaded) immediately after the <br>
     * properties are loaded<br>
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        if (!lazyInit) {
            refresh();
        }
    }

    /**
     * Fetches the latest codelist information from the database and returns it as a map
     * @return Map codelist information
     */
    abstract protected Map<String, String> retrieveMap();
}
