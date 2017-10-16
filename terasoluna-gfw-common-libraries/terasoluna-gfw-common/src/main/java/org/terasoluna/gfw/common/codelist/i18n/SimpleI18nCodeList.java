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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.codelist.CodeList;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 * Simple implementation of {@link I18nCodeList}<br>
 * <p>
 * {@link I18nCodeList} has a table of codelist.<br>
 * Each row is a codelist for each language and represented as <strong>unmodifiable linked hash maps</strong>.<br>
 * The key of rows is {@link Locale}.The key of columns is {@link String}(code). <br>
 * There are 3 ways to express the following table.
 * </p>
 * <table border=1>
 * <caption>code list table of week</caption>
 * <tr>
 * <th>row=Locale,column=code</th>
 * <th>0</th>
 * <th>1</th>
 * <th>2</th>
 * <th>3</th>
 * <th>4</th>
 * <th>5</th>
 * <th>6</th>
 * </tr>
 * <tr>
 * <th>en</th>
 * <td>Sun.</td>
 * <td>Mon.</td>
 * <td>Tue.</td>
 * <td>Wed.</td>
 * <td>Thu.</td>
 * <td>Fri.</td>
 * <td>Sat.</td>
 * </tr>
 * <tr>
 * <th>ja</th>
 * <td>日</td>
 * <td>月</td>
 * <td>火</td>
 * <td>水</td>
 * <td>木</td>
 * <td>金</td>
 * <td>土</td>
 * </tr>
 * </table>
 * <h3>(Way 1) set by rows</h3>
 * 
 * <pre>
 * &lt;bean id=&quot;CL_I18N_WEEK&quot;
 *     class=&quot;org.terasoluna.gfw.common.codelist.i18n.SimpleI18nCodeList&quot;&gt;
 *     &lt;property name=&quot;rows&quot;&gt;
 *         &lt;util:map&gt;
 *             &lt;entry key=&quot;en&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;0&quot; value=&quot;Sun.&quot; /&gt;
 *                     &lt;entry key=&quot;1&quot; value=&quot;Mon.&quot; /&gt;
 *                     &lt;entry key=&quot;2&quot; value=&quot;Tue.&quot; /&gt;
 *                     &lt;entry key=&quot;3&quot; value=&quot;Wed.&quot; /&gt;
 *                     &lt;entry key=&quot;4&quot; value=&quot;Thu.&quot; /&gt;
 *                     &lt;entry key=&quot;5&quot; value=&quot;Fri.&quot; /&gt;
 *                     &lt;entry key=&quot;6&quot; value=&quot;Sat.&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;ja&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;0&quot; value=&quot;日&quot; /&gt;
 *                     &lt;entry key=&quot;1&quot; value=&quot;月&quot; /&gt;
 *                     &lt;entry key=&quot;2&quot; value=&quot;火&quot; /&gt;
 *                     &lt;entry key=&quot;3&quot; value=&quot;水&quot; /&gt;
 *                     &lt;entry key=&quot;4&quot; value=&quot;木&quot; /&gt;
 *                     &lt;entry key=&quot;5&quot; value=&quot;金&quot; /&gt;
 *                     &lt;entry key=&quot;6&quot; value=&quot;土&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *         &lt;/util:map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <h3>(Way2) set by rows with codelist</h3>
 * 
 * <pre>
 * &lt;bean id=&quot;CL_I18N_WEEK&quot;
 *     class=&quot;org.terasoluna.gfw.common.codelist.i18n.SimpleI18nCodeList&quot;&gt;
 *     &lt;property name=&quot;rowsByCodeList&quot;&gt;
 *         &lt;util:map&gt;
 *             &lt;entry key=&quot;en&quot; value-ref=&quot;CL_WEEK_EN&quot; /&gt;
 *             &lt;entry key=&quot;ja&quot; value-ref=&quot;CL_WEEK_JA&quot; /&gt;
 *         &lt;/util:map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * 
 *  &lt;bean id=&quot;CL_WEEK_EN&quot; class=&quot;org.terasoluna.gfw.common.codelist.SimpleMapCodeList&quot;&gt;
 *     &lt;property name=&quot;map&quot;&gt;
 *         &lt;util:map&gt;
 *             &lt;entry key=&quot;0&quot; value=&quot;Sun.&quot; /&gt;
 *             &lt;entry key=&quot;1&quot; value=&quot;Mon.&quot; /&gt;
 *             &lt;entry key=&quot;2&quot; value=&quot;Tue.&quot; /&gt;
 *             &lt;entry key=&quot;3&quot; value=&quot;Wed.&quot; /&gt;
 *             &lt;entry key=&quot;4&quot; value=&quot;Thu.&quot; /&gt;
 *             &lt;entry key=&quot;5&quot; value=&quot;Fri.&quot; /&gt;
 *             &lt;entry key=&quot;6&quot; value=&quot;Sat.&quot; /&gt;
 *         &lt;/util:map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * 
 * &lt;bean id=&quot;CL_WEEK_JA&quot; class=&quot;org.terasoluna.gfw.common.codelist.SimpleMapCodeList&quot;&gt;
 *     &lt;property name=&quot;map&quot;&gt;
 *         &lt;util:map&gt;
 *             &lt;entry key=&quot;0&quot; value=&quot;日&quot; /&gt;
 *             &lt;entry key=&quot;1&quot; value=&quot;月&quot; /&gt;
 *             &lt;entry key=&quot;2&quot; value=&quot;火&quot; /&gt;
 *             &lt;entry key=&quot;3&quot; value=&quot;水&quot; /&gt;
 *             &lt;entry key=&quot;4&quot; value=&quot;木&quot; /&gt;
 *             &lt;entry key=&quot;5&quot; value=&quot;金&quot; /&gt;
 *             &lt;entry key=&quot;6&quot; value=&quot;土&quot; /&gt;
 *         &lt;/util:map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * <h3>(Way3) set by columns</h3>
 * 
 * <pre>
 * &lt;bean id=&quot;CL_I18N_WEEK&quot;
 *     class=&quot;org.terasoluna.gfw.common.codelist.i18n.SimpleI18nCodeList&quot;&gt;
 *     &lt;property name=&quot;columns&quot;&gt;
 *         &lt;util:map&gt;
 *             &lt;entry key=&quot;0&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Sun.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;日&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;1&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Mon.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;月&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;2&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Tue.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;火&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;3&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Wed.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;水&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;4&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Thu.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;木&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;5&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Fri.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;金&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *             &lt;entry key=&quot;6&quot;&gt;
 *                 &lt;util:map&gt;
 *                     &lt;entry key=&quot;en&quot; value=&quot;Sat.&quot; /&gt;
 *                     &lt;entry key=&quot;ja&quot; value=&quot;土&quot; /&gt;
 *                 &lt;/util:map&gt;
 *             &lt;/entry&gt;
 *         &lt;/util:map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 */
