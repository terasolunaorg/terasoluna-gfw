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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class to generate the random string that can be used as value part of {@code TransactionToken}
 */
public class TokenStringGenerator {

    public static final String DEFAULT_ALGORITHM = "MD5";

    private final AtomicLong counter = new AtomicLong();

    private final String internalSeed;

    private final String algorithm;

    /**
     * Constructor.
     * <p>
     * Uses MD5 as default Algorithm.
     * </p>
     */
    public TokenStringGenerator() {
        this(DEFAULT_ALGORITHM);
    }

    /**
     * Constructor. Algorithm to be used for generating the token value can be passed as an argument <br>
     * @param algorithm Algorithm to be used for generating the token value (must not be null)
     * @throws IllegalArgumentException algorithm is null or invalid
     */
    public TokenStringGenerator(final String algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("algorithm must not be null");
        }
        try {
            MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("The given algorithm is invalid. algorithm="
                    + algorithm, e);
        }
        this.algorithm = algorithm;
        this.internalSeed = Long.toHexString(new SecureRandom().nextLong());
    }

    /**
     * Generates random token string<br>
     * <p>
     * Uses {@code MD5} as default algorithm if not externally specified. A seed is to be passed as parameter which will be used
     * as a base to generate the random token string value.
     * </p>
     * @param seed any value to use as base to generate token string value (must not be null)
     * @return token string
     * @throws IllegalArgumentException seed is null
     */
    public String generate(final String seed) {
        if (seed == null) {
            throw new IllegalArgumentException("seed must not be null");
        }
        long time = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder(1000);
        sb.append(internalSeed).append(seed).append(time).append(counter
                .getAndIncrement());

        MessageDigest md = createMessageDigest();
        md.update(sb.toString().getBytes());
        byte[] bytes = md.digest();
        return toHexString(bytes);
    }

    MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    protected static String toHexString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String s = Integer.toHexString(b & 0xff);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
