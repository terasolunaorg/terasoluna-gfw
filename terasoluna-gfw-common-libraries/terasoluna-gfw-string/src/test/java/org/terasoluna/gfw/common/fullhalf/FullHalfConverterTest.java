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
import org.junit.jupiter.api.Test;

public class FullHalfConverterTest {

    @Test
    public void unsymmetric() {
        FullHalfConverter converter = new FullHalfConverter(new FullHalfPairsBuilder()
                .pair("‐" /* HYPHEN U+2010 */, "-").pair("—" /* HORIZONTAL BAR U+2015 */, "-")
                .pair("ー" /* KATAKANA-HIRAGANA PROLONGED SOUND MARK U+30FC */, "-")
                .pair("－" /* FULLWIDTH HYPHEN-MINUS U+FF0D */, "-").build());

        assertThat(converter.toHalfwidth("‐—ー－")).isEqualTo("----");
        assertThat(converter.toFullwidth("----")).isEqualTo("‐‐‐‐");
    }

    @Test
    public void unsymmetric_reversed() {
        FullHalfConverter converter = new FullHalfConverter(new FullHalfPairsBuilder()
                .pair("‐", "-" /* HYPHEN-MINUS U+2010 */).pair("‐", "—" /* EM DASH U+2014 */)
                .pair("‐", "ｰ" /* HALFWIDTH KATAKANA-HIRAGANA PROLONGED SOUND MARK U+FF70 */)
                .build());

        assertThat(converter.toHalfwidth("‐‐‐")).isEqualTo("---");
        assertThat(converter.toFullwidth("-—ｰ")).isEqualTo("‐‐‐");
    }

    @Test
    public void testWithCustomAppenadablePredicate() {
        FullHalfConverter converter =
                new FullHalfConverter(new FullHalfPairsBuilder().pair("バ", "ﾊﾞ").pair("ハ", "ﾊ")
                        .pair("゛", "ﾞ").pair("゜", "ﾟ").appendablePredicate(c -> c == 'ﾞ').build());
        assertThat(converter.toFullwidth("ﾊﾞ")).isEqualTo("バ");
        assertThat(converter.toFullwidth("ﾊﾟ")).isEqualTo("ハ゜");
    }

    @Test
    public void testNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new FullHalfConverter(null);
        });
        assertThat(ex).hasMessage("pairs must not be null.");
    }
}
