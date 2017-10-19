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
 * Code points which consist of ASCII Control characters (0x0000-0x001F, 0x007F).
 * @since 5.1.0
 */
public final class ASCIIControlChars extends CodePoints {

    /**
     * Constructor
     */
    public ASCIIControlChars() {
        super(0x0000 /* NULL */, 0x0001 /* START OF HEADING */,
                0x0002 /* START OF TEXT */, 0x0003 /* END OF TEXT */,
                0x0004 /* END OF TRANSMISSION */, 0x0005 /* ENQUIRY */,
                0x0006 /* ACKNOWLEDGE */, 0x0007 /* BELL */,
                0x0008 /* BACKSPACE */, 0x0009 /* HORIZONTAL TABULATION */,
                0x000A /* LINE FEED */, 0x000B /* VERTICAL TABULATION */,
                0x000C /* FORM FEED */, 0x000D /* CARRIAGE RETURN */,
                0x000E /* SHIFT OUT */, 0x000F /* SHIFT IN */,
                0x0010 /* DATA LINK ESCAPE */, 0x0011 /* DEVICE CONTROL ONE */,
                0x0012 /* DEVICE CONTROL TWO */,
                0x0013 /* DEVICE CONTROL THREE */,
                0x0014 /* DEVICE CONTROL FOUR */,
                0x0015 /* NEGATIVE ACKNOWLEDGE */,
                0x0016 /* SYNCHRONOUS IDLE */,
                0x0017 /* END OF TRANSMISSION BLOCK */, 0x0018 /* CANCEL */,
                0x0019 /* END OF MEDIUM */, 0x001A /* SUBSTITUTE */,
                0x001B /* ESCAPE */, 0x001C /* FILE SEPARATOR */,
                0x001D /* GROUP SEPARATOR */, 0x001E /* RECORD SEPARATOR */,
                0x001F /* UNIT SEPARATOR */, 0x007F /* DELETE */);
    }
}
