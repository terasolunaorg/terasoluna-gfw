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

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnumCodeListTest {
    public static enum OrderStatus implements EnumCodeList.CodeListItem {
        RECEIVED("1", "Received"), SENT("2", "Sent"), CANCELLED("3",
                "Cancelled");

        private final String value;

        private final String label;

        private OrderStatus(String code, String label) {
            this.value = code;
            this.label = label;
        }

        @Override
        public String getCodeValue() {
            return value;
        }

        @Override
        public String getCodeLabel() {
            return label;
        }
    }

    public static enum IllegalEnum {

    }

    @Test
    public void testAsMap() {
        EnumCodeList codeList = new EnumCodeList(OrderStatus.class);

        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("1", "Received");
        expected.put("2", "Sent");
        expected.put("3", "Cancelled");

        assertThat(codeList.asMap(), is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidEnum() {
        new EnumCodeList(IllegalEnum.class);
    }

}
