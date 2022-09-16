package org.terasoluna.gfw.common.date.dateandtime;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Utility methods to interconvertion {@link java.time.OffsetDateTime} or
 * {@link java.time.ZonedDateTime} with date related.
 * 
 */
public class DateAndTimeConvertUtils {

	/**
	 * Default ZoneId.
	 */
	private static final ZoneId ZONE_ID = ZoneId.systemDefault();

	/**
	 * Default ZoneOffset.
	 */
	private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

	/**
	 * Default Constructor.
	 */
	private DateAndTimeConvertUtils() {
	};

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.LocalDateTime} using
	 * system standard time.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertDateToLocalDateTime(Date date) {
		assertDate(date);
		return date.toInstant().atZone(ZONE_ID).toLocalDateTime();
	}

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.LocalDateTime} using
	 * the time zone id specified in the argument.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @param zoneId
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertDateToLocalDateTimeWithZoneId(Date date, ZoneId zoneId) {
		assertDate(date);
		return date.toInstant().atZone(zoneId).toLocalDateTime();
	}

	/**
	 * Convert {@link java.sql.Timestamp} to {@link org.java.time.ZonedDateTime}
	 * using system standard timezone offset.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param timestamp
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
		assertTimestamp(timestamp);
		return timestamp.toLocalDateTime();
	}

	/**
	 * Convert {@link java.sql.Date} to {@link org.java.time.LocalDateTime} using
	 * system standard time.<br>
	 * normalize hour,minute,second,milliSecond to 0.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertSqlDateToLocalDateTime(java.sql.Date date) {
		assertSqlDate(date);
		return date.toLocalDate().atStartOfDay();
	}

	/**
	 * Convert {@link java.sql.Time} to {@link org.java.time.LocalDateTime} using
	 * system standard time.<br>
	 * normalize year=1970,month=1,day=1.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param time
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertTimeToLocalDateTime(Time time) {
		assertTime(time);
		return time.toLocalTime().atDate(LocalDate.of(1970, 1, 1));
	}

	/**
	 * Convert {@link org.java.time.LocalDateTime} to {@link java.util.Date} using
	 * system standard time.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Date
	 */
	public static Date convertLocalDateTimeToDate(LocalDateTime dateTime) {
		assertLocalDateTime(dateTime);
		return Date.from(ZonedDateTime.of(dateTime, ZONE_ID).toInstant());
	}

	/**
	 * Convert {@link org.java.time.LocalDateTime} to {@link java.sql.Timestamp}
	 * using system standard time.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Timestamp
	 */
	public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
		assertLocalDateTime(dateTime);
		return Timestamp.valueOf(dateTime);
	}

	/**
	 * Convert {@link org.java.time.LocalDateTime} to {@link java.sql.Date} using
	 * system standard time.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return java.sql.Date
	 */
	public static java.sql.Date convertLocalDateTimeToSqlDate(LocalDateTime dateTime) {
		assertLocalDateTime(dateTime);
		return java.sql.Date.valueOf(dateTime.toLocalDate());
	}

	/**
	 * Convert {@link org.java.time.LocalDateTime} to {@link java.sql.Time} using
	 * system standard time.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Time
	 */
	public static Time convertLocalDateTimeToTime(LocalDateTime dateTime) {
		assertLocalDateTime(dateTime);
		return Time.valueOf(dateTime.toLocalTime());
	}

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.OffsetDateTime} using
	 * system standard timezone offset.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertDateToOffsetDateTime(Date date) {
		assertDate(date);
		return date.toInstant().atOffset(ZONE_OFFSET);
	}

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.OffsetDateTime} using
	 * the time zone offset specified in the argument.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @param zoneoffset
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertDateToOffsetDateTimeWithZoneOffset(Date date, ZoneOffset zoneoffset) {
		assertDate(date);
		return date.toInstant().atOffset(zoneoffset);
	}

	/**
	 * Convert {@link java.sql.Timestamp} to {@link org.java.time.OffsetDateTime}
	 * using system standard timezone offset.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param timestamp
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertTimestampToOffsetDateTime(Timestamp timestamp) {
		assertTimestamp(timestamp);
		return timestamp.toInstant().atOffset(ZONE_OFFSET);
	}

	/**
	 * Convert {@link java.sql.Timestamp} to {@link org.java.time.OffsetDateTime}
	 * using the time zone offset specified in the argument.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param timestamp
	 * @param zoneoffset
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertTimestampToOffsetDateTimeWithZoneOffset(Timestamp timestamp,
			ZoneOffset zoneoffset) {
		assertTimestamp(timestamp);
		return timestamp.toInstant().atOffset(zoneoffset);
	}

	/**
	 * Convert {@link java.sql.Date} to {@link org.java.time.OffsetDateTime} using
	 * system standard timezone offset.<br>
	 * normalize hour,minute,second,milliSecond to 0.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertSqlDateToOffsetDateTime(java.sql.Date date) {
		assertSqlDate(date);
		return date.toLocalDate().atStartOfDay().atOffset(ZONE_OFFSET);
	}

	/**
	 * Convert {@link java.sql.Date} to {@link org.java.time.OffsetDateTime} using
	 * the time zone offset specified in the argument.<br>
	 * normalize hour,minute,second,milliSecond to 0.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @param zoneoffset
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertSqlDateToOffsetDateTimeWithZoneOffset(java.sql.Date date,
			ZoneOffset zoneoffset) {
		assertSqlDate(date);
		return date.toLocalDate().atStartOfDay().atOffset(zoneoffset);
	}

	/**
	 * Convert {@link java.sql.Time} to {@link org.java.time.OffsetDateTime} using
	 * system standard timezone offset.<br>
	 * normalize year=1970,month=1,day=1.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param time
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertTimeToOffsetDateTime(Time time) {
		assertTime(time);
		return time.toLocalTime().atDate(LocalDate.of(1970, 1, 1)).atOffset(ZONE_OFFSET);
	}

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.OffsetDateTime} using
	 * the time zone offset specified in the argument.<br>
	 * normalize year=1970,month=1,day=1.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @param zoneoffset
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime convertTimeToOffsetDateTimeWithZoneOffset(Time time, ZoneOffset zoneoffset) {
		assertTime(time);
		return time.toLocalTime().atDate(LocalDate.of(1970, 1, 1)).atOffset(zoneoffset);
	}

	/**
	 * Convert {@link org.java.time.OffsetDateTime} to {@link java.util.Date} using
	 * system standard timezone offset.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param OffsetDateTime
	 * @return Date
	 */
	public static Date convertOffsetDateTimeToDate(OffsetDateTime dateTime) {
		assertOffsetDateTime(dateTime);
		return Date.from(dateTime.toInstant());
	}

	/**
	 * Convert {@link org.java.time.OffsetDateTime} to {@link java.sql.Timestamp}
	 * using system standard timezone offset.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Timestamp
	 */
	public static Timestamp convertOffsetDateTimeToTimestamp(OffsetDateTime dateTime) {
		assertOffsetDateTime(dateTime);
		return Timestamp.from(dateTime.toInstant());
	}

	/**
	 * Convert {@link org.java.time.OffsetDateTime} to {@link java.sql.Date} using
	 * system standard timezone offset.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return java.sql.Date
	 */
	public static java.sql.Date convertOffsetDateTimeToSqlDate(OffsetDateTime dateTime) {
		assertOffsetDateTime(dateTime);
		return java.sql.Date.valueOf(dateTime.toLocalDate());
	}

	/**
	 * Convert {@link org.java.time.OffsetDateTime} to {@link java.sql.Time} using
	 * system standard timezone offset.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Time
	 */
	public static Time convertOffsetDateTimeToTime(OffsetDateTime dateTime) {
		assertOffsetDateTime(dateTime);
		return Time.valueOf(dateTime.toLocalTime());
	}

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.ZonedDateTime} using
	 * system standard timezone zoneId.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertDateToZonedDateTime(Date date) {
		assertDate(date);
		return date.toInstant().atZone(ZONE_ID);
	}

	/**
	 * Convert {@link java.util.Date} to {@link org.java.time.ZonedDateTime} using
	 * the time zone id specified in the argument.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @param zoneId
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertDateToZonedDateTimeWithZoneId(Date date, ZoneId zoneId) {
		assertDate(date);
		return date.toInstant().atZone(zoneId);
	}

	/**
	 * Convert {@link java.sql.Timestamp} to {@link org.java.time.ZonedDateTime}
	 * using system standard timezone zoneId.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param timestamp
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertTimestampToZonedDateTime(Timestamp timestamp) {
		assertTimestamp(timestamp);
		return timestamp.toInstant().atZone(ZONE_ID);
	}

	/**
	 * Convert {@link java.sql.Timestamp} to {@link org.java.time.ZonedDateTime}
	 * using the time zone id specified in the argument.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param timestamp
	 * @param zoneId
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertTimestampToZonedDateTimeWithZoneId(Timestamp timestamp, ZoneId zoneId) {
		assertTimestamp(timestamp);
		return timestamp.toInstant().atZone(zoneId);
	}

	/**
	 * Convert {@link java.sql.Date} to {@link org.java.time.ZonedDateTime} using
	 * system standard timezone zoneId.<br>
	 * normalize hour,minute,second,milliSecond to 0.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertSqlDateToZonedDateTime(java.sql.Date date) {
		assertSqlDate(date);
		return date.toLocalDate().atStartOfDay().atZone(ZONE_ID);
	}

	/**
	 * Convert {@link java.sql.Date} to {@link org.java.time.ZonedDateTime} using
	 * the time zone id specified in the argument.<br>
	 * normalize hour,minute,second,milliSecond to 0.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param date
	 * @param zoneId
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertSqlDateToZonedDateTimeWithZoneId(java.sql.Date date, ZoneId zoneId) {
		assertSqlDate(date);
		return date.toLocalDate().atStartOfDay().atZone(zoneId);
	}

	/**
	 * Convert {@link java.sql.Time} to {@link org.java.time.ZonedDateTime} using
	 * system standard timezone zoneId.<br>
	 * normalize year=1970,month=1,day=1.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param time
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertTimeToZonedDateTime(Time time) {
		assertTime(time);
		return time.toLocalTime().atDate(LocalDate.of(1970, 1, 1)).atZone(ZONE_ID);
	}

	/**
	 * Convert {@link java.sql.Time} to {@link org.java.time.ZonedDateTime} using
	 * the time zone id specified in the argument.<br>
	 * normalize year=1970,month=1,day=1.<br>
	 * Millisecond values are converted to nanoseconds and stored
	 * 
	 * @param time
	 * @param zoneId
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertTimeToZonedDateTimeWithZoneId(Time time, ZoneId zoneId) {
		assertTime(time);
		return time.toLocalTime().atDate(LocalDate.of(1970, 1, 1)).atZone(zoneId);
	}

	/**
	 * Convert {@link org.java.time.ZonedDateTime} to {@link java.util.Date} using
	 * system standard timezone zoneId.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Date
	 */
	public static Date convertZonedDateTimeToDate(ZonedDateTime dateTime) {
		assertZonedDateTime(dateTime);
		return Date.from(dateTime.toInstant());
	}

	/**
	 * Convert {@link org.java.time.ZonedDateTime} to {@link java.sql.Timestamp}
	 * using system standard timezone zoneId.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Timestamp
	 */
	public static Timestamp convertZonedDateTimeToTimestamp(ZonedDateTime dateTime) {
		assertZonedDateTime(dateTime);
		return Timestamp.from(dateTime.toInstant());
	}

	/**
	 * Convert {@link org.java.time.ZonedDateTime} to {@link java.sql.Date} using
	 * system standard timezone zoneId.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return java.sql.Date
	 */
	public static java.sql.Date convertZonedDateTimeToSqlDate(ZonedDateTime dateTime) {
		assertZonedDateTime(dateTime);
		return java.sql.Date.valueOf(dateTime.toLocalDate());
	}

	/**
	 * Convert {@link org.java.time.ZonedDateTime} to {@link java.sql.Time} using
	 * system standard timezone zoneId.<br>
	 * If the nanosecond value is not large enough to be treated as a millisecond,
	 * the millisecond value is truncated
	 * 
	 * @param dateTime
	 * @return Time
	 */
	public static Time convertZonedDateTimeToTime(ZonedDateTime dateTime) {
		assertZonedDateTime(dateTime);
		return Time.valueOf(dateTime.toLocalTime());
	}

	/**
	 * Not null assertion on date<br>
	 * 
	 * @param date assertion target
	 * @throws IllegalArgumentException if the date is null
	 */
	private static void assertDate(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("date must not be null");
		}
	}

	/**
	 * Not null assertion on timestamp<br>
	 * 
	 * @param timestamp assertion target
	 * @throws IllegalArgumentException if the timestamp is null
	 */
	private static void assertTimestamp(Timestamp timestamp) {
		if (timestamp == null) {
			throw new IllegalArgumentException("timestamp must not be null");
		}
	}

	/**
	 * Not null assertion on java.sql.Date<br>
	 * 
	 * @param java.sql.date assertion target
	 * @throws IllegalArgumentException if the java.sql.date is null
	 */
	private static void assertSqlDate(java.sql.Date date) {
		if (date == null) {
			throw new IllegalArgumentException("date must not be null");
		}
	}

	/**
	 * Not null assertion on time<br>
	 * 
	 * @param time assertion target
	 * @throws IllegalArgumentException if the time is null
	 */
	private static void assertTime(Time time) {
		if (time == null) {
			throw new IllegalArgumentException("time must not be null");
		}
	}

	/**
	 * Not null assertion on localdatetime<br>
	 * 
	 * @param offsetdatetime assertion target
	 * @throws IllegalArgumentException if the localdatetime is null
	 */
	private static void assertLocalDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			throw new IllegalArgumentException("localdatetime must not be null");
		}
	}

	/**
	 * Not null assertion on offsetdatetime<br>
	 * 
	 * @param offsetdatetime assertion target
	 * @throws IllegalArgumentException if the offsetdatetime is null
	 */
	private static void assertOffsetDateTime(OffsetDateTime dateTime) {
		if (dateTime == null) {
			throw new IllegalArgumentException("offsetdatetime must not be null");
		}
	}

	/**
	 * Not null assertion on zoneddatetime<br>
	 * 
	 * @param zoneddatetime assertion target
	 * @throws IllegalArgumentException if the zoneddatetime is null
	 */
	private static void assertZonedDateTime(ZonedDateTime dateTime) {
		if (dateTime == null) {
			throw new IllegalArgumentException("zoneddatetime must not be null");
		}
	}
}
