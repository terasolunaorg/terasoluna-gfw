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
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Internationalization code list consisting of values/labels in Enum which implements {@link I18nCodeListItem}.
 * <ul>
 * <li>The result of {@link I18nCodeListItem#getCodeValue} is used as a value of code list (means it is used as a key of map).</li>
 * <li>The result of {@link I18nCodeListItem#getLocalizedCodeLabels} is used as a label of code list (means it is used as a value of map).</li>
 * </ul>
 * <h3>For example :</h3>
 * <h4>Enum</h4>
 * <pre>
 * public enum OrderStatus implements EnumI18nCodeList.I18nCodeListItem {
 *     RECEIVED("1", EnumI18nCodeList.LocalizedCodeLabels.builder()
 *         .label(Locale.JAPANESE, "受付済み")
 *         .label(Locale.ENGLISH, "Received")
 *         .build()),
 *     SENT("2", EnumI18nCodeList.LocalizedCodeLabels.builder()
 *         .label(Locale.JAPANESE, "発送済み")
 *         .label(Locale.ENGLISH, "Sent")
 *         .build()),
 *     CANCELLED("3", EnumI18nCodeList.LocalizedCodeLabels.builder()
 *         .label(Locale.JAPANESE, "キャンセル済み")
 *         .label(Locale.ENGLISH, "Cancelled")
 *         .build());
 *
 *     private final String codeValue;
 *     private final EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels;
 *
 *     private OrderStatus(String codeValue, EnumI18nCodeList.LocalizedCodeLabels localizedCodeLabels) {
 *         this.codeValue = codeValue;
 *         this.localizedCodeLabels = localizedCodeLabels;
 *     }
 *
 *    {@literal @Override}
 *     public String getCodeValue() {
 *         return codeValue;
 *     }
 *
 *    {@literal @Override}
 *     public EnumI18nCodeList.LocalizedCodeLabels getLocalizedCodeLabels() {
 *         return localizedCodeLabels;
 *     }
 * }
 * </pre>
 * <h4>Bean definition</h4>
 * <pre>
 * &lt;bean id=&quot;CL_ORDER_STATUS&quot; class=&quot;org.terasoluna.gfw.common.codelist.i18n.EnumI18nCodeList&quot;&gt;
 *     &lt;constructor-arg value=&quot;com.example.domain.model.OrderStatus&quot;/&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @since 5.1.0
 */
public class EnumI18nCodeList extends AbstractI18nCodeList {

    /**
     * Interface of internationalization item in code list
     */
    public interface I18nCodeListItem {

        /**
         * Return a code value of item.
         *
         * @return code value of the item
         */
        String getCodeValue();

        /**
         * Return a localized code labels of item.
         *
         * @return localized code labels of item
         */
        LocalizedCodeLabels getLocalizedCodeLabels();

    }

    /**
     * Class that hold internationalization labels
     */
    public static final class LocalizedCodeLabels {

        /**
         * Map of localized code labels.
         * <p>
         * Key of the map means "locale" and Value of the map means "localized code label".
         * </p>
         */
        private final Map<Locale, String> localizedCodeLabelMap;

        /**
         * Constructor.
         *
         * @param localizedCodeLabelMap Map of localized code labels (Key of the map means "locale" and Value of the map means "localized code label")
         */
        public LocalizedCodeLabels(Map<Locale, String> localizedCodeLabelMap) {
            this.localizedCodeLabelMap = Collections.unmodifiableMap(localizedCodeLabelMap);
        }

        /**
         * Return a localized code labels as a Map.
         * <p>
         * Key of the map means "locale". Value of the map means "localized code label".<br>
         * Map cannot modify.
         * </p>
         *
         * @return the localized code labels as a Map
         */
        public Map<Locale, String> asMap() {
            return localizedCodeLabelMap;
        }

        /**
         * Return a localized code label.
         *
         * @param locale target locale
         * @return localized code label (If does not exist a code label that corresponds the specified locale, this method is return {@code null})
         * @throws IllegalArgumentException if locale is {@code null}.
         */
        public String getLabel(Locale locale) {
            Assert.notNull(locale, "locale is null");
            return localizedCodeLabelMap.get(locale);
        }

        /**
         * Return a builder object of LocalizedCodeLabels.
         *
         * @return new builder instance of {@link LocalizedCodeLabels}
         */
        public static LocalizedCodeLabelsBuilder builder() {
            return new LocalizedCodeLabelsBuilder();
        }

    }

    /**
     * Builder class of {@link LocalizedCodeLabels}.
     */
    public static final class LocalizedCodeLabelsBuilder {

        /**
         * Map of localized code labels (Key of the map means "locale" and Value of the map means "localized code label")
         */
        private final Map<Locale, String> localizedCodeLabelMap = new HashMap<Locale, String>();

        /**
         * Constructor.
         */
        private LocalizedCodeLabelsBuilder() {
            // NOP
        }

        /**
         * Add a localized label using a default locale of JVM.
         *
         * @param localizedCodeLabel localized code label
         * @return builder instance of itself
         * @see Locale#getDefault()
         * @see #label(Locale, String)
         */
        public LocalizedCodeLabelsBuilder label(String localizedCodeLabel) {
            return label(Locale.getDefault(), localizedCodeLabel);
        }

        /**
         * Add a localized label using a specified local string.
         *
         * @param localeString       corresponding locale string
         * @param localizedCodeLabel localized code label
         * @return builder instance of itself
         * @see org.springframework.util.StringUtils#parseLocaleString(String)
         * @see #label(Locale, String)
         */
        public LocalizedCodeLabelsBuilder label(String localeString, String localizedCodeLabel) {
            return label(StringUtils.parseLocaleString(localeString), localizedCodeLabel);
        }

        /**
         * Add a localized label using a specified locale.
         *
         * @param locale             corresponding locale
         * @param localizedCodeLabel localized code label
         * @return builder instance of itself
         */
        public LocalizedCodeLabelsBuilder label(Locale locale, String localizedCodeLabel) {
            localizedCodeLabelMap.put(locale, localizedCodeLabel);
            return this;
        }

        /**
         * Build a {@link LocalizedCodeLabels} instance.
         *
         * @return new instance of the {@link LocalizedCodeLabels}
         */
        public LocalizedCodeLabels build() {
            return new LocalizedCodeLabels(localizedCodeLabelMap);
        }

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
     * @throws IllegalArgumentException if the given class is {@code null}
     * @throws IllegalArgumentException if the given class does not implement {@link I18nCodeListItem}
     */
    public EnumI18nCodeList(Class<? extends Enum<?>> enumClass) {
        Assert.notNull(enumClass, "the given enumClass is null");
        Assert.isTrue(I18nCodeListItem.class.isAssignableFrom(enumClass),
                "the given enumClass must implement " + I18nCodeListItem.class);

        Method valuesMethod = ReflectionUtils.findMethod(enumClass, "values");
        Enum<?>[] enumValues = (Enum<?>[]) ReflectionUtils.invokeMethod(valuesMethod, enumClass);

        Table<Locale, String, String> localizedCodeListTable = createLocaleTable();
        for (Enum<?> enumValue : enumValues) {
            I18nCodeListItem i18nCodeListItem = (I18nCodeListItem) enumValue;
            for (Map.Entry<Locale, String> localizedLabel : i18nCodeListItem.getLocalizedCodeLabels().asMap().entrySet()) {
                localizedCodeListTable.put(localizedLabel.getKey(), i18nCodeListItem.getCodeValue(), localizedLabel.getValue());
            }
        }

        this.localizedCodeListTable = Tables.unmodifiableTable(localizedCodeListTable);
    }

    /**
     * {@inheritDoc}
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