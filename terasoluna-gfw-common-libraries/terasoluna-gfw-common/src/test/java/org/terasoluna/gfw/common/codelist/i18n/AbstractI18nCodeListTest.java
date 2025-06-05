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
package org.terasoluna.gfw.common.codelist.i18n;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import com.google.common.collect.Sets;

public class AbstractI18nCodeListTest {

    private Locale originalLocale;

    @BeforeEach
    public void before() {
        originalLocale = Locale.getDefault();
    }

    @AfterEach
    public void after() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testAsMap() {
        AbstractI18nCodeList impl = new AbstractI18nCodeList() {

            @Override
            protected Set<Locale> registerCodeListLocales() {
                return Sets.newHashSet();
            }

            @Override
            protected Map<String, String> obtainMap(Locale locale) {
                return Collections.singletonMap("language", locale.getLanguage());
            }
        };

        Locale.setDefault(Locale.ENGLISH);
        assertThat(impl.asMap(), hasEntry("language", Locale.ENGLISH.getLanguage()));

        LocaleContextHolder.setLocale(Locale.GERMAN);
        assertThat(impl.asMap(), hasEntry("language", Locale.GERMAN.getLanguage()));

        LocaleContextHolder.setLocale(Locale.FRENCH);
        assertThat(impl.asMap(), hasEntry("language", Locale.FRENCH.getLanguage()));

        Locale.setDefault(Locale.JAPANESE);
        assertThat(impl.asMap(), hasEntry("language", Locale.FRENCH.getLanguage()));
    }

    @Test
    public void testRegisterAvailableLocalesNull() {
        AbstractI18nCodeList impl = new AbstractI18nCodeList() {

            @Override
            protected Set<Locale> registerCodeListLocales() {
                return null;
            }

            @Override
            protected Map<String, String> obtainMap(Locale locale) {
                return null;
            }
        };

        assertThrows(IllegalArgumentException.class, impl::afterPropertiesSet);
    }
}
