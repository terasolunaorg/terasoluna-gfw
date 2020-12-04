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
package org.terasoluna.gfw.common.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

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

    @Test
    public void testPrimary() {
        assertThat(StandardResultMessageType.PRIMARY.getType(), is("primary"));
        assertThat(StandardResultMessageType.PRIMARY.toString(), is("primary"));
    }

    @Test
    public void testSecondary() {
        assertThat(StandardResultMessageType.SECONDARY.getType(), is(
                "secondary"));
        assertThat(StandardResultMessageType.SECONDARY.toString(), is(
                "secondary"));
    }

    @Test
    public void testLight() {
        assertThat(StandardResultMessageType.LIGHT.getType(), is("light"));
        assertThat(StandardResultMessageType.LIGHT.toString(), is("light"));
    }

    @Test
    public void testDark() {
        assertThat(StandardResultMessageType.DARK.getType(), is("dark"));
        assertThat(StandardResultMessageType.DARK.toString(), is("dark"));
    }

}
