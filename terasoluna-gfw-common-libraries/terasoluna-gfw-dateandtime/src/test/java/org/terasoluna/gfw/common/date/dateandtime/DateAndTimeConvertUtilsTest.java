package org.terasoluna.gfw.common.date.dateandtime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Constructor;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * Test class for DateAndTimeConvertUtils.
 */
public class DateAndTimeConvertUtilsTest {

	/**
	 * ZoneOffset fixed at JST.
	 */
	private final static ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(9);

	/**
	 * ZoneId fixed at Tokyo.
	 */
	private final static ZoneId ZONE_ID = ZoneId.of("Asia/Tokyo");

	@Test
	public void testDateAndTimeConvertUtils() throws Exception {
		// set up
		Constructor<DateAndTimeConvertUtils> constructor = DateAndTimeConvertUtils.class.getDeclaredConstructor();
		assertThat(constructor.isAccessible(), is(false));
		constructor.setAccessible(true);

		// assert
		assertThat(constructor.newInstance(), notNullValue());

		constructor.setAccessible(false);
	}

	/**
	 * Testing of functionality to convert LocalDatetime to Date.
	 */
	@Test
	public void testConvertLocalDateTimeToDate01() {

		LocalDateTime dateTime = LocalDateTime.of(2012, 9, 3, 23, 7, 11, 100);

		Date date = DateAndTimeConvertUtils.convertLocalDateTimeToDate(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(date);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to Date.<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertLocalDateTimeToDate02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertLocalDateTimeToDate(null));
		assertThat(ex.getMessage(), is("localdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to Timestamp
	 */
	@Test
	public void testConvertLocalDateTimeToTimestamp01() {

		LocalDateTime dateTime = LocalDateTime.of(2012, 9, 3, 23, 7, 11, 100);

		Timestamp timestamp = DateAndTimeConvertUtils.convertLocalDateTimeToTimestamp(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(timestamp);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to Timestamp<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertLocalDateTimeToTimestamp02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertLocalDateTimeToTimestamp(null));
		assertThat(ex.getMessage(), is("localdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to java.sql.Date
	 */
	@Test
	public void testConvertLocalDateTimeToSqlDate01() {

		LocalDateTime dateTime = LocalDateTime.of(2012, 9, 3, 23, 7, 11, 100);

		java.sql.Date sqlDate = DateAndTimeConvertUtils.convertLocalDateTimeToSqlDate(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(sqlDate);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(0));
		assertThat(result.get(Calendar.MINUTE), is(0));
		assertThat(result.get(Calendar.SECOND), is(0));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to java.sql.Date<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertLocalDateTimeToSqlDate02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertLocalDateTimeToSqlDate(null));
		assertThat(ex.getMessage(), is("localdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to Time
	 */
	@Test
	public void testConvertLocalDateTimeToTime01() {

		LocalDateTime dateTime = LocalDateTime.of(2012, 9, 3, 23, 7, 11, 100);

		Time time = DateAndTimeConvertUtils.convertLocalDateTimeToTime(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(time);

		assertThat(result.get(Calendar.YEAR), is(1970));
		assertThat(result.get(Calendar.MONTH) + 1, is(1));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(1));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert LocalDatetime to Time<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertLocalDateTimeToTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertLocalDateTimeToTime(null));
		assertThat(ex.getMessage(), is("localdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert Date to LocalDatetime
	 */
	@Test
	public void testConvertDateToLocalDateTime01() {

		Date date = new Date(1346681231100L);

		LocalDateTime result = DateAndTimeConvertUtils.convertDateToLocalDateTime(date);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(100000000));
	}

	/**
	 * Testing of functionality to convert Date to LocalDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertDateToLocalDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertDateToLocalDateTime(null));
		assertThat(ex.getMessage(), is("date must not be null"));
	}

	/**
	 * Testing of functionality to convert Timestamp to LocalDatetime
	 */
	@Test
	public void testConvertTimestampToLocalDateTime01() {
		Timestamp timestamp = new Timestamp(1346681231100L);
		LocalDateTime result = DateAndTimeConvertUtils.convertTimestampToLocalDateTime(timestamp);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(100000000));
	}

	/**
	 * Testing of functionality to convert Timestamp to LocalDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertTimestampToLocalDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertTimestampToLocalDateTime(null));
		assertThat(ex.getMessage(), is("timestamp must not be null"));
	}

	/**
	 * Testing of functionality to convert java.sql.Date to LocalDatetime.
	 */
	@Test
	public void testConvertSqlDateToLocalDateTime01() {
		java.sql.Date date = new java.sql.Date(1346681231100L);
		LocalDateTime result = DateAndTimeConvertUtils.convertSqlDateToLocalDateTime(date);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(0));
		assertThat(result.getMinute(), is(0));
		assertThat(result.getSecond(), is(0));
		assertThat(result.getNano(), is(0));
	}

	/**
	 * Testing of functionality to convert java.sql.Date to LocalDatetime.<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertSqlDateToLocalDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertSqlDateToLocalDateTime(null));
		assertThat(ex.getMessage(), is("date must not be null"));
	}

	/**
	 * Testing of functionality to convert Time to LocalDatetime
	 */
	@Test
	public void testConvertTimeToLocalDateTime01() {
		Time time = new Time(1346681231100L);
		LocalDateTime result = DateAndTimeConvertUtils.convertTimeToLocalDateTime(time);

		assertThat(result.getYear(), is(1970));
		assertThat(result.getMonthValue(), is(1));
		assertThat(result.getDayOfMonth(), is(1));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(0));
	}

	/**
	 * Testing of functionality to convert Time to LocalDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertTimeToLocalDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertTimeToLocalDateTime(null));
		assertThat(ex.getMessage(), is("time must not be null"));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to Date.
	 */
	@Test
	public void testConvertOffsetDateTimeToDate01() {

		OffsetDateTime dateTime = OffsetDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_OFFSET);

		Date date = DateAndTimeConvertUtils.convertOffsetDateTimeToDate(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(date);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to Date.<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertOffsetDateTimeToDate02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertOffsetDateTimeToDate(null));
		assertThat(ex.getMessage(), is("offsetdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to Timestamp
	 */
	@Test
	public void testConvertOffsetDateTimeToTimestamp01() {

		OffsetDateTime dateTime = OffsetDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_OFFSET);

		Timestamp timestamp = DateAndTimeConvertUtils.convertOffsetDateTimeToTimestamp(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(timestamp);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to Timestamp<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertOffsetDateTimeToTimestamp02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertOffsetDateTimeToTimestamp(null));
		assertThat(ex.getMessage(), is("offsetdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to java.sql.Date
	 */
	@Test
	public void testConvertOffsetDateTimeToSqlDate01() {

		OffsetDateTime dateTime = OffsetDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_OFFSET);

		java.sql.Date sqlDate = DateAndTimeConvertUtils.convertOffsetDateTimeToSqlDate(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(sqlDate);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(0));
		assertThat(result.get(Calendar.MINUTE), is(0));
		assertThat(result.get(Calendar.SECOND), is(0));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to java.sql.Date<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertOffsetDateTimeToSqlDate02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertOffsetDateTimeToSqlDate(null));
		assertThat(ex.getMessage(), is("offsetdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to Time
	 */
	@Test
	public void testConvertOffsetDateTimeToTime01() {

		OffsetDateTime dateTime = OffsetDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_OFFSET);

		Time time = DateAndTimeConvertUtils.convertOffsetDateTimeToTime(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(time);

		assertThat(result.get(Calendar.YEAR), is(1970));
		assertThat(result.get(Calendar.MONTH) + 1, is(1));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(1));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert OffsetDatetime to Time<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertOffsetDateTimeToTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertOffsetDateTimeToTime(null));
		assertThat(ex.getMessage(), is("offsetdatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert Date to OffsetDatetime
	 */
	@Test
	public void testConvertDateToOffsetDateTime01() {

		Date date = new Date(1346681231100L);

		OffsetDateTime result = DateAndTimeConvertUtils.convertDateToOffsetDateTime(date);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(100000000));
	}

	/**
	 * Testing of functionality to convert Date to OffsetDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertDateToOffsetDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertDateToOffsetDateTime(null));
		assertThat(ex.getMessage(), is("date must not be null"));
	}

	/**
	 * Testing of functionality to convert Timestamp to OffsetDatetime
	 */
	@Test
	public void testConvertTimestampToOffsetDateTime01() {
		Timestamp timestamp = new Timestamp(1346681231100L);
		OffsetDateTime result = DateAndTimeConvertUtils.convertTimestampToOffsetDateTime(timestamp);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(100000000));
	}

	/**
	 * Testing of functionality to convert Timestamp to OffsetDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertTimestampToOffsetDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertTimestampToOffsetDateTime(null));
		assertThat(ex.getMessage(), is("timestamp must not be null"));
	}

	/**
	 * Testing of functionality to convert java.sql.Date to OffsetDatetime.
	 */
	@Test
	public void testConvertSqlDateToOffsetDateTime01() {
		java.sql.Date date = new java.sql.Date(1346681231100L);
		OffsetDateTime result = DateAndTimeConvertUtils.convertSqlDateToOffsetDateTime(date);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(0));
		assertThat(result.getMinute(), is(0));
		assertThat(result.getSecond(), is(0));
		assertThat(result.getNano(), is(0));
	}

	/**
	 * Testing of functionality to convert java.sql.Date to OffsetDatetime.<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertSqlDateToOffsetDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertSqlDateToOffsetDateTime(null));
		assertThat(ex.getMessage(), is("date must not be null"));
	}

	/**
	 * Testing of functionality to convert Time to OffsetDatetime
	 */
	@Test
	public void testConvertTimeToOffsetDateTime01() {
		Time time = new Time(1346681231100L);
		OffsetDateTime result = DateAndTimeConvertUtils.convertTimeToOffsetDateTime(time);

		assertThat(result.getYear(), is(1970));
		assertThat(result.getMonthValue(), is(1));
		assertThat(result.getDayOfMonth(), is(1));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(0));
	}

	/**
	 * Testing of functionality to convert Time to OffsetDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertTimeToOffsetDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertTimeToOffsetDateTime(null));
		assertThat(ex.getMessage(), is("time must not be null"));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to Date.
	 */
	@Test
	public void testConvertZonedDateTimeToDate01() {

		ZonedDateTime dateTime = ZonedDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_ID);

		Date date = DateAndTimeConvertUtils.convertZonedDateTimeToDate(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(date);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to Date.<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertZonedDateTimeToDate02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertZonedDateTimeToDate(null));
		assertThat(ex.getMessage(), is("zoneddatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to Timestamp
	 */
	@Test
	public void testConvertZonedDateTimeToTimestamp01() {

		ZonedDateTime dateTime = ZonedDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_ID);

		Timestamp timestamp = DateAndTimeConvertUtils.convertZonedDateTimeToTimestamp(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(timestamp);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to Timestamp<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertZonedDateTimeToTimestamp02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertZonedDateTimeToTimestamp(null));
		assertThat(ex.getMessage(), is("zoneddatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to java.sql.Date
	 */
	@Test
	public void testConvertZonedDateTimeToSqlDate01() {

		ZonedDateTime dateTime = ZonedDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_ID);

		java.sql.Date sqlDate = DateAndTimeConvertUtils.convertZonedDateTimeToSqlDate(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(sqlDate);

		assertThat(result.get(Calendar.YEAR), is(2012));
		assertThat(result.get(Calendar.MONTH) + 1, is(9));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(3));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(0));
		assertThat(result.get(Calendar.MINUTE), is(0));
		assertThat(result.get(Calendar.SECOND), is(0));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to java.sql.Date<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertZonedDateTimeToSqlDate02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertZonedDateTimeToSqlDate(null));
		assertThat(ex.getMessage(), is("zoneddatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to Time
	 */
	@Test
	public void testConvertZonedDateTimeToTime01() {

		ZonedDateTime dateTime = ZonedDateTime.of(2012, 9, 3, 23, 7, 11, 100, ZONE_ID);

		Time time = DateAndTimeConvertUtils.convertZonedDateTimeToTime(dateTime);
		Calendar result = Calendar.getInstance();
		result.setTime(time);

		assertThat(result.get(Calendar.YEAR), is(1970));
		assertThat(result.get(Calendar.MONTH) + 1, is(1));
		assertThat(result.get(Calendar.DAY_OF_MONTH), is(1));
		assertThat(result.get(Calendar.HOUR_OF_DAY), is(23));
		assertThat(result.get(Calendar.MINUTE), is(7));
		assertThat(result.get(Calendar.SECOND), is(11));
		assertThat(result.get(Calendar.MILLISECOND), is(0));
	}

	/**
	 * Testing of functionality to convert ZonedDatetime to Time<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertZonedDateTimeToTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertZonedDateTimeToTime(null));
		assertThat(ex.getMessage(), is("zoneddatetime must not be null"));
	}

	/**
	 * Testing of functionality to convert Date to ZonedDatetime
	 */
	@Test
	public void testConvertDateToZonedDateTime01() {

		Date date = new Date(1346681231100L);

		ZonedDateTime result = DateAndTimeConvertUtils.convertDateToZonedDateTime(date);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(100000000));
	}

	/**
	 * Testing of functionality to convert Date to ZonedDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertDateToZonedDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertDateToZonedDateTime(null));
		assertThat(ex.getMessage(), is("date must not be null"));
	}

	/**
	 * Testing of functionality to convert Timestamp to ZonedDatetime
	 */
	@Test
	public void testConvertTimestampToZonedDateTime01() {
		Timestamp timestamp = new Timestamp(1346681231100L);
		ZonedDateTime result = DateAndTimeConvertUtils.convertTimestampToZonedDateTime(timestamp);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(100000000));
	}

	/**
	 * Testing of functionality to convert Timestamp to ZonedDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertTimestampToZonedDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertTimestampToZonedDateTime(null));
		assertThat(ex.getMessage(), is("timestamp must not be null"));
	}

	/**
	 * Testing of functionality to convert java.sql.Date to ZonedDatetime.
	 */
	@Test
	public void testConvertSqlDateToZonedDateTime01() {
		java.sql.Date date = new java.sql.Date(1346681231100L);
		ZonedDateTime result = DateAndTimeConvertUtils.convertSqlDateToZonedDateTime(date);

		assertThat(result.getYear(), is(2012));
		assertThat(result.getMonthValue(), is(9));
		assertThat(result.getDayOfMonth(), is(3));
		assertThat(result.getHour(), is(0));
		assertThat(result.getMinute(), is(0));
		assertThat(result.getSecond(), is(0));
		assertThat(result.getNano(), is(0));
	}

	/**
	 * Testing of functionality to convert java.sql.Date to ZonedDatetime.<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertSqlDateToZonedDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertSqlDateToZonedDateTime(null));
		assertThat(ex.getMessage(), is("date must not be null"));
	}

	/**
	 * Testing of functionality to convert Time to ZonedDatetime
	 */
	@Test
	public void testConvertTimeToZonedDateTime01() {
		Time time = new Time(1346681231100L);
		ZonedDateTime result = DateAndTimeConvertUtils.convertTimeToZonedDateTime(time);

		assertThat(result.getYear(), is(1970));
		assertThat(result.getMonthValue(), is(1));
		assertThat(result.getDayOfMonth(), is(1));
		assertThat(result.getHour(), is(23));
		assertThat(result.getMinute(), is(7));
		assertThat(result.getSecond(), is(11));
		assertThat(result.getNano(), is(0));
	}

	/**
	 * Testing of functionality to convert Time to ZonedDatetime<br>
	 * If an exception is thrown
	 */
	@Test
	public void testConvertTimeToZonedDateTime02() {
		// testing
		Exception ex = assertThrows(IllegalArgumentException.class,
				() -> DateAndTimeConvertUtils.convertTimeToZonedDateTime(null));
		assertThat(ex.getMessage(), is("time must not be null"));
	}
}
