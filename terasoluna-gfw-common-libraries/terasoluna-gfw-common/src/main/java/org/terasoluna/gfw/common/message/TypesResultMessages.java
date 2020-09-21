/*
 * Copyright(c) 2013 NTT DATA Corporation.
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
package org.terasoluna.gfw.common.message;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.terasoluna.gfw.common.message.StandardResultMessageType.INFO;

/**
 * Types Messages which have {@link ResultMessageType} and map of {@link ResultMessages}
 */
public class TypesResultMessages implements Serializable {
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1397838425857595762L;

    /**
     * main type
     */
    private final ResultMessageType mainType;

    /**
     * messages map
     */
    private final Map<ResultMessageType, ResultMessages> map = new HashMap<ResultMessageType, ResultMessages>();

    /**
     * default type
     */
    private static final ResultMessageType DEFAULT_TYPE = INFO;

    /**
     * default attribute name for TypesResultMessages
     */
    public static final String DEFAULT_TYPES_MESSAGES_ATTRIBUTE_NAME = StringUtils
            .uncapitalize(TypesResultMessages.class.getSimpleName());

    /**
     * Constructor.
     * @param typesMessages types messages to add
     */
    public TypesResultMessages(ResultMessages... typesMessages) {
        this(null, typesMessages);
    }

    /**
     * Constructor.
     * @param mainType main type
     * @param typesMessages types messages to add
     */
    public TypesResultMessages(ResultMessageType mainType, ResultMessages... typesMessages) {
        this.mainType = Objects.isNull(mainType) ? DEFAULT_TYPE : mainType;
        addAll(typesMessages);
    }

    /**
     * returns main type
     * @return main type
     */
    public ResultMessageType getMainType() {
        return mainType;
    }

    /**
     * add all types resultMessages (excludes <code>null</code> types resultMessages)<br>
     * <p>
     * if <code>types resultMessages</code> is <code>null</code>, no type resultMessages is added.
     * </p>
     * @param typesMessages types resultMessages to add
     * @return this result types messages
     */
    public TypesResultMessages addAll(ResultMessages... typesMessages) {
        Assert.notNull(typesMessages, "types resultMessages must not be null");
        pushAll(typesMessages);
        return this;
    }

    /**
     * returns map
     * @return map
     */
    public Map<ResultMessageType, ResultMessages> getMap() {
        return map;
    }

    /**
     * Outputs main type in this {@code TypesResultMessages} and the map of messages itself
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TypesResultMessages [main type=" + mainType + ", map=" + map + "]";
    }

    /**
     * push messages(batch).
     * invoke this method's resultMessages instance must not be null.
     * @param typesMessages types resultMessages instances
     */
    private void pushAll(ResultMessages... typesMessages) {
        for (ResultMessages resultMessages : typesMessages) {
            add(resultMessages);
        }
    }

    /**
     * add a ResultMessages
     * @param resultMessages ResultMessages instance
     * @return this result types messages
     */
    public TypesResultMessages add(ResultMessages resultMessages) {
        if (Objects.nonNull(resultMessages)) {
            push(resultMessages);
        } else {
            throw new IllegalArgumentException("resultMessages must not be null");
        }
        return this;
    }

    /**
     * Returns <tt>value</tt> if this map contains a mapping for the type.
     *
     * @param   type   The type whose presence in this map is to be tested
     * @return <tt>value</tt> if this map contains a mapping for the type.
     */
    public ResultMessages get(Object type) {
        return map.get(type);
    }

    /**
     * push messages.
     * invoke this method's resultMessages instance must not be null.
     * @param resultMessages resultMessages instance
     */
    private void push(ResultMessages resultMessages) {
        initTypeNotExist(resultMessages.getType()).addAll(resultMessages.getList());
    }

    /**
     *  get or create a ResultMessages instance.
     * <p>
     * if <code>resultMessages</code> is <code>null</code>, create new ResultMessages instance is added.
     * </p>
     * <p>
     * if <code>resultMessages</code> is <code>instance</code>, return.
     * </p>
     * @param type message type
     * @return ResultMessages instance
     */
    private ResultMessages initTypeNotExist(ResultMessageType type) {
        ResultMessages resultMessages = get(type);
        if (Objects.isNull(resultMessages)) {
            resultMessages = new ResultMessages(type);
            map.put(type, resultMessages);
        }
        return resultMessages;
    }
}