public class SimpleI18nCodeList extends AbstractI18nCodeList implements
                                InitializingBean {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(
            SimpleI18nCodeList.class);

    /**
     * codelist table.
     */
    Table<Locale, String, String> codeListTable;

    /**
     * supplier to return a {@link LinkedHashMap} object.
     */
    private static final Supplier<LinkedHashMap<String, String>> LINKED_HASH_MAP_SUPPLIER = new Supplier<LinkedHashMap<String, String>>() {
        @Override
        public LinkedHashMap<String, String> get() {
            return Maps.newLinkedHashMap();
        }
    };

    /**
     * <p>
     * returns row of codelist table.
     * </p>
     * @see org.terasoluna.gfw.common.codelist.i18n.I18nCodeList#asMap(java.util.Locale)
     */
    @Override
    public Map<String, String> asMap(Locale locale) {
        Assert.notNull(locale, "locale is null");
        return codeListTable.row(locale);
    }

    /**
     * check whether the code list table is null.<br>
     * <p>
     * output warn log in case of table is null.
     * </p>
     */
    private void checkTable() {
        if (codeListTable != null) {
            logger.warn("Codelist table has already built, but re-build");
        }
    }

    /**
     * set table by rows ({@link Map}).<br>
     * <p>
     * The key is {@link Locale} and the value is {@link Map} which represents {@link CodeList}.<br>
     * </p>
     * @param rows table by rows ({@link Map}) per locale
     */
    public void setRows(Map<Locale, Map<String, String>> rows) {
        checkTable();
        Table<Locale, String, String> table = createTable();
        for (Map.Entry<Locale, Map<String, String>> e : rows.entrySet()) {
            Locale locale = e.getKey();
            Map<String, String> row = e.getValue();
            for (Map.Entry<String, String> re : row.entrySet()) {
                String value = re.getKey();
                String label = re.getValue();
                table.put(locale, value, label);
            }
        }
        this.codeListTable = Tables.unmodifiableTable(table);
    }

    /**
     * set table by rows ({@link CodeList})<br>
     * <p>
     * The key is {@link Locale} and the value is {@link CodeList}.<br>
     * </p>
     * @param rows table by rows ({@link CodeList}) per locale
     */
    public void setRowsByCodeList(Map<Locale, CodeList> rows) {
        checkTable();
        Table<Locale, String, String> table = createTable();
        for (Map.Entry<Locale, CodeList> e : rows.entrySet()) {
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
     * set table by columns ({@link Map})<br>
     * <p>
     * The key is {@link Locale} and value is {@link Map}. <br>
     * </p>
     * @param cols table by columns ({@link Map}) per locale
     */
    public void setColumns(Map<String, Map<Locale, String>> cols) {
        checkTable();
        Table<Locale, String, String> table = createTable();
        for (Map.Entry<String, Map<Locale, String>> e : cols.entrySet()) {
            String value = e.getKey();
            Map<Locale, String> col = e.getValue();
            for (Map.Entry<Locale, String> ce : col.entrySet()) {
                Locale locale = ce.getKey();
                String label = ce.getValue();
                table.put(locale, value, label);
            }
        }
        this.codeListTable = Tables.unmodifiableTable(table);
    }

    /**
     * create table which consist of {@link LinkedHashMap} factory.
     * @return table
     */
    private Table<Locale, String, String> createTable() {
        Map<Locale, Map<String, String>> backingMap = Maps.newLinkedHashMap();
        Table<Locale, String, String> table = Tables.newCustomTable(backingMap,
                LINKED_HASH_MAP_SUPPLIER);
        return table;
    }

    /**
     * <p>
     * check whether codeListTable is initialized.
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(codeListTable, "codeListTable is not initialized!");
    }
}
