/*
 * Copyright (C) 2013 terasoluna.org
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

import javax.servlet.jsp.JspException;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;

/**
 * JSP tag that provides pagination functionality<br>
 * <p>
 * </p>
 */
public class PaginationTag extends HtmlEscapingAwareTag {

    /**
     * Page object
     */
    private Object page;

    /**
     * Path template of pagination
     */
    private String pathTmpl = PaginationInfo.DEFAULT_PATH_TEMPLATE;

    /**
     * Query template of pagination
     */
    private String queryTmpl = PaginationInfo.DEFAULT_QUERY_TEMPLATE;

    /**
     * Query of search criteria
     * @since 1.0.1
     */
    private String criteriaQuery;

    /**
     * Maximum display count
     */
    private int maxDisplayCount = PaginationInfo.DEFAULT_MAX_DISPLAY_COUNT;

    /**
     * Outer tag of HTML that make up pagination
     */
    private String outerElement = PaginationInfo.DEFAULT_OUTER_ELM;

    /**
     * Value of "class" attribute of outer tag of pagination
     */
    private String outerElementClass = PaginationInfo.DEFAULT_OUTER_CLASS;

    /**
     * Inner tag of HTML that make up pagination
     */
    private String innerElement = PaginationInfo.DEFAULT_INNER_ELM;

    /**
     * Display text of the link of back to the top
     */
    private String firstLinkText = PaginationInfo.DEFAULT_FIRST_LINK_TEXT;

    /**
     * Display text of the link of back to previous
     */
    private String previousLinkText = PaginationInfo.DEFAULT_PREVIOUS_LINK_TEXT;

    /**
     * Display text of the link to proceed to the next
     */
    private String nextLinkText = PaginationInfo.DEFAULT_NEXT_LINK_TEXT;

    /**
     * Display text of the link to forward to the last page
     */
    private String lastLinkText = PaginationInfo.DEFAULT_LAST_LINK_TEXT;

    /**
     * Anchor link which is set when the link is disabled.
     */
    private String disabledHref = PaginationInfo.DEFAULT_DISABLED_HREF;

    /**
     * Class name that enables the link
     */
    private String activeClass = PaginationInfo.DEFAULT_ACTIVE_CLASS;

    /**
     * Class name that enables the link
     */
    private String disabledClass = PaginationInfo.DEFAULT_DISABLED_CLASS;

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6608906896698983954L;

    /**
     * create {@code TagWriter} instance.
     * @return {@code TagWriter} instance that will render the content of the tag to JSP page.
     */
    TagWriter createTagWriter() {
        TagWriter tagWriter = new TagWriter(this.pageContext);
        return tagWriter;
    }

    /**
     * Renders a pagination link.
     * @return {@link javax.servlet.jsp.tagext.Tag#EVAL_BODY_INCLUDE}
     * @throws Exception
     */
    @Override
    protected int doStartTagInternal() throws Exception {
        if (page instanceof Page) {
            Page<?> p = (Page<?>) page;
            if (p.getTotalElements() <= 0) {
                return EVAL_BODY_INCLUDE;
            }

            TagWriter tagWriter = createTagWriter();

            PaginationInfo info = new PaginationInfo(p, pathTmpl, queryTmpl, criteriaQuery, maxDisplayCount);
            BeginAndEnd be = info.getBeginAndEnd();

            startOuterElement(tagWriter);

            writeFirstAndPreviousLink(tagWriter, info);
            for (int i = be.getBegin(); i <= be.getEnd(); i++) {
                writePageLink(tagWriter, info, i);
            }
            writeNextAndLastLink(tagWriter, info);
            endOuterElement(tagWriter);
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Renders a anchor.<br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param href url of anchor
     * @param value text of anchor
     * @throws JspException
     */
    protected void writeAnchor(TagWriter tagWriter, String href, String value) throws JspException {
        if (StringUtils.hasText(href)) {
            tagWriter.startTag(PaginationInfo.A_ELM);
            tagWriter.writeAttribute(PaginationInfo.HREF_ATTR, href);
            tagWriter.appendValue(value);
            tagWriter.endTag();
        } else {
            tagWriter.appendValue(value);
        }
    }

    /**
     * Renders start tag.
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @throws JspException
     */
    protected void startOuterElement(TagWriter tagWriter) throws JspException {
        if (StringUtils.hasText(outerElement)) {
            tagWriter.startTag(outerElement);

            if (StringUtils.hasText(outerElementClass)) {
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        outerElementClass);
            }
        }
    }

    /**
     * Renders end tag.<br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @throws JspException
     */
    protected void endOuterElement(TagWriter tagWriter) throws JspException {
        if (StringUtils.hasText(outerElement)) {
            tagWriter.endTag(); // ul
        }
    }

    /**
     * Renders page link.<br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param info {@code PaginationInfo} instance the holds all the information required by the {@code PaginationTag}
     * @throws JspException
     */
    protected void writePageLink(TagWriter tagWriter, PaginationInfo info,
            int page) throws JspException {
        tagWriter.startTag(innerElement); // <li>
        if (info.isCurrent(page)) {
            tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR, activeClass);
        }

        writeAnchor(tagWriter, info.getPageUrl(page), String.valueOf(page + 1)); // a

        tagWriter.endTag(); // </li>
    }

