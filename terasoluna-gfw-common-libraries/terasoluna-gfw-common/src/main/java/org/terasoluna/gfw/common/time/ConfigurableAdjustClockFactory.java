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
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;

/**
 * Implementation of {@link ClockFactory} that obtain a Clock adjusted specific duration using value and unit from system
 * default clock.
 * @since 5.8.0
 * @author Atsushi Yoshikawa
 */
public class ConfigurableAdjustClockFactory implements ClockFactory {

    private final long adjustedValue;

    private final TemporalUnit adjustedValueUnit;

    /**
     * Set up duration value and unit to be adjusted.
     * @param adjustedValue duration value to be adjusted
     * @param adjustedValueUnit estimable duration units of the value to be adjusted
     */
    public ConfigurableAdjustClockFactory(long adjustedValue,
            TemporalUnit adjustedValueUnit) {
        this.adjustedValue = adjustedValue;
        this.adjustedValueUnit = adjustedValueUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock fixed(ZoneId zone) {
        return Clock.fixed(tick().instant(), zone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock tick(ZoneId zone) {
        Clock systemClock = Clock.system(zone);
        return Clock.offset(systemClock, Duration.of(adjustedValue,
                adjustedValueUnit));
    }
}
