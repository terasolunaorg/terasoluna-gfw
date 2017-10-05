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

import org.terasoluna.gfw.common.date.DefaultClassicDateFactory;

import java.util.Date;

/**
 * Abstract implementation of {@link StandardDateFactory}.
 * <p>
 * This class converts from {@link java.time.ZonedDateTime} to {@link java.util.Date}, {@link java.sql.Timestamp},
 * {@link java.sql.Date}, {@link java.sql.Time} <br>
 * so all things that concrete classes do is to return current {@link java.time.ZonedDateTime}.
 * </p>
 * @since 5.4.0
 */
public abstract class AbstractStandardDateFactory extends
                                                  DefaultClassicDateFactory
                                                  implements
                                                  StandardDateFactory {

    /**
     * Returns {@link java.util.Date} instance for the current date.
     * @return Date current date
     */
    @Override
    public Date newDate() {
        return Date.from(newZonedDateTime().toInstant());
    }
}
