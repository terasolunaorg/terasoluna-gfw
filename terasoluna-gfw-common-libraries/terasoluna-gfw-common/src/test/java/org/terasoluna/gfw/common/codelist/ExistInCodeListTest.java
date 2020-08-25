/*
 * Copyright(c) 2013 NTT DATA Corporation.
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
package org.terasoluna.gfw.common.codelist;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.annotation.Validated;
import org.terasoluna.gfw.common.codelist.BirthDay.FORMATTED;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExistInCodeListTest {

    @Inject
    Validator validator;

    @Inject
    CodeService codeService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_allValidStringCode() {
        Person p = new Person();
        p.gender = "F";
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringBuilderCode() {
        Employee e = new Employee();
        e.gender = new StringBuilder("F");
        e.lang = new StringBuilder("JP");
        Set<ConstraintViolation<Employee>> result = validator.validate(e);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringCodeWithNull() {
        Person p = new Person();
        p.gender = null;
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringBuilderCodeWithNull() {
        Employee e = new Employee();
        e.gender = null;
        e.lang = new StringBuilder("JP");
        Set<ConstraintViolation<Employee>> result = validator.validate(e);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringCodeWithEmpty() {
        Person p = new Person();
        p.gender = "";
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_allValidStringBuilderCodeWithEmpty() {
        Employee e = new Employee();
        e.gender = new StringBuilder("");
        e.lang = new StringBuilder("JP");
        Set<ConstraintViolation<Employee>> result = validator.validate(e);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_hasInValidStringCode() {
        Person p = new Person();
        p.gender = "G";
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.size(), is(1));
    }

    @Test
    public void test_hasInValidStringBuilderCode() {
        Employee e = new Employee();
        e.gender = new StringBuilder("G");
        e.lang = new StringBuilder("JP");
        Set<ConstraintViolation<Employee>> result = validator.validate(e);
        assertThat(result.size(), is(1));
    }

    @Test
    public void test_allInValidStringCode() {
        Person p = new Person();
        p.gender = "G";
        p.lang = "FR";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.size(), is(2));
    }

    @Test
    public void test_allInValidStringBuilderCode() {
        Employee e = new Employee();
        e.gender = new StringBuilder("G");
        e.lang = new StringBuilder("FR");
        Set<ConstraintViolation<Employee>> result = validator.validate(e);
        assertThat(result.size(), is(2));
    }

    @Test
    public void test_validCharacterCode() {
        Customer c = new Customer();
        c.gender = 'F';
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_validCharacterCodeWithNull() {
        Customer c = new Customer();
        c.gender = null;
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_hasInValidCharacterCode() {
        Customer c = new Customer();
        c.gender = 'G';
        c.lang = "JP";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.size(), is(1));
        assertThat(((ConstraintViolation<Customer>) result.toArray()[0])
                .getMessage(), is("Does not exist in CD_GENDER"));
    }

    @Test
    public void test_inValidStringAndCharacterCode() {
        Customer c = new Customer();
        c.gender = 'G';
        c.lang = "FR";
        Set<ConstraintViolation<Customer>> result = validator.validate(c);
        assertThat(result.size(), is(2));
    }

    @Test
    public void test_validIntegerAndLongCode() {
        BirthDay b = new BirthDay();
        b.month = 1;
        b.day = 1L;
        Set<ConstraintViolation<BirthDay>> result = validator.validate(b);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_validIntegerAndLongFormattedCode() {
        BirthDay b = new BirthDay();
        b.month = 1;
        b.day = 1L;
        Set<ConstraintViolation<BirthDay>> result = validator.validate(b,
                FORMATTED.class);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void test_invalidIntegerAndLongCode() {
        BirthDay b = new BirthDay();
        b.month = 13;
        b.day = 32L;
        Set<ConstraintViolation<BirthDay>> result = validator.validate(b);
        assertThat(result.size(), is(2));
    }

    @Test
    public void test_validMultipleExistInCodeList() {
        // check 0~12
        {
            Order order = new Order(0);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.isEmpty(), is(true));
        }
        {
            Order order = new Order(6);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.isEmpty(), is(true));
        }
        {
            Order order = new Order(12);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.isEmpty(), is(true));
        }
    }

    @Test
    public void test_invalidMultipleExistInCodeList() {
        {
            Order order = new Order(1);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(2);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(4);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(5);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(7);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(8);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(9);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
        }
        {
            Order order = new Order(10);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(1));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            Order order = new Order(11);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
        {
            // out of range!
            Order order = new Order(18);
            Set<ConstraintViolation<Order>> result = validator.validate(order);
            assertThat(result.size(), is(2));
            SortedSet<String> messages = new TreeSet<String>();
            for (ConstraintViolation<Order> violation : result) {
                messages.add(violation.getMessage());
            }
            Iterator<String> iterator = messages.iterator();
            assertThat(iterator.next(), is("number must be even"));
            assertThat(iterator.next(), is("number must be multiples of 3"));
        }
    }

    @Test
    public void issue16_testMessage() {
        Person p = new Person();
        p.gender = "X"; // invalid value
        p.lang = "JP";
        Set<ConstraintViolation<Person>> result = validator.validate(p);
        assertThat(result.size(), is(1));
        ConstraintViolation<Person> error = result.iterator().next();
        assertThat(error.getMessageTemplate(), is(
                "{org.terasoluna.gfw.common.codelist.ExistInCodeList.message}"));
        assertThat(error.getMessage(), is("Does not exist in CD_GENDER"));
    }

    @Test
    public void issue401_testMethodValidationByValidValue() {
        String actualLabel = codeService.getGenderLabel("M"); // call a method using valid code value
        assertThat(actualLabel, is("Male"));
    }

    @Test
    public void issue401_testMethodValidationByInvalidValue() {
        try {
            codeService.getGenderLabel("U"); // call a method using invalid code value
            fail("should be become a validation error.");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> actualViolations = e
                    .getConstraintViolations();
            assertThat(actualViolations.size(), is(1));
            assertThat(actualViolations.iterator().next().getMessage(), is(
                    "Does not exist in CD_GENDER"));
        }
    }

    @Test
    public void testCollectionValid() {
        Role r = new Role();
        r.setRoles(Arrays.asList("M", "A", "U"));

        Set<ConstraintViolation<Role>> violations = validator.validate(r);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testCollectionSingleElementInvalid() {
        Role r = new Role();
        r.setRoles(Arrays.asList("M", "A", "T"));

        Set<ConstraintViolation<Role>> violations = validator.validate(r);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(1));
    }

    @Test
    public void testCollectionMultipleElementInvalid() {
        Role r = new Role();
        r.setRoles(Arrays.asList("S", "A", "T"));

        Set<ConstraintViolation<Role>> violations = validator.validate(r);

        assertThat(violations, is(notNullValue()));
        assertThat(violations.size(), is(2));
    }

    @Validated
    public interface CodeService {
        String getGenderLabel(
                @ExistInCodeList(codeListId = "CD_GENDER") String genderCode);
    }

    @Service
    public static class CodeServiceImpl implements CodeService {
        @Inject
        @Named("CD_GENDER")
        CodeList genderCodeList;

        @Override
        public String getGenderLabel(String genderCode) {
            return genderCodeList.asMap().get(genderCode);
        }
    }

}

class Person {
    @ExistInCodeList(codeListId = "CD_GENDER")
    public String gender;

    @ExistInCodeList(codeListId = "CD_LANG")
    public String lang;
}

class Customer {
    @ExistInCodeList(codeListId = "CD_GENDER")
    public Character gender;

    @ExistInCodeList(codeListId = "CD_LANG")
    public String lang;
}

class Employee {
    @ExistInCodeList(codeListId = "CD_GENDER")
    public StringBuilder gender;

    @ExistInCodeList(codeListId = "CD_LANG")
    public StringBuilder lang;
}

class BirthDay {
    @ExistInCodeList(codeListId = "CD_MONTH")
    @ExistInCodeList(codeListId = "CD_MONTH_FORMATTED", groups = FORMATTED.class)
    public Integer month;

    @ExistInCodeList(codeListId = "CD_DAY")
    @ExistInCodeList(codeListId = "CD_DAY_FORMATTED", groups = FORMATTED.class)
    public Long day;

    public interface FORMATTED {
    }
}

class Order {
    @ExistInCodeList(codeListId = "CD_MULTIPLES_OF_3", message = "number must be multiples of 3")
    @ExistInCodeList(codeListId = "CD_EVEN", message = "number must be even")
    private Integer orderNumber;

    public Order(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }
}

class Role {
    private List<@ExistInCodeList(codeListId = "CD_ROLE") String> roles;

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

}
