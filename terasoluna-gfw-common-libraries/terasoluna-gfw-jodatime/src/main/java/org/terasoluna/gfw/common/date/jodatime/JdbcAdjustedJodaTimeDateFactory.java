/*
 * Copyright (C) 2013-2017 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.common.date.jodatime;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Concrete Implementation class of {@link org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory}.
 * <P>
 * Adds an adjustment value to current system date. <br>
 * Database is used to store the adjustment value to current system date. <br>
 * Depending on the settings, the adjustment value can also be cached. <br>
 * </P>
 * @since 5.0.0
 */
public class JdbcAdjustedJodaTimeDateFactory extends AbstractJodaTimeDateFactory
                                             implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(
            JdbcAdjustedJodaTimeDateFactory.class);

    /**
     * JDBC Template used to access the database to fetch the adjustment value.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * SQL query used to access the database
     */
    private String adjustedValueQuery;

    /**
     * Used to configure whether to cache the adjustment value
     */
    private volatile boolean useCache = false;

    /**
     * Adjustment value which is currently stored as cache
     */
    private final AtomicLong cachedAdjustedValue = new AtomicLong(0);

    /**
     * Returns the adjusted current system {@link org.joda.time.DateTime}
     * <P>
     * Adjustment value can come from DB or cached adjustment value can <br>
     * be used to adjust the current system DateTime <br>
     * configuration.<br>
     * @return DateTime adjusted current DateTime
     */
    @Override
    public DateTime newDateTime() {
        long adjustedValue;
        if (isUseCache()) {
            adjustedValue = cachedAdjustedValue.get();
        } else {
            adjustedValue = reload();
        }
        return new DateTime().plus(adjustedValue);
    }

    /**
     * Reloads the adjustment value from the database.
     * <P>
     * Depending on the settings, the reloaded fresh value will also be cached.
     * <P>
     * @return long adjustment value
     */
    public long reload() {
        Long adjustedValue = jdbcTemplate.queryForObject(adjustedValueQuery,
                Long.class);
        if (adjustedValue == null) {
            adjustedValue = cachedAdjustedValue.get();
            logger.warn("adjusted value is null. use {}", adjustedValue);
        } else if (isUseCache()) {
            logger.debug("cache adjusted value = {}", adjustedValue);
            cachedAdjustedValue.set(adjustedValue);
        }

        return adjustedValue;
    }

    /**
     * Reloads the adjustment value from the database.
     * <P>
     * Depending on the settings, the reloaded fresh value will also be cached.
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(jdbcTemplate, "jdbcTemplate must not be null");
        Assert.hasLength(adjustedValueQuery,
                "adjustedValueQuery must not be empty");
        reload();
    }

    /**
     * Sets JDBC Template from DataSource
     * @param dataSource dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Sets JDBC Template
     * @param jdbcTemplate jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Sets SQL query to fetch Adjustment value.
     * @param adjustedValueQuery SQL query to fetch Adjustment value
     */
    public void setAdjustedValueQuery(String adjustedValueQuery) {
        this.adjustedValueQuery = adjustedValueQuery;
    }

    /**
     * Sets the attribute that specifies whether to cache the adjustment value
     * @param useCache Attribute that specifies whether to cache the adjustment value
     */
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * Fetches the attribute that specifies whether to cache the adjustment value
     * @return boolean Attribute that specifies whether to cache the adjustment value
     */
    public boolean isUseCache() {
        return useCache;
    }
}
