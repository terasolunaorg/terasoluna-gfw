/*
 * Copyright (C) 2013-2016 terasoluna.org
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

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Internationalization code list consisting of values/labels in Enum which implements {@link I18nCodeListItem}.
 * <ul>
 * <li>The result of {@link I18nCodeListItem#getCodeValue} is used as a value of code list (means it is used as a key of map).</li>
 * <li>The result of {@link I18nCodeListItem#getLocalizedCodeLabels} is used as a label of code list (means it is used as a value of map).</li>
 * </ul>
 * <p/>
 * <h3>For example :</h3>
 * <h4>Enum</h4>
 * <pre>
 * public static enum OrderStatus implements I18nEnumCodeList.I18nCodeListItem {
 *     RECEIVED("1", new HashMap&lt;Locale, String&gt;() {{
 *         put(Locale.JAPANESE, "受付済み");
 *         put(Locale.ENGLISH, "Received");
 *     }}),
 *     SENT("2", new HashMap&lt;Locale, String&gt;() {{
 *         put(Locale.JAPANESE, "発送済み");
 *         put(Locale.ENGLISH, "Sent");
 *     }}),
 *     CANCELLED("3", new HashMap&lt;Locale, String&gt;() {{
 *         put(Locale.JAPANESE, "キャンセル済み");
 *         put(Locale.ENGLISH, "Cancelled");
 *     }});
 *
 *     private final String value;
 *     private final Map&lt;Locale, String&gt; localizedLabels;
 *
 *     private OrderStatus(String value, Map&lt;Locale, String&gt; localizedLabels) {
 *         this.value = value;
 *         this.localizedLabels = localizedLabels;
 *     }
 *
 *     public String getCodeValue() {
 *         return value;
 *     }
 *
 *     public Map&lt;Locale, String&gt; getLocalizedCodeLabels() {
 *         return localizedLabels;
 *     }
 *
 * }
 * </pre>
 * <p/>
 * <h4>Bean definition</h4>
 * <pre>
 * &lt;bean id=&quot;CL_ORDER_STATUS&quot; class=&quot;org.terasoluna.gfw.common.codelist.i18n.I18nEnumCodeList&quot;&gt;
 *     &lt;constructor-arg value=&quot;com.example.domain.model.OrderStatus&quot;/&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @since 5.1.0
 */
public class I18nEnumCodeList extends AbstractI18nCodeList {

    /**
     * Interface of internationalization item in code list
     *
     * @since 5.1.0
     */
    public static interface I18nCodeListItem {

        /**
         * Returns the code value of item.
         *
         * @return code value of the item
         */
        String getCodeValue();

        /**
         * Returns localized code labels of item.
         *
         * @return localized code labels of the item
         */
        Map<Locale, String> getLocalizedCodeLabels();

    }

    /**
     * Supplier to return a {@link LinkedHashMap} object.
     */
    private static final Supplier<LinkedHashMap<String, String>> LINKED_HASH_MAP_SUPPLIER = new Supplier<LinkedHashMap<String, String>>() {
        @Override
        public LinkedHashMap<String, String> get() {
            return Maps.newLinkedHashMap();
        }
    };

    /**
     * Code list table per locale
     */
    private final Table<Locale, String, String> localizedCodeListTable;

    /**
     * Constructor.
     *
     * @param enumClass Enum class of which this code list consists. Must implement {@link I18nCodeListItem}
     * @throws java.lang.IllegalArgumentException if the given class is {@code null}
     * @throws java.lang.IllegalArgumentException if the given class does not implement {@link I18nCodeListItem}
     */
    public I18nEnumCodeList(Class<? extends Enum<?>> enumClass) {
        Assert.notNull(enumClass, "the given enumClass is null");
        Assert.isTrue(I18nCodeListItem.class.isAssignableFrom(enumClass),
                "the given enumClass must implement " + I18nCodeListItem.class);

        Method valuesMethod = ReflectionUtils.findMethod(enumClass, "values");
        Enum<?>[] enumValues = (Enum<?>[]) ReflectionUtils.invokeMethod(
                valuesMethod, enumClass);

        Table<Locale, String, String> localizedCodeListTable = createLocaleTable();
        for (Enum<?> enumValue : enumValues) {
            I18nCodeListItem i18nCodeListItem = (I18nCodeListItem) enumValue;
            for (Map.Entry<Locale, String> localizedLabel : i18nCodeListItem.getLocalizedCodeLabels().entrySet()) {
                localizedCodeListTable.put(localizedLabel.getKey(), i18nCodeListItem.getCodeValue(), localizedLabel.getValue());
            }
        }

        this.localizedCodeListTable = Tables.unmodifiableTable(localizedCodeListTable);
    }

    /**
     * Returns code list map of specified locale.
     *
     * @throws IllegalArgumentException if the given locale is {@code null}
     * @see org.terasoluna.gfw.common.codelist.i18n.I18nCodeList#asMap(java.util.Locale)
     */
    @Override
    public Map<String, String> asMap(Locale locale) {
        Assert.notNull(locale, "the given locale is null");
        return localizedCodeListTable.row(locale);
    }

    /**
     * Create table which consist of {@link LinkedHashMap} factory.
     *
     * @return table which consist of {@link LinkedHashMap} factory
     */
    private Table<Locale, String, String> createLocaleTable() {
        Map<Locale, Map<String, String>> backingMap = Maps.newLinkedHashMap();
        return Tables.newCustomTable(backingMap, LINKED_HASH_MAP_SUPPLIER);
    }

}