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
package org.terasoluna.gfw.common.message;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.util.SerializationUtils;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageType;
import org.terasoluna.gfw.common.message.ResultMessages;

import static org.terasoluna.gfw.common.message.StandardResultMessageType.*;

public class ResultMessagesTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        new ResultMessages(null);
    }

    @Test
    public void testGetType() {
        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        assertThat(messages.getType(), is((ResultMessageType) ERROR));
    }

    @Test
    public void testGetMessages() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");
        ResultMessage msg2 = ResultMessage.fromCode("bar");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR, msg1, msg2);
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void testAdd01() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");
        ResultMessage msg2 = ResultMessage.fromCode("bar");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR, msg1);
        messages.add(msg2);
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
        System.out.println(messages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd02_null() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR, msg1);
        messages.add((ResultMessage) null);
    }

    @Test
    public void testAdd03() {
        ResultMessage msg1 = ResultMessage.fromText("foo");
        ResultMessage msg2 = ResultMessage.fromText("bar");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR, msg1);
        messages.add(msg2);
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
        System.out.println(messages);
    }

    @Test
    public void testAdd11() {
        ResultMessage msg1 = ResultMessage.fromCode("foo");
        ResultMessage msg2 = ResultMessage.fromCode("bar");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.add("foo");
        messages.add("bar");
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd12_null() {
        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.add("foo");
        messages.add((String) null);
    }

    @Test
    public void testAdd21() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.add("foo", "aa");
        messages.add("bar", "bb");
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdd22_null() {

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.add("foo", "aa");
        messages.add((String) null, "bb");
    }

    @Test
    public void testAddAll01() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.addAll(msg1, msg2);
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAll02_null() {

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.addAll((ResultMessage[]) null);
    }

    @Test
    public void testAddAll11() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.addAll(Arrays.asList(msg1, msg2));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAll12_null() {

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.addAll((Collection<ResultMessage>) null);
    }

    @Test
    public void testIsNotEmpty01() {
        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR)
                .add("foo");
        assertThat(messages.isNotEmpty(), is(true));
    }

    @Test
    public void testIsNotEmpty02() {
        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        assertThat(messages.isNotEmpty(), is(false));
    }

    @Test
    public void testIterator() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = new ResultMessages((ResultMessageType) ERROR);
        messages.addAll(Arrays.asList(msg1, msg2));

        List<ResultMessage> result = new ArrayList<ResultMessage>();
        for (ResultMessage message : messages) {
            result.add(message);
        }

        assertThat(result, is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void testError() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.error().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is((ResultMessageType) ERROR));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWarn() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.warn().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is((ResultMessageType) WARN));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void testInfo() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.info().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is((ResultMessageType) INFO));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void testSuccess() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.success().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is((ResultMessageType) SUCCESS));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void testDanger() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.danger().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is((ResultMessageType) DANGER));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void issue24_testWarning() {
        ResultMessage msg1 = ResultMessage.fromCode("foo", "aa");
        ResultMessage msg2 = ResultMessage.fromCode("bar", "bb");

        ResultMessages messages = ResultMessages.warning().add("foo", "aa").add(
                "bar", "bb");

        assertThat(messages.getType(), is((ResultMessageType) WARNING));
        assertThat(messages.getList(), is(Arrays.asList(msg1, msg2)));
    }

    @Test
    public void testSerialization() {
        try {
            byte[] serialized = SerializationUtils.serialize(
                    new ResultMessages((ResultMessageType) ERROR));
            SerializationUtils.deserialize(serialized);
        } catch (SerializationFailedException e) {
            fail();
        }
    }
}
