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
package org.terasoluna.gfw.common.date.dateandtime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Concrete Implementation class of
 * {@link org.terasoluna.gfw.common.date.dateandtime.DateAndTimeDateFactory}.
 * <P>
 * The {@link java.time.LocalDateTime},{@link java.time.OffsetDateTime} or
 * {@link java.time.ZonedDateTime} value which is to be returned as current
 * system date is stored in database. <br>
 * </P>
 * 
 * @since 5.0.0
 */
public class JdbcFixedDateAndTimeDateFactory extends AbstractDateAndTimeDateFactory {

	/**
	 * JDBC Template used to access the database to fetch the adjustment value.
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * SQL query used to access the database
	 */
	private String currentTimestampQuery;

	/**
	 * {@link org.springframework.jdbc.core.RowMapper} implementation maps the
	 * {@link java.sql.Timestamp} fetched from database into a
	 * {@link java.time.LocalDateTime} instance
	 */
	private static final RowMapper<LocalDateTime> LOCALDATE_ROW_MAPPER = new RowMapper<LocalDateTime>() {
		@Override
		public LocalDateTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			Timestamp ts = rs.getTimestamp(1);
			return LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
		}
	};

	/**
	 * {@link org.springframework.jdbc.core.RowMapper} implementation maps the
	 * {@link java.sql.Timestamp} fetched from database into a
	 * {@link java.time.OffsetDateTime} instance
	 */
	private static final RowMapper<OffsetDateTime> OFFSETDATE_ROW_MAPPER = new RowMapper<OffsetDateTime>() {
		@Override
		public OffsetDateTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			Timestamp ts = rs.getTimestamp(1);
			return ts.toInstant().atOffset(ZoneOffset.ofHours(9));
		}
	};

	/**
	 * {@link org.springframework.jdbc.core.RowMapper} implementation maps the
	 * {@link java.sql.Timestamp} fetched from database into a
	 * {@link java.time.OffsetDateTime} instance
	 */
	private static final RowMapper<ZonedDateTime> ZONEDDATE_ROW_MAPPER = new RowMapper<ZonedDateTime>() {
		@Override
		public ZonedDateTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			Timestamp ts = rs.getTimestamp(1);
			return ts.toInstant().atZone(ZoneId.systemDefault());
		}
	};

	/**
	 * Returns the {@link java.time.LocalDateTime} instance which contains the value
	 * fetched from DB
	 */
	@Override
	public LocalDateTime newLocalDateTime() {
		LocalDateTime now = jdbcTemplate.queryForObject(currentTimestampQuery, LOCALDATE_ROW_MAPPER);
		return now;
	}

	/**
	 * Returns the {@link java.time.OffsetDateTime} instance which contains the
	 * value fetched from DB
	 */
	@Override
	public OffsetDateTime newOffsetDateTime() {
		OffsetDateTime now = jdbcTemplate.queryForObject(currentTimestampQuery, OFFSETDATE_ROW_MAPPER);
		return now;
	}

	/**
	 * Returns the {@link java.time.ZonedDateTime} instance which contains the value
	 * fetched from DB
	 */
	@Override
	public ZonedDateTime newZonedDateTime() {
		ZonedDateTime now = jdbcTemplate.queryForObject(currentTimestampQuery, ZONEDDATE_ROW_MAPPER);
		return now;
	}

	/**
	 * Sets JDBC Template from DataSource
	 * 
	 * @param dataSource dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * the current timestamp received as parameter. Sets
	 * 
	 * @param currentTimestampQuery SQL to retrieve current timestamp
	 */
	public void setCurrentTimestampQuery(String currentTimestampQuery) {
		this.currentTimestampQuery = currentTimestampQuery;
	}
}
