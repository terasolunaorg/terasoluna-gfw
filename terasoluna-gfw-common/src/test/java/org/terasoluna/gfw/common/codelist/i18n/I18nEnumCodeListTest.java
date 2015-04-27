/*
 * Copyright (C) 2013-2016 terasoluna.org
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class I18nEnumCodeListTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAsMapJapanese() {
        I18nEnumCodeList codeList = new I18nEnumCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "受付済み");
        expected.put("2", "発送済み");
        expected.put("3", "キャンセル済み");

        assertThat(codeList.asMap(Locale.JAPANESE), is(expected));
    }

    @Test
    public void testAsMapEnglish() {
        I18nEnumCodeList codeList = new I18nEnumCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "Received");
        expected.put("2", "Sent");
        expected.put("3", "Cancelled");

        assertThat(codeList.asMap(Locale.ENGLISH), is(expected));
    }

    @Test
    public void testAsMapUnknownLocale() {
        I18nEnumCodeList codeList = new I18nEnumCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();

        assertThat(codeList.asMap(Locale.GERMANY), is(expected));
    }

    @Test
    public void testAsMapDefaultLocale() {
        I18nEnumCodeList codeList = new I18nEnumCodeList(DefaultLocaleEnum.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "label1");
        expected.put("2", "label2");
        expected.put("3", "label3");

        assertThat(codeList.asMap(), is(expected));
    }

    @Test
    public void testAsMapLocaleIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("locale is null");

        I18nCodeList codeList = new I18nEnumCodeList(OrderStatus.class);
        codeList.asMap(null);
    }

    @Test
    public void testConstructorSetInvalidEnum() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("the given enumClass must implement " + I18nEnumCodeList.I18nCodeListItem.class);

        new I18nEnumCodeList(IllegalEnum.class);
    }

    @Test
    public void testConstructorSetNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("the given enumClass is null");

        new I18nEnumCodeList(null);
    }

    public static enum OrderStatus implements I18nEnumCodeList.I18nCodeListItem {
        RECEIVED("1", new HashMap<Locale, String>() {{
            put(Locale.JAPANESE, "受付済み");
            put(Locale.ENGLISH, "Received");
        }}),
        SENT("2", new HashMap<Locale, String>() {{
            put(Locale.JAPANESE, "発送済み");
            put(Locale.ENGLISH, "Sent");
        }}),
        CANCELLED("3", new HashMap<Locale, String>() {{
            put(Locale.JAPANESE, "キャンセル済み");
            put(Locale.ENGLISH, "Cancelled");
        }});

        private final String value;
        private final Map<Locale, String> localizedLabels;

        private OrderStatus(String value, Map<Locale, String> localizedLabels) {
            this.value = value;
            this.localizedLabels = localizedLabels;
        }

        @Override
        public String getCodeValue() {
            return value;
        }

        @Override
        public Map<Locale, String> getLocalizedCodeLabels() {
            return localizedLabels;
        }

    }


    public static enum IllegalEnum {
    }

    public static enum DefaultLocaleEnum implements I18nEnumCodeList.I18nCodeListItem {
        RECEIVED("1", new HashMap<Locale, String>() {{
            put(Locale.getDefault(), "label1");
        }}),
        SENT("2", new HashMap<Locale, String>() {{
            put(Locale.getDefault(), "label2");
        }}),
        CANCELLED("3", new HashMap<Locale, String>() {{
            put(Locale.getDefault(), "label3");
        }});

        private final String value;
        private final Map<Locale, String> localizedLabels;

        private DefaultLocaleEnum(String value, Map<Locale, String> localizedLabels) {
            this.value = value;
            this.localizedLabels = localizedLabels;
        }

        @Override
        public String getCodeValue() {
            return value;
        }

        @Override
        public Map<Locale, String> getLocalizedCodeLabels() {
            return localizedLabels;
        }

    }

}
