/*
 * Copyright (C) 2013-2016 NTT DATA Corporation
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
package org.terasoluna.gfw.web.token.transaction;

/**
 * Represents the possible types of {@link TransactionToken}
 */
public enum TransactionTokenType {

    /**
     * A {@link TransactionToken} of type {@link #NONE} is created when {@link TransactionTokenCheck} annotation is not found
     * for a particular handlerMethod. In this case, the token check functionality is not invoked
     */
    NONE(false, false, false),
    /**
     * Indicates beginning of the boundary of transaction token check. <br>
     * <p>
     * Transaction token check will not be performed in this type of {@link TransactionToken}. Only a new token will be created
     * and rendered to the view as a hidden element
     */
    BEGIN(false, true, false),
    /**
     * Indicates that the corresponding handler method is within the transaction boundary <br>
     * <p>
     * Transaction token check will be performed in this type of {@link TransactionToken}.
     */
    IN(true, true, false),
    /**
     * Indicates end of the boundary of transaction token check. <br>
     * <p>
     * Transaction token check will be performed in this type of {@link TransactionToken}.
     */
    END(true, false, false),
    /**
     * Indicates that the corresponding handler method is within the transaction boundary <br>
     * <p>
     * Transaction token check will be performed in this type of {@link TransactionToken},
     * but Transaction token is no update.<br>
     * To use if you want to take over the same transaction token between the same transaction.<br>
     * For example,This type is used in the method that does not return a transaction token, such as a file download.
     */
    CHECK(true, false, true),
    /**
     * Indicates that the corresponding handler method is without the transaction boundary <br>
     * <p>
     * Transaction token check will not be performed in this type of {@link TransactionToken},
     * and Transaction token is no update.
     * To use if you want to take over the same transaction token between the same transaction.<br>
     * For example,This type is used in the method that does not return a transaction token, such as a file download.
     */
    KEEP(false, false, true),
    /**
     * Indicates that the corresponding handler method is within the transaction boundary <br>
     * <p>
     * Transaction token check will not be performed in this type of {@link TransactionToken},
     * but Transaction token is updated.
     */
    UPDATE(false, true, true);

    /**
     * whether need to validate token
     */
    private boolean needValidate;

    /**
     * whether need to create token
     */
    private boolean needCreate;

    /**
     * whether need to take over token
     */
    private boolean needKeep;

    /**
     * depending on type of a {@link TransactionToken} instance decides whether it needs to be checked for correctness or to be
     * newly created.
     * @param needValidate, whether needs to checked for correctness
     * @param needCreate, whether it needs to be newly created
     * @param needKeep, whether need to take over token
     */
    TransactionTokenType(final boolean needValidate, final boolean needCreate,
            final boolean needKeep) {
        this.needValidate = needValidate;
        this.needCreate = needCreate;
        this.needKeep = needKeep;
    }

    /**
     * Indicates if a {@link TransactionToken} needs to be checked for correctness depending on it type
     * @return true if it has be to checked for correctness. otherwise, false
     */
    public boolean needValidate() {
        return needValidate;
    }

    /**
     * Indicates if a {@link TransactionToken} needs to be checked newly created
     * @return true if it has be to checked for correctness. otherwise, false
     */
    public boolean needCreate() {
        return needCreate;
    }

    /**
     * Indicates if a {@link TransactionToken} needs to take over token.
     * @return true take over token. otherwise, false
     */
    public boolean needKeep() {
        return needKeep;
    }
}