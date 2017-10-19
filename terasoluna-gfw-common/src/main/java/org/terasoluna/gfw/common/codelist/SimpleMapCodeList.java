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
import java.util.Map;

/**
 * Implementation class for the codelist functionality. codelist is maintained as a map.
 */
public class SimpleMapCodeList extends AbstractCodeList {

    /**
     * Property holding the codelist information
     */
    private Map<String, String> map;

    /**
     * Returns the codelist Information as a Map instance
     * @return Map codelist information
     * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
     */
    @Override
    public Map<String, String> asMap() {
        return map;
    }

    /**
     * Sets codelist information in a Map This map is stored as an unmodifiable Map
     * @param map codelist information
     */
    public void setMap(Map<String, String> map) {
        this.map = Collections.unmodifiableMap(map);
    }

}
