/*
 * Copyright (C) 2013-2015 terasoluna.org
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

import org.junit.Test;

public class ToHalfwidthTest {

    @Test
    public void testToFull() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("!"), is("！"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("\""), is("”"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("#"), is("＃"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("$"), is("＄"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("%"), is("％"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("&"), is("＆"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("'"), is("’"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("("), is("（"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(")"), is("）"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("*"), is("＊"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("+"), is("＋"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(","), is("，"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("-"), is("－"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("."), is("．"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("/"), is("／"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("0"), is("０"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("1"), is("１"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("2"), is("２"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("3"), is("３"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("4"), is("４"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("5"), is("５"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("6"), is("６"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("7"), is("７"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("8"), is("８"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("9"), is("９"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(":"), is("："));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(";"), is("；"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("<"), is("＜"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("="), is("＝"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(">"), is("＞"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("?"), is("？"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("@"), is("＠"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("A"), is("Ａ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("B"), is("Ｂ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("C"), is("Ｃ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("D"), is("Ｄ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("E"), is("Ｅ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("F"), is("Ｆ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("G"), is("Ｇ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("H"), is("Ｈ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("I"), is("Ｉ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("J"), is("Ｊ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("K"), is("Ｋ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("L"), is("Ｌ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("M"), is("Ｍ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("N"), is("Ｎ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("O"), is("Ｏ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("P"), is("Ｐ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Q"), is("Ｑ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("R"), is("Ｒ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("S"), is("Ｓ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("T"), is("Ｔ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("U"), is("Ｕ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("V"), is("Ｖ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("W"), is("Ｗ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("X"), is("Ｘ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Y"), is("Ｙ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Z"), is("Ｚ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("["), is("［"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("\\"), is("￥"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("]"), is("］"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("^"), is("＾"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("_"), is("＿"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("`"), is("｀"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("a"), is("ａ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("b"), is("ｂ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("c"), is("ｃ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("d"), is("ｄ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("e"), is("ｅ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("f"), is("ｆ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("g"), is("ｇ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("h"), is("ｈ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("i"), is("ｉ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("j"), is("ｊ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("k"), is("ｋ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("l"), is("ｌ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("m"), is("ｍ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("n"), is("ｎ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("o"), is("ｏ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("p"), is("ｐ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("q"), is("ｑ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("r"), is("ｒ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("s"), is("ｓ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("t"), is("ｔ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("u"), is("ｕ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("v"), is("ｖ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("w"), is("ｗ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("x"), is("ｘ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("y"), is("ｙ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("z"), is("ｚ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("{"), is("｛"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("|"), is("｜"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("}"), is("｝"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("~"), is(String.valueOf('\uff5e')));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("｡"), is("。"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("｢"), is("「"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("｣"), is("」"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("､"), is("、"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("･"), is("・"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｧ"), is("ァ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｨ"), is("ィ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｩ"), is("ゥ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｪ"), is("ェ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｫ"), is("ォ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｬ"), is("ャ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｭ"), is("ュ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｮ"), is("ョ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｯ"), is("ッ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｰ"), is("ー"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｱ"), is("ア"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｲ"), is("イ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｳ"), is("ウ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｴ"), is("エ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｵ"), is("オ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶ"), is("カ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｷ"), is("キ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｸ"), is("ク"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｹ"), is("ケ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｺ"), is("コ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｻ"), is("サ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｼ"), is("シ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｽ"), is("ス"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｾ"), is("セ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｿ"), is("ソ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾀ"), is("タ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾁ"), is("チ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾂ"), is("ツ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾃ"), is("テ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾄ"), is("ト"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾅ"), is("ナ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾆ"), is("ニ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾇ"), is("ヌ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾈ"), is("ネ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾉ"), is("ノ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊ"), is("ハ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾋ"), is("ヒ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾌ"), is("フ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾍ"), is("ヘ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾎ"), is("ホ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾏ"), is("マ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾐ"), is("ミ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾑ"), is("ム"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾒ"), is("メ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾓ"), is("モ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾔ"), is("ヤ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾕ"), is("ユ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾖ"), is("ヨ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾗ"), is("ラ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾘ"), is("リ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾙ"), is("ル"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾚ"), is("レ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾛ"), is("ロ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾝ"), is("ン"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶﾞ"), is("ガ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｷﾞ"), is("ギ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｸﾞ"), is("グ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｹﾞ"), is("ゲ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｺﾞ"), is("ゴ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｻﾞ"), is("ザ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｼﾞ"), is("ジ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｽﾞ"), is("ズ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｾﾞ"), is("ゼ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｿﾞ"), is("ゾ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾀﾞ"), is("ダ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾁﾞ"), is("ヂ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾂﾞ"), is("ヅ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾃﾞ"), is("デ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾄﾞ"), is("ド"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊﾞ"), is("バ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾋﾞ"), is("ビ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾌﾞ"), is("ブ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾍﾞ"), is("べ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾎﾞ"), is("ボ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊﾟ"), is("パ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾋﾟ"), is("ピ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾌﾟ"), is("プ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾍﾟ"), is("ペ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾎﾟ"), is("ポ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｳﾞ"), is("ヴ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾜﾞ"), is(String.valueOf('\u30f7')));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｦﾞ"), is(String.valueOf('\u30fa')));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾞ"), is("゛"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾟ"), is("゜"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth(" "), is("　"));
    }

    @Test
    public void testToFull_NotMapped() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("あ"), is("あ"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("一"), is("一"));
    }

    @Test
    public void testToFull_String() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾒﾛﾝ"), is("メロン"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Hello World!"), is("Ｈｅｌｌｏ　Ｗｏｒｌｄ！"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊﾛｰﾜｰﾙﾄﾞ!"), is("ハローワールド！"));
    }

    @Test
    public void testToFull_NotMappedAndMappedString() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("完熟ﾒﾛﾝﾊﾟﾝ"), is("完熟メロンパン"));
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾏﾁｭﾋﾟﾁｭ遺跡"), is("マチュピチュ遺跡"));
    }

    @Test
    public void testToFull_String_NotMapped() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("あいうえお"), is("あいうえお"));
    }

    @Test
    public void testToFull_String_Mixed() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("あいうえおｶｷｸｹｺ"), is("あいうえおカキクケコ"));
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
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｶﾞｻﾞﾀﾞﾊﾞｳﾞ"), is("ガザダバヴ"));
    }

    @Test
    public void testToFull07_半角半濁点一文字が全角半濁点に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾎﾟ"), is("ポ"));
    }

    @Test
    public void testToFull08_半角半濁点が複数文字全角半濁点に変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟ"), is("パピプペポ"));
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
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Bﾜﾞ8"), is("Ｂ\u30f7８"));
    }

    @Test
    public void testToFull16_半角濁点ワが文字列の途中にある時全角濁点ワに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("Bｦﾞ8"), is("Ｂ\u30fa８"));
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
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｻBﾜﾞ"), is("サＢ\u30f7"));
    }

    @Test
    public void testToFull20_半角濁点ヲが文字列の末尾にある時全角濁点ヲに変換されること() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toFullwidth("ｱBｦﾞ"), is("アＢ\u30fa"));
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
