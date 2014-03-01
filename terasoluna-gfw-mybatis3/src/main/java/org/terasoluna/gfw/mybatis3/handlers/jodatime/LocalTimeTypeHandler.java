package org.terasoluna.gfw.mybatis3.handlers.jodatime;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

@MappedTypes(LocalTime.class)
public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            LocalTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(
                i,
                new Time(new DateTime(1970, 1, 1, parameter.getHourOfDay(),
                        parameter.getMinuteOfHour(), parameter
                                .getSecondOfMinute(), parameter
                                .getMillisOfSecond()).getMillis()));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return toLocalTime(rs.getTime(columnName));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return toLocalTime(rs.getTime(columnIndex));
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return toLocalTime(cs.getTime(columnIndex));
    }

    private LocalTime toLocalTime(Time time) {
        if (time == null) {
            return null;
        } else {
            return newLocalTime(time);
        }
    }

    protected LocalTime newLocalTime(Time time) {
        return new LocalTime(time.getTime());
    }

}
