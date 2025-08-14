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
package org.terasoluna.gfw.common.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class ResultMessageTest {

    @Test
    public void test01() {
        ResultMessage message = ResultMessage.fromText("text");

        assertThat(message.getText()).isEqualTo("text");
        assertThat(message.getCode()).isNull();
        assertThat(message.getArgs()).isEmpty();
    }

    @Test
    public void test02() {
        ResultMessage message = ResultMessage.fromCode("xxx.yyy.code");

        assertThat(message.getText()).isNull();
        assertThat(message.getCode()).isEqualTo("xxx.yyy.code");
        assertThat(message.getArgs()).isEmpty();
    }

    @Test
    public void test03() {
        ResultMessage message = ResultMessage.fromCode("xxx.yyy.code", "a", 1, "x");
        System.out.println(message);

        assertThat(message.getText()).isNull();
        assertThat(message.getCode()).isEqualTo("xxx.yyy.code");
        assertThat(message.getArgs()).containsExactly("a", 1, "x");
    }

    @Test
    public void test04() {
        ResultMessage message = ResultMessage.fromCode("xxx.yyy.code", (Object[]) null);

        assertThat(message.getText()).isNull();
        assertThat(message.getCode()).isEqualTo("xxx.yyy.code");
        assertThat(message.getArgs()).isEmpty();
    }

    @Test
    public void test07() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResultMessage.fromCode(null);
        });
    }

    @Test
    public void test08() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResultMessage.fromCode(null, (Object[]) null);
        });
    }

    @Test
    public void test09() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResultMessage.fromText(null);
        });
    }

    /**
     * This test uses SerializationUtils#deserialize(byte[]) Although this mechanism is deprecated,
     * it is still used because the data to be deserialized is guaranteed.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void test10() {
        byte[] serialized = SerializationUtils.serialize(ResultMessage.fromText("foo"));
        assertDoesNotThrow(() -> {
            SerializationUtils.deserialize(serialized);
        });
    }

    @Test
    @SuppressWarnings("unlikely-arg-type")
    public void testEquals01() {
        // set up
        Object[] object = new Object[0];

        ResultMessage msg1 = ResultMessage.fromText("foo");
        ResultMessage msg2 = ResultMessage.fromCode("foo");
        ResultMessage msg3 = ResultMessage.fromCode("foo", "a");
        ResultMessage msg4 = new ResultMessage("foo", object, null);
        ResultMessage msg5 = new ResultMessage("foo", object, "foo");

        // assert
        assertThat(msg1.equals(msg1)).isEqualTo(true);
        assertThat(msg1.equals(null)).isEqualTo(false);
        assertThat(msg1.equals("a")).isEqualTo(false);
        assertThat(msg2.equals(msg3)).isEqualTo(false);
        assertThat(msg1.equals(ResultMessage.fromText("bar"))).isEqualTo(false);
        assertThat(msg1.equals(msg2)).isEqualTo(false);
        assertThat(msg2.equals(ResultMessage.fromCode("hoo"))).isEqualTo(false);
        assertThat(msg4.equals(msg5)).isEqualTo(false);

        Set<ResultMessage> set = new HashSet<ResultMessage>();
        set.add(msg1);
        set.add(msg2);
        set.add(msg3);
        assertThat(set).contains(msg1);
        assertThat(set).contains(ResultMessage.fromText("foo"));
        assertThat(set).contains(ResultMessage.fromCode("foo"));
        assertThat(set).contains(ResultMessage.fromCode("foo", "a"));
    }

}
