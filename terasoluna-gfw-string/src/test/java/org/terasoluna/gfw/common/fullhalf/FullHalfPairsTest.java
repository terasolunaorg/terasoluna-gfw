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
