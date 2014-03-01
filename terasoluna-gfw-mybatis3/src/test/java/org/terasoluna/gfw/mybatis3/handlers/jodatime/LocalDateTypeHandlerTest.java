package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.joda.time.LocalDate;
import org.junit.Test;

public class LocalDateTypeHandlerTest {

    private LocalDateTypeHandler testTarget = new LocalDateTypeHandler();

    @Test
    public void setNonNullParameterValuVariation1() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 1;
        LocalDate parameter = new LocalDate(2014, 2, 28);

        // test
        testTarget.setNonNullParameter(ps, position, parameter, JdbcType.DATE);

        // assert
        verify(ps).setDate(position, new Date(parameter.toDate().getTime()));

    }

    @Test
    public void setNonNullParameterValuVariation2() throws SQLException {

        // setup test data
        PreparedStatement ps = mock(PreparedStatement.class);
        int position = 2;
        LocalDate parameter = new LocalDate(2016, 2, 29);

        // test
        testTarget.setNonNullParameter(ps, position, parameter, JdbcType.DATE);

        // assert
        verify(ps).setDate(position, new Date(parameter.toDate().getTime()));

    }

    @Test
    public void getNullableResult_ResultSetColumnName() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Date returnDate = new Date(LocalDate.parse("2015-02-27").toDate()
                .getTime());
        when(rs.getDate(columnName)).thenReturn(returnDate);

        // test
        LocalDate actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is(new LocalDate(returnDate.getTime())));

    }

    @Test
    public void getNullableResult_ResultSetColumnName_returnNull()
            throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        String columnName = "lastModifiedAt";
        Date returnDate = null;
        when(rs.getDate(columnName)).thenReturn(returnDate);

        // test
        LocalDate actual = testTarget.getNullableResult(rs, columnName);

        // assert
        assertThat(actual, is((LocalDate) null));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex() throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Date returnDate = new Date(LocalDate.parse("2014-02-27").toDate()
                .getTime());
        when(rs.getDate(position)).thenReturn(returnDate);

        // test
        LocalDate actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is(new LocalDate(returnDate.getTime())));

    }

    @Test
    public void getNullableResult_ResultSetColumnIndex_returnNull()
            throws SQLException {
        // setup test data
        ResultSet rs = mock(ResultSet.class);
        int position = 1;
        Date returnDate = null;
        when(rs.getDate(position)).thenReturn(returnDate);

        // test
        LocalDate actual = testTarget.getNullableResult(rs, position);

        // assert
        assertThat(actual, is((LocalDate) null));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Date returnDate = new Date(LocalDate.parse("2014-02-26").toDate()
                .getTime());
        when(cs.getDate(position)).thenReturn(returnDate);

        // test
        LocalDate actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is(new LocalDate(returnDate.getTime())));

    }

    @Test
    public void getNullableResult_CallableStatementColumnIndex_returnNull()
            throws SQLException {
        // setup test data
        CallableStatement cs = mock(CallableStatement.class);
        int position = 1;
        Date returnDate = null;
        when(cs.getDate(position)).thenReturn(returnDate);

        // test
        LocalDate actual = testTarget.getNullableResult(cs, position);

        // assert
        assertThat(actual, is((LocalDate) null));

    }
}
