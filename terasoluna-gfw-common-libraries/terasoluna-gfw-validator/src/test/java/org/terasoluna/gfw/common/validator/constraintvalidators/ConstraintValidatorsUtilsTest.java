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
package org.terasoluna.gfw.common.validator.constraintvalidators;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class ConstraintValidatorsUtilsTest {

    @Test
    public void testConstraintValidatorsUtils() throws Exception {
        // set up
        Constructor<ConstraintValidatorsUtils> constructor = ConstraintValidatorsUtils.class
                .getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());
    }

    @Test
    public void testGetPropertyValueBeanAndPropertyNameNotNull() throws Exception {
        // set up
        FooBean foo = new FooBean();
        foo.setBar("test");

        // test
        Object o = ConstraintValidatorsUtils.getPropertyValue(foo, "bar");

        // assert
        assertThat(o, notNullValue());
    }

    @Test
    public void testGetPropertyValueBeanNull() throws Exception {
        // test
        Object o = ConstraintValidatorsUtils.getPropertyValue(null, "bar");

        // assert
        assertThat(o, nullValue());
    }

    @Test
    public void testGetPropertyValuePropertyNameNull() throws Exception {
        // test
        Object o = ConstraintValidatorsUtils.getPropertyValue(new FooBean(),
                null);

        // assert
        assertThat(o, nullValue());
    }

    @Test
    public void testIsEmptyValueNull() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty(null);

        // assert
        assertThat(b, is(true));
    }

    @Test
    public void testIsEmptyValueBlank() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty("");

        // assert
        assertThat(b, is(true));
    }

    @Test
    public void testIsEmptyValueNotNull() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty("test");

        // assert
        assertThat(b, is(false));
    }

    class FooBean {

        private String bar;

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }

    }
}
