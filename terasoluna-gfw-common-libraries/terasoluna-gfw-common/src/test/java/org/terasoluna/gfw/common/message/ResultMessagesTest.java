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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.DANGER;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.DARK;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.ERROR;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.INFO;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.LIGHT;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.PRIMARY;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.SECONDARY;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.SUCCESS;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.WARNING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.Test.None;
import org.springframework.util.SerializationUtils;

public class ResultMessagesTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new ResultMessages(null);
    }

    @Test
    public void testGetType() {
        ResultMessages messages = new ResultMessages(ERROR);
        assertThat(messages.getType(), is(ERROR));
    }

    @Test
    public void testGetMessages() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");
        ResultMessage msg2 = ResultMessage.fromCode("bar");

        ResultMessages messages = new ResultMessages(ERROR, msg1, msg2);
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testAdd01() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");
        ResultMessage msg2 = ResultMessage.fromCode("bar");

        ResultMessages messages = new ResultMessages(ERROR, msg1);
        messages.add(msg2);
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd02_null() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");

        ResultMessages messages = new ResultMessages(ERROR, msg1);
        messages.add((ResultMessage) null);
    }

    @Test
    public void testAdd03() {
        ResultMessage msg1 = ResultMessage.fromText("foo");
        ResultMessage msg2 = ResultMessage.fromText("bar");

        ResultMessages messages = new ResultMessages(ERROR, msg1);
        messages.add(msg2);
        assertThat(messages.getList(), contains(msg1, msg2));
        System.out.println(messages);
    }

    @Test
    public void testAdd11() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");
        ResultMessage msg2 = ResultMessage.fromCode("bar");

        ResultMessages messages = new ResultMessages(ERROR);
        messages.add("foo");
        messages.add("bar");
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd12_null() {
        ResultMessages messages = new ResultMessages(ERROR);
        messages.add("foo");
        messages.add((String) null);
    }

    @Test
    public void testAdd21() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages(ERROR);
        messages.add("foo", "aa");
        messages.add("bar", "bb");
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd22_null() {

        ResultMessages messages = new ResultMessages(ERROR);
        messages.add("foo", "aa");
        messages.add((String) null, "bb");
    }

    @Test
    public void testAddAll01() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages(ERROR);
        messages.addAll(msg1, msg2);
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAll02_null() {

        ResultMessages messages = new ResultMessages(ERROR);
        messages.addAll((ResultMessage[]) null);
    }

    @Test
    public void testAddAll11() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages(ERROR);
        messages.addAll(Arrays.asList(msg1, msg2));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAll12_null() {

        ResultMessages messages = new ResultMessages(ERROR);
        messages.addAll((Collection<ResultMessage>) null);
    }

    @Test
    public void testIsNotEmpty01() {
        ResultMessages messages = new ResultMessages(ERROR).add("foo");
        assertThat(messages.isNotEmpty(), is(true));
    }

    @Test
    public void testIsNotEmpty02() {
        ResultMessages messages = new ResultMessages(ERROR);
        assertThat(messages.isNotEmpty(), is(false));
    }

    @Test
    public void testIterator() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages(ERROR);
        messages.addAll(Arrays.asList(msg1, msg2));

        List<ResultMessage> result = new ArrayList<ResultMessage>();
        for (ResultMessage message : messages) {
            result.add(message);
        }

        assertThat(result, contains(msg1, msg2));
    }

    @Test
    public void testError() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.error().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(ERROR));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testInfo() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.info().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(INFO));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testSuccess() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.success().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(SUCCESS));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testDanger() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.danger().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(DANGER));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void issue24_testWarning() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.warning().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(WARNING));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testPrimary() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.primary().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(PRIMARY));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testSecondary() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.secondary().add("foo", "aa")
                .add("bar", "bb");

        assertThat(messages.getType(), is(SECONDARY));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testLight() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.light().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(LIGHT));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test
    public void testDark() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.dark().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is(DARK));
        assertThat(messages.getList(), contains(msg1, msg2));
    }

    @Test(expected = None.class)
    public void testSerialization() {
        byte[] serialized = SerializationUtils.serialize(
                new ResultMessages(ERROR));
        SerializationUtils.deserialize(serialized);
    }
}
