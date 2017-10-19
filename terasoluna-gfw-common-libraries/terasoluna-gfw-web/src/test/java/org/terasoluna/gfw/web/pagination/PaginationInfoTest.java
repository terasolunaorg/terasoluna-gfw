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
package org.terasoluna.gfw.web.pagination;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponents;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;

public class PaginationInfoTest {

    private Page<String> page;

    private final String pathTmpl = "terasoluna";

    private final String queryTmpl = "value=test&page={page}&size={size}";

    @Before
    public void setup() {

        // Note:
        // The page property of PageRequest is 0 start.
        // For example:
        //
        // 0(1 page) : 1-5
        // 1(2 page) : 6-10
        // 2(3 page) : 11-15
        // 3(4 page) : 16-20
        // 4(5 page) : 21-25
        // 5(6 page) : 26-30
        // 6(7 page) : 31-35
        // ...
        PageRequest pageable = new PageRequest(5, 5);

        // set up the Page object for test.
        //
        // current page : 6 page (26-30)
        // total page : 7 page (total : 31)
        // (not first page and not last page)

        // 5 dummy record for 6 page(26-30)
        List<String> mockedList = new ArrayList<String>();
        mockedList.add("terasoluna");
        mockedList.add("terasoluna");
        mockedList.add("terasoluna");
        mockedList.add("terasoluna");
        mockedList.add("terasoluna");
        page = new PageImpl<String>(mockedList, pageable, 31L);
    }

    /**
     * Sort Null set
     */
    @Test
    public void testCreateAttributeMap_SortNull() {

        int page = 1;
        int size = 1;

        // run
        Map<String, Object> attributesMap = PaginationInfo.createAttributeMap(
                page, size, null);

        // assert
        assertThat(Integer.valueOf(attributesMap.get("page").toString()), is(
                page));
        assertThat(Integer.valueOf(attributesMap.get("size").toString()), is(
                size));
        assertNull(attributesMap.get("sortOrderProperty"));
        assertNull(attributesMap.get("sortOrderDirection"));
    }

    /**
     * Sort Set
     */
    @Test
    public void testCreateAttributeMap_SortSet() {

        int page = 1;
        int size = 1;
        Sort mockedSort = new Sort(Direction.DESC, "id");

        // run
        Map<String, Object> attributesMap = PaginationInfo.createAttributeMap(
                page, size, mockedSort);

        // assert
        assertThat(Integer.valueOf(attributesMap.get("page").toString()), is(
                page));
        assertThat(Integer.valueOf(attributesMap.get("size").toString()), is(
                size));
        assertThat(attributesMap.get("sortOrderProperty").toString(), is("id"));
        assertThat(attributesMap.get("sortOrderDirection").toString(), is(
                "DESC"));
    }

    @Test
    public void testCreateAttributeMapfail() {
        // set up
        int page = 1;
        int size = 1;

        Sort sort = mock(Sort.class);
        Iterator<Order> orders = mock(Iterator.class);
        when(sort.iterator()).thenReturn(orders);
        when(orders.hasNext()).thenReturn(false);

        // run
        Map<String, Object> attributesMap = PaginationInfo.createAttributeMap(
                page, size, sort);

        // assert
        assertThat(Integer.valueOf(attributesMap.get("page").toString()), is(
                page));
        assertThat(Integer.valueOf(attributesMap.get("size").toString()), is(
                size));
        assertNull(attributesMap.get("sortOrderProperty"));
        assertNull(attributesMap.get("sortOrderDirection"));
    }

    @Test
    public void testGetCurrent() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        int currentNum = info.getCurrent();

