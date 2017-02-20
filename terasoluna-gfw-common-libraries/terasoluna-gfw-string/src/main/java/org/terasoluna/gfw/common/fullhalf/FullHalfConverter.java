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
package org.terasoluna.gfw.common.fullhalf;

import java.util.*;

/**
 * Convert which converts from fullwidth to halfwidth and from halfwidth to fullwidth. This implementation does not have the
 * mapping table and intended to be given by the constructor like following:
 *
 * <pre>
 * <code>FullHalfConverter converter = new FullHalfConverter(new FullHalfPairsBuilder()
 *   .pair("Ａ", "A")
 *   .pair("Ｂ", "B")
 *   .build());</code>
 * </pre>
 * <p>
 * If the halfwidth or fullwidth in the given pair is already registered, the former is preferred. Note that it cannot be
 * overridden.
 * </p>
 * @since 5.1.0
 */
public final class FullHalfConverter {
    /**
     * map to convert from fullwidth char
     */
    private final Map<String, FullHalfPair> fullwidthMap;

    /**
     * map to convert from halfwidth char
     */
    private final Map<String, FullHalfPair> halfwidthMap;

    /**
     * predicates if the given character is appendable like 'ﾞ' or 'ﾟ'.
     */
    private final FullHalfPairs.AppendablePredicate predicate;

    /**
     * Constructor.
     * @param pairs pair of fullwidth-halfwidth. must not be null. If the halfwidth or fullwidth in the given pair is already
     *            registered, the former is preferred.
     * @see FullHalfConverter
     * @throws IllegalArgumentException if the given pairs is null
     */
    public FullHalfConverter(FullHalfPairs pairs) {
        if (pairs == null) {
            throw new IllegalArgumentException("pairs must not be null.");
        }
        Set<FullHalfPair> pairSet = pairs.pairs();
        Map<String, FullHalfPair> f = new HashMap<String, FullHalfPair>();
        Map<String, FullHalfPair> h = new HashMap<String, FullHalfPair>();
        for (FullHalfPair pair : pairSet) {
            // first definition is prior
            if (!f.containsKey(pair.fullwidth())) {
                f.put(pair.fullwidth(), pair);
            }
            if (!h.containsKey(pair.halfwidth())) {
                h.put(pair.halfwidth(), pair);
            }
        }
        this.fullwidthMap = Collections.unmodifiableMap(f);
        this.halfwidthMap = Collections.unmodifiableMap(h);
        this.predicate = pairs.predicate();
    }

    /**
     * Converts from fullwidth to halfwidth as much as possible with the given mapping table.
     * @param fullwidth string to convert
     * @return converted string. if the given string is null or empty, returns as it is.
     */
    public String toHalfwidth(String fullwidth) {
        if (fullwidth == null || fullwidth.isEmpty()) {
            return fullwidth;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fullwidth.length(); i++) {
            String s = String.valueOf(fullwidth.charAt(i));
            builder.append(halfwidth(s));
        }
        return builder.toString();
    }

    /**
     * Converts from halfwidth to fullwidth as much as possible with the given mapping table.
     * @param halfwidth string to convert
     * @return converted string. if the given string is null or empty, returns as it is.
     */
    public String toFullwidth(String halfwidth) {
        if (halfwidth == null || halfwidth.isEmpty()) {
            return halfwidth;
        }
        StringBuilder builder = new StringBuilder();
        Queue<String> buffer = new LinkedList<String>(); // use queue as 1 element buffer

        for (int i = 0; i < halfwidth.length(); i++) {
            char c = halfwidth.charAt(i);
            String s = String.valueOf(c);
            // next loop when the buffer is empty
            if (buffer.isEmpty()) {
                buffer.add(s);
                continue;
            }
            // poll the previous string from buffer
            String prev = buffer.poll();
            // check if the target character is appendable
            if (predicate.isAppendable(c)) {
                // check if "previous string"+"appendable char" is contained in the mapping table
                FullHalfPair pair = this.halfwidthMap.get(prev + s);
                if (pair != null) {
                    // append the concatenated string
                    builder.append(pair.fullwidth());
                } else {
                    // append fullwidth of the previous string and current string
                    builder.append(fullwidth(prev));
                    builder.append(fullwidth(s));
                }
            } else {
                // append fullwidth of the previous string and put current string into the buffer
                builder.append(fullwidth(prev));
                buffer.add(s);
            }
        }
        // append the string in the buffer if exists
        if (!buffer.isEmpty()) {
            builder.append(fullwidth(buffer.poll()));
        }
        return builder.toString();
    }

    /**
     * Returns fullwidth string if the given halfwidth string exists in the pairs
     * @param s halfwidth
     * @return fullwidth for the given halfwidth
     */
    private String fullwidth(String s) {
        FullHalfPair pair = this.halfwidthMap.get(s);
        if (pair != null) {
            return pair.fullwidth();
        } else {
            return s;
        }
    }

    /**
     * Returns halfwidth string if the given fullwidth string exists in the pairs
     * @param s fullwidth
     * @return halfwidth for the given fullwidth
     */
    private String halfwidth(String s) {
        FullHalfPair pair = this.fullwidthMap.get(s);
        if (pair != null) {
            return pair.halfwidth();
        } else {
            return s;
        }
    }
}
