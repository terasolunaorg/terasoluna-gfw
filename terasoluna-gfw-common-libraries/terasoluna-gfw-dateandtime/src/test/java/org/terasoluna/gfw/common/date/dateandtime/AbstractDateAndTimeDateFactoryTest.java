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

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;
import org.terasoluna.gfw.common.date.dateandtime.AbstractDateAndTimeDateFactory.DateTimeType;

/**
 * Test class for AbstractDateAndTimeDateFactory.
 *
 */
public class AbstractDateAndTimeDateFactoryTest {

	/**
	 * Tests for generating a Date from an LocalDateTime.
	 */
	@Test
	public void testNewDateToLocal() {
		StubDateFactory factory = new StubDateFactory();
		Date now = factory.newDate();
		LocalDateTime result = DateAndTimeConvertUtils.convertDateToLocalDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a Timestamp from an LocalDateTime.
	 */
	@Test
	public void testNewTimestampToLocal() {
		StubDateFactory factory = new StubDateFactory();
		Timestamp now = factory.newTimestamp();
		LocalDateTime result = DateAndTimeConvertUtils.convertTimestampToLocalDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a java.sql.Date from an LocalDateTime.
	 */
	@Test
	public void testNewSqlDateToLocal() {
		StubDateFactory factory = new StubDateFactory();
		java.sql.Date now = factory.newSqlDate();
		LocalDateTime result = DateAndTimeConvertUtils.convertSqlDateToLocalDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(0));
		assertThat(result.getMinute(), is(0));
		assertThat(result.getSecond(), is(0));
	}

	/**
	 * Tests for generating a Time from an LocalDateTime.
	 */
	@Test
	public void testNewTimeToLocal() {
		StubDateFactory factory = new StubDateFactory();
		Time now = factory.newTime();
		LocalDateTime result = DateAndTimeConvertUtils.convertTimeToLocalDateTime(now);

		assertThat(result.getYear(), is(1970));
		assertThat(result.getMonthValue(), is(1));
		assertThat(result.getDayOfMonth(), is(1));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a Date from an OffsetDateTime.
	 */
	@Test
	public void testNewDateToOffset() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.OFFSET);
		Date now = factory.newDate();
		OffsetDateTime result = DateAndTimeConvertUtils.convertDateToOffsetDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a Timestamp from an OffsetDateTime.
	 */
	@Test
	public void testNewTimestampToOffset() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.OFFSET);
		Timestamp now = factory.newTimestamp();
		OffsetDateTime result = DateAndTimeConvertUtils.convertTimestampToOffsetDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a java.sql.Date from an OffsetDateTime.
	 */
	@Test
	public void testNewSqlDateToOffset() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.OFFSET);
		java.sql.Date now = factory.newSqlDate();
		OffsetDateTime result = DateAndTimeConvertUtils.convertSqlDateToOffsetDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(0));
		assertThat(result.getMinute(), is(0));
		assertThat(result.getSecond(), is(0));
	}

	/**
	 * Tests for generating a Time from an OffsetDateTime.
	 */
	@Test
	public void testNewTimeToOffset() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.OFFSET);
		Time now = factory.newTime();
		OffsetDateTime result = DateAndTimeConvertUtils.convertTimeToOffsetDateTime(now);

		assertThat(result.getYear(), is(1970));
		assertThat(result.getMonthValue(), is(1));
		assertThat(result.getDayOfMonth(), is(1));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a Date from an ZonedDateTime.
	 */
	@Test
	public void testNewDateToZoned() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.ZONED);
		Date now = factory.newDate();
		ZonedDateTime result = DateAndTimeConvertUtils.convertDateToZonedDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a Timestamp from an ZonedDateTime.
	 */
	@Test
	public void testNewTimeStampToZoned() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.ZONED);
		Timestamp now = factory.newTimestamp();
		ZonedDateTime result = DateAndTimeConvertUtils.convertTimestampToZonedDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * Tests for generating a java.sql.Date from an ZonedDateTime.
	 */
	@Test
	public void testNewSqlDateToZoned() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.ZONED);
		java.sql.Date now = factory.newSqlDate();
		ZonedDateTime result = DateAndTimeConvertUtils.convertSqlDateToZonedDateTime(now);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(0));
		assertThat(result.getMinute(), is(0));
		assertThat(result.getSecond(), is(0));
	}

	/**
	 * Tests for generating a Time from an ZonedDateTime.
	 */
	@Test
	public void testNewTimeToZoned() {
		StubDateFactory factory = new StubDateFactory();
		factory.setDateTimeType(DateTimeType.ZONED);
		Time now = factory.newTime();
		ZonedDateTime result = DateAndTimeConvertUtils.convertTimeToZonedDateTime(now);

		assertThat(result.getYear(), is(1970));
		assertThat(result.getMonthValue(), is(1));
		assertThat(result.getDayOfMonth(), is(1));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
	}

	/**
	 * DateFactory for fixing the generated date and time.
	 */
	class StubDateFactory extends AbstractDateAndTimeDateFactory {

		@Override
		public LocalDateTime newLocalDateTime() {
			return LocalDateTime.of(2012, 9, 3, 23, 7, 11, 100);
		}

		@Override
		public OffsetDateTime newOffsetDateTime() {
			return OffsetDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZoneOffset.ofHours(9));
		}

		@Override
		public ZonedDateTime newZonedDateTime() {
			return ZonedDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZoneId.of("Asia/Tokyo"));
		}

	}

}
