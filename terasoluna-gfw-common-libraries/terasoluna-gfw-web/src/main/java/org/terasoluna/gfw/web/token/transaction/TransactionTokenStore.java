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
 * Interface representing the store for {@link TransactionToken}
 */
public interface TransactionTokenStore {

    /**
     * Fetches the token string of the {@link TransactionToken} instance passed as argument, from the token store and clears its
     * value part from the store
     * @param token {@link TransactionToken} instance (must not be null)
     * @return token string corresponding
     * @throws IllegalArgumentException token is null
     */
    String getAndClear(TransactionToken token);

    /**
     * Removes the {@link TransactionToken} altogether from the token store
     * @param token {@link TransactionToken} instance (must not be null)
     * @throws IllegalArgumentException token is null
     */
    void remove(TransactionToken token);

    /**
     * create token key to store from token name
     * @param tokenName token name (namespace)
     * @return key created key
     */
    String createAndReserveTokenKey(String tokenName);

    /**
     * Stores the {@link TransactionToken} altogether in the token store
     * @param token {@link TransactionToken} instance (must not be null)
     * @throws IllegalArgumentException token is null
     */
    void store(TransactionToken token);
}
