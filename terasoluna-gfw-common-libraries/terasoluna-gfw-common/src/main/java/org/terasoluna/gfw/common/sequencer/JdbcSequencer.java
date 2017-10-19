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
package org.terasoluna.gfw.common.sequencer;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * Implementation class for the Sequencing Functionality Uses JDBC to query the database to fetch the next value in a sequence
 * @param <T> type of sequence value
 */
public class JdbcSequencer<T> implements Sequencer<T>, InitializingBean {

    /**
     * DataSource information to access the database. must not be <code>null</code>
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Query for fetching next value of the sequence. must not be empty
     */
    private String nextValueQuery;

    /**
     * Query for fetching current value of the sequence. must not be empty
     */
    private String currentValueQuery;

    /**
     * Type of the class in which the next value of the sequence is to be returned. must not be <code>null</code>
     */
    private Class<T> sequenceClass;

    /**
     * fetches the next value in a sequence by executing the query
     * @return T next value in the sequence
     * @see org.terasoluna.gfw.common.sequencer.Sequencer#getNext()
     */
    @Override
    public T getNext() {
        T seq = jdbcTemplate.queryForObject(nextValueQuery, sequenceClass);
        return seq;
    }

    /**
     * fetches the current value in a sequence by executing the query
     * @return T current value in the sequence
     * @see org.terasoluna.gfw.common.sequencer.Sequencer#getCurrent()
     */
    @Override
    public T getCurrent() {
        T seq = jdbcTemplate.queryForObject(currentValueQuery, sequenceClass);
        return seq;
    }

    /**
     * Sets the query which is executed to fetch the next value in a sequence. must not be empty
     * @param nextValueQuery query used for fetching the next value in the sequence
     */
    public void setNextValueQuery(String nextValueQuery) {
        this.nextValueQuery = nextValueQuery;
    }

    /**
     * Sets the query which is executed to fetch the current value in a sequence. must not be empty
     * @param currentValueQuery query used for fetching the current value in the sequence
     */
    public void setCurrentValueQuery(String currentValueQuery) {
        this.currentValueQuery = currentValueQuery;
    }

    /**
     * Sets the type of the class whose instance is returned as the next value in the sequence. must not be null
     * @param sequenceClass java type of sequence value
     */
    public void setSequenceClass(Class<T> sequenceClass) {
        this.sequenceClass = sequenceClass;
    }

    /**
     * Sets the DataSource information used for accessing the database for fetching the next value of the sequence. must not be
     * null
     * @param dataSource DataSource instance for fetching a sequence value
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Sets JdbcTemplate directly. must not be null
     * @param jdbcTemplate JdbcTemplate instance for fetch a sequence value
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * <p>
     * check properties. throw IllegalArgumentException under the following conditions
     * </p>
     * <ul>
     * <li>{@link #jdbcTemplate} is null</li>
     * <li>{@link #nextValueQuery} is empty</li>
     * <li>{@link #currentValueQuery} is empty</li>
     * <li>{@link #sequenceClass} is null</li>
     * </ul>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws IllegalArgumentException {
        Assert.notNull(jdbcTemplate, "jdbcTemplate must not be null");
        Assert.hasLength(nextValueQuery, "nextValueQuery must not be empty");
        Assert.hasLength(currentValueQuery,
                "currentValueQuery must not be empty");
        Assert.notNull(sequenceClass, "sequenceClass must not be null");
    }
}
