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
package org.terasoluna.gfw.common.codepoints;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.terasoluna.gfw.common.codepoints.catalog.ABCD;
import org.terasoluna.gfw.common.codepoints.catalog.AbstractCodePoints;
import org.terasoluna.gfw.common.codepoints.catalog.IllegalCodePoints;

public class CodePointsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testContainsAll() {
        CodePoints codePoints = new CodePoints("あ", "い", "う");

        assertThat(codePoints.containsAll("あ"), is(true));
        assertThat(codePoints.containsAll("い"), is(true));
        assertThat(codePoints.containsAll("う"), is(true));
        assertThat(codePoints.containsAll("あい"), is(true));
        assertThat(codePoints.containsAll("いう"), is(true));
        assertThat(codePoints.containsAll("うあ"), is(true));
        assertThat(codePoints.containsAll("あいう"), is(true));
        assertThat(codePoints.containsAll("あいうえ"), is(false));
    }

    /**
     * Asserts that {1, 2, 3, 4} ∩ {2, 3, 4, 5} ∩ {3, 4, 5,6} ＝ {3, 4}
     */
    @Test
    public void testIntersection() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSet0.add(1);
        materialSet0.add(2);
        materialSet0.add(3);
        materialSet0.add(4);
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(2);
        materialSet1.add(3);
        materialSet1.add(4);
        materialSet1.add(5);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(3);
        materialSet2.add(4);
        materialSet2.add(5);
        materialSet2.add(6);
        materialSets.add(materialSet2);

        CodePoints result = new CodePoints(materialSet0).intersect(
                new CodePoints(materialSet1)).intersect(
                        new CodePoints(materialSet2));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                false));
    }

    /**
     * Asserts that {} ∩ {1, 2, 3, 4} ∩ {2, 3, 4, 5} ∩ {3, 4, 5, 6}＝{}
     */
    @Test
    public void testIntersection_firstEmptySet() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(1);
        materialSet1.add(2);
        materialSet1.add(3);
        materialSet1.add(4);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(2);
        materialSet2.add(3);
        materialSet2.add(4);
        materialSet2.add(5);
        materialSets.add(materialSet2);
        Set<Integer> materialSet3 = new HashSet<Integer>();
        materialSet3.add(3);
        materialSet3.add(4);
        materialSet3.add(5);
        materialSet3.add(6);
        materialSets.add(materialSet3);

        CodePoints result = new CodePoints(materialSet0).intersect(
                new CodePoints(materialSet1)).intersect(
                        new CodePoints(materialSet2)).intersect(
                                new CodePoints(materialSet3));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                false));
    }

    /**
     * Asserts that {1, 2, 3, 4} ∩ {2, 3, 4, 5} ∩ {3, 4, 5, 6} ∩ {} ＝ {}
     */
    @Test
    public void testIntersection_lastEmptySet() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSet0.add(1);
        materialSet0.add(2);
        materialSet0.add(3);
        materialSet0.add(4);
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(2);
        materialSet1.add(3);
        materialSet1.add(4);
        materialSet1.add(5);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(3);
        materialSet2.add(4);
        materialSet2.add(5);
        materialSet2.add(6);
        materialSets.add(materialSet2);
        Set<Integer> materialSet3 = new HashSet<Integer>();
        materialSets.add(materialSet3);

        CodePoints result = new CodePoints(materialSet0).intersect(
                new CodePoints(materialSet1)).intersect(
                        new CodePoints(materialSet2)).intersect(
                                new CodePoints(materialSet3));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                false));
    }

    /**
     * Asserts that {1, 2, 3, 4} ∪ {2, 3, 4, 5} ∪ {3, 4, 5, 6} ＝ {1, 2, 3, 4, 5, 6})
     */
    @Test
    public void testUnion() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSet0.add(1);
        materialSet0.add(2);
        materialSet0.add(3);
        materialSet0.add(4);
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(2);
        materialSet1.add(3);
        materialSet1.add(4);
        materialSet1.add(5);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(3);
        materialSet2.add(4);
        materialSet2.add(5);
        materialSet2.add(6);
        materialSets.add(materialSet2);

        CodePoints result = new CodePoints(materialSet0).union(
                new CodePoints(materialSet1)).union(
                        new CodePoints(materialSet2));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                true));
    }

    /**
     * Asserts that {1, 2, 3, 4} ∪ {2, 3, 4, 5} ∪ {3, 4, 5, 6} ∪ {} ＝ {1, 2, 3, 4, 5, 6}
     */
    @Test
    public void testUnion_emptySet() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSet0.add(1);
        materialSet0.add(2);
        materialSet0.add(3);
        materialSet0.add(4);
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(2);
        materialSet1.add(3);
        materialSet1.add(4);
        materialSet1.add(5);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(3);
        materialSet2.add(4);
        materialSet2.add(5);
        materialSet2.add(6);
        materialSets.add(materialSet2);
        Set<Integer> materialSet3 = new HashSet<Integer>();
        materialSets.add(materialSet3);

        CodePoints result = new CodePoints(materialSet0).union(
                new CodePoints(materialSet1)).union(
                        new CodePoints(materialSet2)).union(
                                new CodePoints(materialSet3));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                true));
    }

    /**
     * Asserts that {1, 2, 3, 4, 5, 6} － {1, 2, 3} － {1, 3, 5} ＝ {4, 6}
     */
    @Test
    public void testSubtract() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSet0.add(1);
        materialSet0.add(2);
        materialSet0.add(3);
        materialSet0.add(4);
        materialSet0.add(5);
        materialSet0.add(6);
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(1);
        materialSet1.add(2);
        materialSet1.add(3);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(1);
        materialSet2.add(3);
        materialSet2.add(5);
        materialSets.add(materialSet2);

        CodePoints result = new CodePoints(materialSet0).subtract(
                new CodePoints(materialSet1)).subtract(
                        new CodePoints(materialSet2));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                true));
    }

    /**
     * Asserts that {} － {1, 2, 3} － {1, 3, 5} ＝ {}
     */
    @Test
    public void testSubtract_firstEmptySet() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(1);
        materialSet1.add(2);
        materialSet1.add(3);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(1);
        materialSet2.add(3);
        materialSet2.add(5);
        materialSets.add(materialSet2);

        CodePoints result = new CodePoints(materialSet0).subtract(
                new CodePoints(materialSet1)).subtract(
                        new CodePoints(materialSet2));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                false));
    }

    /**
     * Asserts that {1, 2, 3, 4, 5, 6} － {1, 2, 3} － {1, 3, 5} － {} ＝ {4, 6}
     */
    @Test
    public void testSubtract_lastEmptySet() {
        List<Set<Integer>> materialSets = new ArrayList<Set<Integer>>();
        Set<Integer> materialSet0 = new HashSet<Integer>();
        materialSet0.add(1);
        materialSet0.add(2);
        materialSet0.add(3);
        materialSet0.add(4);
        materialSet0.add(5);
        materialSet0.add(6);
        materialSets.add(materialSet0);
        Set<Integer> materialSet1 = new HashSet<Integer>();
        materialSet1.add(1);
        materialSet1.add(2);
        materialSet1.add(3);
        materialSets.add(materialSet1);
        Set<Integer> materialSet2 = new HashSet<Integer>();
        materialSet2.add(1);
        materialSet2.add(3);
        materialSet2.add(5);
        materialSets.add(materialSet2);
        Set<Integer> materialSet3 = new HashSet<Integer>();
        materialSets.add(materialSet3);

        CodePoints result = new CodePoints(materialSet0).subtract(
                new CodePoints(materialSet1)).subtract(
                        new CodePoints(materialSet2)).subtract(
                                new CodePoints(materialSet3));

        assertThat(result.containsAll(new String(new int[] { 1 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 2 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 3 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 4 }, 0, 1)), is(
                true));
        assertThat(result.containsAll(new String(new int[] { 5 }, 0, 1)), is(
                false));
        assertThat(result.containsAll(new String(new int[] { 6 }, 0, 1)), is(
                true));
    }

    /**
     * U+2000B(「丈」の右上に点を打った字)
     */
    private static final String SURROGATE_PAIR_CHAR_2000B = new String(new int[] {
            0x2000B }, 0, 1);

    /**
     * U+20B9F(「叱」の環境依存文字)
     */
    private static final String SURROGATE_PARE_CHAR_20B9F = new String(new int[] {
            0x20B9F }, 0, 1);

    @Test
    public void testIsAllowedString_null() {
        String testStr = null;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();

        boolean result = new CodePoints(allowedCodePointSet).containsAll(
                testStr);

        assertThat(result, is(true));
    }

    @Test
    public void testIsAllowedString_empty() {
        String testStr = "";
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();

        boolean result = new CodePoints(allowedCodePointSet).containsAll(
                testStr);

        assertThat(result, is(true));
    }

    @Test
    public void testIsAllowedString_allAllowedCodePoints() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうあ"
                + SURROGATE_PAIR_CHAR_2000B;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        boolean result = new CodePoints(allowedCodePointSet).containsAll(
                testStr);

        assertThat(result, is(true));
    }

    @Test
    public void testIsAllowedString_includingNotAllowedCodePoint() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうか"
                + SURROGATE_PAIR_CHAR_2000B;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        boolean result = new CodePoints(allowedCodePointSet).containsAll(
                testStr);

        assertFalse(result);
    }

    @Test
    public void testIsAllowedString_surrogatePairsAreNotAllowed() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうあ"
                + SURROGATE_PAIR_CHAR_2000B;
        boolean result = new CodePoints("あいうえお").containsAll(testStr);

        assertFalse(result);
    }

    @Test
    public void testIsAllowedString_includingNotAllowedSurrogatePair() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうあ"
                + SURROGATE_PARE_CHAR_20B9F;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        boolean result = new CodePoints(allowedCodePointSet).containsAll(
                testStr);

        assertFalse(result);
    }

    @Test
    public void testGetFirstNotAllowedCodePoint_null() {
        String testStr = null;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();

        int result = new CodePoints(allowedCodePointSet).firstExcludedCodePoint(
                testStr);

        assertThat(result, is(CodePoints.NOT_FOUND));
    }

    @Test
    public void testGetFirstNotAllowedCodePoint_empty() {
        String testStr = "";
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();

        int result = new CodePoints(allowedCodePointSet).firstExcludedCodePoint(
                testStr);

        assertThat(result, is(CodePoints.NOT_FOUND));
    }

    @Test
    public void testGetFirstNotAllowedCodePoint_allAllowedCodePoints() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうあ"
                + SURROGATE_PAIR_CHAR_2000B;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        int result = new CodePoints(allowedCodePointSet).firstExcludedCodePoint(
                testStr);

        assertThat(result, is(CodePoints.NOT_FOUND));
    }

    @Test
    public void testGetFirstNotAllowedCodePoint_includingNotAllowedCodePoint() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうかき"
                + SURROGATE_PAIR_CHAR_2000B;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        int result = new CodePoints(allowedCodePointSet).firstExcludedCodePoint(
                testStr);

        assertThat(result, is("か".codePointAt(0)));
    }

    @Test
    public void testGetFirstNotAllowedCodePoint_includingNotAllowedSurrogatePair() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうかき"
                + SURROGATE_PARE_CHAR_20B9F;
        int result = new CodePoints("あいうえお").firstExcludedCodePoint(testStr);

        assertThat(result, is(0x2000B));
    }

    @Test
    public void testGetAllNotAllowedCodePoint_null() {
        String testStr = null;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();

        Set<Integer> result = new CodePoints(allowedCodePointSet)
                .allExcludedCodePoints(testStr);

        assertThat(result.size(), is(0));
    }

    @Test
    public void testGetAllNotAllowedCodePoint_empty() {
        String testStr = "";
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();

        Set<Integer> result = new CodePoints(allowedCodePointSet)
                .allExcludedCodePoints(testStr);

        assertThat(result.size(), is(0));
    }

    @Test
    public void testGetAllNotAllowedCodePoint_allAllowedCodePoints() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうあ"
                + SURROGATE_PAIR_CHAR_2000B;
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        Set<Integer> result = new CodePoints(allowedCodePointSet)
                .allExcludedCodePoints(testStr);

        assertThat(result.size(), is(0));
    }

    @Test
    public void testGetAllNotAllowedCodePoint_includingNotAllowedCodePoint() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうか";
        Set<Integer> allowedCodePointSet = new HashSet<Integer>();
        allowedCodePointSet.add("あ".codePointAt(0));
        allowedCodePointSet.add("い".codePointAt(0));
        allowedCodePointSet.add("う".codePointAt(0));
        allowedCodePointSet.add("え".codePointAt(0));
        allowedCodePointSet.add("お".codePointAt(0));
        allowedCodePointSet.add(0x2000B);

        Set<Integer> result = new CodePoints(allowedCodePointSet)
                .allExcludedCodePoints(testStr);

        assertThat(result.size(), is(1));
        assertTrue(result.contains("か".codePointAt(0)));
    }

    @Test
    public void testGetAllNotAllowedCodePoint_includingNotAllowedSurrogatePair() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうあ";

        Set<Integer> result = new CodePoints("あいうえお").allExcludedCodePoints(
                testStr);

        assertThat(result.size(), is(1));
        assertThat(result.contains(0x2000B), is(true));
    }

    @Test
    public void testGetAllNotAllowedCodePoint_includingMultipleNotAllowedCodePoints() {
        String testStr = SURROGATE_PAIR_CHAR_2000B + "あいうきかくか"
                + SURROGATE_PARE_CHAR_20B9F;

        Set<Integer> result = new CodePoints("あいうえお").allExcludedCodePoints(
                testStr);

        assertThat(result.size(), is(5));
        Iterator<Integer> it = result.iterator();
        assertThat(it.next().intValue(), is(0x2000B));
        assertThat(it.next().intValue(), is("き".codePointAt(0)));
        assertThat(it.next().intValue(), is("か".codePointAt(0)));
        assertThat(it.next().intValue(), is("く".codePointAt(0)));
        assertThat(it.next().intValue(), is(0x20B9F));
    }

    @Test
    public void testOf_caches_are_same_instance() {
        ABCD cp1 = CodePoints.of(ABCD.class);
        ABCD cp2 = CodePoints.of(ABCD.class);
        assertThat(cp1, is(sameInstance(cp2)));
    }

    @Test
    public void testOf_illegal_access() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("public default constructor not found");
        CodePoints.of(IllegalCodePoints.class);
    }

    @Test
    public void testOf_instantiation_fail() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "exception occurred while initializing");
        CodePoints.of(AbstractCodePoints.class);
    }

    @Test
    public void testEquals() {
        // set up
        ABCD cp1 = new ABCD();
        ABCD cp2 = new ABCD();
        ABCD cp3 = cp1;

        // assert
        assertThat(cp1.equals(cp2), is(true));
        assertThat(cp1.equals(cp3), is(true));
    }

    @Test
    public void testEquals_different_codepoints() {
        CodePoints cp1 = new CodePoints("abcd");
        CodePoints cp2 = new CodePoints("ABCD");
        assertThat(cp1.equals(cp2), is(false));
    }

    @Test
    public void testEquals_same_codepoints_other_class() {
        CodePoints cp1 = new ABCD();
        CodePoints cp2 = new CodePoints("ABCD");
        assertThat(cp1.equals(cp2), is(false));
    }

    @Test
    public void testEqualsNull() {
        // set up
        CodePoints cp1 = new CodePoints("ABCD");
        CodePoints cp2 = null;

        // assert
        assertThat(cp1.equals(cp2), is(false));
    }

    @Test
    public void testHashCode() {
        // set up
        CodePoints cp1 = new CodePoints("ABCD");
        CodePoints cp2 = new CodePoints("ABCD");

        // assert
        assertThat(cp1.hashCode(), is(cp2.hashCode()));
    }
}
