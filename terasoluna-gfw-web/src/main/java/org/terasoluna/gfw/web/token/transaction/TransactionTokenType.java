/*
 * Copyright (C) 2013-2016  NTT DATA Corporation
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
    NONE(false, false),
    /**
     * Indicates beginning of the boundary of transaction token check. <br>
     * <p>
     * Transaction token check will not be performed in this type of {@link TransactionToken}. Only a new token will be created
     * and rendered to the view as a hidden element
     */
    BEGIN(false, true),
    /**
     * Indicates that the corresponding handler method is within the transaction boundary <br>
     * <p>
     * Transaction token check will be performed in this type of {@link TransactionToken}.
     */
    IN(true, true),
    /**
     * Indicates end of the boundary of transaction token check. <br>
     * <p>
     * Transaction token check will be performed in this type of {@link TransactionToken}.
     */
    END(true, false);

    /**
     * whether need to validate token
     */
    private boolean needValidate;

    /**
     * whether need to create token
     */
    private boolean needCreate;

    /**
     * depending on type of a {@link TransactionToken} instance decides whether it needs to be checked for correctness or to be
     * newly created.
     * @param needValidate, whether needs to checked for correctness
     * @param needCreate, whether it needs to be newly created
     */
    TransactionTokenType(final boolean needValidate, final boolean needCreate) {
        this.needValidate = needValidate;
        this.needCreate = needCreate;
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

}
