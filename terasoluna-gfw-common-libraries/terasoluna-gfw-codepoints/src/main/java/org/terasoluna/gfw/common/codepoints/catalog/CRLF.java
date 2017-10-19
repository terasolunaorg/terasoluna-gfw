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
 * Code points which consist of 0x000A(LINE FEED) and 0x000D(CARRIAGE RETURN)
 * @since 5.1.0
 */
public final class CRLF extends CodePoints {

    /**
     * Constructor
     */
    public CRLF() {
        super(0x000A /* LINE FEED */, 0x000D /* CARRIAGE RETURN */);
    }
}
