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
import java.time.ZoneId;

/**
 * Implementation of {@link ClockFactory} that obtain a system default clock.
 * @since 5.8.0
 * @author Atsushi Yoshikawa
 */
public class DefaultClockFactory implements ClockFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock fixed(ZoneId zone) {
        return Clock.fixed(tick(zone).instant(), zone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock tick(ZoneId zone) {
        return Clock.system(zone);
    }
}
