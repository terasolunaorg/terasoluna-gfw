package org.terasoluna.gfw.mybatis3.test.model;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class TestJodaTimeModel {
    private Long id;
    private DateTime dateTime;
    private DateTime dateTimeDt;
    private DateTime dateTimeTm;
    private DateMidnight dateMidnight;
    private DateMidnight dateMidnightDt;
    private DateMidnight dateMidnightTm;
    private LocalDateTime localDateTime;
    private LocalDateTime localDateTimeDt;
    private LocalDateTime localDateTimeTm;
    private LocalDate localDate;
    private LocalTime localTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTimeDt() {
        return dateTimeDt;
    }

    public void setDateTimeDt(DateTime dateTimeDt) {
        this.dateTimeDt = dateTimeDt;
    }

    public DateTime getDateTimeTm() {
        return dateTimeTm;
    }

    public void setDateTimeTm(DateTime dateTimeTm) {
        this.dateTimeTm = dateTimeTm;
    }

    public DateMidnight getDateMidnight() {
        return dateMidnight;
    }

    public void setDateMidnight(DateMidnight dateMidnight) {
        this.dateMidnight = dateMidnight;
    }

    public DateMidnight getDateMidnightDt() {
        return dateMidnightDt;
    }

    public void setDateMidnightDt(DateMidnight dateMidnightDt) {
        this.dateMidnightDt = dateMidnightDt;
    }

    public DateMidnight getDateMidnightTm() {
        return dateMidnightTm;
    }

    public void setDateMidnightTm(DateMidnight dateMidnightTm) {
        this.dateMidnightTm = dateMidnightTm;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTimeDt() {
        return localDateTimeDt;
    }

    public void setLocalDateTimeDt(LocalDateTime localDateTimeDt) {
        this.localDateTimeDt = localDateTimeDt;
    }

    public LocalDateTime getLocalDateTimeTm() {
        return localDateTimeTm;
    }

    public void setLocalDateTimeTm(LocalDateTime localDateTimeTm) {
        this.localDateTimeTm = localDateTimeTm;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

}
