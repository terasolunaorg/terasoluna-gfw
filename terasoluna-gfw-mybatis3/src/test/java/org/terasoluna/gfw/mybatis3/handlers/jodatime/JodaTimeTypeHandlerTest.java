package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terasoluna.gfw.mybatis3.test.model.TestJodaTimeModel;
import org.terasoluna.gfw.mybatis3.test.repository.TestJodaTimeModelRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class JodaTimeTypeHandlerTest {

    @Inject
    TestJodaTimeModelRepository repository;

    @Test
    public void setDefaultTimeZone() {
        // setup
        DateTime now = new DateTime();
        TestJodaTimeModel model = new TestJodaTimeModel();
        model.setDateTime(now);
        model.setDateTimeDt(now);
        model.setDateTimeTm(now);
        model.setDateMidnight(now.toDateMidnight());
        model.setDateMidnightDt(now.toDateMidnight());
        model.setDateMidnightTm(now.toDateMidnight());
        model.setLocalDateTime(now.toLocalDateTime());
        model.setLocalDateTimeDt(now.toLocalDateTime());
        model.setLocalDateTimeTm(now.toLocalDateTime());
        model.setLocalDate(now.toLocalDate());
        model.setLocalTime(now.toLocalTime());

        // test
        repository.insert(model);
        TestJodaTimeModel loadedModel = repository.findOne(model.getId());

        // assert
        {
            assertThat(loadedModel.getDateTime(), is(model.getDateTime()));
            assertThat(loadedModel.getDateMidnight(),
                    is(model.getDateMidnight()));
            assertThat(loadedModel.getLocalDateTime(),
                    is(model.getLocalDateTime()));
            assertThat(loadedModel.getLocalDate(), is(model.getLocalDate()));
            assertThat(loadedModel.getLocalTime(), is(model.getLocalTime()));
        }
        {
            assertThat(loadedModel.getDateTimeDt(), is(model.getDateTimeDt()
                    .property(DateTimeFieldType.dayOfMonth()).roundFloorCopy()));
            assertThat(
                    loadedModel.getDateMidnightDt(),
                    is(model.getDateMidnightDt()
                            .property(DateTimeFieldType.dayOfMonth())
                            .roundFloorCopy()));
            assertThat(
                    loadedModel.getLocalDateTimeDt(),
                    is(model.getLocalDateTimeDt()
                            .property(DateTimeFieldType.dayOfMonth())
                            .roundFloorCopy()));
        }

        {
            assertThat(
                    loadedModel.getDateTimeTm(),
                    is(new DateTime(1970, 1, 1, 0, 0, 0, 0).plus(model
                            .getDateTimeTm().toLocalTime().getMillisOfDay())));
            assertThat(loadedModel.getDateMidnightTm(), is(new DateMidnight(
                    1970, 1, 1)));
            assertThat(loadedModel.getLocalDateTimeTm(), is(new LocalDateTime(
                    1970, 1, 1, 0, 0, 0, 0).plusMillis(model.getDateTimeTm()
                    .toLocalTime().getMillisOfDay())));
        }
    }

    @Test
    public void setNull() {
        // setup
        TestJodaTimeModel model = new TestJodaTimeModel();

        // test
        repository.insert(model);
        TestJodaTimeModel loadedModel = repository.findOne(model.getId());

        // assert
        {
            assertThat(loadedModel.getDateTime(), is(model.getDateTime()));
            assertThat(loadedModel.getDateMidnight(),
                    is(model.getDateMidnight()));
            assertThat(loadedModel.getLocalDateTime(),
                    is(model.getLocalDateTime()));
            assertThat(loadedModel.getLocalDate(), is(model.getLocalDate()));
            assertThat(loadedModel.getLocalTime(), is(model.getLocalTime()));
        }
        {
            assertThat(loadedModel.getDateTimeDt(), is(model.getDateTimeDt()));
            assertThat(loadedModel.getDateMidnightDt(),
                    is(model.getDateMidnightDt()));
            assertThat(loadedModel.getLocalDateTimeDt(),
                    is(model.getLocalDateTimeDt()));
        }
        {
            assertThat(loadedModel.getDateTimeTm(), is(model.getDateTimeTm()));
            assertThat(loadedModel.getDateMidnightTm(),
                    is(model.getDateMidnightTm()));
            assertThat(loadedModel.getLocalDateTimeTm(),
                    is(model.getLocalDateTimeTm()));
        }
    }

    @Test
    public void setUTC() {
        // set up
        DateTimeZone timeZone = DateTimeZone.UTC;
        DateTime now = new DateTime(timeZone);
        TestJodaTimeModel model = new TestJodaTimeModel();
        model.setDateTime(now);
        model.setDateTimeDt(now);
        model.setDateTimeTm(now);
        model.setDateMidnight(now.toDateMidnight());
        model.setDateMidnightDt(now.toDateMidnight());
        model.setDateMidnightTm(now.toDateMidnight());
        model.setLocalDateTime(now.toLocalDateTime());
        model.setLocalDateTimeDt(now.toLocalDateTime());
        model.setLocalDateTimeTm(now.toLocalDateTime());
        model.setLocalDate(now.toLocalDate());
        model.setLocalTime(now.toLocalTime());

        // test
        repository.insert(model);
        TestJodaTimeModel loadedModel = repository.findOne(model.getId());

        // assert
        {
            assertThat(loadedModel.getDateTime(), is(model.getDateTime()
                    .toDateTime(DateTimeZone.getDefault())));
            assertThat(loadedModel.getDateMidnight(), is(model
                    .getDateMidnight().toDateTime(DateTimeZone.getDefault())
                    .toDateMidnight()));

            assertThat(loadedModel.getLocalDateTime(),
                    is(model.getLocalDateTime()));
            assertThat(loadedModel.getLocalDate(), is(model.getLocalDate()));
            assertThat(loadedModel.getLocalTime(), is(model.getLocalTime()));

        }
        {
            assertThat(
                    loadedModel.getDateTimeDt(),
                    is(model.getDateTimeDt()
                            .toDateTime(DateTimeZone.getDefault())
                            .property(DateTimeFieldType.dayOfMonth())
                            .roundFloorCopy()));
            assertThat(loadedModel.getDateMidnightDt(), is(model
                    .getDateMidnightDt().toDateTime(DateTimeZone.getDefault())
                    .toDateMidnight().property(DateTimeFieldType.dayOfMonth())
                    .roundFloorCopy()));
            assertThat(
                    loadedModel.getLocalDateTimeDt(),
                    is(model.getLocalDateTimeDt()
                            .property(DateTimeFieldType.dayOfMonth())
                            .roundFloorCopy()));
        }
        {
            assertThat(
                    loadedModel.getDateTimeTm(),
                    is(new DateTime(1970, 1, 1, 0, 0, 0, 0).plus(model
                            .getDateTimeTm()
                            .toDateTime(DateTimeZone.getDefault())
                            .toLocalTime().getMillisOfDay())));
            assertThat(loadedModel.getDateMidnightTm(), is(new DateMidnight(
                    1970, 1, 1)));

            assertThat(loadedModel.getLocalDateTimeTm(), is(new LocalDateTime(
                    1970, 1, 1, 0, 0, 0, 0).plusMillis(model.getDateTimeTm()
                    .toLocalTime().getMillisOfDay())));
        }

    }

}
