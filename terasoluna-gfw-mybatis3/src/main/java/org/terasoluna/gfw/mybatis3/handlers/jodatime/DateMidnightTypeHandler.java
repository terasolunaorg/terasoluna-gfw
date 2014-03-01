package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.DateMidnight;

@MappedTypes(DateMidnight.class)
public class DateMidnightTypeHandler extends BaseTypeHandler<DateMidnight> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            DateMidnight parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getMillis()));
    }

    @Override
    public DateMidnight getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return toDateMidnight(rs.getTimestamp(columnName));
    }

    @Override
    public DateMidnight getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return toDateMidnight(rs.getTimestamp(columnIndex));
    }

    @Override
    public DateMidnight getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return toDateMidnight(cs.getTimestamp(columnIndex));
    }

    private DateMidnight toDateMidnight(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return newDateMidnight(timestamp);
        }
    }

    protected DateMidnight newDateMidnight(Timestamp timestamp) {
        return new DateMidnight(timestamp.getTime());
    }

}
