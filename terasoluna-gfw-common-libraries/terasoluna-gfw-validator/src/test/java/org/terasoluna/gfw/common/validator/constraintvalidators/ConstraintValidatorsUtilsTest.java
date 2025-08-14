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
package org.terasoluna.gfw.common.validator.constraintvalidators;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

public class ConstraintValidatorsUtilsTest {

    @Test
    public void testConstraintValidatorsUtils() throws Exception {
        // set up
        Constructor<ConstraintValidatorsUtils> constructor =
                ConstraintValidatorsUtils.class.getDeclaredConstructor();
        assertThat(constructor.canAccess(null)).isEqualTo(false);
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance()).isNotNull();
    }

    @Test
    public void testGetPropertyValueBeanAndPropertyNameNotNull() throws Exception {
        // set up
        FooBean foo = new FooBean();
        foo.setBar("test");

        // test
        Object o = ConstraintValidatorsUtils.getPropertyValue(foo, "bar");

        // assert
        assertThat(o).isNotNull();
    }

    @Test
    public void testGetPropertyValueBeanNull() throws Exception {
        // test
        Object o = ConstraintValidatorsUtils.getPropertyValue(null, "bar");

        // assert
        assertThat(o).isNull();
    }

    @Test
    public void testGetPropertyValuePropertyNameNull() throws Exception {
        // test
        Object o = ConstraintValidatorsUtils.getPropertyValue(new FooBean(), null);

        // assert
        assertThat(o).isNull();
    }

    @Test
    public void testIsEmptyValueNull() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty(null);

        // assert
        assertThat(b).isEqualTo(true);
    }

    @Test
    public void testIsEmptyValueBlank() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty("");

        // assert
        assertThat(b).isEqualTo(true);
    }

    @Test
    public void testIsEmptyValueNotNull() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty("test");

        // assert
        assertThat(b).isEqualTo(false);
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
