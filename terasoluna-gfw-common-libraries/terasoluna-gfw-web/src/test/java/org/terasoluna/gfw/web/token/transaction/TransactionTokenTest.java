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

public class TransactionTokenTest {

    @Test
    public void TestTransactionToken01() {
        // setup expected result
        String expectedTokenName = "aaa";
        String expectedTokenKey = "123";
        String expectedTokenValue = "456";

        // run
        TransactionToken token = new TransactionToken("aaa~123~456");

        // assert
        String resultTokenName = token.getTokenName();
        String resultTokenKey = token.getTokenKey();
        String resultTokenValue = token.getTokenValue();

        assertThat(resultTokenName).isEqualTo(expectedTokenName);
        assertThat(resultTokenKey).isEqualTo(expectedTokenKey);
        assertThat(resultTokenValue).isEqualTo(expectedTokenValue);
    }

    @Test
    public void TestTransactionToken02() {
        // setup expected result
        String expectedTokenName = "aaa";
        String expectedTokenKey = "123";
        String expectedTokenValue = "456";

        // run
        TransactionToken token = new TransactionToken("aaa", "123", "456");

        // assert
        String resultTokenName = token.getTokenName();
        String resultTokenKey = token.getTokenKey();
        String resultTokenValue = token.getTokenValue();

        assertThat(resultTokenName).isEqualTo(expectedTokenName);
        assertThat(resultTokenKey).isEqualTo(expectedTokenKey);
        assertThat(resultTokenValue).isEqualTo(expectedTokenValue);
    }

    @Test
    public void testValid01() {
        // run
        TransactionToken token = new TransactionToken("aaa", "123", "456");
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(true);
    }

    @Test
    public void testValid02() {
        // run
        TransactionToken token = new TransactionToken("aaa", "123", "");
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testValid03() {
        // run
        TransactionToken token = new TransactionToken("aaa", "123", null);
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testValid04() {
        // run
        TransactionToken token = new TransactionToken("aaa", "", "456");
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testValid05() {
        // run
        TransactionToken token = new TransactionToken("aaa", null, "456");
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testValid06() {
        // run
        TransactionToken token = new TransactionToken("", "123", "456");
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testValid07() {
        // run
        TransactionToken token = new TransactionToken(null, "123", "456");
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testValid08() {
        // run
        TransactionToken token = new TransactionToken(null, null, null);
        boolean valid = token.valid();

        // assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void testToString() {
        // run
        TransactionToken token = new TransactionToken("tokenName", "tokenKey", "tokenValue");
        String result = token.getTokenString();

        // assert
        assertThat(result).isEqualTo(token.getTokenName() + "~" + token.getTokenKey() + "~" + token.getTokenValue());
    }
}
