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
package org.terasoluna.gfw.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;

public class SystemExceptionTest {

    String code = "code01";

    String msg = "msg01";

    Throwable cause = new Throwable();

    @Test
    public void testSystemExceptionStringStringThrowable() {
        SystemException ex = new SystemException(code, msg, cause);
        assertThat(ex.getCode(), is(code));
        assertThat(ex.getMessage(), is(msg));
        assertThat(ex.getCause(), is(cause));
    }

    @Test
    public void testSystemExceptionStringString() {
        SystemException ex = new SystemException(code, msg);
        assertThat(ex.getCode(), is(code));
        assertThat(ex.getMessage(), is(msg));

    }

    @Test
    public void testSystemExceptionStringThrowable() {
        SystemException ex = new SystemException(code, cause);
        assertThat(ex.getCode(), is(code));
        assertThat(ex.getCause(), is(cause));
    }

}
