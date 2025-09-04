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
package org.terasoluna.gfw.common.fullhalf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class FullHalfPairsBuilderTest {

    @Test
    public void testFullIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new FullHalfPairsBuilder().pair(null, "a").build());
        assertThat(ex).hasMessage("fullwidth must be 1 length string (fullwidth = null)");
    }

    @Test
    public void testFullIsEmptyString() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new FullHalfPairsBuilder().pair("", "a").build());
        assertThat(ex).hasMessage("fullwidth must be 1 length string (fullwidth = )");
    }

    @Test
    public void testFullIsTwoString() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new FullHalfPairsBuilder().pair("aa", "a").build());
        assertThat(ex).hasMessage("fullwidth must be 1 length string (fullwidth = aa)");
    }

    @Test
    public void testHalfIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new FullHalfPairsBuilder().pair("a", null).build());
        assertThat(ex).hasMessage("halfwidth must be 1 or 2 length string (halfwidth = null)");
    }

    @Test
    public void testHalfIsEmptyString() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new FullHalfPairsBuilder().pair("a", "").build());
        assertThat(ex).hasMessage("halfwidth must be 1 or 2 length string (halfwidth = )");
    }

    @Test
    public void testHalfIsThreeString() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new FullHalfPairsBuilder().pair("a", "aaa").build());
        assertThat(ex).hasMessage("halfwidth must be 1 or 2 length string (halfwidth = aaa)");
    }

    @Test
    public void testSamePairIsIgnored() {
        Set<FullHalfPair> set =
                new FullHalfPairsBuilder().pair("ａ", "a").pair("ａ", "a").build().pairs();
        assertThat(set).hasSize(1);
    }
}
