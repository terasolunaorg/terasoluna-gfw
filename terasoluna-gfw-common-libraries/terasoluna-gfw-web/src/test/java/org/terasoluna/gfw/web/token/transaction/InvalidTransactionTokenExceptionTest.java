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
package org.terasoluna.gfw.web.token.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class InvalidTransactionTokenExceptionTest {

    @Test
    public void testExceptionConstructor() {

        InvalidTransactionTokenException e =
                assertThrows(InvalidTransactionTokenException.class, () -> {
                    throw new InvalidTransactionTokenException();
                });
        assertThat(e.getMessage(), is("Invalid Transaction Token Exception !!!"));
    }

    @Test
    public void testExceptionConstructorWithCustomMessage() {

        InvalidTransactionTokenException e =
                assertThrows(InvalidTransactionTokenException.class, () -> {
                    throw new InvalidTransactionTokenException("Custom Message");
                });
        assertThat(e.getMessage(), is("Custom Message"));
    }

}
