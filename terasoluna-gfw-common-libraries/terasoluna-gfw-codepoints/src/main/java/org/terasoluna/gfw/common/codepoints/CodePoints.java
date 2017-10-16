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

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents the collection of code point. This class holds immutable code points as {@link java.util.Set} and provides
 * <ul>
 * <li>check method if the code points in the given string are included</li>
 * <li>set operations (union, subtract, intersect)</li>
 * </ul>
 * <h3>How to create an instance</h3> Use Factory method to create a cached instance
 *
 * <pre>
 * <code>CodePoints cp = CodePoints.of(ASCIIPrintableChars.class);</code>
 * </pre>
 * 
 * The constructor can be also used. In this case, of course, the set of code points are not cached and created every time.
 * 
 * <pre>
 * <code>CodePoints cp = new ASCIIPrintableChars();</code>
 * </pre>
 * 
 * There are three types of constructor:
 * <ol>
 * <li>Pass {@code int} varargs
 * 
 * <pre>
 * <code>CodePoints cp = new CodePoints(0x0061, 0x0062); // a b</code>
 * </pre>
 * 
 * </li>
 * <li>Pass {@link java.util.Collection} of {@link java.lang.Integer}
 * 
 * <pre>
 * <code>{@literal Set<Integer>} set = new {@literal HashSet<>}();
 * set.add(0x0061); // a
 * set.add(0x0062); // b
 * CodePoints cp = new CodePoints(set);</code>
 * </pre>
 * 
 * </li>
 * <li>Pass {@link java.lang.String} varargs including the target code points
 * 
 * <pre>
 * <code>CodePoints cp = new CodePoints("ab");
 * CodePoints cp = new CodePoints("a", "b"); // is same </code>
 * </pre>
 * 
 * </li>
 * <li>Pass existing {@link CodePoints}. This type is intended to use for the definition of new code points. The set in the
 * {@link CodePoints} are shared.
 *
 * <pre>
 * <code>CodePoints cp = ...;
 * CodePoints newCp = new CodePoints(cp); </code>
 * </pre>
 *
 * </li>
 * </ol>
 * <h3>How to check strings</h3> {@link #containsAll(String)} returns {@code true} if all code points in the given string are
 * included in the target code points. Otherwise {@code false} is returned.
 * 
 * <pre>
 * <code>CodePoints cp = new CodePoints(0x0061, 0x0062); // a b
 * cp.containsAll("a"); // true
 * cp.containsAll("b"); // true
 * cp.containsAll("ab"); // true
 * cp.containsAll("c"); // false
 * cp.containsAll("abc"); // false
 * </code>
 * </pre>
 * 
 * {@link #firstExcludedCodePoint(String)} return the first code point in the given string which is not included in the target
 * code points.
 *
 * <pre>
 * <code>CodePoints cp = new CodePoints(0x0061, 0x0062); // a b
 * cp.firstExcludedContPoint("abc"); // 0x0063 (c)
 * cp.firstExcludedContPoint("abcad"); // 0x0063 (c)
 * cp.firstExcludedContPoint("ab"); // CodePoints#NOT_FOUND
 * </code>
 * </pre>
 *
 * {@link #allExcludedCodePoints(String)} returns set of code points in the given string which are not not included in the
 * target.
 * 
 * <pre>
 * <code>CodePoints cp = new CodePoints(0x0061, 0x0062); // a b
 * cp.allExcludedCodePoints("abc"); // [0x0063 (c)]
 * cp.allExcludedCodePoints("abcad"); // [0x0063 (c), 0x0064 (d)]
 * cp.allExcludedCodePoints("ab"); // []
 * </code>
 * </pre>
 *
 * <h3>How to compose code points</h3>
 * <p>
 * {@code CodePoints} provides composable APIs. Since a {@code CodePoints} instance is immutable. These API does not effect the
 * state of {@code CodePoints} instances.
 * </p>
 * <h4>Union</h4>
 * <p>
 * Use {@link #union(CodePoints)}
 * </p>
 *
 * <pre>
 * <code>CodePoints ab = new CodePoints(0x0061 , 0x0062); // a b
 * CodePoints cd = new CodePoints(0x0063, 0x0064); // c d
 * CodePoints abcd = ab.union(cd); // a b c d</code>
 * </pre>
 *
 * <h4>Subtract</h4>
 * <p>
 * Use {@link #subtract(CodePoints)}
 * </p>
 *
 * <pre>
 * <code>CodePoints abcd = new CodePoints(0x0061 , 0x0062, 0x0063, 0x0064); // a b c d
 * CodePoints cd = new CodePoints(0x0063, 0x0064); // c d
 * CodePoints ab = abcd.subtract(cd); // a b</code>
 * </pre>
 *
 * <h4>Intersect</h4>
 * <p>
 * Use {@link #intersect(CodePoints)}
 * </p>
 *
 * <pre>
 * <code>CodePoints abcd = new CodePoints(0x0061 , 0x0062, 0x0063, 0x0064); // a b c d
 * CodePoints cde = new CodePoints(0x0063, 0x0064, 0x0064 ); // c d e
 * CodePoints cd = abcd.intersect(cde); // c d</code>
 * </pre>
 * 
 * <h3>How to define new code points</h3>
 * <p>
 * Extend {@link CodePoints} to define new code points. Following is a simple code points:
 * </p>
 * 
 * <pre>
 * <code>public class ABCD extends CodePoints {
 *   public ABCD() {
 *     super(0x0061, 0x0062, 0x0063, 0x0064); // a b c d
 *   }
 * }</code>
 * </pre>
 * <p>
 * New code points can be created using the combination of existing code points.
 * </p>
 * 
 * <pre>
 * <code>public class X_JIS_0208_Hiragana_Katakana extends CodePoints {
 *   public X_JIS_0208_Hiragana_Katakana() {
 *     super(new X_JIS_0208_Hiragana().union(new X_JIS_0208_Hiragana_Katakana()));
 *   }
 * }</code>
 * </pre>
 * <p>
 * Not that, <code>new</code> is used not to cache temporary code points. If {@code X_JIS_0208_Hiragana} and
 * {@code X_JIS_0208_Hiragana_Katakana} are also intended to be used, use {@link #of(Class)} instead of {@code new} so that
 * these are cached:
 * </p>
 * 
 * <pre>
 * <code>public class X_JIS_0208_Hiragana_Katakana extends CodePoints {
 *   public X_JIS_0208_Hiragana_Katakana() {
 *     super(CodePoints.of(X_JIS_0208_Hiragana.class).union(CodePoints.of(X_JIS_0208_Hiragana_Katakana.class)));
 *   }
 * }</code>
 * </pre>
 * @since 5.1.0
 */
public class CodePoints implements Serializable {

    /**
     * shows no code point is found in the given string which is not included in the target code points.
     */
    public static final int NOT_FOUND = Integer.MIN_VALUE;

    /**
     * {@code CodePoints} cache
     */
    private static final ConcurrentMap<Class<? extends CodePoints>, CodePoints> cache = new ConcurrentHashMap<Class<? extends CodePoints>, CodePoints>();

    /**
     * set for code points.
     */
    private final Set<Integer> set;

    /**
     * Constructor with the given {@code java.lang.Integer} code points
     * @param codePoints array of actual code points
     */
    public CodePoints(Integer... codePoints) {
        Set<Integer> s = new HashSet<Integer>(codePoints.length);
        Collections.addAll(s, codePoints);
        this.set = Collections.unmodifiableSet(s);
    }

    /**
     * Constructor with the given {@code java.lang.String}
     * @param strings array of strings which include target code points
     */
    public CodePoints(String... strings) {
        Set<Integer> s = new HashSet<Integer>();
        for (String str : strings) {
            int len = str.length();
            int codePoint;
            for (int i = 0; i < len; i += Character.charCount(codePoint)) {
                codePoint = str.codePointAt(i);
                s.add(codePoint);
            }
        }
        this.set = Collections.unmodifiableSet(s);
    }

    /**
     * Constructor with the given {@code java.lang.Integer} code points
     * @param codePoints collection of actual code points
     */
    public CodePoints(Collection<Integer> codePoints) {
        Set<Integer> s = new HashSet<Integer>(codePoints);
        this.set = Collections.unmodifiableSet(s);
    }

    /**
     * Constructor with the given {@code CodePoints}. The {@code java.util.Set} object inside {@code CodePoints} is shared.
     * @param codePoints actual code points
     */
    public CodePoints(CodePoints codePoints) {
        this.set = codePoints.set;
    }

    /**
     * returns whether all code points in the given string are included in the target code points.
     * @param s target string
     * @return {@code true} if all code points in the given string are included in the target code points。Otherwise
     *         {@code false} is returned.
     */
    public boolean containsAll(String s) {
        return this.firstExcludedCodePoint(s) == NOT_FOUND;
    }

    /**
     * returns the first code point in the given string which is not included in the target code points.
     * @param s target string
     * @return first code point in the given string which is not included in the target code points. {@link #NOT_FOUND} is
     *         returned if all code points in the given string are included in the target code points.
     */
    public int firstExcludedCodePoint(String s) {
        if (s == null || s.isEmpty()) {
            return NOT_FOUND;
        }
        // http://www.ibm.com/developerworks/jp/ysl/library/java/j-unicode_surrogate/
        int len = s.length();
        int codePoint;
        for (int i = 0; i < len; i += Character.charCount(codePoint)) {
            codePoint = s.codePointAt(i);
            if (!set.contains(codePoint)) {
                return codePoint;
            }
        }
        return NOT_FOUND;
    }

    /**
     * returns set of code points in the given string which are not not included in the target.
     * @param s target string
     * @return set of code points in the given string which are not not included in the target. an empty set is returned if all
     *         code points in the given string are included in the target code points.
     */
    public Set<Integer> allExcludedCodePoints(String s) {
        if (s == null || s.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Integer> excludedCodePoints = new LinkedHashSet<Integer>();
        // http://www.ibm.com/developerworks/jp/ysl/library/java/j-unicode_surrogate/
        int len = s.length();
        Integer codePoint;
        for (int i = 0; i < len; i += Character.charCount(codePoint)) {
            codePoint = s.codePointAt(i);
            if (!set.contains(codePoint)) {
                excludedCodePoints.add(codePoint);
            }
        }
        return excludedCodePoints;
    }

    /**
     * unite two set of code points
     * @param codePoints code points to unite
     * @return united code points
     */
    public CodePoints union(CodePoints codePoints) {
        Set<Integer> setTmp = new HashSet<Integer>(this.set);
        setTmp.addAll(codePoints.set);
        return new CodePoints(setTmp);
    }

    /**
     * subtract two set of code points
     * @param codePoints code points to subtract
     * @return subtracted code points
     */
    public CodePoints subtract(CodePoints codePoints) {
        Set<Integer> setTmp = new HashSet<Integer>(this.set);
        setTmp.removeAll(codePoints.set);
        return new CodePoints(setTmp);
    }

    /**
     * intersect two set of code points
     * @param codePoints code points to intersect
     * @return intersected code points
     */
    public CodePoints intersect(CodePoints codePoints) {
        Set<Integer> setTmp = new HashSet<Integer>(this.set);
        setTmp.retainAll(codePoints.set);
        return new CodePoints(setTmp);
    }

    /**
     * Produces cached {@link CodePoints}. At first time, a new {@link CodePoints} is created. After second time, same instance
     * is returned.
     * @param clazz {@link CodePoints} class to create
     * @param <T> {@link CodePoints} class
     * @return cached instance
     */
    @SuppressWarnings("unchecked")
    public static <T extends CodePoints> T of(Class<T> clazz) {
        if (cache.containsKey(clazz)) {
            return (T) cache.get(clazz);
        }
        try {
            T codePoints = clazz.newInstance();
            cache.put(clazz, codePoints);
            return codePoints;
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("exception occurred while initializing", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("public default constructor not found", e);
        }
    }

    /**
     * Helper method to check whether all code points in the given string are included in any of the code points list.
     * @param s target string
     * @param codePointsList
     * @return {@code true} if all code points in the given string are included in any of the code points list. Otherwise
     *         {@code false} is returned.
     */
    public static boolean containsAllInAnyCodePoints(String s,
            final CodePoints... codePointsList) {
        Map<Integer, Integer> excludedCounts = new HashMap<Integer, Integer>();
        for (CodePoints codePoints : codePointsList) {
            Set<Integer> excluded = codePoints.allExcludedCodePoints(s);
            if (excluded.isEmpty()) {
                // return immediately if the given string consists of a code points.
                return true;
            }

            for (Integer codePoint : excluded) {
                // count the number of CodePoints in the given list which forbade the given code point
                Integer count = excludedCounts.get(codePoint);
                if (count != null) {
                    excludedCounts.put(codePoint, count + 1);
                } else {
                    excludedCounts.put(codePoint, 1);
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : excludedCounts.entrySet()) {
            if (entry.getValue() == codePointsList.length) {
                // All CodePoints forbade the given code point.
                // This means there are some code points which are not included in any given CodePoints' list
                return false;
            }
        }
        // OK if each code point is included in some CodePoints' list
        return true;
    }

    /**
     * equals method
     * @param o object to check
     * @return {@code true} if the given object equals to this instance. {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodePoints that = (CodePoints) o;

        return set.equals(that.set);

    }

    /**
     * hash code of the instance
     * @return hash code
     */
    @Override
    public int hashCode() {
        return set.hashCode();
    }
}
