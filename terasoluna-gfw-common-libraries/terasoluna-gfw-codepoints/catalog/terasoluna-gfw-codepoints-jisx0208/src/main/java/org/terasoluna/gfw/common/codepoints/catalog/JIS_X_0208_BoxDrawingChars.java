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
 * Code points which consist of JIS X 0208's row 8 (Box drawing characters)
 * @since 5.1.0
 */
public final class JIS_X_0208_BoxDrawingChars extends CodePoints {

    /**
     * Constructor.
     */
    public JIS_X_0208_BoxDrawingChars() {
        super(0x2500 /* ─ (08-01) */, 0x2502 /* │ (08-02) */,
                0x250C /* ┌ (08-03) */, 0x2510 /* ┐ (08-04) */,
                0x2518 /* ┘ (08-05) */, 0x2514 /* └ (08-06) */,
                0x251C /* ├ (08-07) */, 0x252C /* ┬ (08-08) */,
                0x2524 /* ┤ (08-09) */, 0x2534 /* ┴ (08-10) */,
                0x253C /* ┼ (08-11) */, 0x2501 /* ━ (08-12) */,
                0x2503 /* ┃ (08-13) */, 0x250F /* ┏ (08-14) */,
                0x2513 /* ┓ (08-15) */, 0x251B /* ┛ (08-16) */,
                0x2517 /* ┗ (08-17) */, 0x2523 /* ┣ (08-18) */,
                0x2533 /* ┳ (08-19) */, 0x252B /* ┫ (08-20) */,
                0x253B /* ┻ (08-21) */, 0x254B /* ╋ (08-22) */,
                0x2520 /* ┠ (08-23) */, 0x252F /* ┯ (08-24) */,
                0x2528 /* ┨ (08-25) */, 0x2537 /* ┷ (08-26) */,
                0x253F /* ┿ (08-27) */, 0x251D /* ┝ (08-28) */,
                0x2530 /* ┰ (08-29) */, 0x2525 /* ┥ (08-30) */,
                0x2538 /* ┸ (08-31) */, 0x2542 /* ╂ (08-32) */);
    }
}
