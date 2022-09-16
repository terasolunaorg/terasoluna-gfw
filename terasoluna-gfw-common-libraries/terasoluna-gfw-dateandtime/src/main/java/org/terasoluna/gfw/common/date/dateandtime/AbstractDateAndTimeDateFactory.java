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

import java.time.OffsetDateTime;
import java.util.Date;

import org.terasoluna.gfw.common.date.DefaultClassicDateFactory;

/**
 * Abstract implementation of
 * {@link org.terasoluna.gfw.common.date.dateandtime.DateAndTimeDateFactory}.
 * <p>
 * This class converts from {@link java.time.OffsetDateTime} or
 * {@link java.time.ZonedDateTime} to {@link java.util.Date},
 * {@link java.sql.Timestamp}, {@link java.sql.Date}, {@link java.sql.Time} <br>
 * so all things that concrete classes do is to return current
 * {@link java.time.LocalDateTime},{@link java.time.OffsetDateTime} or
 * {@link java.time.ZonedDateTime}.
 * </p>
 * 
 * @since 5.0.0
 */
public abstract class AbstractDateAndTimeDateFactory extends DefaultClassicDateFactory
		implements DateAndTimeDateFactory {

	/**
	 * Type of DateTime used to generate the Date. Default is
	 * {@link java.time.LocalDateTime}
	 */
	private String dateTimeType = "LocalDateTime";

	/**
	 * Generates and returns {@link java.util.Date} instance of the current time
	 * from the DateTime specified in dateTimeType. *
	 * <p>
	 * Nanosecond values that are too small to be treated as milliseconds are
	 * discarded when converted to Date.
	 * </p>
	 * 
	 * @return Date current date
	 */
	@Override
	public Date newDate() {

		if (dateTimeType.equals("LocalDateTime")) {
			return Date.from(newLocalDateTime().toInstant(OffsetDateTime.now().getOffset()));
		} else if (dateTimeType.equals("OffsetDateTime")) {
			return Date.from(newOffsetDateTime().toInstant());
		} else if (dateTimeType.equals("ZonedDateTime")) {
			return Date.from(newZonedDateTime().toInstant());
		} else {
			return new Date();
		}
	}

	/**
	 * Specify the DateTime used to generate the {@link java.util.Date}
	 * 
	 * @param dateTimeType
	 */
	public void setDateTimeType(DateTimeType dateTimeType) {
		this.dateTimeType = dateTimeType.getDateTimeType();
	}

	/**
	 * Types of DateTime that can be used to generate a {@link java.util.Date}
	 */
	public enum DateTimeType {
		Local("LocalDateTime"), OFFSET("OffsetDateTime"), ZONED("ZonedDateTime"), DATE("Date");

		private String dateTimeType;

		DateTimeType(String dateTimeType) {
			this.dateTimeType = dateTimeType;
		}

		public String getDateTimeType() {
			return dateTimeType;
		}

	};

}
