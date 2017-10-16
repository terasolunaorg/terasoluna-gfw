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
package org.terasoluna.gfw.web.el;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Convert {@code Object} to {@code Map<String, String>} using {@link FormattingConversionService}.
 * <p>
 * The converted Map can be populated to the original Object using {@link org.springframework.validation.DataBinder}.
 * </p>
 * <h3>Example1</h3>
 *
 * <pre>
 * <code>
 * class SearchUserForm {
 *   SearchUserCriteriaForm criteria;
 *   Boolean rememberCriteria;
 * }
 * </code>
 * </pre>
 *
 * <pre>
 * <code>
 * class SearchUserCriteriaForm {
 *   String name;
 *   Integer age;
 * }
 * </code>
 * </pre>
 * <p>
 * {@code SearchUserForm} instance will be converted as follows {@code "rememberCriteria":"true", "criteria.name":"yamada",
 * "criteria.age":"20"}
 * </p>
 * <h3>Example2</h3>
 *
 * <pre>
 * <code>
 * class BatchUpdateUserForm {
 *   List{@literal <UpdateUserCriteriaForm>} criteria;
 *   LogicalOperator operator;
 * }
 * </code>
 * </pre>
 *
 * <pre>
 * <code>
 * enum LogicalOperator {
 *   AND, OR
 * }
 * </code>
 * </pre>
 *
 * <pre>
 * <code>
 * class UpdateUserForm {
 *   String name;
 *   Integer age;
 * }
 * </code>
 * </pre>
 * <p>
 * {@code BatchUpdateUserForm} instance will be converted as follows {@code "criteria[0].name":"yamada",
 * "criteria[0].age":"20", "criteria[1].name":"tanaka", "criteria[1].name":"50", "operator":"AND"}
 * </p>
 * <h3>Example3</h3>
 *
 * <pre>
 * <code>class SearchAndBatchUpdateUserForm {
 *   SearchUserCriteriaForm criteria;
 *   List{@literal <User>} users;
 * }
 * </code>
 * </pre>
 *
 * <pre>
 * <code>
 * class SearchUserCriteriaForm {
 *   String name;
 *   Integer age;
 * }
 * </code>
 * </pre>
 *
 * <pre>
 * <code>
 * class User {
 *   String name;
 *   Integer age;
 * }
 * </code>
 * </pre>
 * <p>
 * {@code SearchUserCriteriaForm} instance will be converted as follows
 * {@code criteria.name=suzuki&criteria.age=30&users[0].name=yamada&users[0].age=20&users[1].name=tanaka&users[1].age=50}
 * </p>
 * <p>
 * If the value of a property is {@code null}, the value is converted to an empty string and the key is prefixed with
 * {@code "_"}. Request parameter that start with {@code "_"} is reset parameter provided by Spring Web MVC. If a reset
 * parameter is specified, Spring Web MVC bind {@code null} to a property value.<br>
 * e.g.) {@code "_rememberCriteria":"", "_criteria.name":"", "_criteria.age":""}
 * </p>
 * <p>
 * If the value of a {@link Iterable} or Array property is empty element, the value is converted to an empty string. If a empty
 * string is specified, Spring Web MVC bind empty element to a property value.<br>
 * e.g.) {@code "users":"", "criteria.name":"suzuki", "criteria.age":"30"}
 * </p>
 * <p>
 * If the value of a {@link Map} property is empty element, it is not converted. As a result, the property value will be a
 * default value of server side implementation because Spring Web MVC does not bind request parameter to the property value.
 * </p>
 * @since 5.0.1
 */
class ObjectToMapConverter {
    /**
     * type descriptor of string for format a value.
     */
    private static final TypeDescriptor STRING_DESC = TypeDescriptor.valueOf(
            String.class);

    /**
     * conversion service for formatting a value.
     */
    private final FormattingConversionService conversionService;

    /**
     * Constructor
     * @param conversionService {@link FormattingConversionService} to use. must not be null
     */
    public ObjectToMapConverter(FormattingConversionService conversionService) {
        Assert.notNull(conversionService,
                "'conversionService' must not be null");
        this.conversionService = conversionService;
    }

