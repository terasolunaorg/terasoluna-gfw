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

public class ToFullwidthTest {

    @Test
    public void testToFull() throws Exception {
        Map<String, String> fullHalfs = new DefaultFullHalfCodePointsMap();
        for (Entry<String, String> fullHalf : fullHalfs.entrySet()) {
            assertThat(DefaultFullHalf.INSTANCE.toFullwidth(fullHalf
                    .getValue()), is(fullHalf.getKey()));
        }
    }

    @Test
    public void testToFull_NotMapped() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("あ"), is("あ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("一"), is("一"));
    }

    @Test
    public void testToFull_String() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾒﾛﾝ"), is("メロン"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Hello World!"), is(
                "Ｈｅｌｌｏ　Ｗｏｒｌｄ！"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊﾛｰﾜｰﾙﾄﾞ!"), is(
                "ハローワールド！"));
    }

    @Test
    public void testToFull_NotMappedAndMappedString() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("完熟ﾒﾛﾝﾊﾟﾝ"), is(
                "完熟メロンパン"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾏﾁｭﾋﾟﾁｭ遺跡"), is(
                "マチュピチュ遺跡"));
    }

    @Test
    public void testToFull_String_NotMapped() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("あいうえお"), is("あいうえお"));
    }

    @Test
    public void testToFull_String_Mixed() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("あいうえおｶｷｸｹｺ"), is(
                "あいうえおカキクケコ"));
    }

    @Test
    public void testToFull01_半角普通英数字一文字が全角普通英数字に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("A"), is("Ａ"));
    }

    @Test
    public void testToFull02_半角普通文字が複数全角普通文字に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｱﾞ!A8"), is("ア゛！Ａ８"));
    }

    @Test
    public void testToFull03_半角サ一文字が全角サに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｻ"), is("サ"));
    }

    @Test
    public void testToFull04_半角カサタハウが複数全角カサタハウに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶｻﾀﾊｳ"), is("カサタハウ"));
    }

    @Test
    public void testToFull05_半角濁点一文字が全角濁点に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶﾞ"), is("ガ"));
    }

    @Test
    public void testToFull06_半角濁点が複数文字全角濁点に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶﾞｻﾞﾀﾞﾊﾞｳﾞ"), is(
                "ガザダバヴ"));
    }

    @Test
    public void testToFull07_半角半濁点一文字が全角半濁点に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾎﾟ"), is("ポ"));
    }

    @Test
    public void testToFull08_半角半濁点が複数文字全角半濁点に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ"), is(
                "パピプペポ"));
    }

    @Test
    public void testToFull09_半角ワが全角ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾜ"), is("ワ"));
    }

    @Test
    public void testToFull10_半角ヲが全角ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｦ"), is("ヲ"));
    }

    @Test
    public void testToFull11_半角濁点ワが全角濁点ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾜﾞ"), is("\u30f7"));
    }

    @Test
    public void testToFull12_半角濁点ヲが全角濁点ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｦﾞ"), is("\u30fa"));
    }

    @Test
    public void testToFull13_半角ワが文字列の途中にある時全角ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Aﾜ1"), is("Ａワ１"));
    }

    @Test
    public void testToFull14_半角ヲが文字列の途中にある時全角ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Bｦ8"), is("Ｂヲ８"));
    }

    @Test
    public void testToFull15_半角濁点ワが文字列の途中にある時全角濁点ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Bﾜﾞ8"), is(
                "Ｂ\u30f7８"));
    }

    @Test
    public void testToFull16_半角濁点ワが文字列の途中にある時全角濁点ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Bｦﾞ8"), is(
                "Ｂ\u30fa８"));
    }

    @Test
    public void testToFull17_半角ワが文字列の末尾にある時全角ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｱAﾜ"), is("アＡワ"));
    }

    @Test
    public void testToFull18_半角ヲが文字列の末尾にある時全角ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("tBｦ"), is("ｔＢヲ"));
    }

    @Test
    public void testToFull19_半角濁点ワが文字列の末尾にある時全角濁点ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｻBﾜﾞ"), is(
                "サＢ\u30f7"));
    }

    @Test
    public void testToFull20_半角濁点ヲが文字列の末尾にある時全角濁点ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｱBｦﾞ"), is(
                "アＢ\u30fa"));
    }

    @Test
    public void testToFull21_入力値に全角文字と半角文字を混合させたものを設定し全て全角文字に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("サｼズｾﾞソ"), is("サシズゼソ"));
    }

    @Test
    public void testToFull22_置換対象文字列がnullの時nullを戻り値とし処理を終了すること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(null), is(nullValue()));
    }

    @Test
    public void testToFull23_置換対象文字列が空文字の時空文字を戻り値とし処理を終了すること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(""), is(""));
    }

    @Test
    public void testToFull24_半角半濁点文字が全角半濁点文字に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶﾟｷﾟ"), is("カ゜キ゜"));
    }
}
