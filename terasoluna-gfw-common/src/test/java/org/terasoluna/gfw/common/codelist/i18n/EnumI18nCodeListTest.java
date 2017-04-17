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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class EnumI18nCodeListTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testAsMapWithJapanese() {
        EnumI18nCodeList codeList = new EnumI18nCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "受付済み");
        expected.put("2", "発送済み");
        expected.put("3", "キャンセル済み");

        assertThat(codeList.asMap(Locale.JAPANESE), is(expected));
    }

    @Test
    public void testAsMapWithEnglish() {
        EnumI18nCodeList codeList = new EnumI18nCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "Received");
        expected.put("2", "Sent");
        expected.put("3", "Cancelled");

        assertThat(codeList.asMap(Locale.ENGLISH), is(expected));
    }

    @Test
    public void testAsMapWithUnknownLocale() {
        EnumI18nCodeList codeList = new EnumI18nCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();

        assertThat(codeList.asMap(Locale.GERMANY), is(expected));
    }

    @Test
    public void testAsMap() {
        EnumI18nCodeList codeList = new EnumI18nCodeList(DefaultLocaleEnum.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "label1");
        expected.put("2", "label2");
        expected.put("3", "label3");

        assertThat(codeList.asMap(), is(expected));
    }

    @Test
    public void testAsMapWithNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("locale is null");

        I18nCodeList codeList = new EnumI18nCodeList(OrderStatus.class);
        codeList.asMap(null);
    }

    @Test
    public void testConstructorWithInvalidEnum() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("the given enumClass must implement " + EnumI18nCodeList.I18nCodeListItem.class);

        new EnumI18nCodeList(IllegalEnum.class);
    }

    @Test
    public void testConstructorWithNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("the given enumClass is null");

        new EnumI18nCodeList(null);
    }

    @Test
    public void testGetLabelOfLocalizedCodeLabels() {
        EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels = OrderStatus.SENT.getLocalizedCodeLabels();
        assertThat(localizedCodeLabels.getLabel(Locale.JAPANESE), is("発送済み"));
        assertThat(localizedCodeLabels.getLabel(Locale.ENGLISH), is("Sent"));
        assertThat(localizedCodeLabels.getLabel(Locale.GERMANY), is(nullValue()));
    }

    @Test
    public void testGetLabelOfLocalizedCodeLabelsWithNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("locale is null");
        assertThat(OrderStatus.SENT.getLocalizedCodeLabels().getLabel(null), is(nullValue()));
    }

    @Test
    public void testAsMapOfLocalizedCodeLabelsCannotModify() {
        expectedException.expect(UnsupportedOperationException.class);
        OrderStatus.SENT.getLocalizedCodeLabels().asMap().put(Locale.GERMANY, "Versendet");
    }

    public enum OrderStatus implements EnumI18nCodeList.I18nCodeListItem {
        RECEIVED("1", EnumI18nCodeList.LocalizedCodeLabels.builder()
                .label("ja", "受付済み")
                .label(Locale.ENGLISH, "Received")
                .build()),
        SENT("2", EnumI18nCodeList.LocalizedCodeLabels.builder()
                .label("ja", "発送済み")
                .label(Locale.ENGLISH, "Sent")
                .build()),
        CANCELLED("3", EnumI18nCodeList.LocalizedCodeLabels.builder()
                .label("ja", "キャンセル済み")
                .label(Locale.ENGLISH, "Cancelled")
                .build());

        private final String codeValue;
        private final EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels;

        private OrderStatus(String codeValue, EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels) {
            this.codeValue = codeValue;
            this.localizedCodeLabels = localizedCodeLabels;
        }

        @Override
        public String getCodeValue() {
            return codeValue;
        }

        @Override
        public EnumI18nCodeList.LocalizedCodeLabels getLocalizedCodeLabels() {
            return localizedCodeLabels;
        }

    }

    public enum DefaultLocaleEnum implements EnumI18nCodeList.I18nCodeListItem {

        RECEIVED("1", EnumI18nCodeList.LocalizedCodeLabels.builder()
                .label("label1")
                .build()),
        SENT("2", EnumI18nCodeList.LocalizedCodeLabels.builder()
                .label("label2")
                .build()),
        CANCELLED("3", EnumI18nCodeList.LocalizedCodeLabels.builder()
                .label("label3")
                .build());

        private final String codeValue;
        private final EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels;

        private DefaultLocaleEnum(String codeValue, EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels) {
            this.codeValue = codeValue;
            this.localizedCodeLabels = localizedCodeLabels;
        }

        @Override
        public String getCodeValue() {
            return codeValue;
        }

        @Override
        public EnumI18nCodeList.LocalizedCodeLabels getLocalizedCodeLabels() {
            return localizedCodeLabels;
        }

    }

    public enum IllegalEnum {
    }

}