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
package org.terasoluna.gfw.common.codelist.i18n;

import java.util.Locale;
import java.util.Map;

import org.terasoluna.gfw.common.codelist.AbstractCodeList;

/**
 * Abstract extended implementation of {@link AbstractCodeList}. Adds Internationalization support to {@link AbstractCodeList}
 * by implementing {I18nCodeList} interface.
 */
public abstract class AbstractI18nCodeList extends AbstractCodeList implements
                                           I18nCodeList {

    /**
     * <p>
     * Returns a codelist as map for the default locale ({@link Locale#getDefault()}). <br>
     * if there is no codelist for the locale, returns an empty map.
     * </p>
     * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
     */
    @Override
    public Map<String, String> asMap() {
        return asMap(Locale.getDefault());
    }

}
