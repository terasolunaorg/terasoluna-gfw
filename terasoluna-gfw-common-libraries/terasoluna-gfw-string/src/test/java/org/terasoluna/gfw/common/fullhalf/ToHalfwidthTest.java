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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class ToHalfwidthTest {
    @Test
    public void testToFull() throws Exception {
        Map<String, String> fullHalfs = new DefaultFullHalfCodePointsMap();
        for (Entry<String, String> fullHalf : fullHalfs.entrySet()) {
            assertThat(DefaultFullHalf.INSTANCE.toHalfwidth(fullHalf.getKey()),
                    is(fullHalf.getValue()));
        }
    }

    @Test
    public void testToFull_NotMapped() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("あ"), is("あ"));
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("一"), is("一"));
    }

    @Test
    public void testToFull_MappedString() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("メロン"), is("ﾒﾛﾝ"));
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｈｅｌｌｏ　Ｗｏｒｌｄ！"), is(
                "Hello World!"));
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ハローワールド！"), is(
                "ﾊﾛｰﾜｰﾙﾄﾞ!"));
    }

    @Test
    public void testToFull_NotMappedAndMappedString() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("完熟メロンパン"), is(
                "完熟ﾒﾛﾝﾊﾟﾝ"));
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("マチュピチュ遺跡"), is(
                "ﾏﾁｭﾋﾟﾁｭ遺跡"));
    }

    @Test
    public void testToHalf01_全角文字が半角文字に複数文字変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ａ！ア"), is("A!ｱ"));
    }

    @Test
    public void testToHalf02_全角文字に対して半角文字に一文字変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ａ"), is("A"));
    }

    @Test
    public void testToHalf03_全角カサタハが半角カサタハに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("カサタハ"), is("ｶｻﾀﾊ"));
    }

    @Test
    public void testToHalf04_全角濁点文字が二文字に分解されて表示されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ガザダ"), is("ｶﾞｻﾞﾀﾞ"));
    }

    @Test
    public void testToHalf05_全角濁点半濁点文字が二文字に分解されて表示されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("パポ"), is("ﾊﾟﾎﾟ"));
    }

    @Test
    public void testToHalf06_全角ワが半角ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ａワ"), is("Aﾜ"));
    }

    @Test
    public void testToHalf07_全角ヲが半角ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ａヲ"), is("Aｦ"));
    }

    @Test
    public void testToHalf08_全角濁点ワが半角濁点ﾜに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ア\u30f7"), is("ｱﾜﾞ"));
    }

    @Test
    public void testToHalf09_全角濁点ヲが半角濁点ｦに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("\u30faヴ"), is("ｦﾞｳﾞ"));
    }

    @Test
    public void testToHalf10_半角全角混合文字が全て半角に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("サ\u30faｲAピ"), is(
                "ｻｦﾞｲAﾋﾟ"));
    }

    @Test
    public void testToHalf11_置換対象文字列が空文字の時空文字を戻り値とし処理を終了すること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth(""), is(""));
    }

    @Test
    public void testToHalf12_置換対象文字列がnullの時nullを戻り値とし処理を終了すること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth(null), is(nullValue()));
    }

    @Test
    public void testToHalf13_半角化不可能な文字列がそのまま全角文字列で出力されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("入力値"), is("入力値"));
    }
}
