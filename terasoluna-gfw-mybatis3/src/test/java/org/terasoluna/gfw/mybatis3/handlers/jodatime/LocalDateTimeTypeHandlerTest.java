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
import org.joda.time.LocalDateTime;
import org.junit.Test;

public class LocalDateTimeTypeHandlerTest {

    private LocalDateTimeTypeHandler testTarget = new LocalDateTimeTypeHandler();

    @Test
    public void setNonNullParameterValuVariation1() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 1;
        LocalDateTime parameter = new LocalDateTime(2014, 2, 28, 1, 2, 3, 4);

        // test
        testTarget.setNonNullParameter(ps, position, parameter,
                JdbcType.TIMESTAMP);

        // assert
        verify(ps).setTimestamp(position,
                new Timestamp(parameter.toDate().getTime()));

    }

    @Test
    public void setNonNullParameterValuVariation2() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 2;
        LocalDateTime parameter = new LocalDateTime(2016, 2, 29, 5, 6, 7, 8);

        // test
        testTarget.setNonNullParameter(ps, position, parameter,
                JdbcType.TIMESTAMP);

        // assert
        verify(ps).setTimestamp(position,
                new Timestamp(parameter.toDate().getTime()));

    }

    @Test
    public void getNullableResult_ResultSetColumnName() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Timestamp returnTimestamp = new Timestamp(new LocalDateTime(2014, 3, 4,
                5, 6, 7, 8).toDate().getTime());
        when(rs.getTimestamp(columnName)).thenReturn(returnTimestamp);

        // test
        LocalDateTime actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is(new LocalDateTime(returnTimestamp.getTime())));

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
        LocalDateTime actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is((LocalDateTime) null));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Timestamp returnTimestamp = new Timestamp(new LocalDateTime(2014, 4, 5,
                6, 7, 8, 9).toDate().getTime());
        when(rs.getTimestamp(position)).thenReturn(returnTimestamp);

        // test
        LocalDateTime actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is(new LocalDateTime(returnTimestamp.getTime())));

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
        LocalDateTime actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is((LocalDateTime) null));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Timestamp returnTimestamp = new Timestamp(new LocalDateTime(2014, 4, 5,
                6, 7, 8, 9).toDate().getTime());
        when(cs.getTimestamp(position)).thenReturn(returnTimestamp);

        // test
        LocalDateTime actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is(new LocalDateTime(returnTimestamp.getTime())));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex_returnNull()
            throws SQLException {
        // setup test data

        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Timestamp returnTimestamp = null;
        when(cs.getTimestamp(position)).thenReturn(returnTimestamp);

        LocalDateTime actual = testTarget.getNullableResult(cs, position);

        assertThat(actual, is((LocalDateTime) null));

    }
}
