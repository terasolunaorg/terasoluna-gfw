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

import org.junit.jupiter.api.Test;

public class StandardResultMessageTypeTest {

    @Test
    public void testError() {
        assertThat(StandardResultMessageType.ERROR.getType()).isEqualTo("error");
        assertThat(StandardResultMessageType.ERROR.toString()).isEqualTo("error");
    }

    @Test
    public void testDanger() {
        assertThat(StandardResultMessageType.DANGER.getType()).isEqualTo("danger");
        assertThat(StandardResultMessageType.DANGER.toString()).isEqualTo("danger");
    }

    @Test
    public void testInfo() {
        assertThat(StandardResultMessageType.INFO.getType()).isEqualTo("info");
        assertThat(StandardResultMessageType.INFO.toString()).isEqualTo("info");
    }

    @Test
    public void testSuccess() {
        assertThat(StandardResultMessageType.SUCCESS.getType()).isEqualTo("success");
        assertThat(StandardResultMessageType.SUCCESS.toString()).isEqualTo("success");
    }

    @Test
    public void issue24_testWarning() {
        assertThat(StandardResultMessageType.WARNING.getType()).isEqualTo("warning");
        assertThat(StandardResultMessageType.WARNING.toString()).isEqualTo("warning");
    }

    @Test
    public void testPrimary() {
        assertThat(StandardResultMessageType.PRIMARY.getType()).isEqualTo("primary");
        assertThat(StandardResultMessageType.PRIMARY.toString()).isEqualTo("primary");
    }

    @Test
    public void testSecondary() {
        assertThat(StandardResultMessageType.SECONDARY.getType()).isEqualTo("secondary");
        assertThat(StandardResultMessageType.SECONDARY.toString()).isEqualTo("secondary");
    }

    @Test
    public void testLight() {
        assertThat(StandardResultMessageType.LIGHT.getType()).isEqualTo("light");
        assertThat(StandardResultMessageType.LIGHT.toString()).isEqualTo("light");
    }

    @Test
    public void testDark() {
        assertThat(StandardResultMessageType.DARK.getType()).isEqualTo("dark");
        assertThat(StandardResultMessageType.DARK.toString()).isEqualTo("dark");
    }

}
