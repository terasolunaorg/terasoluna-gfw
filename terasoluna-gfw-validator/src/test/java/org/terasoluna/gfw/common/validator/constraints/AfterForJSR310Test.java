/*
 * Copyright (C) 2013-2015 terasoluna.org
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
package org.terasoluna.gfw.common.validator.constraints;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.AfterForJSR310Test.AfterForJSR310TestForm;

import lombok.Data;

/**
 * Test class of {@link After} for JSR310 Date and Time API
 */
public class AfterForJSR310Test extends
                               AbstractConstraintsTest<AfterForJSR310TestForm> {

    private static final String MESSAGE_VALIDATION_ERROR = "must be after %s";

    @Before
    public void before() {
        form = new AfterForJSR310TestForm();
    }

    /**
     * for {@link LocalDate} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForLocalDate() throws Throwable {

        {
            form.setLocalDateProperty(LocalDate.parse("2015-10-01"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015-10-01")));
        }

        {
            form.setLocalDateProperty(LocalDate.parse("2015-10-02"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link LocalDate} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForLocalDate() throws Throwable {

        {
            form.setLocalDateProperty(LocalDate.parse("2015-10-01"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015年10月01日")));
        }

        {
            form.setLocalDateProperty(LocalDate.parse("2015-10-02"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link LocalDate} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForLocalDate() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setLocalDateProperty(LocalDate.parse("2015-10-01"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link LocalDateTime} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForLocalDateTime() throws Throwable {

        {
            form.setLocalDateTimeProperty(LocalDateTime
                    .parse("2015-10-01T00:00:00"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015-10-01T00:00:00")));
        }

        {
            form.setLocalDateTimeProperty(LocalDateTime
                    .parse("2015-10-02T00:00:00"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link LocalDateTime} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForLocalDateTime() throws Throwable {

        {
            form.setLocalDateTimeProperty(LocalDateTime
                    .parse("2015-10-01T00:00:00"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015年10月01日00時00分00秒")));
        }

        {
            form.setLocalDateTimeProperty(LocalDateTime
                    .parse("2015-10-02T00:00:00"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link LocalDateTime} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForLocalDateTime() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setLocalDateTimeProperty(LocalDateTime
                .parse("2015-10-01T00:00:00"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link ZonedDateTime} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForZonedDateTime() throws Throwable {

        {
            form.setZonedDateTimeProperty(ZonedDateTime
                    .parse("2015-10-01T00:00:00+09:00[Asia/Tokyo]"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR,
                            "2015-10-01T00:00:00+09:00[Asia/Tokyo]")));
        }

        {
            form.setZonedDateTimeProperty(ZonedDateTime
                    .parse("2015-10-02T00:00:00+09:00[Asia/Tokyo]"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link ZonedDateTime} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForZonedDateTime() throws Throwable {

        {
            form.setZonedDateTimeProperty(ZonedDateTime
                    .parse("2015-10-01T00:00:00+09:00[Asia/Tokyo]"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR,
                            "2015年10月01日00時00分00秒+09:00 JST")));
        }

        {
            form.setZonedDateTimeProperty(ZonedDateTime
                    .parse("2015-10-02T00:00:00+09:00[Asia/Tokyo]"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link ZonedDateTime} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForZonedDateTime() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setZonedDateTimeProperty(ZonedDateTime
                .parse("2015-10-01T00:00:00+09:00[Asia/Tokyo]"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link LocalTime} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForLocalTime() throws Throwable {

        {
            form.setLocalTimeProperty(LocalTime.parse("15:10:01"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "15:10:01")));
        }

        {
            form.setLocalTimeProperty(LocalTime.parse("15:10:02"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link LocalTime} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForLocalTime() throws Throwable {

        {
            form.setLocalTimeProperty(LocalTime.parse("15:10:01"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "15時10分01秒")));
        }

        {
            form.setLocalTimeProperty(LocalTime.parse("15:10:02"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link LocalTime} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForLocalTime() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setLocalTimeProperty(LocalTime.parse("15:10:01"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link OffsetDateTime} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForOffsetDateTime() throws Throwable {

        {
            form.setOffsetDateTimeProperty(OffsetDateTime
                    .parse("2015-10-01T00:00:00+09:00"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR,
                            "2015-10-01T00:00:00+09:00")));
        }

        {
            form.setOffsetDateTimeProperty(OffsetDateTime
                    .parse("2015-10-02T00:00:00+09:00"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link OffsetDateTime} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForOffsetDateTime() throws Throwable {

        {
            form.setOffsetDateTimeProperty(OffsetDateTime
                    .parse("2015-10-01T00:00:00+09:00"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR,
                            "2015年10月01日00時00分00秒+09:00")));
        }

        {
            form.setOffsetDateTimeProperty(OffsetDateTime
                    .parse("2015-10-02T00:00:00+09:00"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link OffsetDateTime} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForOffsetDateTime() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setOffsetDateTimeProperty(OffsetDateTime
                .parse("2015-10-01T00:00:00+09:00"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link OffsetTime} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForOffsetTime() throws Throwable {

        {
            form.setOffsetTimeProperty(OffsetTime.parse("15:10:01+09:00"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "15:10:01+09:00")));
        }

        {
            form.setOffsetTimeProperty(OffsetTime.parse("15:10:02+09:00"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link OffsetTime} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForOffsetTime() throws Throwable {

        {
            form.setOffsetTimeProperty(OffsetTime.parse("15:10:01+09:00"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "15時10分01秒+09:00")));
        }

        {
            form.setOffsetTimeProperty(OffsetTime.parse("15:10:02+09:00"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link OffsetTime} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForOffsetTime() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setOffsetTimeProperty(OffsetTime.parse("15:10:01+09:00"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link Year} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForYear() throws Throwable {

        {
            form.setYearProperty(Year.parse("2015"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015")));
        }

        {
            form.setYearProperty(Year.parse("2016"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link Year} property. specify format. expected valid if input value is after(grater than) value parsed specified
     * format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForYear() throws Throwable {

        {
            form.setYearProperty(Year.parse("2015"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015年")));
        }

        {
            form.setYearProperty(Year.parse("2016"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link Year} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForYear() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setYearProperty(Year.parse("2015"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link YearMonth} property. specify value. expected valid if input value is after(grater than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForYearMonth() throws Throwable {

        {
            form.setYearMonthProperty(YearMonth.parse("2015-10"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015-10")));
        }

        {
            form.setYearMonthProperty(YearMonth.parse("2015-11"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link YearMonth} property. specify format. expected valid if input value is after(grater than) value parsed
     * specified format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForYearMonth() throws Throwable {

        {
            form.setYearMonthProperty(YearMonth.parse("2015-10"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015年10月")));
        }

        {
            form.setYearMonthProperty(YearMonth.parse("2015-11"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link YearMonth} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForYearMonth() throws Throwable {
        setExpectedFailedToInitialize(DateTimeParseException.class);

        form.setYearMonthProperty(YearMonth.parse("2015-10"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * Validation group japanese format.
     */
    private static interface SpecifyFormat {
    };

    /**
     * Validation group invalid format.
     */
    private static interface IllegalFormat {
    };

    @Data
    public class AfterForJSR310TestForm {
        @After.List({
                @After("2015-10-01"),
                @After(value = "2015年10月01日", format = "uuuu年MM月dd日", groups = { SpecifyFormat.class }),
                @After(value = "2015-10-01", format = "uuuu年MM月dd日", groups = { IllegalFormat.class }) })
        private LocalDate localDateProperty;

        @After.List({
                @After("2015-10-01T00:00:00"),
                @After(value = "2015年10月01日00時00分00秒", format = "uuuu年MM月dd日HH時mm分ss秒", groups = { SpecifyFormat.class }),
                @After(value = "2015-10-01T00:00:00", format = "uuuu年MM月dd日HH時mm分ss秒", groups = { IllegalFormat.class }) })
        private LocalDateTime localDateTimeProperty;

        @After.List({
                @After("2015-10-01T00:00:00+09:00[Asia/Tokyo]"),
                @After(value = "2015年10月01日00時00分00秒+09:00 JST", format = "uuuu年MM月dd日HH時mm分ss秒xxxxx zzz", groups = { SpecifyFormat.class }),
                @After(value = "2015-10-01T00:00:00+09:00[Asia/Tokyo]", format = "uuuu年MM月dd日HH時mm分ss秒xxxxx zzz", groups = { IllegalFormat.class }) })
        private ZonedDateTime zonedDateTimeProperty;

        @After.List({
                @After("15:10:01"),
                @After(value = "15時10分01秒", format = "HH時mm分ss秒", groups = { SpecifyFormat.class }),
                @After(value = "15:10:01", format = "HH時mm分ss秒", groups = { IllegalFormat.class }) })
        private LocalTime localTimeProperty;

        @After.List({
                @After("2015-10-01T00:00:00+09:00"),
                @After(value = "2015年10月01日00時00分00秒+09:00", format = "uuuu年MM月dd日HH時mm分ss秒xxxxx", groups = { SpecifyFormat.class }),
                @After(value = "2015-10-01T00:00:00+09:00", format = "uuuu年MM月dd日HH時mm分ss秒xxxxx", groups = { IllegalFormat.class }) })
        private OffsetDateTime offsetDateTimeProperty;

        @After.List({
                @After("15:10:01+09:00"),
                @After(value = "15時10分01秒+09:00", format = "HH時mm分ss秒xxxxx", groups = { SpecifyFormat.class }),
                @After(value = "15:10:01+09:00", format = "HH時mm分ss秒xxxxx", groups = { IllegalFormat.class }) })
        private OffsetTime offsetTimeProperty;

        @After.List({
                @After("2015"),
                @After(value = "2015年", format = "uuuu年", groups = { SpecifyFormat.class }),
                @After(value = "2015", format = "uuuu年", groups = { IllegalFormat.class }) })
        private Year yearProperty;

        @After.List({
                @After("2015-10"),
                @After(value = "2015年10月", format = "uuuu年MM月", groups = { SpecifyFormat.class }),
                @After(value = "2015-10", format = "uuuu年MM月", groups = { IllegalFormat.class }) })
        private YearMonth yearMonthProperty;
    }
}
