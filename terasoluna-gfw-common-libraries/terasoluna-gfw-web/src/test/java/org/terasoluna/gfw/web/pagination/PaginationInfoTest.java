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
package org.terasoluna.gfw.web.pagination;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void before() {

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
        PageRequest pageable = PageRequest.of(5, 5);

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
        Map<String, Object> attributesMap = PaginationInfo.createAttributeMap(page, size, null);

        // assert
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("page", page);
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("size", size);
        assertThat(attributesMap, not(hasKey("sortOrderProperty")));
        assertThat(attributesMap, not(hasKey("sortOrderDirection")));
    }

    /**
     * Sort Set
     */
    @Test
    public void testCreateAttributeMap_SortSet() {

        int page = 1;
        int size = 1;
        Sort mockedSort = Sort.by(Direction.DESC, "id");

        // run
        Map<String, Object> attributesMap =
                PaginationInfo.createAttributeMap(page, size, mockedSort);

        // assert
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("page", page);
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("size", size);
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("sortOrderProperty", "id");
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("sortOrderDirection", "DESC");
    }

    @Test
    public void testCreateAttributeMapfail() {
        // set up
        int page = 1;
        int size = 1;

        Sort sort = mock(Sort.class);
        @SuppressWarnings("unchecked")
        Iterator<Order> orders = mock(Iterator.class);
        when(sort.iterator()).thenReturn(orders);
        when(orders.hasNext()).thenReturn(false);

        // run
        Map<String, Object> attributesMap = PaginationInfo.createAttributeMap(page, size, sort);

        // assert
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("page", page);
        org.assertj.core.api.Assertions.assertThat(attributesMap).containsEntry("size", size);
        assertThat(attributesMap, not(hasKey("sortOrderProperty")));
        assertThat(attributesMap, not(hasKey("sortOrderDirection")));
    }

    @Test
    public void testGetCurrent() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        int currentNum = info.getCurrent();

        // assert
        org.assertj.core.api.Assertions.assertThat(currentNum).isEqualTo(5);
    }

    @Test
    public void testGetPathTmpl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        String path = info.getPathTmpl();

        // assert
        org.assertj.core.api.Assertions.assertThat(path).isEqualTo(pathTmpl);

    }

    @Test
    public void testGetQueryTmpl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        String query = info.getQueryTmpl();

        // assert
        org.assertj.core.api.Assertions.assertThat(query).isEqualTo(queryTmpl);
    }

    @Test
    public void testGetMaxDisplayCount() {
        // parameter and expected
        int maxDisplayCount = 0;

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, maxDisplayCount);

        // run
        int getMaxCountNum = info.getMaxDisplayCount();

        // assert
        org.assertj.core.api.Assertions.assertThat(getMaxCountNum).isEqualTo(maxDisplayCount);
    }

    @Test
    public void testGetPageUri() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 10);

        // run
        UriComponents pathQueryStr = info.getPageUri();

        // expected
        String expectedUri = pathTmpl + "?" + queryTmpl;

        // assert
        org.assertj.core.api.Assertions.assertThat(pathQueryStr.toUriString()).isEqualTo(expectedUri);
    }

    @Test
    public void testGetFirstUrl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=0&size=5";

        // run
        String firstURL = info.getFirstUrl();

        // assert
        org.assertj.core.api.Assertions.assertThat(firstURL).isEqualTo(expectedURL);
    }

    @Test
    public void testGetLastUrl() {
        List<String> mockedList = new ArrayList<String>();
        PageRequest pageable = PageRequest.of(2, 2);
        page = new PageImpl<String>(mockedList, pageable, 4L);

        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=1&size=2";

        // run
        String lastURL = info.getLastUrl();

        // assert
        org.assertj.core.api.Assertions.assertThat(lastURL).isEqualTo(expectedURL);
    }

    @Test
    public void testGetPreviousUrl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=4&size=5";

        // run
        String previousURL = info.getPreviousUrl();

        // assert
        org.assertj.core.api.Assertions.assertThat(previousURL).isEqualTo(expectedURL);
    }

    @Test
    public void testGetNextUrl() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // expected
        String expectedURL = "terasoluna?value=test&page=6&size=5";

        // run
        String nextURL = info.getNextUrl();

        // assert
        org.assertj.core.api.Assertions.assertThat(nextURL).isEqualTo(expectedURL);
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
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(false);
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
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(true);
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
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(false);
    }

    /**
     * current page last
     */
    @Test
    public void testIsLastPage_Last() {
        // parameter
        List<String> mockedList = new ArrayList<String>();
        PageRequest pageable = PageRequest.of(2, 2);
        page = new PageImpl<String>(mockedList, pageable, 6L);
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, 0);

        // run
        boolean result = info.isLastPage();

        // assert
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(true);
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
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(true);
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
        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(false);
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
        org.assertj.core.api.Assertions.assertThat(endNumBig.getBegin()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(endNumBig.getEnd()).isEqualTo(6);
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
        org.assertj.core.api.Assertions.assertThat(endNumBig.getBegin()).isEqualTo(4);
        org.assertj.core.api.Assertions.assertThat(endNumBig.getEnd()).isEqualTo(6);
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
        org.assertj.core.api.Assertions.assertThat(endNumBig.getBegin()).isEqualTo(5);
        org.assertj.core.api.Assertions.assertThat(endNumBig.getEnd()).isEqualTo(4);
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsEmpty() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=0&size=5";

        // assert
        // That the criteria query is not append
        org.assertj.core.api.Assertions.assertThat(info.getPageUrl(0)).isEqualTo(expectedURL);
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "a=%2B", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=0&size=5&a=%2B";

        // assert
        // That the criteria query is append
        org.assertj.core.api.Assertions.assertThat(info.getPageUrl(0)).isEqualTo(expectedURL);
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified_startWithQuestionMark() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "?a=%2B", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=1&size=5&a=%2B";

        // assert
        // That the question-mark(?) is remove
        org.assertj.core.api.Assertions.assertThat(info.getPageUrl(1)).isEqualTo(expectedURL);
        org.assertj.core.api.Assertions.assertThat(info.getCriteriaQuery()).isEqualTo("a=%2B");
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified_startWithAndMark() {
        PaginationInfo info = new PaginationInfo(page, pathTmpl, queryTmpl, "&a=%2B", false, 10);

        // expected
        String expectedURL = "terasoluna?value=test&page=2&size=5&a=%2B";

        // assert
        // That the and-mark(&) is remove
        org.assertj.core.api.Assertions.assertThat(info.getPageUrl(2)).isEqualTo(expectedURL);
        org.assertj.core.api.Assertions.assertThat(info.getCriteriaQuery()).isEqualTo("a=%2B");
    }

    @Test
    public void issue12_testGetPageUrl_criteriaQueryIsSpecified_queryTmplNotSpecified() {
        PaginationInfo info =
                new PaginationInfo(page, "/{page}/{size}", null, "a=<>&\"'a1", false, 10);

        // expected
        String expectedURL = "/3/5?a=&lt;&gt;&amp;&quot;&#39;a1";

        // assert
        // That the question-mark(?) is append
        org.assertj.core.api.Assertions.assertThat(info.getPageUrl(3)).isEqualTo(expectedURL);
    }

    @Test
    public void issue12_testGetPageUrl_disableHtmlEscapeOfCriteriaQueryIsTrue_queryTmplNotSpecified() {
        PaginationInfo info =
                new PaginationInfo(page, "/{page}/{size}", null, "a=<>&\"'a1", true, 10);

        // expected
        String expectedURL = "/3/5?a=<>&\"'a1";

        // assert
        // That the question-mark(?) is append
        org.assertj.core.api.Assertions.assertThat(info.getPageUrl(3)).isEqualTo(expectedURL);
    }

}
