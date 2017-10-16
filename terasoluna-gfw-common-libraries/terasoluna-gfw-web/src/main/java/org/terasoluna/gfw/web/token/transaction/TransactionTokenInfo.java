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
package org.terasoluna.gfw.web.token.transaction;

/**
 * Contains meta-information about a created TransactionToken
 */
public class TransactionTokenInfo {

    /**
     * namespace of this transaction token
     */
    private final String tokenName;

    /**
     * Gives information regarding the type of {@link TransactionToken}
     */
    private final TransactionTokenType tokenType;

    /**
     * Constructor <br>
     * @param tokenName name part of the {@link TransactionToken} represented by this {@link TransactionTokenInfo} instance
     * @param tokenType type of the {@link TransactionToken} represented by this {@link TransactionTokenInfo} instance
     */
    public TransactionTokenInfo(final String tokenName,
            final TransactionTokenType tokenType) {
        this.tokenName = tokenName;
        this.tokenType = tokenType;
    }

    /**
     * Returns namespace of this transaction token
     * @return tokenName
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * Returns type of the {@link TransactionToken} represented by this {@link TransactionTokenInfo} instance
     * @return TransactionType
     */
    public TransactionTokenType getTransactionTokenType() {
        return tokenType;
    }

    /**
     * Checks whether the {@link TransactionToken} represented by this {@link TransactionTokenInfo} object needs to be checked
     * whether it is a valid token
     * @return true if needs to be checked if its a valid token. false otherwise
     */
    public boolean needValidate() {
        return tokenType.needValidate();
    }

    /**
     * Checks whether the a new token is to be created to be represented by this {@link TransactionTokenInfo} object
     * @return true if needs to be created. false otherwise
     */
    public boolean needCreate() {
        return tokenType.needCreate();
    }

    /**
     * Check whether the {@link TransactionToken} represented by this {@link TransactionTokenInfo} object needs to be taken
     * over.
     * @return {@code true} if need to be taken over token. {@code false} otherwise
     */
    public boolean needKeep() {
        return tokenType.needKeep();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TransactionTokenInfo [tokenName=" + tokenName
                + ", transitionType=" + tokenType + "]";
    }

}
