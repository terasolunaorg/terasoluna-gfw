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
package org.terasoluna.gfw.common.codelist.i18n;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class AbstractI18nCodeListTest {

    @Test
    public void testAsMap() {
        AbstractI18nCodeList impl = new AbstractI18nCodeList() {

            @Override
            public Map<String, String> asMap(Locale locale) {
                // Following return value will be implementation specific
                // Just returning an newly created instance for testing
                return new HashMap<String, String>();
            }

        };

        // Call super class asMap method
        Map<String, String> map = impl.asMap();
        assertNotNull(map);
    }

}
