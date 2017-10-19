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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.jsp.JspException;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;
import org.terasoluna.gfw.web.util.JspTagUtils;

/**
 * JSP tag that provides pagination functionality<br>
 */
public class PaginationTag extends RequestContextAwareTag {

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
     * Flag to indicate whether html escaping of criteriaQuery is to be disabled or not
     * @since 1.0.1
     */
    private boolean disableHtmlEscapeOfCriteriaQuery;

    /**
     * Flag to enable the link of current page.
     * @since 5.0.0
     */
    private boolean enableLinkOfCurrentPage;

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
     * @throws Exception If fail a tag writing
     */
    @Override
    protected int doStartTagInternal() throws Exception {
        if (page instanceof Page) {
            Page<?> p = (Page<?>) page;
            if (p.getTotalElements() <= 0) {
                return EVAL_BODY_INCLUDE;
            }

            TagWriter tagWriter = createTagWriter();

            PaginationInfo info = new PaginationInfo(p, pathTmpl, queryTmpl, criteriaQuery, disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);

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
     * @throws JspException If fail a tag writing
     */
    protected void writeAnchor(TagWriter tagWriter, String href,
            String value) throws JspException {
        if (StringUtils.hasText(href)) {
            tagWriter.startTag(PaginationInfo.A_ELM);
            tagWriter.writeAttribute(PaginationInfo.HREF_ATTR, href);
            tagWriter.appendValue(value);
            tagWriter.endTag(true);
        } else {
            tagWriter.appendValue(value);
        }
    }

    /**
     * Renders start tag.
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @throws JspException If fail a tag writing
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
     * @throws JspException If fail a tag writing
     */
    protected void endOuterElement(TagWriter tagWriter) throws JspException {
        if (StringUtils.hasText(outerElement)) {
            tagWriter.endTag(true); // ul
        }
    }

    /**
     * Renders page link.<br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param info {@code PaginationInfo} instance the holds all the information required by the {@code PaginationTag}
     * @param page page position
     * @throws JspException If fail a tag writing
     */
    protected void writePageLink(TagWriter tagWriter, PaginationInfo info,
            int page) throws JspException {
        tagWriter.startTag(innerElement); // <li>
        if (info.isCurrent(page)) {
            tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR, activeClass);
            if (enableLinkOfCurrentPage) {
                writeAnchor(tagWriter, info.getPageUrl(page), String.valueOf(
                        page + 1)); // a
            } else {
                writeAnchor(tagWriter, disabledHref, String.valueOf(page + 1)); // a
            }
        } else {
            writeAnchor(tagWriter, info.getPageUrl(page), String.valueOf(page
                    + 1)); // a
        }
        tagWriter.endTag(true); // </li>
    }

    /**
     * Renders link for going to the first page and previous page.<br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param info {@code PaginationInfo} instance the holds all the information required by the {@code PaginationTag}
     * @throws JspException If fail a tag writing
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

                tagWriter.endTag(true); // </li>
            }

            if (StringUtils.hasText(previousLinkText)) {
                // write previous link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass);

                writeAnchor(tagWriter, disabledHref, previousLinkText); // a

                tagWriter.endTag(true); // </li>
            }
        } else {
            if (StringUtils.hasText(firstLinkText)) {
                // write first link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getFirstUrl(), firstLinkText); // a

                tagWriter.endTag(true); // </li>
            }

            if (StringUtils.hasText(previousLinkText)) {
                // write previous link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getPreviousUrl(), previousLinkText); // a

                tagWriter.endTag(true); // </li>
            }
        }
    }

    /**
     * Renders link for going to the next page and last page <br>
     * @param tagWriter {@code TagWriter} instance that will render the content of the tag to JSP page
     * @param info {@code PaginationInfo} instance the holds all the information required by the {@code PaginationTag}
     * @throws JspException If fail a tag writing
     */
    protected void writeNextAndLastLink(TagWriter tagWriter,
            PaginationInfo info) throws JspException {
        if (info.isLastPage()) {

            if (StringUtils.hasText(nextLinkText)) {
                // write next link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass);

                writeAnchor(tagWriter, disabledHref, nextLinkText); // a

                tagWriter.endTag(true); // </li>

            }

            if (StringUtils.hasText(lastLinkText)) {
                // write last link

                tagWriter.startTag(innerElement); // <li>
                tagWriter.writeAttribute(PaginationInfo.CLASS_ATTR,
                        disabledClass); // a
                writeAnchor(tagWriter, disabledHref, lastLinkText);
                tagWriter.endTag(true); // </li>
            }
        } else {
            if (StringUtils.hasText(nextLinkText)) {
                // write next link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getNextUrl(), nextLinkText); // a

                tagWriter.endTag(true); // </li>
            }

            if (StringUtils.hasText(lastLinkText)) {
                // write last link

                tagWriter.startTag(innerElement); // <li>

                writeAnchor(tagWriter, info.getLastUrl(), lastLinkText); // a

                tagWriter.endTag(true); // </li>
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
        this.disableHtmlEscapeOfCriteriaQuery = false;
        this.enableLinkOfCurrentPage = false;
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
     * @param page a Page instance
     */
    public void setPage(Object page) {
        this.page = page;
    }

    /**
     * Set path template of pagination<br>
     * @param pathTmpl Path template of pagination
     */
    public void setPathTmpl(String pathTmpl) {
        this.pathTmpl = pathTmpl;
    }

    /**
     * Set query template of pagination<br>
     * @param queryTmpl Query template of pagination
     */
    public void setQueryTmpl(String queryTmpl) {
        this.queryTmpl = queryTmpl;
    }

    /**
     * Set query of search criteria<br>
     * @param criteriaQuery Query of search criteria
     * @since 1.0.1
     */
    public void setCriteriaQuery(String criteriaQuery) {
        this.criteriaQuery = criteriaQuery;
    }

    /**
     * Sets the value for disableHtmlEscapeOfCriteriaQuery property.
     * <p>
     * IF set to true, html escaping of criteriaQuery is disabled. <br>
     * By default, disableHtmlEscapeOfCriteriaQuery is set to <code>false</code>. This means <br>
     * html escaping is enable and will be performed by default.
     * @param disableHtmlEscapeOfCriteriaQuery value of disableHtmlEscapeOfCriteriaQuery
     * @throws JspException If value that is not true or false is specified.
     * @since 1.0.1
     */
    public void setDisableHtmlEscapeOfCriteriaQuery(
            String disableHtmlEscapeOfCriteriaQuery) throws JspException {
        this.disableHtmlEscapeOfCriteriaQuery = JspTagUtils.toBoolean(
                disableHtmlEscapeOfCriteriaQuery, false,
                "disableHtmlEscapeOfCriteriaQuery");
    }

    /**
     * Sets the value for enableLinkOfCurrentPage property.
     * <p>
     * IF set to true, link of current page is enabled. <br>
     * By default, enableLinkOfCurrentPage is set to <code>false</code>. This means <br>
     * link of current page is disabled by default.
     * @param enableLinkOfCurrentPage value of enableLinkOfCurrentPage
     * @throws JspException If value that is not true or false is specified.
     * @since 5.0.0
     */
    public void setEnableLinkOfCurrentPage(
            String enableLinkOfCurrentPage) throws JspException {
        this.enableLinkOfCurrentPage = JspTagUtils.toBoolean(
                enableLinkOfCurrentPage, false, "enableLinkOfCurrentPage");
    }

    /**
     * Set maximum display count<br>
     * @param maxDisplayCount Maximum display count
     */
    public void setMaxDisplayCount(int maxDisplayCount) {
        this.maxDisplayCount = maxDisplayCount;
    }

    /**
     * Set outer tag of HTML that make up pagination<br>
     * @param outerElement Outer tag of HTML that make up pagination
     */
    public void setOuterElement(String outerElement) {
        this.outerElement = outerElement;
    }

    /**
     * Set value of "class" attribute of outer tag of pagination <br>
     * @param outerElementClass Value of "class" attribute of outer tag of pagination
     */
    public void setOuterElementClass(String outerElementClass) {
        this.outerElementClass = outerElementClass;
    }

    /**
     * Set inner tag of HTML that make up pagination <br>
     * @param innerElement Inner tag of HTML that make up pagination
     */
    public void setInnerElement(String innerElement) {
        this.innerElement = innerElement;
    }

    /**
     * Set display text of the link of back to the top <br>
     * @param firstLinkText Display text of the link of back to the top
     */
    public void setFirstLinkText(String firstLinkText) {
        this.firstLinkText = firstLinkText;
    }

    /**
     * Set display text of the link of back to previous <br>
     * @param previousLinkText Display text of the link of back to previous
     */
    public void setPreviousLinkText(String previousLinkText) {
        this.previousLinkText = previousLinkText;
    }

    /**
     * Set display text of the link to proceed to the next <br>
     * @param nextLinkText Display text of the link to proceed to the next
     */
    public void setNextLinkText(String nextLinkText) {
        this.nextLinkText = nextLinkText;
    }

    /**
     * Set display text of the link to forward to the last page <br>
     * @param lastLinkText Display text of the link to forward to the last page
     */
    public void setLastLinkText(String lastLinkText) {
        this.lastLinkText = lastLinkText;
    }

    /**
     * Set anchor link which is set when the link is disabled. <br>
     * @param disabledHref Anchor link which is set when the link is disabled
     */
    public void setDisabledHref(String disabledHref) {
        this.disabledHref = disabledHref;
    }

    /**
     * Set class name that enables the link <br>
     * @param activeClass Class name that enables the link
     */
    public void setActiveClass(String activeClass) {
        this.activeClass = activeClass;
    }

    /**
     * Set class name that enables the link <br>
     * @param disabledClass Class name that enables the link
     */
    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    /**
     * special handling for the serialization and deserialization process
     * @param out ObjectOutputStream
     * @throws IOException
     * @see java.io.Serializable
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * special handling for the serialization and deserialization process
     * @param in ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     * @see java.io.Serializable
     */
    private void readObject(
            ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
