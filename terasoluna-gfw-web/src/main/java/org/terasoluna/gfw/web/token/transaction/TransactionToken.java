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

import java.io.Serializable;

/**
 * Class representing the transaction token string
 */
public class TransactionToken implements Serializable {

    /**
     * serial version UID.
     */
    private static final long serialVersionUID = -917084514722659745L;

    /**
     * separator to separate token name, key, value
     */
    static final String TOKEN_STRING_SEPARATOR = "~";

    /**
     * Name of token
     */
    private final String tokenName;

    /**
     * token key
     */
    private final String tokenKey;

    /**
     * token value
     */
    private final String tokenValue;

    /**
     * Single argument constructor <br>
     * <p>
     * A valid <code>tokenString</code> should be in the following form: <code>tokenName~tokenKey~tokenValue</code>. <br>
     * By default an <code>~ (tilde)</code> is used as a separator for the tokenString
     * @param tokenString
     */
    public TransactionToken(final String tokenString) {
        String tokenName = "";
        String tokenKey = "";
        String tokenValue = "";
        if (tokenString != null && !tokenString.isEmpty()) {
            String[] strs = tokenString.split(TOKEN_STRING_SEPARATOR);
            if (strs.length == 3) {
                tokenName = strs[0];
                tokenKey = strs[1];
                tokenValue = strs[2];
            }
        }
        this.tokenName = tokenName;
        this.tokenKey = tokenKey;
        this.tokenValue = tokenValue;
    }

    /**
     * Multiple argument constructor.
     * <p>
     * Receives token name, key and value separately. All the three form to make the token string in the following format:
     * {@code tokenName~tokenKey~tokenValue}
     * @param tokenName
     * @param tokenKey
     * @param tokenValue
     */
    public TransactionToken(final String tokenName, final String tokenKey,
            final String tokenValue) {
        this.tokenName = tokenName;
        this.tokenKey = tokenKey;
        this.tokenValue = tokenValue;
    }

    /**
     * Getter method for token name
     * @return String token name
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * Getter method for token key
     * @return String token key
     */
    public String getTokenKey() {
        return tokenKey;
    }

    /**
     * Getter method for token value
     * @return String token value
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * Checks whether the an instance of <code>TransactionToken</code> is valid. Which means all values are present for all the
     * three parts of <code>TransactionToken</code>
     * @return if all values are present, return <code>true</code>
     */
    public boolean valid() {
        if (tokenKey == null || tokenKey.isEmpty()) {
            return false;
        }
        if (tokenValue == null || tokenValue.isEmpty()) {
            return false;
        }
        if (tokenName == null || tokenName.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Returns the token string
     * @return token string
     */
    public String getTokenString() {
        return tokenName + TOKEN_STRING_SEPARATOR + tokenKey
                + TOKEN_STRING_SEPARATOR + tokenValue;
    }

}
