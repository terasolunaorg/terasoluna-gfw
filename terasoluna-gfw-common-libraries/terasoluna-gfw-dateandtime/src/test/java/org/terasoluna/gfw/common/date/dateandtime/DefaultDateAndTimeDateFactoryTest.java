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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;

/**
 * Test class for DefaultDateAndTimeDateFactory.
 */
public class DefaultDateAndTimeDateFactoryTest {

	/**
	 * Check that the {@link org.java.time.LocalDateTime} generated using the system
	 * time is equal to the one retrieved by System.currentTimeMillis().
	 */
	@Test
	public void testNewLocalDateTime() {
		Clock.fixed(Instant.now(), ZoneId.systemDefault());

		DateAndTimeDateFactory factory = new DefaultDateAndTimeDateFactory();

		long currentTimeMillis = System.currentTimeMillis();
		LocalDateTime result = factory.newLocalDateTime();

		assertThat(result.toEpochSecond(ZoneOffset.ofHours(9)), is(currentTimeMillis / 1000));
	}

	/**
	 * Check that the {@link org.java.time.OffsetDateTime} generated using the
	 * system time is equal to the one retrieved by System.currentTimeMillis().
	 */
	@Test
	public void testNewOffsetDateTime() {
		Clock.fixed(Instant.now(), ZoneId.systemDefault());

		DateAndTimeDateFactory factory = new DefaultDateAndTimeDateFactory();

		long currentTimeMillis = System.currentTimeMillis();
		OffsetDateTime result = factory.newOffsetDateTime();

		assertThat(result.toInstant().toEpochMilli(), is(currentTimeMillis));
	}

	/**
	 * Check that the {@link org.java.time.ZonedDateTime} generated using the system
	 * time is equal to the one retrieved by System.currentTimeMillis().
	 */
	@Test
	public void testNewZonedDateTime() {
		Clock.fixed(Instant.now(), ZoneId.systemDefault());

		DateAndTimeDateFactory factory = new DefaultDateAndTimeDateFactory();

		long currentTimeMillis = System.currentTimeMillis();
		ZonedDateTime result = factory.newZonedDateTime();

		assertThat(result.toInstant().toEpochMilli(), is(currentTimeMillis));
	}

}
