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
import java.time.ZoneId;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implementation of {@link ClockFactory} that obtain a clock based on database column of time
 * stamp.
 * @since 5.8.0
 * @author Atsushi Yoshikawa
 */
public class JdbcClockFactory implements ClockFactory {

    private final JdbcTemplate jdbcTemplate;

    private final String currentTimestampQuery;

    /**
     * Set up data source and query to get current time stamp.
     * @param dataSource data source used in {@link JdbcTemplate}
     * @param currentTimestampQuery query to get current time stamp
     */
    public JdbcClockFactory(DataSource dataSource, String currentTimestampQuery) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.currentTimestampQuery = currentTimestampQuery;
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
        return Clock.offset(systemClock, Duration.between(systemClock.instant(), instant(zone)));
    }

    /**
     * Obtain a instant of date and time based on database column of time stamp.
     * @param zone time-zone
     * @return instant of date and time
     */
    private Instant instant(ZoneId zone) {
        return jdbcTemplate.queryForObject(currentTimestampQuery, //
                (rs, rowNum) -> rs.getTimestamp(1).toLocalDateTime().atZone(zone)) //
                .toInstant();
    }
}
