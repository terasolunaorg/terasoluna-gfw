package org.terasoluna.gfw.mybatis3.test.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.mybatis3.test.model.TestJodaTimeModel;

public interface TestJodaTimeModelRepository {

    @Transactional
    @Insert("INSERT INTO t_jodatime"
            + " (id, date_time, date_time_dt, date_time_tm"
            + ", date_midnight, date_midnight_dt, date_midnight_tm"
            + ", local_date_time, local_date_time_dt, local_date_time_tm"
            + ", local_date, local_time)" + " VALUES"
            + " (#{id}, #{dateTime}, #{dateTimeDt}, #{dateTimeTm}"
            + ", #{dateMidnight}, #{dateMidnightDt}, #{dateMidnightTm}"
            + ", #{localDateTime}, #{localDateTimeDt}, #{localDateTimeTm}"
            + ", #{localDate}, #{localTime})")
    @SelectKey(statement = "SELECT NEXTVAL('s_jodatime')", keyProperty = "id", before = true, resultType = Long.class)
    void insert(TestJodaTimeModel model);

    @Transactional(readOnly = true)
    @Select("SELECT * FROM t_jodatime WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "dateTime", column = "date_time"),
            @Result(property = "dateTimeDt", column = "date_time_dt"),
            @Result(property = "dateTimeTm", column = "date_time_tm"),
            @Result(property = "dateMidnight", column = "date_midnight"),
            @Result(property = "dateMidnightDt", column = "date_midnight_dt"),
            @Result(property = "dateMidnightTm", column = "date_midnight_tm"),
            @Result(property = "localDateTime", column = "local_date_time"),
            @Result(property = "localDateTimeDt", column = "local_date_time_dt"),
            @Result(property = "localDateTimeTm", column = "local_date_time_tm"),
            @Result(property = "localDate", column = "local_date"),
            @Result(property = "localTime", column = "local_time") })
    TestJodaTimeModel findOne(Long id);

}
