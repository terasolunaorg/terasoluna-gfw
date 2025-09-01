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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.StringWriter;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.SerializationUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.form.TagWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

public class PaginationTagTest {

    protected StringWriter writer;

    protected MockPageContext pageContext;

    protected PaginationTag tag;

    Page<String> page;

    protected MockPageContext createPageContext() {
        MockServletContext sc = new MockServletContext();
        WebApplicationContext wac = mock(WebApplicationContext.class);
        when(wac.getServletContext()).thenReturn(sc);
        MockHttpServletRequest request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();
        sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);
        return new MockPageContext(sc, request, response);
    }

    @SuppressWarnings({"serial", "unchecked"})
    @BeforeEach
    public void before() throws Exception {
        this.writer = new StringWriter();
        this.pageContext = createPageContext();
        this.tag = new PaginationTag() {
            @Override
            TagWriter createTagWriter() {
                return new TagWriter(writer);
            }
        };
        tag.setPageContext(pageContext);
        this.page = mock(Page.class);
    }

    @Test
    public void testRelease01() throws Exception {

        tag.release();

        assertThat(ReflectionTestUtils.getField(tag, "page")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "pathTmpl")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "queryTmpl")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "criteriaQuery")).isNull();
        assertThat((boolean) ReflectionTestUtils.getField(tag, "disableHtmlEscapeOfCriteriaQuery"))
                .isEqualTo(false);
        assertThat((boolean) ReflectionTestUtils.getField(tag, "enableLinkOfCurrentPage"))
                .isEqualTo(false);
        assertThat(ReflectionTestUtils.getField(tag, "outerElement")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "outerElementClass")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "innerElement")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "innerElementClass")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "anchorClass")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "firstLinkText")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "previousLinkText")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "nextLinkText")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "lastLinkText")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "disabledHref")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "activeClass")).isNull();
        assertThat(ReflectionTestUtils.getField(tag, "disabledClass")).isNull();
    }

    /**
     * default case 1.
     *
     * <pre>
     *  - current page 0 = first page
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal01() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li><li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li><li class=\"active\"><a href=\"javascript:void(0)\">1</a></li><li><a href=\"?page=1&size=10\">2</a></li><li><a href=\"?page=2&size=10\">3</a></li><li><a href=\"?page=3&size=10\">4</a></li><li><a href=\"?page=4&size=10\">5</a></li><li><a href=\"?page=5&size=10\">6</a></li><li><a href=\"?page=6&size=10\">7</a></li><li><a href=\"?page=7&size=10\">8</a></li><li><a href=\"?page=8&size=10\">9</a></li><li><a href=\"?page=9&size=10\">10</a></li><li><a href=\"?page=1&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * normal case 2.
     *
     * <pre>
     *  - current page 1 > first page
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal02() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(1);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=0&size=10\">&lt;</a></li><li><a href=\"?page=0&size=10\">1</a></li><li class=\"active\"><a href=\"javascript:void(0)\">2</a></li><li><a href=\"?page=2&size=10\">3</a></li><li><a href=\"?page=3&size=10\">4</a></li><li><a href=\"?page=4&size=10\">5</a></li><li><a href=\"?page=5&size=10\">6</a></li><li><a href=\"?page=6&size=10\">7</a></li><li><a href=\"?page=7&size=10\">8</a></li><li><a href=\"?page=8&size=10\">9</a></li><li><a href=\"?page=9&size=10\">10</a></li><li><a href=\"?page=2&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * normal case 3.
     *
     * <pre>
     *  - current page 5 = maxDisplayCount/2
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal03() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(5);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=4&size=10\">&lt;</a></li><li><a href=\"?page=0&size=10\">1</a></li><li><a href=\"?page=1&size=10\">2</a></li><li><a href=\"?page=2&size=10\">3</a></li><li><a href=\"?page=3&size=10\">4</a></li><li><a href=\"?page=4&size=10\">5</a></li><li class=\"active\"><a href=\"javascript:void(0)\">6</a></li><li><a href=\"?page=6&size=10\">7</a></li><li><a href=\"?page=7&size=10\">8</a></li><li><a href=\"?page=8&size=10\">9</a></li><li><a href=\"?page=9&size=10\">10</a></li><li><a href=\"?page=6&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * normal case 4.
     *
     * <pre>
     *  - current page 20 > maxDisplayCount
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal04() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=19&size=10\">&lt;</a></li><li><a href=\"?page=15&size=10\">16</a></li><li><a href=\"?page=16&size=10\">17</a></li><li><a href=\"?page=17&size=10\">18</a></li><li><a href=\"?page=18&size=10\">19</a></li><li><a href=\"?page=19&size=10\">20</a></li><li class=\"active\"><a href=\"javascript:void(0)\">21</a></li><li><a href=\"?page=21&size=10\">22</a></li><li><a href=\"?page=22&size=10\">23</a></li><li><a href=\"?page=23&size=10\">24</a></li><li><a href=\"?page=24&size=10\">25</a></li><li><a href=\"?page=21&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * normal case 5.
     *
     * <pre>
     *  - current page 95 = total - maxDisplayCount/2
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal05() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(95);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=94&size=10\">&lt;</a></li><li><a href=\"?page=90&size=10\">91</a></li><li><a href=\"?page=91&size=10\">92</a></li><li><a href=\"?page=92&size=10\">93</a></li><li><a href=\"?page=93&size=10\">94</a></li><li><a href=\"?page=94&size=10\">95</a></li><li class=\"active\"><a href=\"javascript:void(0)\">96</a></li><li><a href=\"?page=96&size=10\">97</a></li><li><a href=\"?page=97&size=10\">98</a></li><li><a href=\"?page=98&size=10\">99</a></li><li><a href=\"?page=99&size=10\">100</a></li><li><a href=\"?page=96&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * normal case 6.
     *
     * <pre>
     *  - current page 99 = last page
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal06() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(99);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=98&size=10\">&lt;</a></li><li><a href=\"?page=90&size=10\">91</a></li><li><a href=\"?page=91&size=10\">92</a></li><li><a href=\"?page=92&size=10\">93</a></li><li><a href=\"?page=93&size=10\">94</a></li><li><a href=\"?page=94&size=10\">95</a></li><li><a href=\"?page=95&size=10\">96</a></li><li><a href=\"?page=96&size=10\">97</a></li><li><a href=\"?page=97&size=10\">98</a></li><li><a href=\"?page=98&size=10\">99</a></li><li class=\"active\"><a href=\"javascript:void(0)\">100</a></li><li class=\"disabled\"><a href=\"javascript:void(0)\">&gt;</a></li><li class=\"disabled\"><a href=\"javascript:void(0)\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * normal case 7.
     *
     * <pre>
     *  - current page 100 > last page
     *  - page size 10
     *  - total pages 100
     *  - total elements 1000
     * </pre>
     */
    @Test
    public void testDoStartTagInternal07() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(100);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=99&size=10\">&lt;</a></li><li><a href=\"?page=90&size=10\">91</a></li><li><a href=\"?page=91&size=10\">92</a></li><li><a href=\"?page=92&size=10\">93</a></li><li><a href=\"?page=93&size=10\">94</a></li><li><a href=\"?page=94&size=10\">95</a></li><li><a href=\"?page=95&size=10\">96</a></li><li><a href=\"?page=96&size=10\">97</a></li><li><a href=\"?page=97&size=10\">98</a></li><li><a href=\"?page=98&size=10\">99</a></li><li><a href=\"?page=99&size=10\">100</a></li><li><a href=\"?page=101&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * abnormal case 1.
     *
     * <pre>
     *  - total elements 0
     * </pre>
     */
    @Test
    public void testDoStartTagInternal08() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(0L);

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected = "";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * abnormal case 2.
     *
     * <pre>
     *  - page is null
     * </pre>
     */
    @Test
    public void testDoStartTagInternal09() throws Exception {

        Page<String> page = null;

        tag.setPage(page);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected = "";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 1.
     *
     * <pre>
     * -queryTmpl
     * </pre>
     */
    @Test
    public void testDoStartTagInternal10() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setQueryTmpl("p={page}&s={size}");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?p=0&s=10\">&lt;&lt;</a></li><li><a href=\"?p=19&s=10\">&lt;</a></li><li><a href=\"?p=15&s=10\">16</a></li><li><a href=\"?p=16&s=10\">17</a></li><li><a href=\"?p=17&s=10\">18</a></li><li><a href=\"?p=18&s=10\">19</a></li><li><a href=\"?p=19&s=10\">20</a></li><li class=\"active\"><a href=\"javascript:void(0)\">21</a></li><li><a href=\"?p=21&s=10\">22</a></li><li><a href=\"?p=22&s=10\">23</a></li><li><a href=\"?p=23&s=10\">24</a></li><li><a href=\"?p=24&s=10\">25</a></li><li><a href=\"?p=21&s=10\">&gt;</a></li><li><a href=\"?p=99&s=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 2.
     *
     * <pre>
     * -pathTmpl
     * </pre>
     */
    @Test
    public void testDoStartTagInternal11() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setPathTmpl("p/{page}/{size}/");
        tag.setQueryTmpl("");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"p/0/10/\">&lt;&lt;</a></li><li><a href=\"p/19/10/\">&lt;</a></li><li><a href=\"p/15/10/\">16</a></li><li><a href=\"p/16/10/\">17</a></li><li><a href=\"p/17/10/\">18</a></li><li><a href=\"p/18/10/\">19</a></li><li><a href=\"p/19/10/\">20</a></li><li class=\"active\"><a href=\"javascript:void(0)\">21</a></li><li><a href=\"p/21/10/\">22</a></li><li><a href=\"p/22/10/\">23</a></li><li><a href=\"p/23/10/\">24</a></li><li><a href=\"p/24/10/\">25</a></li><li><a href=\"p/21/10/\">&gt;</a></li><li><a href=\"p/99/10/\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 2.
     *
     * <pre>
     * -use sort order
     * </pre>
     */
    @Test
    public void testDoStartTagInternal12() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);
        when(page.getSort()).thenReturn(Sort.by(new Sort.Order(Direction.DESC, "id")));

        tag.setPage(page);

        // customize
        tag.setQueryTmpl(
                "page={page}&size={size}&sort={sortOrderProperty}&desc={sortOrderDirection}");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10&sort=id&desc=DESC\">&lt;&lt;</a></li><li><a href=\"?page=19&size=10&sort=id&desc=DESC\">&lt;</a></li><li><a href=\"?page=15&size=10&sort=id&desc=DESC\">16</a></li><li><a href=\"?page=16&size=10&sort=id&desc=DESC\">17</a></li><li><a href=\"?page=17&size=10&sort=id&desc=DESC\">18</a></li><li><a href=\"?page=18&size=10&sort=id&desc=DESC\">19</a></li><li><a href=\"?page=19&size=10&sort=id&desc=DESC\">20</a></li><li class=\"active\"><a href=\"javascript:void(0)\">21</a></li><li><a href=\"?page=21&size=10&sort=id&desc=DESC\">22</a></li><li><a href=\"?page=22&size=10&sort=id&desc=DESC\">23</a></li><li><a href=\"?page=23&size=10&sort=id&desc=DESC\">24</a></li><li><a href=\"?page=24&size=10&sort=id&desc=DESC\">25</a></li><li><a href=\"?page=21&size=10&sort=id&desc=DESC\">&gt;</a></li><li><a href=\"?page=99&size=10&sort=id&desc=DESC\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 3.
     *
     * <pre>
     * -maxDisplayCount
     * </pre>
     */
    @Test
    public void testDoStartTagInternal13() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setMaxDisplayCount(5);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=0&size=10\">&lt;&lt;</a></li><li><a href=\"?page=19&size=10\">&lt;</a></li><li><a href=\"?page=18&size=10\">19</a></li><li><a href=\"?page=19&size=10\">20</a></li><li class=\"active\"><a href=\"javascript:void(0)\">21</a></li><li><a href=\"?page=21&size=10\">22</a></li><li><a href=\"?page=22&size=10\">23</a></li><li><a href=\"?page=21&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 4.
     *
     * <pre>
     * -outerElement is empty
     * -innerElement
     * </pre>
     */
    @Test
    public void testDoStartTagInternal14() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setOuterElement("");
        tag.setInnerElement("span");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<span><a href=\"?page=0&size=10\">&lt;&lt;</a></span><span><a href=\"?page=19&size=10\">&lt;</a></span><span><a href=\"?page=15&size=10\">16</a></span><span><a href=\"?page=16&size=10\">17</a></span><span><a href=\"?page=17&size=10\">18</a></span><span><a href=\"?page=18&size=10\">19</a></span><span><a href=\"?page=19&size=10\">20</a></span><span class=\"active\"><a href=\"javascript:void(0)\">21</a></span><span><a href=\"?page=21&size=10\">22</a></span><span><a href=\"?page=22&size=10\">23</a></span><span><a href=\"?page=23&size=10\">24</a></span><span><a href=\"?page=24&size=10\">25</a></span><span><a href=\"?page=21&size=10\">&gt;</a></span><span><a href=\"?page=99&size=10\">&gt;&gt;</a></span>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 5.
     *
     * <pre>
     * -innerElement is span
     * </pre>
     */
    @Test
    public void testDoStartTagInternal15() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setOuterElement("p");
        tag.setInnerElement("span");

        int ret = tag.doStartTagInternal();

        System.out.println(getOutput().replaceAll(Pattern.quote("\""), "\\\\\""));

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<p><span><a href=\"?page=0&size=10\">&lt;&lt;</a></span><span><a href=\"?page=19&size=10\">&lt;</a></span><span><a href=\"?page=15&size=10\">16</a></span><span><a href=\"?page=16&size=10\">17</a></span><span><a href=\"?page=17&size=10\">18</a></span><span><a href=\"?page=18&size=10\">19</a></span><span><a href=\"?page=19&size=10\">20</a></span><span class=\"active\"><a href=\"javascript:void(0)\">21</a></span><span><a href=\"?page=21&size=10\">22</a></span><span><a href=\"?page=22&size=10\">23</a></span><span><a href=\"?page=23&size=10\">24</a></span><span><a href=\"?page=24&size=10\">25</a></span><span><a href=\"?page=21&size=10\">&gt;</a></span><span><a href=\"?page=99&size=10\">&gt;&gt;</a></span></p>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 6.
     *
     * <pre>
     * -no display
     * </pre>
     */
    @Test
    public void testDoStartTagInternal16() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(20);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setMaxDisplayCount(0);
        tag.setFirstLinkText("");
        tag.setLastLinkText("");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li><a href=\"?page=19&size=10\">&lt;</a></li><li><a href=\"?page=21&size=10\">&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 7.
     *
     * <pre>
     * -outer tag class change.
     * -inner tag class change.
     * -anchor tag class change.
     * </pre>
     */
    @Test
    public void testDoStartTagInternal17() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setOuterElementClass("all");
        tag.setInnerElementClass("item");
        tag.setAnchorClass("link");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul class=\"all\"><li class=\"disabled\"><a class=\"link\" href=\"javascript:void(0)\">&lt;&lt;</a></li><li class=\"disabled\"><a class=\"link\" href=\"javascript:void(0)\">&lt;</a></li><li class=\"active\"><a class=\"link\" href=\"javascript:void(0)\">1</a></li><li class=\"item\"><a class=\"link\" href=\"?page=1&size=10\">2</a></li><li class=\"item\"><a class=\"link\" href=\"?page=2&size=10\">3</a></li><li class=\"item\"><a class=\"link\" href=\"?page=3&size=10\">4</a></li><li class=\"item\"><a class=\"link\" href=\"?page=4&size=10\">5</a></li><li class=\"item\"><a class=\"link\" href=\"?page=5&size=10\">6</a></li><li class=\"item\"><a class=\"link\" href=\"?page=6&size=10\">7</a></li><li class=\"item\"><a class=\"link\" href=\"?page=7&size=10\">8</a></li><li class=\"item\"><a class=\"link\" href=\"?page=8&size=10\">9</a></li><li class=\"item\"><a class=\"link\" href=\"?page=9&size=10\">10</a></li><li class=\"item\"><a class=\"link\" href=\"?page=1&size=10\">&gt;</a></li><li class=\"item\"><a class=\"link\" href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * customized case 7.
     *
     * <pre>
     * -outer tag class change.
     * -inner tag class change.
     * -anchor tag class change.
     * </pre>
     */
    @Test
    public void testDoStartTagInternal17_2() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(99);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);

        // customize
        tag.setOuterElementClass("all");
        tag.setInnerElementClass("item");
        tag.setAnchorClass("link");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul class=\"all\"><li class=\"item\"><a class=\"link\" href=\"?page=0&size=10\">&lt;&lt;</a></li><li class=\"item\"><a class=\"link\" href=\"?page=98&size=10\">&lt;</a></li><li class=\"item\"><a class=\"link\" href=\"?page=90&size=10\">91</a></li><li class=\"item\"><a class=\"link\" href=\"?page=91&size=10\">92</a></li><li class=\"item\"><a class=\"link\" href=\"?page=92&size=10\">93</a></li><li class=\"item\"><a class=\"link\" href=\"?page=93&size=10\">94</a></li><li class=\"item\"><a class=\"link\" href=\"?page=94&size=10\">95</a></li><li class=\"item\"><a class=\"link\" href=\"?page=95&size=10\">96</a></li><li class=\"item\"><a class=\"link\" href=\"?page=96&size=10\">97</a></li><li class=\"item\"><a class=\"link\" href=\"?page=97&size=10\">98</a></li><li class=\"item\"><a class=\"link\" href=\"?page=98&size=10\">99</a></li><li class=\"active\"><a class=\"link\" href=\"javascript:void(0)\">100</a></li><li class=\"disabled\"><a class=\"link\" href=\"javascript:void(0)\">&gt;</a></li><li class=\"disabled\"><a class=\"link\" href=\"javascript:void(0)\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    @Test
    public void testDoStartTagInternal_disabledHref_is_empty() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setDisabledHref("");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected =
                "<ul><li class=\"disabled\">&lt;&lt;</li><li class=\"disabled\">&lt;</li><li class=\"active\">1</li><li><a href=\"?page=1&size=10\">2</a></li><li><a href=\"?page=2&size=10\">3</a></li><li><a href=\"?page=3&size=10\">4</a></li><li><a href=\"?page=4&size=10\">5</a></li><li><a href=\"?page=5&size=10\">6</a></li><li><a href=\"?page=6&size=10\">7</a></li><li><a href=\"?page=7&size=10\">8</a></li><li><a href=\"?page=8&size=10\">9</a></li><li><a href=\"?page=9&size=10\">10</a></li><li><a href=\"?page=1&size=10\">&gt;</a></li><li><a href=\"?page=99&size=10\">&gt;&gt;</a></li></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    @Test
    public void issue12_testDoStartTagInternal_criteriaQuery_specified() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setCriteriaQuery("a=%2B&b=+&c=%3D&d=%26");
        tag.setDisableHtmlEscapeOfCriteriaQuery("false");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        String expectedCriteriaQuery = "&a=%2B&amp;b=+&amp;c=%3D&amp;d=%26";
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">2</a></li>");
        expected.append("<li><a href=\"?page=2&size=10" + expectedCriteriaQuery + "\">3</a></li>");
        expected.append(
                "<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">&gt;</a></li>");
        expected.append(
                "<li><a href=\"?page=99&size=10" + expectedCriteriaQuery + "\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the and-mark(&) is add
        // That the criteria query is not encode
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue12_testDoStartTagInternal_criteriaQuery_specified_startWith_questionMark()
            throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setCriteriaQuery("?a=%2B&b=+&c=%3D&d=%26");
        tag.setDisableHtmlEscapeOfCriteriaQuery(null);

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        String expectedCriteriaQuery = "&a=%2B&amp;b=+&amp;c=%3D&amp;d=%26";
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">2</a></li>");
        expected.append("<li><a href=\"?page=2&size=10" + expectedCriteriaQuery + "\">3</a></li>");
        expected.append(
                "<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">&gt;</a></li>");
        expected.append(
                "<li><a href=\"?page=99&size=10" + expectedCriteriaQuery + "\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the question-mark(?) is remove
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue12_testDoStartTagInternal_disableHtmlEscapeOfCriteriaQuery_specified_true()
            throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setCriteriaQuery("&a=%2B&b=+&c=%3D&d=%26&e=<>\"'");
        tag.setDisableHtmlEscapeOfCriteriaQuery("true");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        String expectedCriteriaQuery = "&a=%2B&b=+&c=%3D&d=%26&e=<>\"'";
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">2</a></li>");
        expected.append("<li><a href=\"?page=2&size=10" + expectedCriteriaQuery + "\">3</a></li>");
        expected.append(
                "<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">&gt;</a></li>");
        expected.append(
                "<li><a href=\"?page=99&size=10" + expectedCriteriaQuery + "\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the and-mark(&) is remove
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue12_testDoStartTagInternal_criteriaQuery_specified_queryImpl_notSpecified()
            throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setPathTmpl("/{page}/{size}");
        tag.setQueryTmpl(null);
        tag.setCriteriaQuery("a=b");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">2</a></li>");
        expected.append("<li><a href=\"/2/10?a=b\">3</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">&gt;</a></li>");
        expected.append("<li><a href=\"/99/10?a=b\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the question-mark(?) is add
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue13_14_testDoStartTagInternal_linkOfCurrentPage_specified_true()
            throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setPathTmpl("/{page}/{size}");
        tag.setQueryTmpl(null);
        tag.setCriteriaQuery("a=b");
        tag.setEnableLinkOfCurrentPage("true");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"/0/10?a=b\">1</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">2</a></li>");
        expected.append("<li><a href=\"/2/10?a=b\">3</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">&gt;</a></li>");
        expected.append("<li><a href=\"/99/10?a=b\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the current link is enabled
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue13_14_testDoStartTagInternal_linkOfCurrentPage_specified_false()
            throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setPathTmpl("/{page}/{size}");
        tag.setQueryTmpl(null);
        tag.setCriteriaQuery("a=b");
        tag.setEnableLinkOfCurrentPage("false");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">2</a></li>");
        expected.append("<li><a href=\"/2/10?a=b\">3</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">&gt;</a></li>");
        expected.append("<li><a href=\"/99/10?a=b\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the current link is enabled
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue13_14_testDoStartTagInternal_linkOfCurrentPage_isEmpty() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setPathTmpl("/{page}/{size}");
        tag.setQueryTmpl(null);
        tag.setCriteriaQuery("a=b");
        tag.setEnableLinkOfCurrentPage("");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">2</a></li>");
        expected.append("<li><a href=\"/2/10?a=b\">3</a></li>");
        expected.append("<li><a href=\"/1/10?a=b\">&gt;</a></li>");
        expected.append("<li><a href=\"/99/10?a=b\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the current link is enabled
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void issue12_testDoStartTagInternal_criteriaQuery_specified_startWith_andMark()
            throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(10);
        when(page.getTotalPages()).thenReturn(100);
        when(page.getTotalElements()).thenReturn(1000L);

        tag.setPage(page);
        tag.setMaxDisplayCount(3);
        tag.setCriteriaQuery("&a=%2B&b=+&c=%3D&d=%26");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);

        StringBuilder expected = new StringBuilder();
        String expectedCriteriaQuery = "&a=%2B&amp;b=+&amp;c=%3D&amp;d=%26";
        expected.append("<ul>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;&lt;</a></li>");
        expected.append("<li class=\"disabled\"><a href=\"javascript:void(0)\">&lt;</a></li>");
        expected.append("<li class=\"active\"><a href=\"javascript:void(0)\">1</a></li>");
        expected.append("<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">2</a></li>");
        expected.append("<li><a href=\"?page=2&size=10" + expectedCriteriaQuery + "\">3</a></li>");
        expected.append(
                "<li><a href=\"?page=1&size=10" + expectedCriteriaQuery + "\">&gt;</a></li>");
        expected.append(
                "<li><a href=\"?page=99&size=10" + expectedCriteriaQuery + "\">&gt;&gt;</a></li>");
        expected.append("</ul>");

        // That the and-mark(&) is remove
        assertThat(getOutput()).isEqualTo(expected.toString());
    }

    @Test
    public void testSetters() throws Exception {

        PaginationTag tag1 = new PaginationTag();

        assertDoesNotThrow(() -> {
            tag1.setPageContext(pageContext);

            // Just for ensuring coverage
            tag1.createTagWriter();
            tag1.setPreviousLinkText("");
            tag1.setNextLinkText("");
            tag1.setDisabledHref("");
            tag1.setActiveClass("");
            tag1.setDisabledClass("");
            tag1.setEnableLinkOfCurrentPage("");
        });

    }

    /**
     * This test uses SerializationUtils#deserialize(byte[]) Although this mechanism is deprecated,
     * it is still used because the data to be deserialized is guaranteed.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testSerialization() {
        byte[] serialized = SerializationUtils.serialize(new PaginationTag());
        assertDoesNotThrow(() -> {
            SerializationUtils.deserialize(serialized);
        });
    }

    /**
     * check that anchor tags are not self-closing in paginationTag.
     */
    @Test
    public void testWriteAnchor() throws Exception {

        tag.writeAnchor(tag.createTagWriter(), PaginationInfo.DEFAULT_DISABLED_HREF, "");
        String expected = "<a href=\"javascript:void(0)\"></a>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    /**
     * check that ul tags are not self-closing in paginationTag.
     */
    @Test
    public void testEndOuterElement() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(0);
        when(page.getTotalPages()).thenReturn(0);
        when(page.getTotalElements()).thenReturn(1L);

        tag.setPage(page);
        tag.setFirstLinkText("");
        tag.setLastLinkText("");
        tag.setPreviousLinkText("");
        tag.setNextLinkText("");

        int ret = tag.doStartTagInternal();

        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        String expected = "<ul></ul>";
        assertThat(getOutput()).isEqualTo(expected);
    }

    @Test
    public void testEndOuterElement2() throws Exception {

        // set mock behavior
        when(page.getNumber()).thenReturn(1);
        when(page.getSize()).thenReturn(0);
        when(page.getTotalPages()).thenReturn(2);
        when(page.getTotalElements()).thenReturn(1L);

        tag.setPage(page);
        tag.setFirstLinkText("");
        tag.setLastLinkText("");
        tag.setPreviousLinkText("");
        tag.setNextLinkText("");

        int ret = tag.doStartTagInternal();

        String expected =
                "<ul><li><a href=\"?page=0&size=0\">1</a></li><li class=\"active\"><a href=\"javascript:void(0)\">2</a></li></ul>";

        // assert
        assertThat(ret).isEqualTo(TagSupport.EVAL_BODY_INCLUDE);
        assertThat(getOutput()).isEqualTo(expected);
    }

    protected String getOutput() {
        return this.writer.toString();
    }
}
