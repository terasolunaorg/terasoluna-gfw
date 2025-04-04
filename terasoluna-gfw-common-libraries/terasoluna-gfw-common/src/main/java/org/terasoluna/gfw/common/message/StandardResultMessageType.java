/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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
package org.terasoluna.gfw.common.message;

/**
 * Defines the standard result message types. <br>
 * Classified into following types:<br>
 * <ul>
 * <li><code>success</code></li>
 * <li><code>info</code></li>
 * <li><code>warning(Added from 5.0.0)</code></li>
 * <li><code>error</code></li>
 * <li><code>danger</code></li>
 * <li><code>primary(Added from 5.7.0)</code></li>
 * <li><code>secondary(Added from 5.7.0)</code></li>
 * <li><code>light(Added from 5.7.0)</code></li>
 * <li><code>dark(Added from 5.7.0)</code></li>
 * </ul>
 * The level of <code>danger</code> is as same as <code>error</code> and <code>danger</code> is
 * usually used as alias for <code>error</code>.
 */
public enum StandardResultMessageType implements ResultMessageType {
    /**
     * message type is <code>success</code>.
     */
    SUCCESS("success"),
    /**
     * message type is <code>info</code>.
     */
    INFO("info"),
    /**
     * message type is <code>warning</code>.
     * @since 5.0.0
     */
    WARNING("warning"),
    /**
     * message type is <code>error</code>.
     */
    ERROR("error"),
    /**
     * message type is <code>danger</code>.
     */
    DANGER("danger"),
    /**
     * message type is <code>primary</code>.
     * @since 5.7.0
     */
    PRIMARY("primary"),
    /**
     * message type is <code>secondary</code>.
     * @since 5.7.0
     */
    SECONDARY("secondary"),
    /**
     * message type is <code>light</code>.
     * @since 5.7.0
     */
    LIGHT("light"),
    /**
     * message type is <code>dark</code>.
     * @since 5.7.0
     */
    DARK("dark");

    /**
     * message type
     */
    private final String type;

    /**
     * Create ResultMessageType instance<br>
     * @param type message type
     */
    private StandardResultMessageType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    /**
     * <p>
     * returns message type
     * </p>
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.type;
    }
}
