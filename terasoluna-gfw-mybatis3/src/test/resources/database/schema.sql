DROP TABLE IF EXISTS t_jodatime;
DROP SEQUENCE IF EXISTS s_jodatime;

CREATE TABLE t_jodatime (
    id BIGINT NOT NULL
    ,date_time TIMESTAMP
    ,date_time_dt DATE
    ,date_time_tm TIME
    ,date_midnight TIMESTAMP
    ,date_midnight_dt DATE
    ,date_midnight_tm TIME
    ,local_date_time TIMESTAMP
    ,local_date_time_dt DATE
    ,local_date_time_tm TIME
    ,local_date DATE
    ,local_time TIME
    ,CONSTRAINT pk_jodatime PRIMARY KEY (id)
);

CREATE SEQUENCE s_jodatime;