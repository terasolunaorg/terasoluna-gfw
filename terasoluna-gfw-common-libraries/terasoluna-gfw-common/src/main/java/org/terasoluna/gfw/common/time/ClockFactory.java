/*
 * Copyright(c) 2013 NTT DATA Corporation.
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
 * Interface that obtains a {@link Clock}.
 * 
 * @since 5.6.0
 * @author Atsushi Yoshikawa
 * @see Clock
 */
public interface ClockFactory {

    /**
     * Obtains a fixed clock with specific time-zone.
     *
     * @param zone time-zone
     * @return fixed clock
     */
    Clock fixed(ZoneId zone);

    /**
     * Obtains a fixed clock with system default time-zone.
     *
     * @return fixed clock
     */
    default Clock fixed() {
        return fixed(ZoneId.systemDefault());
    }

    /**
     * Obtains a tick(not fixed) clock with system default time-zone.
     *
     * @param zone time-zone
     * @return tick(not fixed) clock
     */
    Clock tick(ZoneId zone);

    /**
     * Obtains a tick(not fixed) available clock with system default time-zone.
     *
     * @return tick(not fixed) clock
     */
    default Clock tick() {
        return tick(ZoneId.systemDefault());
    }
}
