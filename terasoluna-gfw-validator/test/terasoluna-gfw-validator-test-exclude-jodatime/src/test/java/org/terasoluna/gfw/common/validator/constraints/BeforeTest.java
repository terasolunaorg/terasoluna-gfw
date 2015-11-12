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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.BeforeTest.BeforeTestForm;

import lombok.Data;

/**
 * Test class of {@link Before}
 */
public class BeforeTest extends AbstractConstraintsTest<BeforeTestForm> {

    private static final String MESSAGE_VALIDATION_ERROR = "must be before %s";

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static final Calendar cal = Calendar.getInstance();

    @org.junit.Before
    public void before() {
        form = new BeforeTestForm();
    }

    /**
     * input null value. expected valid.
     * @throws Throwable
     */
    @Test
    public void testInputNull() throws Throwable {

        violations = validator.validate(form);
        assertThat(violations.size(), is(0));
    }

    /**
     * for {@link Date} property. specify value. expected valid if input value is after(less than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForDate() throws Throwable {

        {
            form.setDateProperty(df.parse("2015-10-01"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015-10-01")));
        }

        {
            form.setDateProperty(df.parse("2015-09-30"));

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link Date} property. specify format. expected valid if input value is after(less than) value parsed specified
     * format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForDate() throws Throwable {

        {
            form.setDateProperty(df.parse("2015-10-01"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015年10月01日")));
        }

        {
            form.setDateProperty(df.parse("2015-09-30"));

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link Date} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForDate() throws Throwable {
        setExpectedFailedToInitialize(ParseException.class);

        form.setDateProperty(df.parse("2015-10-01"));

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * for {@link Calendar} property. specify value. expected valid if input value is after(less than) value.
     * @throws Throwable
     */
    @Test
    public void testSpecifyValueForCalendar() throws Throwable {

        {
            cal.setTime(df.parse("2015-10-01"));
            form.setCalendarProperty(cal);

            violations = validator.validate(form);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015-10-01")));
        }

        {
            cal.setTime(df.parse("2015-09-30"));
            form.setCalendarProperty(cal);

            violations = validator.validate(form);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link Calendar} property. specify format. expected valid if input value is after(less than) value parsed specified
     * format.
     * @throws Throwable
     */
    @Test
    public void testSpecifyFormatForCalendar() throws Throwable {

        {
            cal.setTime(df.parse("2015-10-01"));
            form.setCalendarProperty(cal);

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(1));
            assertThat(violations.iterator().next().getMessage(), is(String
                    .format(MESSAGE_VALIDATION_ERROR, "2015年10月01日")));
        }

        {
            cal.setTime(df.parse("2015-09-30"));
            form.setCalendarProperty(cal);

            violations = validator.validate(form, SpecifyFormat.class);
            assertThat(violations.size(), is(0));
        }
    }

    /**
     * for {@link Calendar} property. specify illegal format and value. expected {@code ValidationException} caused by
     * {@code IllegalArgumentException} that message is {@code failed to initialize validator by invalid argument}.
     * @throws Throwable
     */
    @Test
    public void testSpecifyIllegalFormatForCalendar() throws Throwable {
        setExpectedFailedToInitialize(ParseException.class);

        cal.setTime(df.parse("2015-10-01"));
        form.setCalendarProperty(cal);

        validator.validate(form, IllegalFormat.class);
    }

    /**
     * specify not support type. expected {@code ValidationException} caused by {@code IllegalArgumentException} that message is
     * {@code validator does not support this type: java.lang.String}
     * @throws Throwable
     */
    @Test
    public void testAnnotateUnexpectedType() throws Throwable {
        setExpectedTypeNotSupport(String.class);

        form.setOtherProperty("2015-10-01");

        validator.validate(form, UnexpectedType.class);
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

    /**
     * Validation group unexpected type.
     */
    private static interface UnexpectedType {
    };

    @Data
    public class BeforeTestForm {
        @Before.List({
                @Before("2015-10-01"),
                @Before(value = "2015年10月01日", format = "yyyy年MM月dd日", groups = { SpecifyFormat.class }),
                @Before(value = "2015-10-01", format = "yyyy年MM月dd日", groups = { IllegalFormat.class }) })
        private Date dateProperty;

        @Before.List({
                @Before("2015-10-01"),
                @Before(value = "2015年10月01日", format = "yyyy年MM月dd日", groups = { SpecifyFormat.class }),
                @Before(value = "2015-10-01", format = "yyyy年MM月dd日", groups = { IllegalFormat.class }) })
        private Calendar calendarProperty;

        @Before(value = "2015-10-01", groups = { UnexpectedType.class })
        private String otherProperty;
    }
}
