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
        expectedException.expectMessage("fullwidth must be 1 length string");
        new FullHalfPairsBuilder().pair(null, "a").build();
    }

    @Test
    public void testFullIsEmptyString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("fullwidth must be 1 length string");
        new FullHalfPairsBuilder().pair("", "a").build();
    }

    @Test
    public void testFullIsTwoString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("fullwidth must be 1 length string");
        new FullHalfPairsBuilder().pair("aa", "a").build();
    }

    @Test
    public void testHalfIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("halfwidth must be 1 or 2 length string");
        new FullHalfPairsBuilder().pair("a", null).build();
    }

    @Test
    public void testHalfIsEmptyString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("halfwidth must be 1 or 2 length string");
        new FullHalfPairsBuilder().pair("a", "").build();
    }

    @Test
    public void testHalfIsThreeString() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage("halfwidth must be 1 or 2 length string");
        new FullHalfPairsBuilder().pair("a", "aaa").build();
    }

    @Test
    public void testSamePairIsIgnored() {
        Set<FullHalfPair> set = new FullHalfPairsBuilder().pair("ａ", "a").pair(
                "ａ", "a").build().pairs();
        assertThat(set.size(), is(1));
    }
}
