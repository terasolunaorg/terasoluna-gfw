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
package org.terasoluna.gfw.common.time;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Implementation of {@link ClockFactory} that obtain a clock based on specified date and time as string.
 * @since 5.8.0
 * @author Atsushi Yoshikawa
 */
public class ConfigurableClockFactory implements ClockFactory {

    private final String localDateTimeString;

    private final DateTimeFormatter formatter;

    /**
     * Use date and time string formatted in {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     * @param localDateTimeString date and time as string
     */
    public ConfigurableClockFactory(String localDateTimeString) {
        this.localDateTimeString = localDateTimeString;
        this.formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    /**
     * Use date and time string formatted in specific format pattern.
     * @param localDateTimeString date and time as string
     * @param pattern format pattern
     */
    public ConfigurableClockFactory(String localDateTimeString,
            String pattern) {
        this.localDateTimeString = localDateTimeString;
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * Use date and time string formatted in specific {@link FormatStyle}.
     * @param localDateTimeString date and time as string
     * @param dateStyle format style of date
     * @param timeStyle format style of time
     */
    public ConfigurableClockFactory(String localDateTimeString,
            FormatStyle dateStyle, FormatStyle timeStyle) {
        this.localDateTimeString = localDateTimeString;
        this.formatter = DateTimeFormatter.ofLocalizedDateTime(dateStyle,
                timeStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock fixed(ZoneId zone) {
        return Clock.fixed(instant(zone), zone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock tick(ZoneId zone) {
        Clock systemClock = Clock.system(zone);
        return Clock.offset(systemClock, Duration.between(systemClock.instant(),
                instant(zone)));
    }

    /**
     * Obtain a instant of date and time based on specified date and time as string.
     * @param zone time-zone
     * @return instant of date and time
     */
    private Instant instant(ZoneId zone) {
        return LocalDateTime.parse(localDateTimeString, formatter).atZone(zone)
                .toInstant();
    }
}
