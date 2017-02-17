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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Builder to create {@link FullHalfPairs}
 * @since 5.1.0
 */
public final class FullHalfPairsBuilder {
    /**
     * The set of {@link FullHalfPair}s
     */
    private final Set<FullHalfPair> pairs = new LinkedHashSet<FullHalfPair>();

    /**
     * The predicate to check whether the given character is appendable.
     */
    private FullHalfPairs.AppendablePredicate predicate;

    /**
     * Add {@link FullHalfPair} from the given strings.
     * @param fullwidth fullwidth of the pair
     * @param halfwidth fullwidth of the pair
     * @return this instance
     */
    public FullHalfPairsBuilder pair(String fullwidth, String halfwidth) {
        this.pairs.add(new FullHalfPair(fullwidth, halfwidth));
        return this;
    }

    /**
     * Set the predicate to check whether the given character is appendable.
     * @param predicate the predicate to check whether the given character is appendable.
     * @return this instance
     */
    public FullHalfPairsBuilder appendablePredicate(
            FullHalfPairs.AppendablePredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * create {@link FullHalfPairs}
     * @return {@link FullHalfPairs} instance
     */
    public FullHalfPairs build() {
        return new FullHalfPairs(this.pairs, this.predicate);
    }
}
