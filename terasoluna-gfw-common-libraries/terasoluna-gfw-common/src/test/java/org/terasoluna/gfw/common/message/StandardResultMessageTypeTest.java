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

import org.junit.Test;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

public class StandardResultMessageTypeTest {

    @Test
    public void testError() {
        assertThat(StandardResultMessageType.ERROR.getType(), is("error"));
        assertThat(StandardResultMessageType.ERROR.toString(), is("error"));
    }

    @Test
    public void testDanger() {
        assertThat(StandardResultMessageType.DANGER.getType(), is("danger"));
        assertThat(StandardResultMessageType.DANGER.toString(), is("danger"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWarn() {
        assertThat(StandardResultMessageType.WARN.getType(), is("warn"));
        assertThat(StandardResultMessageType.WARN.toString(), is("warn"));
    }

    @Test
    public void testInfo() {
        assertThat(StandardResultMessageType.INFO.getType(), is("info"));
        assertThat(StandardResultMessageType.INFO.toString(), is("info"));
    }

    @Test
    public void testSuccess() {
        assertThat(StandardResultMessageType.SUCCESS.getType(), is("success"));
        assertThat(StandardResultMessageType.SUCCESS.toString(), is("success"));
    }

    @Test
    public void issue24_testWarning() {
        assertThat(StandardResultMessageType.WARNING.getType(), is("warning"));
        assertThat(StandardResultMessageType.WARNING.toString(), is("warning"));
    }

}
