/*
 * Copyright(c) 2013 NTT DATA Corporation.
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

import org.terasoluna.gfw.common.codelist.ReloadableCodeList;

/**
 * Adds Reloadable support to {@link I18nCodeList}
 * @since 5.4.2
 */
public interface ReloadableI18nCodeList extends I18nCodeList,
                                        ReloadableCodeList {

    /**
     * reloads the codelist recursively
     * @param recursive whether or not reload recursively.
     */
    void refresh(boolean recursive);
}
