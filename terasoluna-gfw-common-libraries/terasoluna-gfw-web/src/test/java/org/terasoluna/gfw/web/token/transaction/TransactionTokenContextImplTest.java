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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenContextImpl.ReserveCommand;

public class TransactionTokenContextImplTest {

    @Test
    public void TestTransactionTokenContextImpl01() {

        // setup parameters
        TransactionTokenInfo beginTransactionToken =
                new TransactionTokenInfo("testTokenAttribute1", TransactionTokenType.BEGIN);
        TransactionToken receivedToken = new TransactionToken("aaa");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.CREATE_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(beginTransactionToken, receivedToken);

        // test
        ReserveCommand resultCommand = contextImpl.getReserveCommand();

        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(beginTransactionToken);
    }

    @Test
    public void TestTransactionTokenContextImpl02() {

        // setup parameters
        TransactionTokenInfo beginTransactionToken =
                new TransactionTokenInfo("testTokenAttribute1", TransactionTokenType.BEGIN);
        TransactionToken receivedToken = new TransactionToken("aaa", "key", "value");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.UPDATE_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(beginTransactionToken, receivedToken);

        // test
        ReserveCommand resultCommand = contextImpl.getReserveCommand();

        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(beginTransactionToken);
    }

    @Test
    public void TestTransactionTokenContextImpl03() {

        // setup parameters
        TransactionTokenInfo inTransactionToken =
                new TransactionTokenInfo("testTokenAttribute2", TransactionTokenType.IN);
        TransactionToken receivedToken = new TransactionToken("bbb");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.CREATE_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(inTransactionToken, receivedToken);
        ReserveCommand resultCommand = contextImpl.getReserveCommand();

        // test
        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(inTransactionToken);

    }

    @Test
    public void TestTransactionTokenContextImpl04() {

        // setup parameters
        TransactionTokenInfo inTransactionToken =
                new TransactionTokenInfo("testTokenAttribute2", TransactionTokenType.IN);
        TransactionToken receivedToken = new TransactionToken("bbb", "key", "value");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.UPDATE_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(inTransactionToken, receivedToken);
        ReserveCommand resultCommand = contextImpl.getReserveCommand();

        // test
        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(inTransactionToken);

    }

    @Test
    public void TestTransactionTokenContextImpl05() {

        // setup parameters
        TransactionTokenInfo endTransactionToken =
                new TransactionTokenInfo("testTokenAttribute3", TransactionTokenType.END);
        TransactionToken receivedToken = new TransactionToken("ccc");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.NONE;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(endTransactionToken, receivedToken);

        // test
        ReserveCommand resultCommand = contextImpl.getReserveCommand();
        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(endTransactionToken);
    }

    @Test
    public void TestTransactionTokenContextImpl06() {

        // setup parameters
        TransactionTokenInfo endTransactionToken =
                new TransactionTokenInfo("testTokenAttribute3", TransactionTokenType.END);
        TransactionToken receivedToken = new TransactionToken("ccc", "key", "value");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.REMOVE_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(endTransactionToken, receivedToken);

        // test
        ReserveCommand resultCommand = contextImpl.getReserveCommand();
        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(endTransactionToken);
    }

    @Test
    public void testTransactionTokenContextImpl07() {

        // setup parameters
        TransactionTokenInfo checkTransactionToken =
                new TransactionTokenInfo("checkToken", TransactionTokenType.CHECK);
        TransactionToken receivedToken = new TransactionToken("namespace", "key", "value");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.KEEP_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(checkTransactionToken, receivedToken);

        // test
        ReserveCommand resultCommand = contextImpl.getReserveCommand();
        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(checkTransactionToken);
    }

    @Test
    public void testTransactionTokenContextImpl08() {

        // setup parameters
        TransactionTokenInfo updateTransactionToken =
                new TransactionTokenInfo("updateToken", TransactionTokenType.CHECK);
        TransactionToken receivedToken = new TransactionToken("namespace", "key", "");

        // setup up expected result
        ReserveCommand expectedCommand = ReserveCommand.KEEP_TOKEN;

        // run
        TransactionTokenContextImpl contextImpl =
                new TransactionTokenContextImpl(updateTransactionToken, receivedToken);

        // test
        ReserveCommand resultCommand = contextImpl.getReserveCommand();
        assertThat(resultCommand).isEqualTo(expectedCommand);
        assertThat(contextImpl.getReceivedToken()).isEqualTo(receivedToken);
        assertThat(contextImpl.getTokenInfo()).isEqualTo(updateTransactionToken);
    }
}
