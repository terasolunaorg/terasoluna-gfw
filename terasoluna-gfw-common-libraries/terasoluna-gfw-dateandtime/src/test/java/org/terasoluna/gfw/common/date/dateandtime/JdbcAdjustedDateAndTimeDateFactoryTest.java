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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for JdbcAdjustedDateAndTimeDateFactory.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
@Rollback
// Changed by SPR-13277
public class JdbcAdjustedDateAndTimeDateFactoryTest {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		// crate table
		jdbcTemplate.getJdbcOperations().execute("CREATE TABLE system_adjusted_date(diff long)");
	}

	@After
	public void tearDown() throws Exception {
		// drop table
		jdbcTemplate.getJdbcOperations().execute("DROP TABLE system_adjusted_date");
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When a time 30 minutes ahead is registered
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLocalDateTime01() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		// returns diff as minutes

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime result = dateFactory.newLocalDateTime();

		assertThat(
				(int) (Math.round(
						result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
				is(30));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * 30 and 60 minutes forward.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLocalDateTime02() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(false);
		// returns diff as milliseconds
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		dateFactory.afterPropertiesSet();

		{
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// plus 30 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(30));

		}
		{
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// plus 60 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(60));
		}
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * If the time to advance is cached.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLocalDateTime03() throws Exception {
		// plus 30 minute
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30));

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(true);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		dateFactory.afterPropertiesSet();

		{
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// plus 30 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(30));

		}
		{
			// plus 60 minute
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// still plus 30 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(30));
		}
		{
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// still plus 30 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(30));
		}
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When reloading after caching the advance time.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLocalDateTime04() throws Exception {
		// plus 30 minute
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30));

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(true);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		dateFactory.afterPropertiesSet();

		{
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// plus 30 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(30));

		}
		{
			// plus 60 minute
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// still plus 30 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(30));
		}
		{
			dateFactory.reload();
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime result = dateFactory.newLocalDateTime();

			// plus 60 minute
			assertThat((int) (Math.round(
					result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
					is(60));
		}
	}

	/**
	 * Testing the case when adjustment value fetched from DB is null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLocalDateTime05() throws Exception {

		// added data to db table
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", null)); // setting empty string

		// created datefactory instance
		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		// created comparision targets
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime result = dateFactory.newLocalDateTime();

		// asserts
		assertThat(
				(int) (Math.round(
						result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
				is(0));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When a new JDBCTemplate is generated from a DataSource
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewLocalDateTime_setJdbcTemaplteDirectly() throws Exception {
		// plus 30 minute
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30));

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setJdbcTemplate(new JdbcTemplate(dataSource));
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime result = dateFactory.newLocalDateTime();

		assertThat(
				(int) (Math.round(
						result.toEpochSecond(ZoneOffset.ofHours(9)) - now.toEpochSecond(ZoneOffset.ofHours(9))) / 60.0),
				is(30));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When a time 30 minutes ahead is registered
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewOffsetDateTime01() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		// returns diff as minutes

		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime result = dateFactory.newOffsetDateTime();

		assertThat(
				(int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
						/ 1000.0),
				is(30));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * 30 and 60 minutes forward.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewOffsetDateTime02() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(false);
		// returns diff as milliseconds
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		dateFactory.afterPropertiesSet();

		{
			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// plus 30 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));

		}
		{
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));

			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// plus 60 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(60));
		}
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * If the time to advance is cached.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewOffsetDateTime03() throws Exception {
		// plus 30 minute
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30));

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(true);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		dateFactory.afterPropertiesSet();

		{
			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// plus 30 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));

		}
		{
			// plus 60 minute
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));

			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// still plus 30 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));
		}
		{
			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// still plus 30 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));
		}
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When reloading after caching the advance time.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewOffsetDateTime04() throws Exception {
		// plus 30 minute
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30));

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(true);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		dateFactory.afterPropertiesSet();

		{
			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// plus 30 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));

		}
		{
			// plus 60 minute
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));
			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// still plus 30 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));
		}
		{
			dateFactory.reload();
			OffsetDateTime now = OffsetDateTime.now();
			OffsetDateTime result = dateFactory.newOffsetDateTime();

			// plus 60 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(60));
		}
	}

	/**
	 * Testing the case when adjustment value fetched from DB is null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewOffsetDateTime05() throws Exception {

		// added data to db table
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", null)); // setting empty string

		// created datefactory instance
		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		// created comparision targets
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime result = dateFactory.newOffsetDateTime();

		// asserts
		assertThat(
				(int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0 / 1000.0),
				is(0));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When a new JDBCTemplate is generated from a DataSource
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewOffsetDateTime_setJdbcTemaplteDirectly() throws Exception {
		// plus 30 minute
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30));

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setJdbcTemplate(new JdbcTemplate(dataSource));
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime result = dateFactory.newOffsetDateTime();

		assertThat(
				(int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0 / 1000.0),
				is(30));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When a time 30 minutes ahead is registered
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewZonedDateTime01() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");

		// returns diff as minutes

		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime result = dateFactory.newZonedDateTime();

		assertThat(
				(int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0 / 1000.0),
				is(30));
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * 30 and 60 minutes forward.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewZonedDateTime02() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(false);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as
																								// milliseconds
		dateFactory.afterPropertiesSet();

		{
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30)); // plus 30 minute

		}
		{
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60));

			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			// plus 60 minute
			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(60));
		}
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * If the time to advance is cached.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewZonedDateTime03() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(true);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as
																								// minutes
		dateFactory.afterPropertiesSet();

		{
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30)); // plus 30 minute

		}
		{
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60)); // plus
																														// 60
																														// minute
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));// still plus 30 minute
		}
		{
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));// still plus 30 minute
		}
	}

	/**
	 * Tests to retrieve the time from the database and compare it with the system
	 * time.<br>
	 * When reloading after caching the advance time.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewZonedDateTime04() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setUseCache(true);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as
																								// minutes
		dateFactory.afterPropertiesSet();

		{
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30)); // plus 30 minute

		}
		{
			jdbcTemplate.update("UPDATE system_adjusted_date SET diff = :diff", Collections.singletonMap("diff", 60)); // plus
																														// 60
																														// minute
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(30));// still plus 30 minute
		}
		{
			dateFactory.reload(); // reload!
			ZonedDateTime now = ZonedDateTime.now();
			ZonedDateTime result = dateFactory.newZonedDateTime();

			assertThat((int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0
					/ 1000.0), is(60));// plus 60 minute
		}
	}

	/**
	 * Testing the case when adjustment value fetched from DB is null.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewZonedDateTime05() throws Exception {

		// added data to db table
		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", null)); // setting empty string

		// created datefactory instance
		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setDataSource(dataSource);
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as
																								// minutes

		// created comparision targets
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime result = dateFactory.newZonedDateTime();

		// asserts
		assertThat(
				(int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0 / 1000.0),
				is(0));
	}

	@Test
	public void testNewZonedDateTime_setJdbcTemaplteDirectly() throws Exception {

		jdbcTemplate.update("INSERT INTO system_adjusted_date(diff) VALUES (:diff)",
				Collections.singletonMap("diff", 30)); // plus 30 minute

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setJdbcTemplate(new JdbcTemplate(dataSource));
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date"); // returns diff as
																								// minutes

		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime result = dateFactory.newZonedDateTime();

		assertThat(
				(int) (Math.round(result.toInstant().toEpochMilli() - now.toInstant().toEpochMilli()) / 60.0 / 1000.0),
				is(30));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAfterProperitesSet_jdbcTemplateAndDataSourceIsNull() throws Exception {

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setAdjustedValueQuery("SELECT diff * 60 * 1000 FROM system_adjusted_date");
		dateFactory.setDataSource(null);
		dateFactory.setJdbcTemplate(null);

		dateFactory.afterPropertiesSet();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAfterProperitesSet_adjustedValueQueryIsNull() throws Exception {

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setAdjustedValueQuery(null);
		dateFactory.setDataSource(dataSource);

		dateFactory.afterPropertiesSet();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testAfterProperitesSet_adjustedValueQueryIsEmpty() throws Exception {

		JdbcAdjustedDateAndTimeDateFactory dateFactory = new JdbcAdjustedDateAndTimeDateFactory();
		dateFactory.setAdjustedValueQuery("");
		dateFactory.setDataSource(dataSource);

		dateFactory.afterPropertiesSet();

	}
}
