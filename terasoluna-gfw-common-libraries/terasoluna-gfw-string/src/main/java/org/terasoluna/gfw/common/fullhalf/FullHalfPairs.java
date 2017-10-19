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

import java.util.Set;

/**
 * The set of {@link FullHalfPair}s which stands for the mapping table used in {@link FullHalfConverter}.
 * @since 5.1.0
 */
public class FullHalfPairs {
    /**
     * The set of {@link FullHalfPair}s
     */
    private final Set<FullHalfPair> pairs;

    /**
     * The predicate to check whether the given character is appendable.
     */
    private final AppendablePredicate predicate;

    /**
     * Constructor.<br>
     * The set of {@link FullHalfPair}s and the predicate to check whether the given character is appendable can be given.<br>
     * If predicate is not given (means <code>null</code>),<br>
     * the default predicate is used. The default predicate regards <code>ﾞ</code> and <code>ﾟ</code> as appendable characters.
     * @param pairs The set of {@link FullHalfPair}s
     * @param predicate Predicate to check whether the given character is appendable.
     */
    public FullHalfPairs(Set<FullHalfPair> pairs,
            AppendablePredicate predicate) {
        if (pairs == null) {
            throw new IllegalArgumentException("pairs must not be null");
        }
        if (pairs.isEmpty()) {
            throw new IllegalArgumentException("pairs must not be empty");
        }
        this.pairs = pairs;
        this.predicate = predicate != null ? predicate
                : new AppendablePredicate() {
                    @Override
                    public boolean isAppendable(char c) {
                        return c == 'ﾞ' || c == 'ﾟ';
                    }
                };
    }

    /**
     * Return the set of {@link FullHalfPair}s
     * @return the set of {@link FullHalfPair}s
     */
    public Set<FullHalfPair> pairs() {
        return this.pairs;
    }

    /**
     * Return the predicate to check whether the given character is appendable.
     * @return the predicate to check whether the given character is appendable.
     */
    public AppendablePredicate predicate() {
        return this.predicate;
    }

    /**
     * The predicate to check whether the given character is appendable.
     */
    public interface AppendablePredicate {
        /**
         * Return whether the given character is appendable.
         * @param c the character to check
         * @return whether the given character is appendable.
         */
        boolean isAppendable(char c);
    }
}
