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

import java.io.Serializable;

/**
 * Pair of fullwidth string and halfwidth string.
 * @since 5.1.0
 */
public final class FullHalfPair implements Serializable {
    /**
     * fullwidth of the pair
     */
    private final String fullwidth;

    /**
     * halfwidth of the pair
     */
    private final String halfwidth;

    /**
     * Constructor.
     * @param fullwidth fullwidth of the pair. must not be null and the length must be 1.
     * @param halfwidth halfwidth of the pair. must not be null and the length must be 1 or 2.
     * @throws IllegalArgumentException if fullwidth or halfwidth is null or the length is invalid.
     */
    public FullHalfPair(String fullwidth, String halfwidth) {
        if (fullwidth == null || fullwidth.length() != 1) {
            throw new IllegalArgumentException("fullwidth must be 1 length string (fullwidth = "
                    + fullwidth + ")");
        }
        if (halfwidth == null || (halfwidth.length() != 1 && halfwidth
                .length() != 2)) {
            throw new IllegalArgumentException("halfwidth must be 1 or 2 length string (halfwidth = "
                    + halfwidth + ")");
        }
        this.fullwidth = fullwidth;
        this.halfwidth = halfwidth;
    }

    /**
     * returns fullwidth of the pair
     * @return fullwidth of the pair
     */
    public String fullwidth() {
        return this.fullwidth;
    }

    /**
     * returns halfwidth of the pair
     * @return halfwidth of the pair
     */
    public String halfwidth() {
        return this.halfwidth;
    }

    /**
     * returns whether the given object equals to this instance
     * @param o object to check
     * @return whether the given object equals to this instance
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FullHalfPair that = (FullHalfPair) o;

        if (!fullwidth.equals(that.fullwidth)) {
            return false;
        }
        return halfwidth.equals(that.halfwidth);

    }

    /**
     * returns hash code of this instance
     * @return hash code of this instance
     */
    @Override
    public int hashCode() {
        int result = fullwidth.hashCode();
        result = 31 * result + halfwidth.hashCode();
        return result;
    }
}
