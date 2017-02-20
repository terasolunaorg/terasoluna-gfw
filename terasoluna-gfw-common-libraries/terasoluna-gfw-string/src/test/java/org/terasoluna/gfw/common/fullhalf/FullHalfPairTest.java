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
package org.terasoluna.gfw.common.fullhalf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FullHalfPairTest {

    @Test
    public void testFullHalfPairEquals() {
        // set up
        FullHalfPair FullHalfPair1 = new FullHalfPair("ａ", "a");
        FullHalfPair FullHalfPair2 = new FullHalfPair("ａ", "a");
        FullHalfPair FullHalfPair3 = FullHalfPair1;

        // assert
        assertThat(FullHalfPair1.equals(FullHalfPair2), is(true));
        assertThat(FullHalfPair1.equals(FullHalfPair3), is(true));
    }

    @Test
    public void testFullHalfPairEqualsNull() {
        // set up
        FullHalfPair FullHalfPair = new FullHalfPair("ａ", "a");
        String str = null;

        // assert
        assertThat(FullHalfPair.equals(str), is(false));
    }

    @Test
    public void testFullHalfPairEqualsDifferentClass() {
        // set up
        FullHalfPair FullHalfPair = new FullHalfPair("ａ", "a");
        String str = "a";

        // assert
        assertThat(FullHalfPair.equals(str), is(false));
    }

    @Test
    public void testFullWidthNotEquals() {
        // set up
        FullHalfPair FullHalfPair1 = new FullHalfPair("ａ", "a");
        FullHalfPair FullHalfPair2 = new FullHalfPair("b", "b");

        // assert
        assertThat(FullHalfPair1.equals(FullHalfPair2), is(false));
    }

    @Test
    public void testHalfWidthNotEquals() {
        // set up
        FullHalfPair FullHalfPair1 = new FullHalfPair("ａ", "a");
        FullHalfPair FullHalfPair2 = new FullHalfPair("ａ", "b");

        // assert
        assertThat(FullHalfPair1.equals(FullHalfPair2), is(false));
    }

}
