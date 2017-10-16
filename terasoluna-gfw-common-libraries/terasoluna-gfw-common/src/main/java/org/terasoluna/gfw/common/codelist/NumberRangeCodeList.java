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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Codelist consisting of range of numbers
 */
public class NumberRangeCodeList extends AbstractCodeList implements
                                 InitializingBean {
    /**
     * Start point of range of numbers(default 0)
     */
    private int from = 0;

    /**
     * End point of range of numbers(default 0)
     */
    private int to = 0;

    /**
     * interval between two numbers(default 1)
     */
    private int interval = 1;

    /**
     * Display format of value part of the codelist(default %s)
     */
    private String valueFormat = "%s";

    /**
     * Display format of name part of the codelist(default %s)
     */
    private String labelFormat = "%s";

    /**
     * Codelist
     */
    private Map<String, String> map;

    /**
     * Returns the codelist as a Map<br>
     * <p>
     * The type of Key and Value both, is String.<br>
     * The map is unmodifiable and ordered by number asc.
     * </p>
     * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
     */
    @Override
    public Map<String, String> asMap() {
        return map;
    }

    /**
     * Initializes the codelist with the range of numbers.<br>
     * <p>
     * <code>to</code> must be more than <code>from</code>.
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     * @throws IllegalArgumentException when if from &gt; to.
     */
    @Override
    public void afterPropertiesSet() {
        Assert.isTrue(interval > 0, "interval should be greater than 0");
        Assert.hasLength(valueFormat, "valueFormat must not be empty");
        Assert.hasLength(labelFormat, "labelFormat must not be empty");

        LinkedHashMap<String, String> numbers = new LinkedHashMap<String, String>();
        if (from <= to) {
            for (int i = from; i <= to; i = i + interval) {
                putInMap(numbers, i);
            }
        } else {
            for (int i = from; i >= to; i = i - interval) {
                putInMap(numbers, i);
            }
        }
        map = Collections.unmodifiableMap(numbers);
    }

    private void putInMap(Map<String, String> numbers, int index) {
        String key = String.format(valueFormat, index);
        String value = String.format(labelFormat, index);
        numbers.put(key, value);
    }

    /**
     * Sets the start of the range of numbers(default 0)
     * @param from start value of the range
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * Sets end of the range of numbers(default 0)
     * @param to end value of the range
     */
    public void setTo(int to) {
        this.to = to;
    }

    /**
     * Sets the interval between 2 numbers<br>
     * <p>
     * This number must be positive integer(default 1)
     * @param interval interval value between start value and end value
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Sets the display format of the value part of the codelist(default %s)
     * @see String#format(String, Object...)
     * @param valueFormat format string for code value
     */
    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    /**
     * Sets the display format of the name part of the codelist(default %s)
     * @see String#format(String, Object...)
     * @param labelFormat format string for code label
     */
    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }
}
