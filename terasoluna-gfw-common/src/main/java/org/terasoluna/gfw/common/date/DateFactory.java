/*
 * Copyright (C) 2013-2016 NTT DATA Corporation
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
package org.terasoluna.gfw.common.date;

import java.sql.Time;
import java.sql.Timestamp;

import org.joda.time.DateTime;

/**
 * Interface that creates current system date.<br>
 * <p>
 * create current system date as
 * </p>
 * <ul>
 * <li>
 * {@link org.joda.time.DateTime}</li>
 * <li>
 * {@link java.util.Date}</li>
 * <li>
 * {@link java.sql.Date}</li>
 * <li>
 * {@link java.sql.Timestamp}</li>
 * <li>
 * {@link java.sql.Time}</li>
 * </ul>
 */
public interface DateFactory {
    /**
     * returns current system date as {@link org.joda.time.DateTime} in the default time zone.
     * @return current system date
     */
    DateTime newDateTime();

    /**
     * returns current system date as {@link java.sql.Timestamp}.
     * @return current system date
     */
    Timestamp newTimestamp();

    /**
     * returns current system date as {@link java.util.Date}.
     * @return current system date
     */
    java.util.Date newDate();

    /**
     * returns current system date as {@link java.sql.Date}.
     * <p>
     * normalize hour,minute,second,milliSecond to 0.
     * </p>
     * @return current system date
     */
    java.sql.Date newSqlDate();

    /**
     * returns current system date as {@link java.sql.Time}.
     * <p>
     * normalize year=1970,,month=1,day=1.
     * </p>
     * @return current system date
     */
    Time newTime();
}
