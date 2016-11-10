package org.terasoluna.gfw.common.validator.constraintvalidators;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import javax.validation.ConstraintValidatorContext;

import org.junit.Before;
import org.junit.Test;

public class ConstraintValidatorsUtilsTest {

    private CompareValidator compareValidator;

    @Before
    public void setUp() {
        compareValidator = new CompareValidator();
    }

    @Test
    public void testConstraintValidatorsUtils() throws Exception {
        // set up
        Constructor<ConstraintValidatorsUtils> constructor = ConstraintValidatorsUtils.class.getDeclaredConstructor();
        assertThat(constructor.isAccessible(), is(false));
        constructor.setAccessible(true);

        // assert
        assertThat(constructor.newInstance(), notNullValue());

        constructor.setAccessible(false);
    }

    @Test
    public void testGetPropertyValueBeanAndPropertyNameNotNull() throws Exception {
        // set up
        Object bean = mock(Object.class);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // assert
        assertTrue(compareValidator.isValid(bean, context));
    }

    @Test
    public void testGetPropertyValueBeanNull() throws Exception {
        // set up
        Object bean = null;
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        // assert
        assertTrue(compareValidator.isValid(bean, context));
    }


    @Test
    public void testGetPropertyValuePropertyNameNotNull() throws Exception {
        // set up
        Object bean = mock(Object.class);
        ConstraintValidatorContext context = null;

        // assert
        assertTrue(compareValidator.isValid(bean, context));
    }

    @Test
    public void testGetPropertyValueBeanAndPropertyNameNull() throws Exception {
        // set up
        Object bean = null;
        ConstraintValidatorContext context = null;

        // assert
        assertTrue(compareValidator.isValid(bean, context));
    }
}
