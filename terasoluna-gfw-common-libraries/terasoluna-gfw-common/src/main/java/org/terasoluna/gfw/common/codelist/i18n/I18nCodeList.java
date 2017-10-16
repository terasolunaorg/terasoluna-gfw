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

import org.terasoluna.gfw.common.codelist.CodeList;

/**
 * Adds Internationalization support to {@link CodeList}
 */
public interface I18nCodeList extends CodeList {
    /**
     * Returns the localized codelist as a Map.<br>
     * <p>
     * <code>locale</code> is locale of the target codelist.<br>
     * Key of the map means "value" of the codelist.Value of the map means "label" of the codelist<br>
     * The type of Key and Value both, is String<br>
     * Map must be unmodifiable. <br>
     * If no mappings have the provided locale, an empty map is returned.
     * </p>
     * @param locale locale of codelist
     * @return localized codelist(not null)
     * @throws IllegalArgumentException if locale is null.
     */
    Map<String, String> asMap(Locale locale);
}
