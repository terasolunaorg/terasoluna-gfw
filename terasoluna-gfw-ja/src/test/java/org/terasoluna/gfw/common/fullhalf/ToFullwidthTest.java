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

public class ToFullwidthTest {
	@Test
	public void testToFull() throws Exception {
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("！"), is("!"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("”"), is("\""));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＃"), is("#"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＄"), is("$"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("％"), is("%"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＆"), is("&"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("’"), is("'"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("（"), is("("));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("）"), is(")"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＊"), is("*"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＋"), is("+"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("，"), is(","));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("－"), is("-"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("．"), is("."));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("／"), is("/"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("０"), is("0"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("１"), is("1"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("２"), is("2"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("３"), is("3"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("４"), is("4"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("５"), is("5"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("６"), is("6"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("７"), is("7"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("８"), is("8"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("９"), is("9"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("："), is(":"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("；"), is(";"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＜"), is("<"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＝"), is("="));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＞"), is(">"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("？"), is("?"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＠"), is("@"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ａ"), is("A"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｂ"), is("B"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｃ"), is("C"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｄ"), is("D"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｅ"), is("E"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｆ"), is("F"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｇ"), is("G"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｈ"), is("H"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｉ"), is("I"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｊ"), is("J"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｋ"), is("K"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｌ"), is("L"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｍ"), is("M"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｎ"), is("N"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｏ"), is("O"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｐ"), is("P"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｑ"), is("Q"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｒ"), is("R"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｓ"), is("S"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｔ"), is("T"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｕ"), is("U"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｖ"), is("V"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｗ"), is("W"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｘ"), is("X"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｙ"), is("Y"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｚ"), is("Z"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("［"), is("["));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("￥"), is("\\"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("］"), is("]"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＾"), is("^"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("＿"), is("_"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("｀"), is("`"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ａ"), is("a"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｂ"), is("b"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｃ"), is("c"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｄ"), is("d"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｅ"), is("e"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｆ"), is("f"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｇ"), is("g"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｈ"), is("h"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｉ"), is("i"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｊ"), is("j"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｋ"), is("k"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｌ"), is("l"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｍ"), is("m"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｎ"), is("n"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｏ"), is("o"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｐ"), is("p"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｑ"), is("q"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｒ"), is("r"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｓ"), is("s"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｔ"), is("t"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｕ"), is("u"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｖ"), is("v"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｗ"), is("w"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｘ"), is("x"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｙ"), is("y"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ｚ"), is("z"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("｛"), is("{"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("｜"), is("|"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("｝"), is("}"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth(String.valueOf('\uff5e')), is("~"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("。"), is("｡"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("「"), is("｢"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("」"), is("｣"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("、"), is("､"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("・"), is("･"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ァ"), is("ｧ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ィ"), is("ｨ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ゥ"), is("ｩ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ェ"), is("ｪ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ォ"), is("ｫ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ャ"), is("ｬ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ュ"), is("ｭ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ョ"), is("ｮ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ッ"), is("ｯ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ー"), is("ｰ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ア"), is("ｱ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("イ"), is("ｲ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ウ"), is("ｳ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("エ"), is("ｴ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("オ"), is("ｵ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("カ"), is("ｶ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("キ"), is("ｷ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ク"), is("ｸ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ケ"), is("ｹ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("コ"), is("ｺ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("サ"), is("ｻ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("シ"), is("ｼ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ス"), is("ｽ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("セ"), is("ｾ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ソ"), is("ｿ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("タ"), is("ﾀ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("チ"), is("ﾁ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ツ"), is("ﾂ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("テ"), is("ﾃ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ト"), is("ﾄ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ナ"), is("ﾅ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ニ"), is("ﾆ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヌ"), is("ﾇ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ネ"), is("ﾈ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ノ"), is("ﾉ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ハ"), is("ﾊ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヒ"), is("ﾋ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("フ"), is("ﾌ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヘ"), is("ﾍ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ホ"), is("ﾎ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("マ"), is("ﾏ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ミ"), is("ﾐ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ム"), is("ﾑ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("メ"), is("ﾒ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("モ"), is("ﾓ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヤ"), is("ﾔ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ユ"), is("ﾕ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヨ"), is("ﾖ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ラ"), is("ﾗ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("リ"), is("ﾘ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ル"), is("ﾙ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("レ"), is("ﾚ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ロ"), is("ﾛ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ン"), is("ﾝ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ガ"), is("ｶﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ギ"), is("ｷﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("グ"), is("ｸﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ゲ"), is("ｹﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ゴ"), is("ｺﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ザ"), is("ｻﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ジ"), is("ｼﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ズ"), is("ｽﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ゼ"), is("ｾﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ゾ"), is("ｿﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ダ"), is("ﾀﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヂ"), is("ﾁﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヅ"), is("ﾂﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("デ"), is("ﾃﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ド"), is("ﾄﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("バ"), is("ﾊﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ビ"), is("ﾋﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ブ"), is("ﾌﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("べ"), is("ﾍﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ボ"), is("ﾎﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("パ"), is("ﾊﾟ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ピ"), is("ﾋﾟ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("プ"), is("ﾌﾟ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ペ"), is("ﾍﾟ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ポ"), is("ﾎﾟ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ヴ"), is("ｳﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth(String.valueOf('\u30f7')), is("ﾜﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth(String.valueOf('\u30fa')), is("ｦﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("゛"), is("ﾞ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("゜"), is("ﾟ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("　"), is(" "));
	}

	@Test
	public void testToFull_NotMapped() throws Exception {
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("あ"), is("あ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("一"), is("一"));
	}

	@Test
	public void testToFull_MappedString() throws Exception {
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("メロン"), is("ﾒﾛﾝ"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("Ｈｅｌｌｏ　Ｗｏｒｌｄ！"), is("Hello World!"));
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("ハローワールド！"), is("ﾊﾛｰﾜｰﾙﾄﾞ!"));
	}

    @Test
    public void testToFull_NotMappedAndMappedString() throws Exception {
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("完熟メロンパン"), is("完熟ﾒﾛﾝﾊﾟﾝ"));
        assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("マチュピチュ遺跡"), is("ﾏﾁｭﾋﾟﾁｭ遺跡"));
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
	public void testToHalf10_半角全角混合文字が全て半角に変換されることを() throws Exception {
		assertThat(DefaultFullHalf.INSTANCE.toHalfwidth("サ\u30faｲAピ"), is("ｻｦﾞｲAﾋﾟ"));
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
