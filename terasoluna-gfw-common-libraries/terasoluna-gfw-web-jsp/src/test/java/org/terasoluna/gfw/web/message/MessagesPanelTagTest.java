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
package org.terasoluna.gfw.web.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.message.MessagesPanelTag;

/**
 * Test class for MessagesPanelTag.
 */
public class MessagesPanelTagTest {
    protected StringWriter writer;

    protected WebApplicationContext wac;

    /**
     * mock instance of servlet jsp pageContext.
     */
    protected MockPageContext pageContext;

    /**
     * mock instance of http servlet requset.
     */
    protected MockHttpServletRequest request;

    /**
     * instance of test target.
     */
    protected MessagesPanelTag tag;

    /**
     * create mock PageContext.
     */
    protected MockPageContext createPageContext() {
        MockServletContext sc = new MockServletContext();
        wac = mock(WebApplicationContext.class);
        when(wac.getMessage(eq("hello.world"), eq(new Object[] {}),
                (Locale) anyObject())).thenReturn("hello world!");
        when(wac.getMessage(eq("foo.bar"), eq(new Object[] { 1, 2 }),
                (Locale) anyObject())).thenReturn("foo1 and bar2");

        when(wac.getServletContext()).thenReturn(sc);
        request = new MockHttpServletRequest(sc);
        MockHttpServletResponse response = new MockHttpServletResponse();
        sc.setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                wac);
        return new MockPageContext(sc, request, response);
    }

    /**
     * setup all test case.
     */
    @SuppressWarnings("serial")
    @Before
    public void setUp() throws Exception {
        this.writer = new StringWriter();
        this.pageContext = createPageContext();
        this.tag = new MessagesPanelTag() {
            @Override
            TagWriter createTagWriter() {
                return new TagWriter(writer);
            }
        };
        tag.setPageContext(pageContext);
    }

    @Test
    public void testCreateTagWriter() {
        MessagesPanelTag tag = new MessagesPanelTag();
        tag.setPageContext(pageContext);
        assertThat(tag.createTagWriter(), is(notNullValue()));
    }

    /**
     * Set default messages attribute name & ResultMessages.fromText().<br>
     * check textMessasge in messagesPanelTag.
     */
    @Test
    public void test01() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText(
                        "hello world!")));
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessages set property key.<br>
     * check property key to textMessasge in messagesPanelTag.
     */
    @Test
    public void test02() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add("hello.world"));
        int ret = tag.doStartTag();
        // System.out.println(getOutput()
        // .replaceAll(Pattern.quote("\""), "\\\\\""));
        String expected = "<div class=\"alert alert-error\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & String message.<br>
     * check textMessasge in messagesPanelTag.
     */
    @Test
    public void test03() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                "hello world!");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessage.fromText("String message").<br>
     * check textMessasge in messagesPanelTag.
     */
    @Test
    public void test04() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessage.fromText("hello world!"));
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessage.fromCode("property key").<br>
     * check property key to textMessasge in messagesPanelTag.
     */
    @Test
    public void test05() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessage.fromCode("hello.world"));
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & null.<br>
     * check empty in messagesPanelTag and normal end.
     */
    @Test
    public void test06() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                null);
        int ret = tag.doStartTag();
        String expected = "";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Two String Array.<br>
     * check two textMessages in messagesPanelTag.
     */
    @Test
    public void test07() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                new String[] { "hello", "world" });
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>hello</li><li>world</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Arrays.<br>
     * check two textMessages in messagesPanelTag.
     */
    @Test
    public void test08() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                Arrays.asList("hello", "world"));
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>hello</li><li>world</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Add ResultMessages twice by fromText().<br>
     * check two textMessages in messagesPanelTag.
     */
    @Test
    public void test09() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.warn().add(ResultMessage.fromText("hello")).add(
                        ResultMessage.fromText("world")));
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-warn\"><ul><li>hello</li><li>world</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Add ResultMessages twice by property key.<br>
     * check two messages property key to textMessage in messagesPanelTag.
     */
    @Test
    public void test10() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.success().add("hello.world").add("foo.bar", 1,
                        2));
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-success\"><ul><li>hello world!</li><li>foo1 and bar2</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Date type.<br>
     * check date to textMessage in messagesPanelTag.
     */
    @Test
    public void test11() throws Exception {
        Date now = new Date();
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                now);
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>" + now
                + "</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & change PanelElement,OuterElement and InnerElement.<br>
     * check change PanelElement,OuterElement and PanelElement in messagesPanelTag.
     */
    @Test
    public void test12() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("hello!")).add(
                        ResultMessage.fromText("foo")));
        tag.setPanelElement("p");
        tag.setOuterElement("");
        tag.setInnerElement("span");
        int ret = tag.doStartTag();
        String expected = "<p class=\"alert alert-info\"><span>hello!</span><span>foo</span></p>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use Arrays & change PanelClassName is empty.<br>
     * check no CSS class setting in messagesPanelTag.
     */
    @Test
    public void test13() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                Arrays.asList("foo", "bar"));
        tag.setPanelClassName("");
        int ret = tag.doStartTag();
        String expected = "<div><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & change PanelClassName is empty.<br>
     * check CSS class is PanelTypeClassPrefix and ResultMessages.type in messagesPanelTag.
     */
    @Test
    public void test14() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")).add(
                        ResultMessage.fromText("bar")));
        tag.setPanelClassName("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert-info\"><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & Change PanelClassName and PanelTypeClassPrefix is empty.<br>
     * check CSS class is ResultMessages.type in messagesPanelTag.
     */
    @Test
    public void test15() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")).add(
                        ResultMessage.fromText("bar")));
        tag.setPanelClassName("");
        tag.setPanelTypeClassPrefix("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"info\"><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & Change PanelClassName and PanelTypeClassPrefix is empty.<br>
     * check CSS class is ResultMessages.type in messagesPanelTag.
     */
    @Test
    public void test16() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")).add(
                        ResultMessage.fromText("bar")));
        tag.setPanelClassName("");
        tag.setPanelTypeClassPrefix("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"info\"><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & Change PanelClassName, PanelTypeClassPrefix and MessagesType
     * is empty.<br>
     * check no CSS class in messagesPanelTag.
     */
    @Test
    public void test21() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")).add(
                        ResultMessage.fromText("bar")));
        tag.setPanelClassName("");
        tag.setPanelTypeClassPrefix("");
        tag.setMessagesType("");
        int ret = tag.doStartTag();
        String expected = "<div><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & change OuterElement and InnerElement is empty.<br>
     * check no OuterElement and PanelElement in messagesPanelTag.
     */
    @Test
    public void test17() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")));
        tag.setOuterElement("");
        tag.setInnerElement("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-info\">foo</div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    @Test
    public void testDoStartTagInternalPanelElementAndOuterElementEmpty() throws Exception {
        // set up
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")));
        tag.setPanelElement("");
        tag.setOuterElement("");
        int ret = tag.doStartTag();
        String expected = "<li>foo</li>";

        // assert
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    @Test
    public void testDoStartTagInternalPanelElementEmpty() throws Exception {
        // set up
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")));
        tag.setPanelElement("");
        int ret = tag.doStartTag();
        String expected = "<ul><li>foo</li></ul>";

        // assert
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use ResultMessages & change PanelElement,OuterElement and InnerElement is empty.<br>
     * check JspTagException.
     */
    @Test(expected = JspTagException.class)
    public void test28() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")));
        tag.setPanelElement("");
        tag.setOuterElement("");
        tag.setInnerElement("");
        tag.doStartTag();
    }

    @Test(expected = JspTagException.class)
    public void testDoStartTagInternalElementNull() throws Exception {
        // set up
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.info().add(ResultMessage.fromText("foo")));
        tag.setPanelElement(null);
        tag.setOuterElement(null);
        tag.setInnerElement(null);

        // try
        tag.doStartTag();
    }

    /**
     * Set default messages attribute name & Use Arrays & change PanelClassName is empty.<br>
     * check no CSS class in messagesPanelTag.
     */
    @Test
    public void test18() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                Arrays.asList("foo", null));
        tag.setPanelClassName("");
        int ret = tag.doStartTag();
        String expected = "<div><ul><li>foo</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set attribute name & Use ResultMessages.<br>
     * check messagesPanelTag can get set Attribute name.
     */
    @Test
    public void test19() throws Exception {
        request.setAttribute("result", ResultMessages.error().add(ResultMessage
                .fromText("hello world!")));
        tag.setMessagesAttributeName("result");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use int array.<br>
     * check int to two textMessage in messagesPanelTag.
     */
    @Test
    public void test20() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                new int[] { 1, 2 });
        tag.setPanelClassName("");
        int ret = tag.doStartTag();
        String expected = "<div><ul><li>1</li><li>2</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & set MessagesType is empty.<br>
     * check PanelTypeClassPrefix + MessagesType is empty in messagesPanelTag.
     */
    @Test
    public void test22() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                "hello world!");
        tag.setMessagesType("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & set MessagesType is empty.<br>
     * check PanelTypeClassPrefix + MessagesType is empty in messagesPanelTag.
     */
    @Test
    public void test23() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                "hello world!");
        tag.setMessagesType("error");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use textMessage & set PanelClassName and PanelTypeClassPrefix is empty &
     * MessagesType is "error".<br>
     * check CSS class is error in messagesPanelTag.
     */
    @Test
    public void test24() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                "hello world!");
        tag.setPanelClassName("");
        tag.setPanelTypeClassPrefix("");
        tag.setMessagesType("error");
        int ret = tag.doStartTag();
        String expected = "<div class=\"error\"><ul><li>hello world!</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use arrays & set MessagesType is empty.<br>
     * check PanelTypeClassPrefix + MessagesType is empty and array messages in messagesPanelTag.
     */
    @Test
    public void test25() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                Arrays.asList("foo", "bar"));
        tag.setMessagesType("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use arrays & set MessagesType is "error".<br>
     * check use default CSS class by set MessagesType in messagesPanelTag.
     */
    @Test
    public void test26() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                Arrays.asList("foo", "bar"));
        tag.setMessagesType("error");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Use Array & set PanelClassName and PanelTypeClassPrefix is empty & MessagesType is
     * "error".<br>
     * check CSS class is error in messagesPanelTag.
     */
    @Test
    public void test27() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                Arrays.asList("foo", "bar"));
        tag.setPanelClassName("");
        tag.setPanelTypeClassPrefix("");
        tag.setMessagesType("error");
        int ret = tag.doStartTag();
        String expected = "<div class=\"error\"><ul><li>foo</li><li>bar</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessages.fromText().<br>
     * check that message text gets escaped if htmlEscapingEnabled is set to true.
     */
    @Test
    public void test29() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText("<div>")));
        tag.setDisableHtmlEscape("true");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li><div></li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessages.fromText().<br>
     * check that message text is not escaped if htmlEscapingEnabled is set to false.
     */
    @Test
    public void test30() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText("<div>")));
        tag.setDisableHtmlEscape("false");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>&lt;div&gt;</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessages.fromText().<br>
     * check that message text gets escaped if htmlEscapingEnabled is set to null.
     */
    @Test
    public void test31() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText("<div>")));
        tag.setDisableHtmlEscape(null);
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>&lt;div&gt;</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessages.fromText().<br>
     * check that message text gets escaped if htmlEscapingEnabled is set to empty string.
     */
    @Test
    public void test32() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText("<div>")));
        tag.setDisableHtmlEscape("");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert alert-error\"><ul><li>&lt;div&gt;</li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & ResultMessages.fromText().<br>
     * check that JspTagException occurs if htmlEscapingEnabled is set to unexpected String.
     */
    @Test(expected = JspTagException.class)
    public void test33() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText("<div>")));
        tag.setDisableHtmlEscape("aaaa");
        tag.doStartTag();
    }

    /**
     * Set default messages attribute name & Empty Array.<br>
     * check that start tags are not self-closing in messagesPanelTag.
     */
    @Test
    public void test34() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                new String[] {});
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    /**
     * Set default messages attribute name & Empty String.<br>
     * check that start tags are not self-closing in messagesPanelTag.
     */
    @Test
    public void test35() throws Exception {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                "");
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li></li></ul></div>";
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    protected String getOutput() {
        return this.writer.toString();
    }

    @Test
    public void testAppendPanelTypeClassPrefixPanelTypeClassPrefixNull() throws Exception {
        // set up
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                ResultMessages.error().add(ResultMessage.fromText("foo")));
        tag.setPanelTypeClassPrefix(null);
        int ret = tag.doStartTag();
        String expected = "<div class=\"alerterror\"><ul><li>foo</li></ul></div>";

        // assert
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }

    @Test
    public void testGetTextMessageInstanceofThrowable() throws Exception {
        // set up
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                new Throwable());
        int ret = tag.doStartTag();
        String expected = "<div class=\"alert\"><ul><li></li></ul></div>";

        // assert
        assertThat(getOutput(), is(expected));
        assertThat(ret, is(TagSupport.EVAL_BODY_INCLUDE));
    }
}
