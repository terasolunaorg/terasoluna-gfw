/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspTagException;

public class JspTagUtilsTest {

    @Test
    public void testJspTagUtils() throws Exception {
        // set up
        Constructor<JspTagUtils> constructor = JspTagUtils.class.getDeclaredConstructor();
        assertThat(constructor.canAccess(null)).isFalse();
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance()).isNotNull();

        constructor.setAccessible(false);
    }

    @Test
    public void toBoolean_valueIsTrue() throws JspException {
        assertThat(JspTagUtils.toBoolean("true", false, null)).isTrue();
        assertThat(JspTagUtils.toBoolean("TRUE", false, null)).isTrue();
        assertThat(JspTagUtils.toBoolean("TrUe", false, null)).isTrue();
    }

    @Test
    public void toBoolean_valueIsFalse() throws JspException {
        assertThat(JspTagUtils.toBoolean("false", true, null)).isFalse();
        assertThat(JspTagUtils.toBoolean("FALSE", true, null)).isFalse();
        assertThat(JspTagUtils.toBoolean("FaLsE", true, null)).isFalse();
    }

    @Test
    public void toBoolean_valueIsNullOrBlankOrWhitespace() throws JspException {
        assertThat(JspTagUtils.toBoolean(null, Boolean.TRUE, null)).isTrue();
        assertThat(JspTagUtils.toBoolean("", Boolean.FALSE, null)).isFalse();
    }

    @Test
    public void toBoolean_valueIsNotTrueOrFalse() throws JspException {

        JspTagException e = assertThrows(JspTagException.class, () -> {
            JspTagUtils.toBoolean("on", true, "field1");
        });
        assertThat(e).hasMessage("The value of field1 must be either true or false.");

        e = assertThrows(JspTagException.class, () -> {
            JspTagUtils.toBoolean("off", false, "field2");
        });
        assertThat(e).hasMessage("The value of field2 must be either true or false.");
    }
}
