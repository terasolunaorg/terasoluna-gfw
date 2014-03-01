package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateMidnight;
import org.junit.Test;

public class DateMidnightTypeHandlerTest {

    private DateMidnightTypeHandler testTarget = new DateMidnightTypeHandler();

    @Test
    public void setNonNullParameterValuVariation1() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 1;
        DateMidnight parameter = new DateMidnight(2014, 2, 28);

        // test
        testTarget.setNonNullParameter(ps, position, parameter, JdbcType.DATE);

        // assert
        verify(ps).setTimestamp(position, new Timestamp(parameter.getMillis()));

    }

    @Test
    public void setNonNullParameterValuVariation2() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 2;
        DateMidnight parameter = new DateMidnight(2016, 2, 29);

        // test
        testTarget.setNonNullParameter(ps, position, parameter, JdbcType.DATE);

        // assert
        verify(ps).setTimestamp(position, new Timestamp(parameter.getMillis()));

    }

    @Test
    public void getNullableResult_ResultSetColumnName() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Timestamp returnTimestamp = new Timestamp(DateMidnight.parse(
                "2015-02-27").getMillis());
        when(rs.getTimestamp(columnName)).thenReturn(returnTimestamp);

        // test
        DateMidnight actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is(new DateMidnight(returnTimestamp.getTime())));

    }

    @Test
    public void getNullableResult_ResultSetColumnName_returnNull()
            throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Timestamp returnTimestamp = null;
        when(rs.getTimestamp(columnName)).thenReturn(returnTimestamp);

        // test
        DateMidnight actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is((DateMidnight) null));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Timestamp returnTimestamp = new Timestamp(DateMidnight.parse(
                "2014-02-27").getMillis());
        when(rs.getTimestamp(position)).thenReturn(returnTimestamp);

        // test
        DateMidnight actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is(new DateMidnight(returnTimestamp.getTime())));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex_returnNull()
            throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Timestamp returnTimestamp = null;
        when(rs.getTimestamp(position)).thenReturn(returnTimestamp);

        // test
        DateMidnight actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is((DateMidnight) null));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Timestamp returnTimestamp = new Timestamp(DateMidnight.parse(
                "2014-02-26").getMillis());
        when(cs.getTimestamp(position)).thenReturn(returnTimestamp);

        // test
        DateMidnight actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is(new DateMidnight(returnTimestamp.getTime())));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex_returnNull()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Timestamp returnTimestamp = null;
        when(cs.getTimestamp(position)).thenReturn(returnTimestamp);

        // test
        DateMidnight actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is((DateMidnight) null));

    }
}
