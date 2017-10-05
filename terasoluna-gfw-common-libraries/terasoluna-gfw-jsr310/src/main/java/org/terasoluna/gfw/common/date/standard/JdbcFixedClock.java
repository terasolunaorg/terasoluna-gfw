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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implementation class of {@link Clock} for getting current date from JDBC query result.
 *
 * @since 5.4.0
 */
public class JdbcFixedClock extends Clock {

    private final ZoneId zone;

    private JdbcTemplate jdbcTemplate;

    private String currentTimestampQuery;

    /**
     * Construct a instance with system default time zone.
     */
    public JdbcFixedClock() {
        this(ZoneId.systemDefault());
    }

    /**
     * Construct a instance with specified time zone.
     *
     * @param zone clock's zone
     */
    public JdbcFixedClock(ZoneId zone) {
        this.zone = zone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant instant() {
        return jdbcTemplate.queryForObject(currentTimestampQuery, (rs,
                rowNum) -> rs.getTimestamp(1).toLocalDateTime().atZone(zone))
                .toInstant();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ZoneId getZone() {
        return zone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock withZone(ZoneId zone) {
        if (zone.equals(this.zone)) {
            return this;
        }
        JdbcFixedClock newClock = new JdbcFixedClock(zone);
        newClock.setDataSource(jdbcTemplate.getDataSource());
        newClock.setCurrentTimestampQuery(currentTimestampQuery);
        return newClock;
    }

    /**
     * Sets JDBC Template from DataSource
     * @param dataSource dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * the current timestamp received as parameter. Sets
     * @param currentTimestampQuery SQL to retrieve current timestamp
     */
    public void setCurrentTimestampQuery(String currentTimestampQuery) {
        this.currentTimestampQuery = currentTimestampQuery;
    }

}
