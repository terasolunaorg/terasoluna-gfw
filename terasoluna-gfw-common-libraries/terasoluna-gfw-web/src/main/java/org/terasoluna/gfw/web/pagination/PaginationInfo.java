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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.terasoluna.gfw.web.util.HtmlEscapeUtils;

/**
 * Holds all the information required by the {@code PaginationTag} to display pagination functionality. <br>
 */
public class PaginationInfo {

    /**
     * Stores begin and end page of pagination.
     */
    public static class BeginAndEnd {
        /**
         * begin page of pagination.
         */
        private final int begin;

        /**
         * end of page of pagination.
         */
        private final int end;

        /**
         * Constructor.
         * @param begin begin page of pagination.
         * @param end end of page of pagination.
         */
        public BeginAndEnd(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        /**
         * Returns begin of page of pagination.
         * @return begin of page of pagination.
         */
        public int getBegin() {
            return begin;
        }

        /**
         * Returns end of page of pagination.
         * @return end of page of pagination.
         */
        public int getEnd() {
            return end;
        }
    }

    /**
     * Indicates the default value for maximum display count<br>
     */
    public static final int DEFAULT_MAX_DISPLAY_COUNT = 10;

    /**
     * Default path template of pagination
     */
    public static final String DEFAULT_PATH_TEMPLATE = "";

    /**
     * Default query template of pagination
     */
    public static final String DEFAULT_QUERY_TEMPLATE = "page={page}&size={size}";

    /**
     * page key
     */
    public static final String PAGE_ATTR = "page";

    /**
     * Key name of size
     */
    public static final String SIZE_ATTR = "size";

    /**
     * Property key
     */
    public static final String SORT_ORDER_PROPERTY_ATTR = "sortOrderProperty";

    /**
     * Property key
     */
    public static final String SORT_ORDER_DIRECTION_ATTR = "sortOrderDirection";

    /**
     * Anchor Element
     */
    public static final String A_ELM = "a";

    /**
     * href attribute
     */
    public static final String HREF_ATTR = "href";

    /**
     * class attribute
     */
    public static final String CLASS_ATTR = "class";

    /**
     * Outer tag of HTML that make up pagination
     */
    public static final String DEFAULT_OUTER_ELM = "ul";

    /**
     * Value of "class" attribute of outer tag of pagination
     */
    public static final String DEFAULT_OUTER_CLASS = "";

    /**
     * Inner tag of HTML that make up pagination
     */
    public static final String DEFAULT_INNER_ELM = "li";

    /**
     * Display text of the link of back to the top
     */
    public static final String DEFAULT_FIRST_LINK_TEXT = "&lt;&lt;";

    /**
     * Display text of the link of back to previous
     */
    public static final String DEFAULT_PREVIOUS_LINK_TEXT = "&lt;";

    /**
     * Display text of the link to proceed to the next
     */
    public static final String DEFAULT_NEXT_LINK_TEXT = "&gt;";

    /**
     * Display text of the link to forward to the last page
     */
    public static final String DEFAULT_LAST_LINK_TEXT = "&gt;&gt;";

    /**
     * Anchor link which is set when the link is disabled.
     */
    public static final String DEFAULT_DISABLED_HREF = "javascript:void(0)";

    /**
     * Class name that enables the link
     */
    public static final String DEFAULT_ACTIVE_CLASS = "active";

    /**
     * Class name that enables the link
     */
    public static final String DEFAULT_DISABLED_CLASS = "disabled";

    /**
     * Page is a sublist of a list of objects
     */
    private final Page<?> page;

    /**
     * number of the current page
     */
    private final int current;

    /**
     * Path template of pagination
     */
    private final String pathTmpl;

    /**
     * Query template of pagination
     */
    private final String queryTmpl;

    /**
     * Query of search criteria
     * @since 1.0.1
     */
    private final String criteriaQuery;

    /**
     * Max page number
     */
    private final int maxDisplayCount;

    /**
     * Pagination URI
     */
    private final UriComponents pageUri;

    /**
     * Constructor. Initializes the properties with the arguments passed<br>
     * @param page a Page instance
     * @param pathTmpl path template of pagination
     * @param queryTmpl query template of pagination
     * @param maxDisplayCount max display count
     */
    public PaginationInfo(Page<?> page, String pathTmpl, String queryTmpl,
            int maxDisplayCount) {
        this(page, pathTmpl, queryTmpl, null, false, maxDisplayCount);
    }

    /**
     * Constructor. Initializes the properties with the arguments passed<br>
     * @param page a Page instance
     * @param pathTmpl path template of pagination
     * @param queryTmpl query template of pagination
     * @param criteriaQuery Query of search criteria
     * @param disableHtmlEscapeOfCriteriaQuery Flag to indicate whether html escaping of criteriaQuery is to be disabled or
     *            not.IF set to true, html escaping of criteriaQuery is disabled.
     * @param maxDisplayCount max display count
     * @since 1.0.1
     */
    public PaginationInfo(Page<?> page, String pathTmpl, String queryTmpl,
            String criteriaQuery, boolean disableHtmlEscapeOfCriteriaQuery,
            int maxDisplayCount) {
        this.page = page;
        this.current = page.getNumber();
        this.pathTmpl = pathTmpl;
        this.queryTmpl = queryTmpl;
        if (disableHtmlEscapeOfCriteriaQuery) {
            this.criteriaQuery = removeHeadDelimiterOfQueryString(
                    criteriaQuery);
        } else {
            this.criteriaQuery = HtmlEscapeUtils.htmlEscape(
                    removeHeadDelimiterOfQueryString(criteriaQuery));
        }
        this.maxDisplayCount = maxDisplayCount;
        this.pageUri = UriComponentsBuilder.fromPath(pathTmpl).query(queryTmpl)
                .build();
    }

