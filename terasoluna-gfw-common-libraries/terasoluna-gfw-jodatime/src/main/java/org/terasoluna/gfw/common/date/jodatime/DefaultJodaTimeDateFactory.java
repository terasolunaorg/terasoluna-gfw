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
package org.terasoluna.gfw.common.date.jodatime;

import org.joda.time.DateTime;

/**
 * Default implementation of {@link org.terasoluna.gfw.common.date.jodatime.AbstractJodaTimeDateFactory}<br>
 * this class returns current system date as it is.
 * @since 5.0.0
 * @deprecated It is recommended to migrate to java.time (JSR-310). {@link org.terasoluna.gfw.common.time.DefaultClockFactory}
 */
@Deprecated
public class DefaultJodaTimeDateFactory extends AbstractJodaTimeDateFactory {

    /**
     * <p>
     * Return current system date as it is in the default time zone.
     * </p>
     * @see org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory#newDateTime()
     */
    @Override
    public DateTime newDateTime() {
        return new DateTime();
    }

}