    /**
     * Convert the given map to the flatten map
     * @param prefix prefix of the key
     * @param value map instance to convert
     * @return converted map. all keys are prefixed with the given key
     */
    private Map<String, String> convert(String prefix, Map value) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Object entry : value.entrySet()) {
            Map.Entry e = (Map.Entry) entry;
            if (StringUtils.isEmpty(prefix)) {
                map.putAll(this.convert(e.getKey().toString(), e.getValue()));
            } else {
                map.putAll(this.convert(prefix + "[" + e.getKey() + "]", e
                        .getValue()));
            }
        }
        return map;
    }

    /**
     * Convert the given Iterable to the flatten map
     * @param prefix prefix of the key
     * @param value iterable instance to convert
     * @return converted map. all keys are prefixed with the given key. If given Iterable is empty, the pair of the given
     *         name(prefix) and an empty string is added into map of return value.
     */
    private Map<String, String> convert(String prefix, Iterable value) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Iterator iterator = value.iterator();
        if (!iterator.hasNext()) {
            map.put(prefix, "");
            return map;
        }
        int i = 0;
        while (iterator.hasNext()) {
            map.putAll(this.convert(prefix + "[" + i + "]", iterator.next()));
            i++;
        }
        return map;
    }

    /**
     * Convert the given object to the flattened map.
     * <p>
     * Return empty map if the object is null.
     * </p>
     * @param object object to convert
     * @return converted map.
     * @see ObjectToMapConverter
     */
    public Map<String, String> convert(Object object) {
        if (object == null) {
            return Collections.emptyMap();
        }
        return this.convert("", object);
    }

    /**
     * Convert the given object to the flattened map.
     * <p>
     * Return empty map if the object is null.
     * </p>
     * @param prefix prefix of the key
     * @param object object to convert
     * @return converted map. all keys are prefixed with the given key
     * @see ObjectToMapConverter
     */
    public Map<String, String> convert(String prefix, Object object) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        // at first, try to flatten the given object
        if (flatten(map, "", prefix, object, null)) {
            return map;
        }

        // the given object is a Java Bean
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(
                object);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();

        // flatten properties in the given object
        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            if ("class".equals(name) || !beanWrapper.isReadableProperty(name)) {
                continue;
            }
            Object value = beanWrapper.getPropertyValue(name);
            TypeDescriptor sourceType = beanWrapper.getPropertyTypeDescriptor(
                    name);

            if (!flatten(map, prefix, name, value, sourceType)) {
                // the property can be a Java Bean
                // convert recursively
                Map<String, String> subMap = this.convert(name, value);
                map.putAll(subMap);
            }
        }

        return map;
    }

    /**
     * Add the pair of the given name and value to the given map. The value is flattened if required to be available in query
     * params. <br>
     * Return whether the value is flattened. The value is flattened in the case of the following types:
     * <ul>
     * <li>Array (unless the name is empty)</li>
     * <li>Iterable (unless the name is empty)</li>
     * <li>Map</li>
     * <li>{@link BeanUtils#isSimpleProperty(java.lang.Class)}</li>
     * <li>if {@link #conversionService} can convert</li>
     * </ul>
     * <p>
     * The value is formatted using {@link FormattingConversionService} is possible. If the value of a property is {@code null},
     * the value is converted to an empty string and the key is prefixed with {@code "_"}. Request parameter that start with
     * {@code "_"} is reset parameter provided by Spring Web MVC. If a reset parameter is specified, Spring Web MVC bind
     * {@code null} to a property value.
     * </p>
     * @param map map to add
     * @param prefix prefix of the key
     * @param name name of the value
     * @param value value to convert
     * @param sourceType {@link TypeDescriptor} to use
     * @return flatten map
     */
    private boolean flatten(Map<String, String> map, String prefix, String name,
            Object value, TypeDescriptor sourceType) {
        String key = StringUtils.isEmpty(prefix) ? name : prefix + "." + name;
        if (value == null) {
            String resetKey = "_" + key;
            map.put(resetKey, "");
            // the value has been flatten
            return true;
        }
        Class<?> clazz = value.getClass();
        if (value instanceof Iterable) {
            if (StringUtils.isEmpty(name)) {
                // skip flatten
                return true;
            }
            Iterable iterable = (Iterable) value;
            map.putAll(this.convert(key, iterable));
        } else if (clazz.isArray()) {
            if (StringUtils.isEmpty(name)) {
                // skip flatten
                return true;
            }
            map.putAll(this.convert(key, arrayObjectToList(value)));
        } else if (value instanceof Map) {
            Map m = (Map) value;
            map.putAll(this.convert(key, m));
        } else {
            TypeDescriptor descriptor = (sourceType != null) ? sourceType
                    : TypeDescriptor.forObject(value);
            if (BeanUtils.isSimpleProperty(clazz) || conversionService
                    .canConvert(descriptor, STRING_DESC)) {
                map.put(key, conversionService.convert(value, descriptor,
                        STRING_DESC).toString());
            } else {
                // the value is Java Bean?
                return false;
            }
        }
        // the value has been flatten
        return true;
    }

    /**
     * Convert any array to {@code List<Object>}
     * @param arrayObject array to convert
     * @return list
     */
    private static List<Object> arrayObjectToList(final Object arrayObject) {
        // 'arrayObject' must be array
        return new AbstractList<Object>() {
            @Override
            public Object get(int index) {
                return Array.get(arrayObject, index);
            }

            @Override
            public int size() {
                return Array.getLength(arrayObject);
            }
        };
    }
}
