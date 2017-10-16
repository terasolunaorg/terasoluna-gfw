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
package org.terasoluna.gfw.common.fullhalf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FullHalfConverterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void unsymmetric() {
        FullHalfConverter converter = new FullHalfConverter(new FullHalfPairsBuilder()
                .pair("‐" /* HYPHEN U+2010 */, "-").pair(
                        "—" /* HORIZONTAL BAR U+2015 */, "-").pair(
                                "ー" /* KATAKANA-HIRAGANA PROLONGED SOUND MARK U+30FC */,
                                "-").pair(
                                        "－" /* FULLWIDTH HYPHEN-MINUS U+FF0D */,
                                        "-").build());

        assertThat(converter.toHalfwidth("‐—ー－"), is("----"));
        assertThat(converter.toFullwidth("----"), is("‐‐‐‐"));
    }

    @Test
    public void unsymmetric_reversed() {
        FullHalfConverter converter = new FullHalfConverter(new FullHalfPairsBuilder()
                .pair("‐", "-" /* HYPHEN-MINUS U+2010 */).pair("‐",
                        "—" /* EM DASH U+2014 */).pair("‐",
                                "ｰ" /* HALFWIDTH KATAKANA-HIRAGANA PROLONGED SOUND MARK U+FF70 */)
                .build());

        assertThat(converter.toHalfwidth("‐‐‐"), is("---"));
        assertThat(converter.toFullwidth("-—ｰ"), is("‐‐‐"));
    }

    @Test
    public void testWithCustomAppenadablePredicate() {
        FullHalfConverter converter = new FullHalfConverter(new FullHalfPairsBuilder()
                .pair("バ", "ﾊﾞ").pair("ハ", "ﾊ").pair("゛", "ﾞ").pair("゜", "ﾟ")
                .appendablePredicate(new FullHalfPairs.AppendablePredicate() {
                    @Override
                    public boolean isAppendable(char c) {
                        return c == 'ﾞ';
                    }
                }).build());
        assertThat(converter.toFullwidth("ﾊﾞ"), is("バ"));
        assertThat(converter.toFullwidth("ﾊﾟ"), is("ハ゜"));
    }

    @Test
    public void testNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("pairs must not be null.");
        new FullHalfConverter(null);
    }
}
