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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for JdbcFixedDateAndTimeDateFactory.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
@Rollback
// Changed by SPR-13277
public class JdbcFixedDateAndTimeDateFactoryTest {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * normal case 1. <br>
	 * use prepared table. The nanosecond field in LocalDatetime is not validated
	 * because it is lost when converted to Date.
	 */
	@Test
	public void testNewLocalDateTime01() throws Exception {
		// crate table
		jdbcTemplate.getJdbcOperations().execute("CREATE TABLE system_date(now timestamp NOT NULL)");
		jdbcTemplate.update("INSERT INTO system_date(now) VALUES (:now)", Collections.singletonMap("now", Date.from(
				ZonedDateTime.of(LocalDateTime.of(2012, 9, 11, 2, 25, 15, 100), ZoneId.systemDefault()).toInstant())));

		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT now FROM system_date");

		LocalDateTime now = dateFactory.newLocalDateTime();
		assertThat(now.getYear(), is(2012));
		assertThat(now.getMonthValue(), is(9));
		assertThat(now.getDayOfMonth(), is(11));
		assertThat(now.getHour(), is(2));
		assertThat(now.getMinute(), is(25));
		assertThat(now.getSecond(), is(15));

		// drop table
		jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_date");
	}

	/**
	 * normal case 2. <br>
	 * use literal.
	 */
	@Test
	public void testNewLocalDateTime02() throws Exception {
		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT Timestamp '2012-09-09 2:33:10.222'");

		LocalDateTime now = dateFactory.newLocalDateTime();
		assertThat(now.getYear(), is(2012));
		assertThat(now.getMonthValue(), is(9));
		assertThat(now.getDayOfMonth(), is(9));
		assertThat(now.getHour(), is(2));
		assertThat(now.getMinute(), is(33));
		assertThat(now.getSecond(), is(10));
		assertThat(now.get(ChronoField.MILLI_OF_SECOND), is(222));
	}

	/**
	 * abnormal case 1. <br>
	 * doesn't return timestamp.
	 */
	@Test(expected = DataAccessException.class)
	public void testNewLocalDateTime03() throws Exception {
		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT 1+1");

		dateFactory.newLocalDateTime();
	}

	/**
	 * normal case 1. <br>
	 * use prepared table. The nanosecond field in OffsetDatetime is not validated
	 * because it is lost when converted to Date.
	 */
	@Test
	public void testNewOffsetDateTime01() throws Exception {
		// crate table
		jdbcTemplate.getJdbcOperations().execute("CREATE TABLE system_date(now timestamp NOT NULL)");
		jdbcTemplate.update("INSERT INTO system_date(now) VALUES (:now)", Collections.singletonMap("now",
				Date.from(OffsetDateTime.of(2012, 9, 11, 2, 25, 15, 100, ZoneOffset.of("+9")).toInstant())));

		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT now FROM system_date");

		OffsetDateTime now = dateFactory.newOffsetDateTime();
		assertThat(now.getYear(), is(2012));
		assertThat(now.getMonthValue(), is(9));
		assertThat(now.getDayOfMonth(), is(11));
		assertThat(now.getHour(), is(2));
		assertThat(now.getMinute(), is(25));
		assertThat(now.getSecond(), is(15));

		// drop table
		jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_date");
	}

	/**
	 * normal case 2. <br>
	 * use literal.
	 */
	@Test
	public void testNewOffsetDateTime02() throws Exception {
		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT Timestamp '2012-09-09 2:33:10.222'");

		OffsetDateTime now = dateFactory.newOffsetDateTime();
		assertThat(now.getYear(), is(2012));
		assertThat(now.getMonthValue(), is(9));
		assertThat(now.getDayOfMonth(), is(9));
		assertThat(now.getHour(), is(2));
		assertThat(now.getMinute(), is(33));
		assertThat(now.getSecond(), is(10));
		assertThat(now.get(ChronoField.MILLI_OF_SECOND), is(222));
	}

	/**
	 * abnormal case 1. <br>
	 * doesn't return timestamp.
	 */
	@Test(expected = DataAccessException.class)
	public void testNewOffsetDateTime03() throws Exception {
		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT 1+1");

		dateFactory.newOffsetDateTime();
	}

	/**
	 * normal case 1. <br>
	 * use prepared table. The nanosecond field in OffsetDatetime is not validated
	 * because it is lost when converted to Date.
	 */
	@Test
	public void testNewZonedDateTime01() throws Exception {
		// crate table
		jdbcTemplate.getJdbcOperations().execute("CREATE TABLE system_date(now timestamp NOT NULL)");
		jdbcTemplate.update("INSERT INTO system_date(now) VALUES (:now)", Collections.singletonMap("now",
				Date.from(OffsetDateTime.of(2012, 9, 11, 2, 25, 15, 100, ZoneOffset.of("+9")).toInstant())));

		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT now FROM system_date");

		ZonedDateTime now = dateFactory.newZonedDateTime();
		assertThat(now.getYear(), is(2012));
		assertThat(now.getMonthValue(), is(9));
		assertThat(now.getDayOfMonth(), is(11));
		assertThat(now.getHour(), is(2));
		assertThat(now.getMinute(), is(25));
		assertThat(now.getSecond(), is(15));

		// drop table
		jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_date");
	}

	/**
	 * normal case 2. <br>
	 * use literal.
	 */
	@Test
	public void testNewZonedDateTime02() throws Exception {
		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT Timestamp '2012-09-09 2:33:10.222'");

		ZonedDateTime now = dateFactory.newZonedDateTime();
		assertThat(now.getYear(), is(2012));
		assertThat(now.getMonthValue(), is(9));
		assertThat(now.getDayOfMonth(), is(9));
		assertThat(now.getHour(), is(2));
		assertThat(now.getMinute(), is(33));
		assertThat(now.getSecond(), is(10));
		assertThat(now.get(ChronoField.MILLI_OF_SECOND), is(222));
	}

	/**
	 * abnormal case 1. <br>
	 * doesn't return timestamp.
	 */
	@Test(expected = DataAccessException.class)
	public void testNewZonedDateTime03() throws Exception {
		JdbcFixedDateAndTimeDateFactory dateFactory = new JdbcFixedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setCurrentTimestampQuery("SELECT 1+1");

		dateFactory.newZonedDateTime();
	}

}
