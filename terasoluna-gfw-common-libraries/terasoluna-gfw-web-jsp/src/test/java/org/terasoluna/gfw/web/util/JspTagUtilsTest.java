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
package org.terasoluna.gfw.web.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.junit.Test;

public class JspTagUtilsTest {

    @Test
    public void testJspTagUtils() throws Exception {
        // set up
        Constructor<JspTagUtils> constructor = JspTagUtils.class
                .getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());

        constructor.setAccessible(false);
    }

    @Test
    public void toBoolean_valueIsTrue() throws JspException {
        assertThat(JspTagUtils.toBoolean("true", false, null), is(true));
        assertThat(JspTagUtils.toBoolean("TRUE", false, null), is(true));
        assertThat(JspTagUtils.toBoolean("TrUe", false, null), is(true));
    }

    @Test
    public void toBoolean_valueIsFalse() throws JspException {
        assertThat(JspTagUtils.toBoolean("false", true, null), is(false));
        assertThat(JspTagUtils.toBoolean("FALSE", true, null), is(false));
        assertThat(JspTagUtils.toBoolean("FaLsE", true, null), is(false));
    }

    @Test
    public void toBoolean_valueIsNullOrBlankOrWhitespace() throws JspException {
        assertThat(JspTagUtils.toBoolean(null, Boolean.TRUE, null), is(true));
        assertThat(JspTagUtils.toBoolean("", Boolean.FALSE, null), is(false));
    }

    @Test
    public void toBoolean_valueIsNotTrueOrFalse() throws JspException {
        try {
            JspTagUtils.toBoolean("on", true, "field1");
            fail("should be occurred JspTagException.");
        } catch (JspTagException e) {
            assertThat(e.getMessage(), is(
                    "The value of field1 must be either true or false."));
        }
        try {
            JspTagUtils.toBoolean("off", false, "field2");
            fail("should be occurred JspTagException.");
        } catch (JspTagException e) {
            assertThat(e.getMessage(), is(
                    "The value of field2 must be either true or false."));
        }
    }
}
