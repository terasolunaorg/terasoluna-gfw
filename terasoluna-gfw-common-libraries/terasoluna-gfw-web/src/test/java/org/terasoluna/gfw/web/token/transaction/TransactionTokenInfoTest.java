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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransactionTokenInfoTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void TestTransactionTokenInfo01() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.BEGIN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);

        // assert
        assertThat(tokenName, is(info.getTokenName()));
        assertThat(beginToken, is(info.getTransactionTokenType()));
    }

    @Test
    public void TestNeedCreate01() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.BEGIN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needCreate();
        // assert
        assertThat(output, is(true));
    }

    @Test
    public void TestNeedCreate02() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.IN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needCreate();
        // assert
        assertThat(output, is(true));
    }

    @Test
    public void TestNeedCreate03() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.END;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needCreate();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void TestNeedValidate01() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.BEGIN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needValidate();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void TestNeedValidate02() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.IN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needValidate();
        // assert
        assertThat(output, is(true));
    }

    @Test
    public void TestNeedValidate03() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.END;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needValidate();
        // assert
        assertThat(output, is(true));
    }

    @Test
    public void TestNeedCreate04() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.END;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needCreate();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void TestNeedValidate04() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType beginToken = TransactionTokenType.END;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        boolean output = info.needCreate();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void testNeedCreate05() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.CHECK;

        // run
        TransactionTokenInfo tokenInfo = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = tokenInfo.needCreate();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void testNeedValidate05() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.CHECK;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = info.needValidate();
        // assert
        assertThat(output, is(true));
    }

    @Test
    public void testNeedKeep01() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.NONE;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = info.needKeep();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void testNeedKeep02() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.BEGIN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = info.needKeep();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void testNeedKeep03() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.IN;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = info.needKeep();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void testNeedKeep04() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.END;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = info.needKeep();
        // assert
        assertThat(output, is(false));
    }

    @Test
    public void testNeedKeep05() {
        // setup input parameters

        String tokenName = "aaa";

        TransactionTokenType tokenType = TransactionTokenType.CHECK;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, tokenType);
        boolean output = info.needKeep();
        // assert
        assertThat(output, is(true));
    }

    @Test
    public void TestToString() {
        // setup input parameters

        String tokenName = "tokenName";

        TransactionTokenType beginToken = TransactionTokenType.END;

        // run
        TransactionTokenInfo info = new TransactionTokenInfo(tokenName, beginToken);
        String output = info.toString();
        // assert
        assertThat(output, is("TransactionTokenInfo [tokenName=" + tokenName
                + ", transitionType=" + TransactionTokenType.END + "]"));
    }

}
