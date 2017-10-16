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
package org.terasoluna.gfw.web.token;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class TokenStringGeneratorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenGeneratorAlgorithm() throws Exception {
        try {
            new TokenStringGenerator("InvalidAlgorithm");
        } catch (Exception e) {
            assertThat(e.getCause(), is(instanceOf(
                    NoSuchAlgorithmException.class)));
            assertThat(e.getMessage(), is(
                    "The given algorithm is invalid. algorithm=InvalidAlgorithm"));
            throw e;
        }
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTokenGeneratorAlgorithm() throws Exception {
        try {
            new TokenStringGenerator(null);
        } catch (Exception e) {
            assertThat(e.getMessage(), is("algorithm must not be null"));
            throw e;
        }
        fail();
    }

    @Test
    public void testGenerate_defaultMD5() {
        TokenStringGenerator generator = new TokenStringGenerator();
        String value = generator.generate("hoge");
        assertThat(value, is(notNullValue()));
        assertThat(value.length(), is(32));
    }

    @Test
    public void testGenerate_defaultMD5_empty() {
        TokenStringGenerator generator = new TokenStringGenerator();
        String value = generator.generate("");
        assertThat(value, is(notNullValue()));
        assertThat(value.length(), is(32));
    }

    @Test
    public void testGenerate_SHA256() {
        TokenStringGenerator generator = new TokenStringGenerator("SHA-256");
        String value = generator.generate("hoge");
        assertThat(value, is(notNullValue()));
        assertThat(value.length(), is(64));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_nullValue() throws Exception {
        TokenStringGenerator generator = new TokenStringGenerator();
        try {
            generator.generate(null);
        } catch (Exception e) {
            assertThat(e.getMessage(), is("seed must not be null"));
            throw e;
        }
    }

}