        // assert
        assertThat(currentNum, is(5));
    }

    @Test
    public void testGetPathTmpl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        String path = info.getPathTmpl();

        // assert
        assertThat(path, is(pathTmpl));

    }

    @Test
    public void testGetQueryTmpl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        String query = info.getQueryTmpl();

        // assert
        assertThat(query, is(queryTmpl));
    }

    @Test
    public void testGetMaxDisplayCount() {
        // parameter and expected
        int maxDisplayCount = 0;

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, maxDisplayCount);

        // run
        int getMaxCountNum = info.getMaxDisplayCount();

        // assert
        assertThat(getMaxCountNum, is(maxDisplayCount));
    }

    @Test
    public void testGetPageUri() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        UriComponents pathQueryStr = info.getPageUri();

        // expected
        String expectedUri = pathTmpl + "?" + queryTmpl;

        // assert
        assertThat(pathQueryStr.toUriString(), is(expectedUri));
    }

    @Test
    public void testGetFirstUrl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=0&size=5";

        // run
        String firstURL = info.getFirstUrl();

        // assert
        assertThat(firstURL, is(expectedURL));
    }

    @Test
    public void testGetLastUrl() {
        List<String> mockedList = new ArrayList<String>();
        PageRequest pageable = new PageRequest(2, 2);
        page = new PageImpl<String>(mockedList, pageable, 4L);

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=1&size=2";

        // run
        String lastURL = info.getLastUrl();

        // assert
        assertThat(lastURL, is(expectedURL));
    }

    @Test
    public void testGetPreviousUrl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=4&size=5";

        // run
        String previousURL = info.getPreviousUrl();

        // assert
        assertThat(previousURL, is(expectedURL));
    }

    @Test
    public void testGetNextUrl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=6&size=5";

        // run
        String nextURL = info.getNextUrl();

        // assert
        assertThat(nextURL, is(expectedURL));
    }

    /**
     * current page not first
     */
    @Test
    public void testIsFirstPage_NotFirst() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isFirstPage();

        // assert
        assertFalse(result);
    }

    /**
     * current page first
     */
    @Test
    public void testIsFirstPage_First() {
        // parameter
        List<String> mockedList = new ArrayList<String>();
        page = new PageImpl<String>(mockedList);
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isFirstPage();

        // assert
        assertTrue(result);
    }

    /**
     * current page not last
     */
    @Test
    public void testIsLastPage_NotLast() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isLastPage();

        // assert
        assertFalse(result);
    }

    /**
     * current page last
     */
    @Test
    public void testIsLastPage_Last() {
        // parameter
        List<String> mockedList = new ArrayList<String>();
        PageRequest pageable = new PageRequest(2, 2);
        page = new PageImpl<String>(mockedList, pageable, 6L);
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isLastPage();

        // assert
        assertTrue(result);
    }

    /**
     * parameter number current page
     */
    @Test
    public void testIsCurrent_Current() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isCurrent(5);

        // assert
        assertTrue(result);
    }

    /**
     * parameter number not current page
     */
    @Test
    public void testIsCurrent_NotCurrent() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isCurrent(1);

        // assert
        assertFalse(result);
    }

    /**
     * total pages is smaller than max display count
     */
    @Test
    public void testGetBeginAndEnd_totalPagesIsSmallerThanMaxDisplayCount() {
        // create parameter
        int maxDisplayCount = 10;

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, maxDisplayCount);

        // run
        BeginAndEnd endNumBig = info.getBeginAndEnd();

        // assert
        assertThat(endNumBig.getBegin(), is(0));
        assertThat(endNumBig.getEnd(), is(6));
    }

    /**
     * total pages is greater than max display count
     */
    @Test
    public void testGetBeginAndEnd_totalPagesIsGreaterThanMaxDisplayCount() {
        // create parameter
        int maxDisplayCount = 3;

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, maxDisplayCount);

        // run
        BeginAndEnd endNumBig = info.getBeginAndEnd();

        // assert
        assertThat(endNumBig.getBegin(), is(4));
        assertThat(endNumBig.getEnd(), is(6));
    }

    /**
     * max display count is zero (page link that move to a specific page is not displayed)
     */
    @Test
    public void testGetBeginAndEnd_maxDisplayCountIsZero() {
        // create parameter
        int maxDisplayCount = 0;

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, maxDisplayCount);

        // run
        BeginAndEnd endNumBig = info.getBeginAndEnd();

        // assert
        assertThat(endNumBig.getBegin(), is(5));
        assertThat(endNumBig.getEnd(), is(4));
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsEmpty() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=0&size=5";

        // assert
        // That the criteria query is not append
        assertThat(info.getPageUrl(0), is(expectedURL));
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "a=%2B", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=0&size=5&a=%2B";

        // assert
        // That the criteria query is append
        assertThat(info.getPageUrl(0), is(expectedURL));
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified_startWithQuestionMark() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "?a=%2B", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=1&size=5&a=%2B";

        // assert
        // That the question-mark(?) is remove
        assertThat(info.getPageUrl(1), is(expectedURL));
        assertThat(info.getCriteriaQuery(), is("a=%2B"));
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified_startWithAndMark() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "&a=%2B", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=2&size=5&a=%2B";

        // assert
        // That the and-mark(&) is remove
        assertThat(info.getPageUrl(2), is(expectedURL));
        assertThat(info.getCriteriaQuery(), is("a=%2B"));
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified_queryTmplNotSpecified() {
        PaginationInfo info = new PaginationInfo(page, "/{page}/{size}", null, "a=<>&\"'a1", false, 10);

        // expected
        String expectedURL = "/3/5?a=&lt;&gt;&amp;&quot;&#39;a1";

        // assert
        // That the question-mark(?) is append
        assertThat(info.getPageUrl(3), is(expectedURL));
    }

    @Test
    public void issue12_testGetPageUrl_disableHtmlEscapeOfCriteriaQueryIsTrue_queryTmplNotSpecified() {
        PaginationInfo info = new PaginationInfo(page, "/{page}/{size}", null, "a=<>&\"'a1", true, 10);

        // expected
        String expectedURL = "/3/5?a=<>&\"'a1";

        // assert
        // That the question-mark(?) is append
        assertThat(info.getPageUrl(3), is(expectedURL));
    }

}
