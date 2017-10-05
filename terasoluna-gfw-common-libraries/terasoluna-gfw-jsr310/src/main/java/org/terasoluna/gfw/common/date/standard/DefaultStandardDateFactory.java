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
package org.terasoluna.gfw.common.date.standard;

import java.time.Clock;
import java.time.ZonedDateTime;

/**
 * Default implementation of {@link AbstractStandardDateFactory}<br>
 * this class returns current system date as it is.
 * @since 5.4.0
 */
public class DefaultStandardDateFactory extends AbstractStandardDateFactory {

    private final Clock clock;

    /**
     * Construct an instance with system clock using system default time zone.
     */
    public DefaultStandardDateFactory() {
        this(Clock.systemDefaultZone());
    }

    /**
     * Construct an instance with specified clock.
     *
     * @param clock a factory's clock
     */
    public DefaultStandardDateFactory(Clock clock) {
        this.clock = clock;
    }

    /**
     * <p>
     * Return a current date with specified clock.
     * </p>
     * @return current date with specified clock
     * @see ZonedDateTime#now(Clock)
     */
    @Override
    public ZonedDateTime newZonedDateTime() {
        return ZonedDateTime.now(clock);
    }

}
