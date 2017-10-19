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

import java.util.Map;

/**
 * Interface of Codelist functionality
 */
public interface CodeList {

    /**
     * Returns the codelist as a Map.<br>
     * <p>
     * Map must be unmodifiable. Key part of the map is "value" of the codelist.<br>
     * Value part of the map is "label" of the codelist<br>
     * </p>
     * @return Map codelist
     */
    Map<String, String> asMap();

    /**
     * Returns bean ID of the codelist bean
     * @return String Bean Id of the codelist bean
     */
    String getCodeListId();
}