    /**
     * Remove the delimiter of query string that exists at the head.
     * <p>
     * Target delimiter is '?' or '&'.
     * </p>
     * @param queryString query string
     * @return query string that removed delimiter at the head.
     * @since 1.0.1
     */
    private String removeHeadDelimiterOfQueryString(String queryString) {
        if (!StringUtils.hasLength(queryString)) {
            return queryString;
        }
        if (queryString.startsWith("?") || queryString.startsWith("&")) {
            return queryString.substring(1);
        } else {
            return queryString;
        }
    }

    /**
     * Creates and returns a map of attributes to be used by pagination functionality in displaying the results<br>
     * <p>
     * Pagination functionality supports only a single sort order. If a {@code Sort} instance containing multiple {@code Order}
     * elements is passed as argument, the last {@code Order} element will be applicable
     * </p>
     * @param pageIndex index of page number (page index is start with 0).
     * @param size size of page.
     * @param sort Sort option for queries.
     * @return Map&lt;String, Object&gt; instance of attributes
     */
    public static Map<String, Object> createAttributeMap(int pageIndex,
            int size, Sort sort) {
        Map<String, Object> attr = new HashMap<String, Object>(3);
        attr.put(PAGE_ATTR, pageIndex);
        attr.put(SIZE_ATTR, size);
        if (sort != null) {
            Iterator<Order> orders = sort.iterator();

            if (orders.hasNext()) {
                // support only one order
                Order order = orders.next();
                attr.put(SORT_ORDER_PROPERTY_ATTR, order.getProperty());
                attr.put(SORT_ORDER_DIRECTION_ATTR, order.getDirection()
                        .toString());
            }
        }
        return attr;
    }

    /**
     * Fetches the current page number<br>
     * @return current page number
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Fetches the path template of pagination<br>
     * @return String path template of pagination
     */
    public String getPathTmpl() {
        return pathTmpl;
    }

    /**
     * Fetches the query template of pagination<br>
     * @return String query template of pagination
     */
    public String getQueryTmpl() {
        return queryTmpl;
    }

    /**
     * Fetches the query of search criteria <br>
     * @return String query of search criteria
     * @since 1.0.1
     */
    public String getCriteriaQuery() {
        return criteriaQuery;
    }

    /**
     * Returns max count of page display
     * @return String max count of page display
     */
    public int getMaxDisplayCount() {
        return maxDisplayCount;
    }

    /**
     * Returns URI of Pagination
     * @return String Pagination URI
     */
    protected UriComponents getPageUri() {
        return pageUri;
    }

    /**
     * Returns paginationURL after setting specified page number and display count to it
     * @param pageIndex index of page number (page index is start with 0).
     * @return String Pagination URL with URL page number and display count set to it
     */
    public String getPageUrl(int pageIndex) {
        Map<String, Object> attr = createAttributeMap(pageIndex, page.getSize(),
                page.getSort());
        StringBuilder pageUriBuilder = new StringBuilder(pageUri.expand(attr)
                .encode().toUriString());
        if (StringUtils.hasLength(criteriaQuery)) {
            if (pageUri.getQueryParams().isEmpty()) {
                pageUriBuilder.append("?");
            } else {
                pageUriBuilder.append("&");
            }
            pageUriBuilder.append(criteriaQuery);
        }
        return pageUriBuilder.toString();
    }

    /**
     * Returns first PaginationURL
     * @return String first PaginationURL
     */
    public String getFirstUrl() {
        return getPageUrl(0);
    }

    /**
     * Returns last PaginationURL
     * @return String last PaginationURL
     */
    public String getLastUrl() {
        return getPageUrl(page.getTotalPages() - 1);
    }

    /**
     * Returns PaginationURL of previous page
     * @return String PaginationURL of previous page
     */
    public String getPreviousUrl() {
        return getPageUrl(current - 1);
    }

    /**
     * Returns PaginationURL of next page
     * @return String PaginationURL of next page
     */
    public String getNextUrl() {
        return getPageUrl(current + 1);
    }

    /**
     * checks whether the current page is first page.
     * @return True, if the current page is the first page. False otherwise.
     */
    public boolean isFirstPage() {
        return current == 0;
    }

    /**
     * Determines whether the current page is the last page or not<br>
     * @return True, if the current page is the last page. False otherwise.
     */
    public boolean isLastPage() {
        return current == page.getTotalPages() - 1;
    }

    /**
     * Determines if the page number passed as argument is the currently displayed page number or not <br>
     * @param page page-number
     * @return True, if the page number passed as argument is the currently displayed page number. False otherwise.
     */
    public boolean isCurrent(int page) {
        return current == page;
    }

    /**
     * Returns the class that sets start and end page of pagination
     * <p>
     * @return BeginAndEnd class that sets start and end page of pagination
     */
    public BeginAndEnd getBeginAndEnd() {
        int begin = Math.max(0, current - maxDisplayCount / 2);
        int end = begin + (maxDisplayCount - 1);
        if (end > page.getTotalPages() - 1) {
            end = page.getTotalPages() - 1;
            begin = Math.max(0, end - (maxDisplayCount - 1));
        }
        return new BeginAndEnd(begin, end);
    }
}
