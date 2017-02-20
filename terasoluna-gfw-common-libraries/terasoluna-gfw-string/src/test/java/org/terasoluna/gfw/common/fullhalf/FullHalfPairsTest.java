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

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FullHalfPairsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNullPairs() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("pairs must not be null");
        new FullHalfPairs(null, null);
    }

    @Test
    public void testEmptyPairs() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("pairs must not be empty");
        new FullHalfPairs(Collections.<FullHalfPair> emptySet(), null);
    }

}
