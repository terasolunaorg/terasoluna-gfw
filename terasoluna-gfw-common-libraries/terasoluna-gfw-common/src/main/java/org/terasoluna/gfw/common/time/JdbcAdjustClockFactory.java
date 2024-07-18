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

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implementation of {@link ClockFactory} that obtain a Clock adjusted specific duration using value
 * of database column and unit from system default clock.
 * @since 5.8.0
 * @author Atsushi Yoshikawa
 */
public class JdbcAdjustClockFactory implements ClockFactory {

    private final JdbcTemplate jdbcTemplate;

    private final String adjustedValueQuery;

    private final TemporalUnit adjustedValueUnit;

    /**
     * Set up data source and query to get duration value to be adjusted.
     * @param dataSource data source used in {@link JdbcTemplate}
     * @param adjustedValueQuery query to get duration value to be adjusted
     * @param adjustedValueUnit estimable duration units of the value to be adjusted
     */
    public JdbcAdjustClockFactory(DataSource dataSource, String adjustedValueQuery,
            TemporalUnit adjustedValueUnit) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.adjustedValueQuery = adjustedValueQuery;
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
        return Clock.offset(systemClock, Duration.of(adjustedValue(), adjustedValueUnit));
    }

    /**
     * Obtain a duration value to be adjusted based on database column.
     * @return duration value to be adjusted
     */
    private long adjustedValue() {
        return jdbcTemplate.queryForObject(adjustedValueQuery, (rs, rowNum) -> rs.getLong(1));
    }
}
