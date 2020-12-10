/*
 * Copyright(c) 2013 NTT DATA Corporation.
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

public class BusinessExceptionTest {

    private BusinessException exception;

    @Test
    public void testResultMessagesNullValue() {
        // throw & assert
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new BusinessException(null, null));
        assertThat(ex.getMessage(), is("messages must not be null"));
    }

    @Test
    public void testConstructor1() throws Exception {

        String message = "resultMessages";

        // set up
        ResultMessages resultMessages = ResultMessages.error().add(ResultMessage
                .fromText(message));
        exception = new BusinessException(resultMessages);

        // throw & assert
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            throw exception;
        });
        assertThat(ex.getResultMessages(), is(resultMessages));
        assertThat(ex.getMessage(), is(resultMessages.toString()));
        assertThat(ex.getCause(), is(nullValue()));
    }

    @Test
    public void testConstructor2() throws Exception {

        String message = "resultMessages";
        Exception cause = new IllegalArgumentException("cause");

        // set up
        ResultMessages resultMessages = ResultMessages.error().add(ResultMessage
                .fromText(message));
        exception = new BusinessException(resultMessages, cause);

        // throw & assert
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            throw exception;
        });
        assertThat(ex.getResultMessages(), is(resultMessages));
        assertThat(ex.getMessage(), is(resultMessages.toString()));
        assertThat(ex.getCause(), is(instanceOf(
                IllegalArgumentException.class)));
    }

}
