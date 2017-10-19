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

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class FullHalfPairsBuilderTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFullIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "fullwidth must be 1 length string (fullwidth = null)");
        new FullHalfPairsBuilder().pair(null, "a").build();
    }

    @Test
    public void testFullIsEmptyString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "fullwidth must be 1 length string (fullwidth = )");
        new FullHalfPairsBuilder().pair("", "a").build();
    }

    @Test
    public void testFullIsTwoString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "fullwidth must be 1 length string (fullwidth = aa)");
        new FullHalfPairsBuilder().pair("aa", "a").build();
    }

    @Test
    public void testHalfIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "halfwidth must be 1 or 2 length string (halfwidth = null)");
        new FullHalfPairsBuilder().pair("a", null).build();
    }

    @Test
    public void testHalfIsEmptyString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "halfwidth must be 1 or 2 length string (halfwidth = )");
        new FullHalfPairsBuilder().pair("a", "").build();
    }

    @Test
    public void testHalfIsThreeString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "halfwidth must be 1 or 2 length string (halfwidth = aaa)");
        new FullHalfPairsBuilder().pair("a", "aaa").build();
    }

    @Test
    public void testSamePairIsIgnored() {
        Set<FullHalfPair> set = new FullHalfPairsBuilder().pair("ａ", "a").pair(
                "ａ", "a").build().pairs();
        assertThat(set.size(), is(1));
    }
}
