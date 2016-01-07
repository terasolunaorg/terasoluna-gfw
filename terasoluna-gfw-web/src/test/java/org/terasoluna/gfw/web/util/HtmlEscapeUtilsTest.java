package org.terasoluna.gfw.web.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HtmlEscapeUtilsTest {

    @Test
    public void testHtmlEscape() {
        assertThat(HtmlEscapeUtils.htmlEscape(null), is(""));
        assertThat(HtmlEscapeUtils.htmlEscape(""), is(""));
        assertThat(HtmlEscapeUtils.htmlEscape("<a href=\"\">"),
                is("&lt;a href=&quot;&quot;&gt;"));
        assertThat(HtmlEscapeUtils.htmlEscape("<a href=''>"), is("&lt;a href=&#39;&#39;&gt;"));
        assertThat(HtmlEscapeUtils.htmlEscape("&lt;"), is("&amp;lt;"));
        assertThat(HtmlEscapeUtils.htmlEscape(new boolean[] { true, false }),
                is("[true, false]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new int[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new short[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new long[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new byte[] { 1, 2, 3 }), is("[1, 2, 3]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new double[] { 1, 2, 3 }), is("[1.0, 2.0, 3.0]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new float[] { 1, 2, 3 }), is("[1.0, 2.0, 3.0]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new char[] { 'a', 'b', 'c' }), is("[a, b, c]"));
        assertThat(HtmlEscapeUtils.htmlEscape(new String[] { "a", "b", "c" }), is("[a, b, c]"));
    }
}
