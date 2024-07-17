/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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
 * Stores the context information regarding a particular instance of <code>TransactionToken</code>.
 * <br>
 * <p>
 * Maintains the following information: <br>
 * 1) information defined by {@code @TransactionTokenCheck} attached to a request handler method.
 * <br>
 * 2) what action is to be taken for a <code>TransactionToken</code> at any point of time during the
 * lifetime of this token.<br>
 * 3) token received from the browser <br>
 */
public class TransactionTokenContextImpl implements TransactionTokenContext {

    /** Indicates the command to be executed pertaining to transactionToken */
    enum ReserveCommand {
        CREATE_TOKEN, UPDATE_TOKEN, REMOVE_TOKEN, KEEP_TOKEN, NONE
    };

    /** Meta-Information regarding TransactionToken */
    private final TransactionTokenInfo tokenInfo;

    private final TransactionToken receivedToken;

    private final ReserveCommand defaultCommand;

    /** Indicates the instruction to either create, update or remove a token. It can be no action */
    private ReserveCommand reserveCommand;

    /**
     * Constructor. Initializes <code>reserveCommand</code> on the basis of the argument
     * <code>receivedToken</code> <br>
     * @param tokenInfo transaction token information
     * @param receivedToken received token
     */
    public TransactionTokenContextImpl(final TransactionTokenInfo tokenInfo,
            TransactionToken receivedToken) {
        this.tokenInfo = tokenInfo;
        this.receivedToken = receivedToken;

        if (tokenInfo.needCreate()) {
            if (receivedToken.valid()) {
                defaultCommand = ReserveCommand.UPDATE_TOKEN;
            } else {
                defaultCommand = ReserveCommand.CREATE_TOKEN;
            }
        } else if (tokenInfo.needKeep()) {
            defaultCommand = ReserveCommand.KEEP_TOKEN;
        } else {
            if (receivedToken.valid()) {
                defaultCommand = ReserveCommand.REMOVE_TOKEN;
            } else {
                defaultCommand = ReserveCommand.NONE;
            }
        }
        reserveCommand = defaultCommand;
    }

    /**
     * Fetches <code>TransactionTokenInfo</code> instance
     * @return TransactionTokenInfo
     */
    public TransactionTokenInfo getTokenInfo() {
        return tokenInfo;
    }

    /**
     * returns reserved command<br>
     * @return reserved command
     */
    public ReserveCommand getReserveCommand() {
        return reserveCommand;
    }

    @Override
    public TransactionToken getReceivedToken() {
        return receivedToken;
    }
}
