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
package org.terasoluna.gfw.common.codelist;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Codelist consisting of values/labels in Enum which implements {@link CodeListItem}. <br>
 * The result of {@link CodeListItem#getCodeValue} is used as a value of codelist (means it is used as a key of map). <br>
 * The result of {@link CodeListItem#getCodeLabel} is used as a label of codelist (means it is used as a value of map).
 * @since 5.0.0
 */
public class EnumCodeList extends AbstractCodeList {

    /**
     * interface of item in codelist
     * @since 5.0.0
     */
    public static interface CodeListItem {
        /**
         * Returns the label of item
         * @return label of the item
         */
        String getCodeLabel();

        /**
         * Returns the value of item
         * @return value of the item
         */
        String getCodeValue();
    }

    /**
     * a map holds values and labels of codelist actually.
     */
    private final Map<String, String> codeListMap;

    /**
     * Constructor.
     * @param enumClass Enum class of which this codelist consists. Must implement {@link CodeListItem}
     * @throws java.lang.IllegalArgumentException if the given class does not implement {@link CodeListItem}
     */
    public EnumCodeList(Class<? extends Enum<?>> enumClass) {
        Assert.isTrue(CodeListItem.class.isAssignableFrom(enumClass),
                "the given enumClass must implement " + CodeListItem.class);
        Map<String, String> codeList = new LinkedHashMap<String, String>();
        Method method = ReflectionUtils.findMethod(enumClass, "values");

        Enum<?>[] result = (Enum<?>[]) ReflectionUtils.invokeMethod(method,
                enumClass);
        for (Enum<?> e : result) {
            CodeListItem item = (CodeListItem) e;
            codeList.put(item.getCodeValue(), item.getCodeLabel());
        }

        this.codeListMap = Collections.unmodifiableMap(codeList);
    }

    /**
     * Returns the codelist as a Map<br>
     * <p>
     * The map is unmodifiable.
     * </p>
     * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
     */
    @Override
    public Map<String, String> asMap() {
        return this.codeListMap;
    }

}
