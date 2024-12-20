/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.codelist.ReloadableCodeList;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 * Reloadable implementation of {@link I18nCodeList}<br>
 * <p>
 * {@link I18nCodeList} has a table of codelist.<br>
 * Each row is a codelist for each language and represented as <strong>unmodifiable linked hash
 * maps</strong>.<br>
 * The key of rows is {@link Locale}. <br>
 * </p>
 * <p>
 * To build a table of codelist, set a map of the {@link Locale} and the corresponding
 * {@link ReloadableCodeList}.<br>
 * </p>
 * <h3>set by rows with {@link ReloadableCodeList}</h3>
 *
 * <pre>
 * &lt;bean id=&quot;CL_I18N_WEEK&quot;
 *     class=&quot;org.terasoluna.gfw.common.codelist.i18n.ReloadableI18nCodeList&quot;&gt;
 *     &lt;property name=&quot;rowsByCodeList&quot;&gt;
 *         &lt;util:map&gt;
 *             &lt;entry key=&quot;en&quot; value-ref=&quot;CL_PRICE_EN&quot; /&gt;
 *             &lt;entry key=&quot;ja&quot; value-ref=&quot;CL_PRICE_JA&quot; /&gt;
 *         &lt;/util:map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id=&quot;AbstractJdbcCodeList&quot;
 *     class=&quot;org.terasoluna.gfw.common.codelist.JdbcCodeList&quot; abstract=&quot;true&quot;&gt;
 *     &lt;property name=&quot;jdbcTemplate&quot; ref=&quot;jdbcTemplateForCodeList&quot; /&gt;
 * &lt;/bean&gt;
 *
 *
 * &lt;bean id=&quot;CL_PRICE_EN&quot; parent=&quot;AbstractJdbcCodeList&quot;&gt;
 *     &lt;property name=&quot;querySql&quot;
 *         value=&quot;SELECT code, label FROM price WHERE locale = 'en' ORDER BY code&quot; /&gt;
 *     &lt;property name=&quot;valueColumn&quot; value=&quot;code&quot; /&gt;
 *     &lt;property name=&quot;labelColumn&quot; value=&quot;label&quot; /&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id=&quot;CL_PRICE_JA&quot; parent=&quot;AbstractJdbcCodeList&quot;&gt;
 *     &lt;property name=&quot;querySql&quot;
 *         value=&quot;SELECT code, label FROM price WHERE locale = 'ja' ORDER BY code&quot; /&gt;
 *     &lt;property name=&quot;valueColumn&quot; value=&quot;code&quot; /&gt;
 *     &lt;property name=&quot;labelColumn&quot; value=&quot;label&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @since 5.4.2
 */
public class SimpleReloadableI18nCodeList extends AbstractI18nCodeList
        implements ReloadableI18nCodeList, InitializingBean {
    /**
     * Logger.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(SimpleReloadableI18nCodeList.class);

    /**
     * Codelist table.
     */
    private Table<Locale, String, String> codeListTable;

    /**
     * Codelist for each locale.
     */
    private Map<Locale, ReloadableCodeList> codeLists;

    /**
     * Lazy initialization flag.
     */
    private boolean lazyInit = false;

    /**
     * Supplier to return a {@link LinkedHashMap} object.
     */
    private static final Supplier<LinkedHashMap<String, String>> LINKED_HASH_MAP_SUPPLIER =
            new Supplier<LinkedHashMap<String, String>>() {
                @Override
                public LinkedHashMap<String, String> get() {
                    return Maps.newLinkedHashMap();
                }
            };

    /**
     * Set ({@link ReloadableCodeList}) for each locale.
     * <p>
     * The key is {@link Locale} and the value is {@link ReloadableCodeList}.
     * </p>
     * @param codeLists ({@link ReloadableCodeList}) for each locale
     */
    public void setRowsByCodeList(Map<Locale, ReloadableCodeList> codeLists) {
        this.codeLists = codeLists;
    }

    /**
     * Flag that determines whether the codelist information needs to be eager fetched.
     * @param lazyInit flag
     */
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Reloads the codelist.
     * @see org.terasoluna.gfw.common.codelist.ReloadableCodeList#refresh()
     * @see org.terasoluna.gfw.common.codelist.i18n.SimpleReloadableI18nCodeList#refresh(boolean)
     */
    @Override
    public void refresh() {
        refresh(true);
    }

    /**
     * Reloads the codelist recursively.
     * @param recursive whether or not reload recursively.
     * @see org.terasoluna.gfw.common.codelist.i18n.ReloadableI18nCodeList#refresh(boolean)
     */
    @Override
    public void refresh(boolean recursive) {
        if (recursive) {
            for (ReloadableCodeList codeList : codeLists.values()) {
                codeList.refresh();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("refresh codelist codeListId={}", getCodeListId());
        }
        Table<Locale, String, String> table = createTable();
        for (Map.Entry<Locale, ReloadableCodeList> e : codeLists.entrySet()) {
            Locale locale = e.getKey();
            Map<String, String> row = e.getValue().asMap();
            for (Map.Entry<String, String> re : row.entrySet()) {
                String value = re.getKey();
                String label = re.getValue();
                table.put(locale, value, label);
            }
        }
        this.codeListTable = Tables.unmodifiableTable(table);
    }

    /**
     * Resolve locale and obtain the codelist of specified locale.
     * @see org.terasoluna.gfw.common.codelist.i18n.AbstractI18nCodeList#obtainMap(java.util.Locale)
     */
    @Override
    protected Map<String, String> obtainMap(Locale locale) {
        // If codeListTable is null, that means it is called for the first time
        // and lazyInit must be set to true
        if (codeListTable == null) {
            refresh(true);
        }
        return codeListTable.row(resolveLocale(locale));
    }

    /**
     * This method is called after the properties of the codelist are set.
     * <p>
     * check whether codeLists is initialized. <br>
     * Checks the lazyInit flag to determine whether the codelist should be refreshed after the
     * properties are set.<br>
     * If lazyInit flag is set to true, the codelist is not refreshed immediately. <br>
     * If it is set to false, it is refreshed (values re-loaded) immediately after the properties
     * are loaded
     * </p>
     * @see org.terasoluna.gfw.common.codelist.i18n.AbstractI18nCodeList#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notEmpty(codeLists, "codeLists is not initialized!");
        super.afterPropertiesSet();

        if (!lazyInit) {
            refresh();
        }
    }

    /**
     * Register locales of {@link #codeLists}.
     * @see org.terasoluna.gfw.common.codelist.i18n.AbstractI18nCodeList#registerCodeListLocales()
     */
    @Override
    protected Set<Locale> registerCodeListLocales() {
        return codeLists.keySet();
    }

    /**
     * Create table which consist of {@link LinkedHashMap} factory.
     * @return table
     */
    private Table<Locale, String, String> createTable() {
        Map<Locale, Map<String, String>> backingMap = Maps.newLinkedHashMap();
        Table<Locale, String, String> table =
                Tables.newCustomTable(backingMap, LINKED_HASH_MAP_SUPPLIER);
        return table;
    }
}
