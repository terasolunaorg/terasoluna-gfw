package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

public class LocalTimeTypeHandlerTest {

    private LocalTimeTypeHandler testTarget = new LocalTimeTypeHandler();

    @Test
    public void setNonNullParameterValuVariation1() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 1;
        LocalTime parameter = new LocalTime(0, 0, 0, 1);

        // test
        testTarget.setNonNullParameter(ps, position, parameter, JdbcType.DATE);

        // assert
        verify(ps).setTime(
                position,
                new Time(new DateTime(1970, 1, 1, parameter.getHourOfDay(),
                        parameter.getMinuteOfHour(), parameter
                                .getSecondOfMinute(), parameter
                                .getMillisOfSecond()).getMillis()));

    }

    @Test
    public void setNonNullParameterValuVariation2() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 2;
        LocalTime parameter = new LocalTime(1, 2, 3, 4);

        // test
        testTarget.setNonNullParameter(ps, position, parameter, JdbcType.DATE);

        // assert
        verify(ps).setTime(
                position,
                new Time(new DateTime(1970, 1, 1, parameter.getHourOfDay(),
                        parameter.getMinuteOfHour(), parameter
                                .getSecondOfMinute(), parameter
                                .getMillisOfSecond()).getMillis()));

    }

    @Test
    public void getNullableResult_ResultSetColumnName() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Time returnTime = new Time(
                new DateTime(1970, 1, 1, 0, 1, 2, 3).getMillis());
        when(rs.getTime(columnName)).thenReturn(returnTime);

        // test
        LocalTime actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is(new LocalTime(returnTime.getTime())));

    }

    @Test
    public void getNullableResult_ResultSetColumnName_returnNull()
            throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Time returnDate = null;
        when(rs.getTime(columnName)).thenReturn(returnDate);

        // test
        LocalTime actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is((LocalTime) null));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Time returnTime = new Time(
                new DateTime(1970, 1, 1, 1, 2, 3, 4).getMillis());
        when(rs.getTime(position)).thenReturn(returnTime);

        // test
        LocalTime actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is(new LocalTime(returnTime.getTime())));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex_returnNull()
            throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Time returnTime = null;
        when(rs.getTime(position)).thenReturn(returnTime);

        // test
        LocalTime actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is((LocalTime) null));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Time returnTime = new Time(
                new DateTime(1970, 1, 1, 23, 59, 59, 999).getMillis());
        when(cs.getTime(position)).thenReturn(returnTime);

        // test
        LocalTime actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is(new LocalTime(returnTime.getTime())));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex_returnNull()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Time returnTime = null;
        when(cs.getTime(position)).thenReturn(returnTime);

        // test
        LocalTime actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is((LocalTime) null));

    }
}
