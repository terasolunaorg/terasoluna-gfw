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
package org.terasoluna.gfw.common.exception;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
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

        // throw & assert
        throw new BusinessException(null, null);
    }

    @Test
    public void testConstructor1() throws Exception {

        String message = "resultMessages";

        // expect
        expectedException.expect(BusinessException.class);
        expectedException.expectMessage(message);

        // set up
        ResultMessages resultMessages = ResultMessages.error().add(ResultMessage
                .fromText(message));
        exception = new BusinessException(resultMessages);

        // throw & assert
        throw exception;
    }

    @Test
    public void testConstructor2() throws Exception {

        String message = "resultMessages";
        Exception cause = new IllegalArgumentException("cause");

        // expect
        expectedException.expect(BusinessException.class);
        expectedException.expectMessage(message);

        Matcher<? extends Throwable> matcher = CoreMatchers.instanceOf(
                IllegalArgumentException.class);
        expectedException.expectCause(matcher);

        // set up
        ResultMessages resultMessages = ResultMessages.error().add(ResultMessage
                .fromText(message));
        exception = new BusinessException(resultMessages, cause);

        // throw & assert
        throw exception;
    }

}
