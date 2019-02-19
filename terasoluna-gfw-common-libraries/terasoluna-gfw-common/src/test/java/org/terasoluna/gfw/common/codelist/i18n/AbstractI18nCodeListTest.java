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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

public class AbstractI18nCodeListTest {

    @Test
    public void testAsMap() {
        AbstractI18nCodeList impl = new AbstractI18nCodeList() {

            @Override
            public Map<String, String> asMap(Locale locale) {
                return Collections.singletonMap("language", locale
                        .getLanguage());
            }

        };

        Locale.setDefault(Locale.ENGLISH);
        assertThat(impl.asMap().get("language"), is(Locale.ENGLISH
                .getLanguage()));

        LocaleContextHolder.setLocale(Locale.GERMAN);
        assertThat(impl.asMap().get("language"), is(Locale.GERMAN
                .getLanguage()));

        LocaleContextHolder.setLocale(Locale.FRENCH);
        assertThat(impl.asMap().get("language"), is(Locale.FRENCH
                .getLanguage()));

        Locale.setDefault(Locale.JAPANESE);
        assertThat(impl.asMap().get("language"), is(Locale.FRENCH
                .getLanguage()));
    }

}
