package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.LocalDate;

@MappedTypes(LocalDate.class)
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setDate(i, new Date(parameter.toDate().getTime()));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return toLocalDate(rs.getDate(columnName));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return toLocalDate(rs.getDate(columnIndex));
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return toLocalDate(cs.getDate(columnIndex));
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return newLocalDate(date);
        }
    }

    protected LocalDate newLocalDate(Date date) {
        return new LocalDate(date.getTime());
    }

}
