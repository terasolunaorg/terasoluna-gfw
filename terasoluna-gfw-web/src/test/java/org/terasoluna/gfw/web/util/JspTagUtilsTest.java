/*
 * Copyright (C) 2013 terasoluna.org
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
import static org.junit.Assert.*;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.junit.Test;

public class JspTagUtilsTest {

    @Test
    public void toBoolean_valueIsTrue() throws JspException {
        assertThat(JspTagUtils.toBoolean("true", null, null), is(Boolean.TRUE));
        assertThat(JspTagUtils.toBoolean("TRUE", null, null), is(Boolean.TRUE));
        assertThat(JspTagUtils.toBoolean("TrUe", null, null), is(Boolean.TRUE));
    }

    @Test
    public void toBoolean_valueIsFalse() throws JspException {
        assertThat(JspTagUtils.toBoolean("false", null, null),
                is(Boolean.FALSE));
        assertThat(JspTagUtils.toBoolean("FALSE", null, null),
                is(Boolean.FALSE));
        assertThat(JspTagUtils.toBoolean("FaLsE", null, null),
                is(Boolean.FALSE));
    }

    @Test
    public void toBoolean_valueIsNullOrBlankOrWhitespace() throws JspException {
        assertThat(JspTagUtils.toBoolean(null, Boolean.TRUE, null),
                is(Boolean.TRUE));
        assertThat(JspTagUtils.toBoolean("", Boolean.FALSE, null),
                is(Boolean.FALSE));
        assertThat(JspTagUtils.toBoolean(" \t", null, null), is((Boolean) null));
    }

    @Test
    public void toBoolean_valueIsNotTrueOrFalse() throws JspException {
        try {
            JspTagUtils.toBoolean("on", Boolean.TRUE, "field1");
            fail("should be occurred JspTagException.");
        } catch (JspTagException e) {
            assertThat(e.getMessage(),
                    is("The value of field1 must be either true or false."));
        }
        try {
            JspTagUtils.toBoolean("off", Boolean.FALSE, "field2");
            fail("should be occurred JspTagException.");
        } catch (JspTagException e) {
            assertThat(e.getMessage(),
                    is("The value of field2 must be either true or false."));
        }
    }
}
