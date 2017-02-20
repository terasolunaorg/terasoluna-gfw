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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Concrete Implementation class of {@link org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory}.
 * <P>
 * The {@link org.joda.time.DateTime} value which is to be returned as current system date is stored in database. <br>
 * </P>
 * @since 5.0.0
 */
public class JdbcFixedJodaTimeDateFactory extends AbstractJodaTimeDateFactory {

    /**
     * JDBC Template used to access the database to fetch the adjustment value.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * SQL query used to access the database
     */
    private String currentTimestampQuery;

    /**
     * {@link org.springframework.jdbc.core.RowMapper} implementation maps the {@link java.sql.Timestamp} fetched from database
     * into a {@link org.joda.time.DateTime} instance
     */
    private static final RowMapper<DateTime> DATE_ROW_MAPPER = new RowMapper<DateTime>() {
        @Override
        public DateTime mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp ts = rs.getTimestamp(1);
            return new DateTime(ts.getTime());
        }
    };

    /**
     * Returns the {@link org.joda.time.DateTime} instance which contains the value fetched from DB
     */
    @Override
    public DateTime newDateTime() {
        DateTime now = jdbcTemplate.queryForObject(currentTimestampQuery,
                DATE_ROW_MAPPER);
        return now;
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
