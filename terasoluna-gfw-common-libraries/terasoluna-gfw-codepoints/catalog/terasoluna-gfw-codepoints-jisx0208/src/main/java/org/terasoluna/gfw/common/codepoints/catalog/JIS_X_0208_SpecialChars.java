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
package org.terasoluna.gfw.common.codepoints.catalog;

import org.terasoluna.gfw.common.codepoints.CodePoints;

/**
 * Code points which consist of JIS X 0208's rows 1 and 2 (Special characters).
 * <p>
 * This class defines the code point set to change HORINZONTAL BAR( <tt>U+2015</tt>) to the EM DASH(<tt>U+2014</tt>). <br>
 * For general conversion rules and Unicode conversion tables are different. There is a possibility that a problem may arise
 * according to Unicode conversion table when code point set is defined. <br>
 * Full-size dash of common JIS Kanji is the EM DASH(<tt>U+2014</tt>). <br>
 * Conversion table Unicode consortium provides is HORINZONTAL BAR( <tt>U+2015</tt>).
 * @since 5.1.0
 */
public final class JIS_X_0208_SpecialChars extends CodePoints {

    /**
     * Constructor.
     */
    public JIS_X_0208_SpecialChars() {
        super(0x3000 /* (01-01) */, 0x3001 /* 、 (01-02) */,
                0x3002 /* 。 (01-03) */, 0xFF0C /* ， (01-04) */,
                0xFF0E /* ． (01-05) */, 0x30FB /* ・ (01-06) */,
                0xFF1A /* ： (01-07) */, 0xFF1B /* ； (01-08) */,
                0xFF1F /* ？ (01-09) */, 0xFF01 /* ！ (01-10) */,
                0x309B /* ゛ (01-11) */, 0x309C /* ゜ (01-12) */,
                0x00B4 /* ´ (01-13) */, 0xFF40 /* ｀ (01-14) */,
                0x00A8 /* ¨ (01-15) */, 0xFF3E /* ＾ (01-16) */,
                0xFFE3 /* ￣ (01-17) */, 0xFF3F /* ＿ (01-18) */,
                0x30FD /* ヽ (01-19) */, 0x30FE /* ヾ (01-20) */,
                0x309D /* ゝ (01-21) */, 0x309E /* ゞ (01-22) */,
                0x3003 /* 〃 (01-23) */, 0x4EDD /* 仝 (01-24) */,
                0x3005 /* 々 (01-25) */, 0x3006 /* 〆 (01-26) */,
                0x3007 /* 〇 (01-27) */, 0x30FC /* ー (01-28) */,
                0x2014 /* — (01-29) */, 0x2010 /* ‐ (01-30) */,
                0xFF0F /* ／ (01-31) */, 0x005C /* \ (01-32) */,
                0x301C /* 〜 (01-33) */, 0x2016 /* ‖ (01-34) */,
                0xFF5C /* ｜ (01-35) */, 0x2026 /* … (01-36) */,
                0x2025 /* ‥ (01-37) */, 0x2018 /* ‘ (01-38) */,
                0x2019 /* ’ (01-39) */, 0x201C /* “ (01-40) */,
                0x201D /* ” (01-41) */, 0xFF08 /* （ (01-42) */,
                0xFF09 /* ） (01-43) */, 0x3014 /* 〔 (01-44) */,
                0x3015 /* 〕 (01-45) */, 0xFF3B /* ［ (01-46) */,
                0xFF3D /* ］ (01-47) */, 0xFF5B /* ｛ (01-48) */,
                0xFF5D /* ｝ (01-49) */, 0x3008 /* 〈 (01-50) */,
                0x3009 /* 〉 (01-51) */, 0x300A /* 《 (01-52) */,
                0x300B /* 》 (01-53) */, 0x300C /* 「 (01-54) */,
                0x300D /* 」 (01-55) */, 0x300E /* 『 (01-56) */,
                0x300F /* 』 (01-57) */, 0x3010 /* 【 (01-58) */,
                0x3011 /* 】 (01-59) */, 0xFF0B /* ＋ (01-60) */,
                0x2212 /* − (01-61) */, 0x00B1 /* ± (01-62) */,
                0x00D7 /* × (01-63) */, 0x00F7 /* ÷ (01-64) */,
                0xFF1D /* ＝ (01-65) */, 0x2260 /* ≠ (01-66) */,
                0xFF1C /* ＜ (01-67) */, 0xFF1E /* ＞ (01-68) */,
                0x2266 /* ≦ (01-69) */, 0x2267 /* ≧ (01-70) */,
                0x221E /* ∞ (01-71) */, 0x2234 /* ∴ (01-72) */,
                0x2642 /* ♂ (01-73) */, 0x2640 /* ♀ (01-74) */,
                0x00B0 /* ° (01-75) */, 0x2032 /* ′ (01-76) */,
                0x2033 /* ″ (01-77) */, 0x2103 /* ℃ (01-78) */,
                0xFFE5 /* ￥ (01-79) */, 0xFF04 /* ＄ (01-80) */,
                0x00A2 /* ¢ (01-81) */, 0x00A3 /* £ (01-82) */,
                0xFF05 /* ％ (01-83) */, 0xFF03 /* ＃ (01-84) */,
                0xFF06 /* ＆ (01-85) */, 0xFF0A /* ＊ (01-86) */,
                0xFF20 /* ＠ (01-87) */, 0x00A7 /* § (01-88) */,
                0x2606 /* ☆ (01-89) */, 0x2605 /* ★ (01-90) */,
                0x25CB /* ○ (01-91) */, 0x25CF /* ● (01-92) */,
                0x25CE /* ◎ (01-93) */, 0x25C7 /* ◇ (01-94) */,
                0x25C6 /* ◆ (02-01) */, 0x25A1 /* □ (02-02) */,
                0x25A0 /* ■ (02-03) */, 0x25B3 /* △ (02-04) */,
                0x25B2 /* ▲ (02-05) */, 0x25BD /* ▽ (02-06) */,
                0x25BC /* ▼ (02-07) */, 0x203B /* ※ (02-08) */,
                0x3012 /* 〒 (02-09) */, 0x2192 /* → (02-10) */,
                0x2190 /* ← (02-11) */, 0x2191 /* ↑ (02-12) */,
                0x2193 /* ↓ (02-13) */, 0x3013 /* 〓 (02-14) */,
                0x2208 /* ∈ (02-26) */, 0x220B /* ∋ (02-27) */,
                0x2286 /* ⊆ (02-28) */, 0x2287 /* ⊇ (02-29) */,
                0x2282 /* ⊂ (02-30) */, 0x2283 /* ⊃ (02-31) */,
                0x222A /* ∪ (02-32) */, 0x2229 /* ∩ (02-33) */,
                0x2227 /* ∧ (02-42) */, 0x2228 /* ∨ (02-43) */,
                0x00AC /* ¬ (02-44) */, 0x21D2 /* ⇒ (02-45) */,
                0x21D4 /* ⇔ (02-46) */, 0x2200 /* ∀ (02-47) */,
                0x2203 /* ∃ (02-48) */, 0x2220 /* ∠ (02-60) */,
                0x22A5 /* ⊥ (02-61) */, 0x2312 /* ⌒ (02-62) */,
                0x2202 /* ∂ (02-63) */, 0x2207 /* ∇ (02-64) */,
                0x2261 /* ≡ (02-65) */, 0x2252 /* ≒ (02-66) */,
                0x226A /* ≪ (02-67) */, 0x226B /* ≫ (02-68) */,
                0x221A /* √ (02-69) */, 0x223D /* ∽ (02-70) */,
                0x221D /* ∝ (02-71) */, 0x2235 /* ∵ (02-72) */,
                0x222B /* ∫ (02-73) */, 0x222C /* ∬ (02-74) */,
                0x212B /* Å (02-82) */, 0x2030 /* ‰ (02-83) */,
                0x266F /* ♯ (02-84) */, 0x266D /* ♭ (02-85) */,
                0x266A /* ♪ (02-86) */, 0x2020 /* † (02-87) */,
                0x2021 /* ‡ (02-88) */, 0x00B6 /* ¶ (02-89) */,
                0x25EF /* ◯ (02-94) */);
    }
}
