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
package org.terasoluna.gfw.common.date;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Default implementation of {@link org.terasoluna.gfw.common.date.ClassicDateFactory}.
 * <p>
 * This class returns current system date.
 * </p>
 * @since 5.0.0
 */
public class DefaultClassicDateFactory implements ClassicDateFactory {

    /**
     * Returns {@link java.util.Date} instance for the current date.
     * @return Date current date
     */
    @Override
    public Date newDate() {
        return new Date();
    }

    /**
     * Returns {@link java.sql.Timestamp} instance corresponding to current date and time.
     * @return Timestamp current date
     */
    @Override
    public Timestamp newTimestamp() {
        Date now = newDate();
        return DateConvertUtils.convertToTimestamp(now);
    }

    /**
     * Returns {@link java.sql.Date} instance for the current date.
     * <p>
     * normalize hour,minute,second,milliSecond to 0.
     * </p>
     * @return Date {@link java.sql.Date} instance for current date
     */
    @Override
    public java.sql.Date newSqlDate() {
        Date now = newDate();
        return DateConvertUtils.convertToSqlDate(now);
    }

    /**
     * Returns {@link java.sql.Time} instance for the current time.
     * <p>
     * normalize year=1970,,month=1,day=1.
     * </p>
     * @return Time {@link java.sql.Time} instance for current time
     */
    @Override
    public Time newTime() {
        Date now = newDate();
        return DateConvertUtils.convertToTime(now);
    }

}
