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
package org.terasoluna.gfw.web.el;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.bind.WebDataBinder;

public class ObjectToMapConverterTest {
    ObjectToMapConverter converter =
            new ObjectToMapConverter(new DefaultFormattingConversionService());

    @Test
    public void testConvert0_SimpleJavaBean() throws Exception {
        Map<String, String> map = converter.convert(new SearchUserForm0("yamada", 20));
        assertThat(map).hasSize(2);
        assertThat(map).containsEntry("name", "yamada");
        assertThat(map).containsEntry("age", "20");

        // check reverse conversion
        SearchUserForm0 form = new SearchUserForm0();
        WebDataBinder binder = new WebDataBinder(form);
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getName()).isEqualTo("yamada");
        assertThat(form.getAge()).isEqualTo(20);
    }

    @Test
    public void testConvert0_With_Prefix() throws Exception {
        Map<String, String> map = converter.convert("pre", new SearchUserForm0("yamada", 20));
        assertThat(map).hasSize(2);
        assertThat(map).containsEntry("pre.name", "yamada");
        assertThat(map).containsEntry("pre.age", "20");
    }

    @Test
    public void testConvert1_NestedJavaBean() throws Exception {
        Map<String, String> map = converter
                .convert(new SearchUserForm1(new SearchUserCriteriaForm1("yamada", 20), true));
        assertThat(map).hasSize(3);
        assertThat(map).containsEntry("criteria.name", "yamada");
        assertThat(map).containsEntry("criteria.age", "20");
        assertThat(map).containsEntry("rememberCriteria", "true");

        // check reverse conversion
        SearchUserForm1 form = new SearchUserForm1();
        WebDataBinder binder = new WebDataBinder(form);
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getCriteria()).isNotNull();
        assertThat(form.getCriteria().getName()).isEqualTo("yamada");
        assertThat(form.getCriteria().getAge()).isEqualTo(20);
        assertThat(form.isRememberCriteria()).isEqualTo(true);
    }

    @Test
    public void testConvert2_ListOfJavaBean() throws Exception {
        Map<String, String> map =
                converter
                        .convert(new BatchUpdateUserForm2(
                                Arrays.asList(new UpdateUserCriteriaForm2("yamada", 20),
                                        new UpdateUserCriteriaForm2("tanaka", 50)),
                                LogicalOperator2.AND));
        assertThat(map).hasSize(5);
        assertThat(map).containsEntry("criteria[0].name", "yamada");
        assertThat(map).containsEntry("criteria[0].age", "20");
        assertThat(map).containsEntry("criteria[1].name", "tanaka");
        assertThat(map).containsEntry("criteria[1].age", "50");
        assertThat(map).containsEntry("operator", "AND");

        // check reverse conversion
        BatchUpdateUserForm2 form = new BatchUpdateUserForm2();
        WebDataBinder binder = new WebDataBinder(form);
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getCriteria()).isNotNull();
        assertThat(form.getCriteria()).hasSize(2);
        assertThat(form.getCriteria().get(0).getName()).isEqualTo("yamada");
        assertThat(form.getCriteria().get(0).getAge()).isEqualTo(20);
        assertThat(form.getCriteria().get(1).getName()).isEqualTo("tanaka");
        assertThat(form.getCriteria().get(1).getAge()).isEqualTo(50);
        assertThat(form.getOperator()).isEqualTo(LogicalOperator2.AND);
    }

    @Test
    public void testConvert3_SimpleJavaBeanAndListOfJavaBean() throws Exception {
        Map<String, String> map = converter.convert(
                new SearchAndBatchUpdateUserForm3(new SearchUserCriteriaForm3("suzuki", 30),
                        Arrays.asList(new User3("yamada", 20), new User3("tanaka", 50))));
        assertThat(map).hasSize(6);
        assertThat(map).containsEntry("criteria.name", "suzuki");
        assertThat(map).containsEntry("criteria.age", "30");
        assertThat(map).containsEntry("users[0].name", "yamada");
        assertThat(map).containsEntry("users[0].age", "20");
        assertThat(map).containsEntry("users[1].name", "tanaka");
        assertThat(map).containsEntry("users[1].age", "50");

        // check reverse conversion
        SearchAndBatchUpdateUserForm3 form = new SearchAndBatchUpdateUserForm3();
        WebDataBinder binder = new WebDataBinder(form);
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getCriteria()).isNotNull();
        assertThat(form.getCriteria().getName()).isEqualTo("suzuki");
        assertThat(form.getCriteria().getAge()).isEqualTo(30);
        assertThat(form.getUsers()).isNotNull();
        assertThat(form.getUsers()).hasSize(2);
        assertThat(form.getUsers().get(0).getName()).isEqualTo("yamada");
        assertThat(form.getUsers().get(0).getAge()).isEqualTo(20);
        assertThat(form.getUsers().get(1).getName()).isEqualTo("tanaka");
        assertThat(form.getUsers().get(1).getAge()).isEqualTo(50);
    }

    @Test
    public void testConvert4_MapOfJavaBean() throws Exception {
        Map<String, String> source = new LinkedHashMap<String, String>();
        source.put("aaa", "111");
        source.put("bbb", "222");
        source.put("ccc", "333");
        Map<String, String> map = converter.convert(new SearchForm4(source));

        assertThat(map).hasSize(3);
        assertThat(map).containsEntry("etc[aaa]", "111");
        assertThat(map).containsEntry("etc[bbb]", "222");
        assertThat(map).containsEntry("etc[ccc]", "333");

        // check reverse conversion
        SearchForm4 form = new SearchForm4();
        WebDataBinder binder = new WebDataBinder(form);
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getEtc()).isNotNull();
        assertThat(form.getEtc()).hasSize(3);
        assertThat(form.getEtc()).containsEntry("aaa", "111");
        assertThat(form.getEtc()).containsEntry("bbb", "222");
        assertThat(form.getEtc()).containsEntry("ccc", "333");
    }

    @Test
    public void testConvert5_at_DateTimeFormat() throws Exception {
        Date date1 = Date.from(LocalDateTime.of(2015, 4, 1, 0, 0).toInstant(ZoneOffset.UTC));
        LocalDate localDate1 = LocalDate.of(2015, 6, 10);
        Date date2 = Date.from(LocalDateTime.of(2015, 5, 1, 0, 0).toInstant(ZoneOffset.UTC));
        LocalDate localDate2 = LocalDate.of(2015, 7, 10);

        Map<String, String> map = converter
                .convert(new DateForm5(date1, localDate1, new DateFormItem5(date2, localDate2)));
        assertThat(map).hasSize(4);
        assertThat(map).containsEntry("date", "2015-04-01");
        assertThat(map).containsEntry("localDate", "2015-06-10");
        assertThat(map).containsEntry("item.date", "2015-05-01");
        assertThat(map).containsEntry("item.localDate", "2015-07-10");

        DateForm5 form = new DateForm5();
        WebDataBinder binder = new WebDataBinder(form);
        binder.setConversionService(new DefaultFormattingConversionService());
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getDate()).isEqualTo(date1);
        assertThat(form.getLocalDate()).isEqualTo(localDate1);
        assertThat(form.getItem().getDate()).isEqualTo(date2);
        assertThat(form.getItem().getLocalDate()).isEqualTo(localDate2);
    }

    @Test
    public void testConvert6_Array() throws Exception {
        Map<String, String> map = converter.convert(new ArrayForm6(new int[] {1, 2, 3},
                new double[] {1.1, 1.2}, new byte[] {4, 5, 6}, new String[] {"a", "b", "c"},
                new ArrayFormItem6(new int[] {11, 12, 13}, new double[] {11.1, 11.2},
                        new byte[] {14, 15, 16}, new String[] {"d", "e", "f"})));
        assertThat(map).hasSize(22);
        assertThat(map).containsEntry("array1[0]", "1");
        assertThat(map).containsEntry("array1[1]", "2");
        assertThat(map).containsEntry("array1[2]", "3");
        assertThat(map).containsEntry("array2[0]", "1.1");
        assertThat(map).containsEntry("array2[1]", "1.2");
        assertThat(map).containsEntry("array3[0]", "4");
        assertThat(map).containsEntry("array3[1]", "5");
        assertThat(map).containsEntry("array3[2]", "6");
        assertThat(map).containsEntry("array4[0]", "a");
        assertThat(map).containsEntry("array4[1]", "b");
        assertThat(map).containsEntry("array4[2]", "c");
        assertThat(map).containsEntry("item.array1[0]", "11");
        assertThat(map).containsEntry("item.array1[1]", "12");
        assertThat(map).containsEntry("item.array1[2]", "13");
        assertThat(map).containsEntry("item.array2[0]", "11.1");
        assertThat(map).containsEntry("item.array2[1]", "11.2");
        assertThat(map).containsEntry("item.array3[0]", "14");
        assertThat(map).containsEntry("item.array3[1]", "15");
        assertThat(map).containsEntry("item.array3[2]", "16");
        assertThat(map).containsEntry("item.array4[0]", "d");
        assertThat(map).containsEntry("item.array4[1]", "e");
        assertThat(map).containsEntry("item.array4[2]", "f");

        ArrayForm6 form = new ArrayForm6();
        WebDataBinder binder = new WebDataBinder(form);
        binder.setConversionService(new DefaultFormattingConversionService());
        binder.bind(new MutablePropertyValues(map));
        assertThat(form.getArray1().length).isEqualTo(3);
        assertThat(form.getArray1()[0]).isEqualTo(1);
        assertThat(form.getArray1()[1]).isEqualTo(2);
        assertThat(form.getArray1()[2]).isEqualTo(3);
        assertThat(form.getArray2().length).isEqualTo(2);
        assertThat(form.getArray2()[0]).isEqualTo(1.1);
        assertThat(form.getArray2()[1]).isEqualTo(1.2);
        assertThat(form.getArray3().length).isEqualTo(3);
        assertThat(form.getArray3()[0]).isEqualTo((byte) 4);
        assertThat(form.getArray3()[1]).isEqualTo((byte) 5);
        assertThat(form.getArray3()[2]).isEqualTo((byte) 6);
        assertThat(form.getArray4().length).isEqualTo(3);
        assertThat(form.getArray4()[0]).isEqualTo("a");
        assertThat(form.getArray4()[1]).isEqualTo("b");
        assertThat(form.getArray4()[2]).isEqualTo("c");
        assertThat(form.getItem()).isNotNull();
        assertThat(form.getItem().getArray1().length).isEqualTo(3);
        assertThat(form.getItem().getArray1()[0]).isEqualTo(11);
        assertThat(form.getItem().getArray1()[1]).isEqualTo(12);
        assertThat(form.getItem().getArray1()[2]).isEqualTo(13);
        assertThat(form.getItem().getArray2().length).isEqualTo(2);
        assertThat(form.getItem().getArray2()[0]).isEqualTo(11.1);
        assertThat(form.getItem().getArray2()[1]).isEqualTo(11.2);
        assertThat(form.getItem().getArray3().length).isEqualTo(3);
        assertThat(form.getItem().getArray3()[0]).isEqualTo((byte) 14);
        assertThat(form.getItem().getArray3()[1]).isEqualTo((byte) 15);
        assertThat(form.getItem().getArray3()[2]).isEqualTo((byte) 16);
        assertThat(form.getItem().getArray4().length).isEqualTo(3);
        assertThat(form.getItem().getArray4()[0]).isEqualTo("d");
        assertThat(form.getItem().getArray4()[1]).isEqualTo("e");
        assertThat(form.getItem().getArray4()[2]).isEqualTo("f");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test7_Null() {
        assertThat(converter.convert(null)).isEqualTo((Map<String, String>) Collections.EMPTY_MAP);
    }

    @Test
    public void test8_LackingGetter() {
        assertThat(converter.convert(new LackingGetterForm8("aaa", "bbb")))
                .isEqualTo(Collections.singletonMap("value1", "aaa"));
    }

    @Test
    public void test9_propertiesIsNull() {
        NullValueForm9 form = new NullValueForm9();
        NestedForm9 nestedForm = new NestedForm9();
        form.setNestedForm2(nestedForm);
        Map<String, String> map = converter.convert(form);

        assertThat(map).hasSize(20);
        assertThat(map).containsEntry("_string", "");
        assertThat(map).containsEntry("_integer", "");
        assertThat(map).containsEntry("_date", "");
        assertThat(map).containsEntry("_localDate", "");
        assertThat(map).containsEntry("_customEnum", "");
        assertThat(map).containsEntry("_list", "");
        assertThat(map).containsEntry("_list2[0]", "");
        assertThat(map).containsEntry("_map", "");
        assertThat(map).containsEntry("_map2[key]", "");
        assertThat(map).containsEntry("_array", "");
        assertThat(map).containsEntry("_array2[0]", "");
        assertThat(map).containsEntry("_nestedForm1", "");
        assertThat(map).containsEntry("_nestedForm2.string", "");
        assertThat(map).containsEntry("_nestedForm2.integer", "");
        assertThat(map).containsEntry("_nestedForm2.list", "");
        assertThat(map).containsEntry("_nestedForm2.list2[0]", "");
        assertThat(map).containsEntry("_nestedForm2.map", "");
        assertThat(map).containsEntry("_nestedForm2.map2[key]", "");
        assertThat(map).containsEntry("_nestedForm2.array", "");
        assertThat(map).containsEntry("_nestedForm2.array2[0]", "");
    }

    @Test
    public void test10_propertiesIsEmptyElement() {
        EmptyElementForm10 form = new EmptyElementForm10();
        Map<String, String> map = converter.convert(form);

        assertThat(map).hasSize(4);
        assertThat(map).containsEntry("list", "");
        assertThat(map).containsEntry("array", "");
        assertThat(map).containsEntry("nestedForm.list", "");
        assertThat(map).containsEntry("nestedForm.array", "");
    }

    @Test
    public void testConvert11_Boolean() {
        BooleanForm11 form = new BooleanForm11(true, false, null);
        Map<String, String> map = converter.convert(form);

        assertThat(map).hasSize(3);
        assertThat(map).containsEntry("bool1", "true");
        assertThat(map).containsEntry("bool2", "false");
        assertThat(map).containsEntry("bool3", "");
    }

    public static class SearchUserForm0 {
        private String name;

        private Integer age;

        public SearchUserForm0(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public SearchUserForm0() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static class SearchUserForm1 {
        private SearchUserCriteriaForm1 criteria;

        private boolean rememberCriteria;

        public SearchUserForm1(SearchUserCriteriaForm1 criteria, boolean rememberCriteria) {
            this.criteria = criteria;
            this.rememberCriteria = rememberCriteria;
        }

        public SearchUserForm1() {}

        public SearchUserCriteriaForm1 getCriteria() {
            return criteria;
        }

        public void setCriteria(SearchUserCriteriaForm1 criteria) {
            this.criteria = criteria;
        }

        public boolean isRememberCriteria() {
            return rememberCriteria;
        }

        public void setRememberCriteria(boolean rememberCriteria) {
            this.rememberCriteria = rememberCriteria;
        }
    }

    public static class SearchUserCriteriaForm1 {
        private String name;

        private Integer age;

        public SearchUserCriteriaForm1(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public SearchUserCriteriaForm1() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static class BatchUpdateUserForm2 {
        private List<UpdateUserCriteriaForm2> criteria;

        private LogicalOperator2 operator;

        public BatchUpdateUserForm2(List<UpdateUserCriteriaForm2> criteria,
                LogicalOperator2 operator) {
            this.criteria = criteria;
            this.operator = operator;
        }

        public BatchUpdateUserForm2() {}

        public List<UpdateUserCriteriaForm2> getCriteria() {
            return criteria;
        }

        public void setCriteria(List<UpdateUserCriteriaForm2> criteria) {
            this.criteria = criteria;
        }

        public LogicalOperator2 getOperator() {
            return operator;
        }

        public void setOperator(LogicalOperator2 operator) {
            this.operator = operator;
        }
    }

    public static enum LogicalOperator2 {
        AND, OR
    }

    public static class UpdateUserCriteriaForm2 {
        private String name;

        private Integer age;

        public UpdateUserCriteriaForm2(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public UpdateUserCriteriaForm2() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static class SearchAndBatchUpdateUserForm3 {
        private SearchUserCriteriaForm3 criteria;

        private List<User3> users;

        public SearchAndBatchUpdateUserForm3(SearchUserCriteriaForm3 criteria, List<User3> users) {
            this.criteria = criteria;
            this.users = users;
        }

        public SearchAndBatchUpdateUserForm3() {}

        public SearchUserCriteriaForm3 getCriteria() {
            return criteria;
        }

        public void setCriteria(SearchUserCriteriaForm3 criteria) {
            this.criteria = criteria;
        }

        public List<User3> getUsers() {
            return users;
        }

        public void setUsers(List<User3> users) {
            this.users = users;
        }
    }

    public static class SearchUserCriteriaForm3 {
        private String name;

        private Integer age;

        public SearchUserCriteriaForm3(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public SearchUserCriteriaForm3() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static class User3 {
        private String name;

        private Integer age;

        public User3(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public User3() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static class SearchForm4 {
        private Map<String, String> etc;

        public SearchForm4(Map<String, String> etc) {
            this.etc = etc;
        }

        public SearchForm4() {}

        public Map<String, String> getEtc() {
            return etc;
        }

        public void setEtc(Map<String, String> etc) {
            this.etc = etc;
        }
    }

    public static class DateForm5 {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date date;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate localDate;

        private DateFormItem5 item;

        public DateForm5() {}

        public DateForm5(Date date, LocalDate localDate, DateFormItem5 item) {
            this.date = date;
            this.localDate = localDate;
            this.item = item;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public DateFormItem5 getItem() {
            return item;
        }

        public void setItem(DateFormItem5 item) {
            this.item = item;
        }
    }

    public static class DateFormItem5 {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date date;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate localDate;

        public DateFormItem5() {}

        public DateFormItem5(Date date, LocalDate localDate) {
            this.date = date;
            this.localDate = localDate;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }
    }

    public static class ArrayForm6 {
        private int[] array1;

        private double[] array2;

        private byte[] array3;

        private String[] array4;

        private ArrayFormItem6 item;

        public ArrayForm6() {}

        public ArrayForm6(int[] array1, double[] array2, byte[] array3, String[] array4,
                ArrayFormItem6 item) {
            this.array1 = array1;
            this.array2 = array2;
            this.array3 = array3;
            this.array4 = array4;
            this.item = item;
        }

        public int[] getArray1() {
            return array1;
        }

        public void setArray1(int[] array1) {
            this.array1 = array1;
        }

        public double[] getArray2() {
            return array2;
        }

        public void setArray2(double[] array2) {
            this.array2 = array2;
        }

        public byte[] getArray3() {
            return array3;
        }

        public void setArray3(byte[] array3) {
            this.array3 = array3;
        }

        public String[] getArray4() {
            return array4;
        }

        public void setArray4(String[] array4) {
            this.array4 = array4;
        }

        public ArrayFormItem6 getItem() {
            return item;
        }

        public void setItem(ArrayFormItem6 item) {
            this.item = item;
        }
    }

    public static class ArrayFormItem6 {
        private int[] array1;

        private double[] array2;

        private byte[] array3;

        private String[] array4;

        public ArrayFormItem6() {}

        public ArrayFormItem6(int[] array1, double[] array2, byte[] array3, String[] array4) {
            this.array1 = array1;
            this.array2 = array2;
            this.array3 = array3;
            this.array4 = array4;
        }

        public int[] getArray1() {
            return array1;
        }

        public void setArray1(int[] array1) {
            this.array1 = array1;
        }

        public double[] getArray2() {
            return array2;
        }

        public void setArray2(double[] array2) {
            this.array2 = array2;
        }

        public byte[] getArray3() {
            return array3;
        }

        public void setArray3(byte[] array3) {
            this.array3 = array3;
        }

        public String[] getArray4() {
            return array4;
        }

        public void setArray4(String[] array4) {
            this.array4 = array4;
        }
    }

    public static class LackingGetterForm8 {
        private String value1;

        @SuppressWarnings("unused")
        private String value2;

        public LackingGetterForm8() {}

        public LackingGetterForm8(String value1, String value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue1() {
            return value1;
        }
    }

    public static class NullValueForm9 {
        private String string;

        private Integer integer;

        private Date date;

        private LocalDate localDate;

        private CustomEnum9 customEnum;

        private List<String> list;

        private List<String> list2 = Collections.singletonList(null);

        private Map<String, String> map;

        private Map<String, String> map2 = Collections.singletonMap("key", null);

        private int[] array;

        private String[] array2 = new String[] {null};

        private NestedForm9 nestedForm1;

        private NestedForm9 nestedForm2;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public CustomEnum9 getCustomEnum() {
            return customEnum;
        }

        public void setCustomEnum(CustomEnum9 customEnum) {
            this.customEnum = customEnum;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public List<String> getList2() {
            return list2;
        }

        public void setList2(List<String> list2) {
            this.list2 = list2;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        public Map<String, String> getMap2() {
            return map2;
        }

        public void setMap2(Map<String, String> map2) {
            this.map2 = map2;
        }

        public int[] getArray() {
            return array;
        }

        public void setArray(int[] array) {
            this.array = array;
        }

        public String[] getArray2() {
            return array2;
        }

        public void setArray2(String[] array2) {
            this.array2 = array2;
        }

        public NestedForm9 getNestedForm1() {
            return nestedForm1;
        }

        public void setNestedForm1(NestedForm9 nestedForm1) {
            this.nestedForm1 = nestedForm1;
        }

        public NestedForm9 getNestedForm2() {
            return nestedForm2;
        }

        public void setNestedForm2(NestedForm9 nestedForm2) {
            this.nestedForm2 = nestedForm2;
        }
    }

    public enum CustomEnum9 {
        VALUE1, VALUE2
    }

    public static class NestedForm9 {
        private String string;

        private Integer integer;

        private List<String> list;

        private List<String> list2 = Collections.singletonList(null);

        private Map<String, String> map;

        private Map<String, String> map2 = Collections.singletonMap("key", null);

        private int[] array;

        private String[] array2 = new String[] {null};

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public List<String> getList2() {
            return list2;
        }

        public void setList2(List<String> list2) {
            this.list2 = list2;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        public Map<String, String> getMap2() {
            return map2;
        }

        public void setMap2(Map<String, String> map2) {
            this.map2 = map2;
        }

        public int[] getArray() {
            return array;
        }

        public void setArray(int[] array) {
            this.array = array;
        }

        public String[] getArray2() {
            return array2;
        }

        public void setArray2(String[] array2) {
            this.array2 = array2;
        }

    }

    public static class EmptyElementForm10 {
        private List<String> list = Collections.emptyList();

        private Map<String, String> map = Collections.emptyMap();

        private int[] array = new int[0];

        private NestedForm10 nestedForm = new NestedForm10();

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        public int[] getArray() {
            return array;
        }

        public void setArray(int[] array) {
            this.array = array;
        }

        public NestedForm10 getNestedForm() {
            return nestedForm;
        }

        public void setNestedForm(NestedForm10 nestedForm) {
            this.nestedForm = nestedForm;
        }
    }

    public static class NestedForm10 {
        private List<String> list = Collections.emptyList();

        private Map<String, String> map = Collections.emptyMap();

        private int[] array = new int[0];

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        public int[] getArray() {
            return array;
        }

        public void setArray(int[] array) {
            this.array = array;
        }
    }

    public static class BooleanForm11 {
        private Boolean bool1;

        private Boolean bool2;

        private Boolean bool3;

        public BooleanForm11(Boolean bool1, Boolean bool2, Boolean bool3) {
            this.bool1 = bool1;
            this.bool2 = bool2;
            this.bool3 = bool3;
        }

        public Boolean getBool1() {
            return bool1;
        }

        public void setBool1(Boolean bool1) {
            this.bool1 = bool1;
        }

        public Boolean getBool2() {
            return bool2;
        }

        public void setBool2(Boolean bool2) {
            this.bool2 = bool2;
        }

        public Boolean getBool3() {
            return bool3;
        }

        public void setBool3(Boolean bool3) {
            this.bool3 = bool3;
        }

    }

}
