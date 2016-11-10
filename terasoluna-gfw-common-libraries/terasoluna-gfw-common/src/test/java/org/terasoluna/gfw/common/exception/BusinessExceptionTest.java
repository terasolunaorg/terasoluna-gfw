/*
 * Copyright (C) 2013-2016 NTT DATA Corporation
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
package org.terasoluna.gfw.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

public class BusinessExceptionTest {

    private BusinessException exception;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testResultMessagesNullValue() {
        // expect
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("messages must not be null");

        // test
        new BusinessException(null, null);

    }

    @Test
    public void testGetMessage() throws Exception {
        // set up
        ResultMessages resultMessages = ResultMessages.error().add(ResultMessage.fromText("resultMessages"));
        exception = new BusinessException(resultMessages);

        // assert
        assertThat(exception.getMessage(), is(resultMessages.toString()));
    }

    @Test
    public void testGetResultMessages() throws Exception {
        // set up
        ResultMessages resultMessages = ResultMessages.error().add(ResultMessage.fromText("resultMessages"));
        exception = new BusinessException(resultMessages);

        // assert
        assertThat(exception.getResultMessages(), is(resultMessages));
    }

}
