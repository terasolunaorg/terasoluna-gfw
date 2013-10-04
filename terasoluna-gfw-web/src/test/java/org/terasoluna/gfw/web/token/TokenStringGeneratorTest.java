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
            assertThat(e.getCause(), is(NoSuchAlgorithmException.class));
            assertThat(
                    e.getMessage(),
                    is("The given algorithm is invalid. algorithm=InvalidAlgorithm"));
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
