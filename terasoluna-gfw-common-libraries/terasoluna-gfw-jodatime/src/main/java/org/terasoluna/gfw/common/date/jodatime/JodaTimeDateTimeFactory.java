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
package org.terasoluna.gfw.common.date.jodatime;

import org.joda.time.DateTime;

/**
 * Interface that creates current system date.<br>
 * <p>
 * create current system date as
 * </p>
 * <ul>
 * <li>
 * {@link org.joda.time.DateTime}</li>
 * </ul>
 * @since 5.0.0
 */
public interface JodaTimeDateTimeFactory {
    /**
     * returns current system date as {@link org.joda.time.DateTime} in the default time zone.
     * @return current system date
     */
    DateTime newDateTime();
}
