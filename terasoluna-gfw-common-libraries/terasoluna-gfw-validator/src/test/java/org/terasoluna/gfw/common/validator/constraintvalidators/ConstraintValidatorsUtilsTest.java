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
    public void testGetPropertyValueBeanAndPropertyNameNotNull()
            throws Exception {
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
        assertTrue(b);
    }

    @Test
    public void testIsEmptyValueNotNull() throws Exception {
        // test
        boolean b = ConstraintValidatorsUtils.isEmpty("test");

        // assert
        assertFalse(b);
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