    /**
     * Renders link for going to the first page and previous page.<br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param info {@code PaginationInfo} instance the holds all the information required by the {@code PaginationTag}
     * @throws JspException
     */
    protected void writeFirstAndPreviousLink(TagWriter tagWriter,
            PaginationInfo info) throws JspException {
        if (info.isFirstPage()) {

            if (StringUtils.hasText(firstLinkText)) {
                // write first link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass);

                writeAnchor(tagWriter, disabledHref, firstLinkText); // a

                tagWriter.endTag(); // </li>
            }

            if (StringUtils.hasText(previousLinkText)) {
                // write previous link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass);

                writeAnchor(tagWriter, disabledHref, previousLinkText); // a

                tagWriter.endTag(); // </li>
            }
        } else {
            if (StringUtils.hasText(firstLinkText)) {
                // write first link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getFirstUrl(), firstLinkText); // a

                tagWriter.endTag(); // </li>
            }

            if (StringUtils.hasText(previousLinkText)) {
                // write previous link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getPreviousUrl(), previousLinkText); // a

                tagWriter.endTag(); // </li>
            }
        }
    }

    /**
     * Renders link for going to the next page and last page <br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param info {@code PaginationInfo} instance the holds all the information required by the {@code PaginationTag}
     * @throws JspException
     */
    protected void writeNextAndLastLink(TagWriter tagWriter, PaginationInfo info) throws JspException {
        if (info.isLastPage()) {

            if (StringUtils.hasText(nextLinkText)) {
                // write next link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass);

                writeAnchor(tagWriter, disabledHref, nextLinkText); // a

                tagWriter.endTag(); // </li>

            }

            if (StringUtils.hasText(lastLinkText)) {
                // write last link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass); // a
                writeAnchor(tagWriter, disabledHref, lastLinkText);
                tagWriter.endTag(); // </li>
            }
        } else {
            if (StringUtils.hasText(nextLinkText)) {
                // write next link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getNextUrl(), nextLinkText); // a

                tagWriter.endTag(); // </li>
            }

            if (StringUtils.hasText(lastLinkText)) {
                // write last link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getLastUrl(), lastLinkText); // a

                tagWriter.endTag(); // </li>
            }
        }
    }

    /**
     * Release state.
     * <p>
     * Set to null all fields.
     * </p>
     */
    @Override
    public void release() {
        super.release();
        this.page = null;
        this.pathTmpl = null;
        this.queryTmpl = null;
        this.criteriaQuery = null;
        this.outerElement = null;
        this.outerElementClass = null;
        this.innerElement = null;
        this.firstLinkText = null;
        this.previousLinkText = null;
        this.nextLinkText = null;
        this.lastLinkText = null;
        this.disabledHref = null;
        this.activeClass = null;
        this.disabledClass = null;
    }

    /**
     * Set page object<br>
     * @param page
     */
    public void setPage(Object page) {
        this.page = page;
    }

    /**
     * Set path template of pagination<br>
     * @param pathTmpl
     */
    public void setPathTmpl(String pathTmpl) {
        this.pathTmpl = pathTmpl;
    }

    /**
     * Set query template of pagination<br>
     * @param queryTmpl
     */
    public void setQueryTmpl(String queryTmpl) {
        this.queryTmpl = queryTmpl;
    }

    /**
     * Set query of search criteria<br>
     * @param criteriaQuery
     * @since 1.0.1
     */
    public void setCriteriaQuery(String criteriaQuery) {
        this.criteriaQuery = criteriaQuery;
    }

    /**
     * Set maximum display count<br>
     * @param maxDisplayCount
     */
    public void setMaxDisplayCount(int maxDisplayCount) {
        this.maxDisplayCount = maxDisplayCount;
    }

    /**
     * Set outer tag of HTML that make up pagination<br>
     * @param outerElement
     */
    public void setOuterElement(String outerElement) {
        this.outerElement = outerElement;
    }

    /**
     * Set value of "class" attribute of outer tag of pagination <br>
     * @param outerElementClass
     */
    public void setOuterElementClass(String outerElementClass) {
        this.outerElementClass = outerElementClass;
    }

    /**
     * Set inner tag of HTML that make up pagination <br>
     * @param innerElement
     */
    public void setInnerElement(String innerElement) {
        this.innerElement = innerElement;
    }

    /**
     * Set display text of the link of back to the top <br>
     * @param firstLinkText
     */
    public void setFirstLinkText(String firstLinkText) {
        this.firstLinkText = firstLinkText;
    }

    /**
     * Set display text of the link of back to previous <br>
     * @param previousLinkText
     */
    public void setPreviousLinkText(String previousLinkText) {
        this.previousLinkText = previousLinkText;
    }

    /**
     * Set display text of the link to proceed to the next <br>
     * @param nextLinkText
     */
    public void setNextLinkText(String nextLinkText) {
        this.nextLinkText = nextLinkText;
    }

    /**
     * Set display text of the link to forward to the last page <br>
     * @param lastLinkText
     */
    public void setLastLinkText(String lastLinkText) {
        this.lastLinkText = lastLinkText;
    }

    /**
     * Set anchor link which is set when the link is disabled. <br>
     * @param disabledHref
     */
    public void setDisabledHref(String disabledHref) {
        this.disabledHref = disabledHref;
    }

    /**
     * Set class name that enables the link <br>
     * @param activeClass
     */
    public void setActiveClass(String activeClass) {
        this.activeClass = activeClass;
    }

    /**
     * Set class name that enables the link <br>
     * @param disabledClass
     */
    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

}
