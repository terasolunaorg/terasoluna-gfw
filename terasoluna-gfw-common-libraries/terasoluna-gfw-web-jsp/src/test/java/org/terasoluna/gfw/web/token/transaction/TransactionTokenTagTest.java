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
package org.terasoluna.gfw.web.token.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.junit.Test;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenTag;

/**
 * Test class for TransactionTokenTag
 */
public class TransactionTokenTagTest {

    /**
     * TransactionToken is null
     */
    @Test
    public void testWriteTagContentTagWriter01() {

        // setup arguments
        TransactionTokenTag tag = new TransactionTokenTag();
        PageContext pageContext = mock(PageContext.class);
        tag.setPageContext(pageContext);
        HttpServletRequest request = mock(HttpServletRequest.class);
        StringWriter sw = new StringWriter();
        TagWriter tagWriter = new TagWriter(sw);

        // mock behavior
        when((HttpServletRequest) pageContext.getRequest()).thenReturn(request);
        when((TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME))
                        .thenReturn(null);

        // run
        int result = 1;
        try {
            result = tag.writeTagContent(tagWriter);
        } catch (JspException e) {
            fail();
        }

        // assert
        assertThat(sw.getBuffer().toString(), is(""));
        assertThat(result, is(0));
    }

    /**
     * TransactionToken is not null
     */
    @Test
    public void testWriteTagContentTagWriter02() {

        // setup arguments
        TransactionTokenTag tag = new TransactionTokenTag();
        PageContext pageContext = mock(PageContext.class);
        tag.setPageContext(pageContext);
        HttpServletRequest request = mock(HttpServletRequest.class);
        TransactionToken token = new TransactionToken("tokenName", "tokenkey", "tokenValue");
        StringWriter sw = new StringWriter();
        TagWriter tagWriter = new TagWriter(sw);

        // mock behavior
        when((HttpServletRequest) pageContext.getRequest()).thenReturn(request);
        when((TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME))
                        .thenReturn(token);

        // run
        int result = 1;
        try {
            result = tag.writeTagContent(tagWriter);
        } catch (JspException e) {
            fail();
        }

        // capture
        String expected = "<input type=\"hidden\" name=\""
                + TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER
                + "\" value=\"" + token.getTokenString() + "\"/>";

        // assert
        assertThat(sw.getBuffer().toString(), is(expected));
        assertThat(result, is(0));
    }

    /**
     * JspException occurs
     */
    @Test
    public void testWriteTagContentTagWriter03() {

        // setup arguments
        TransactionTokenTag tag = new TransactionTokenTag();
        PageContext pageContext = mock(PageContext.class);
        tag.setPageContext(pageContext);
        HttpServletRequest request = mock(HttpServletRequest.class);
        TransactionToken token = new TransactionToken("tokenName", "tokenkey", "tokenValue");
        TagWriter tagWriter = mock(TagWriter.class);

        // mock behavior
        when((HttpServletRequest) pageContext.getRequest()).thenReturn(request);
        when((TransactionToken) request.getAttribute(
                TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME))
                        .thenReturn(token);

        // run
        int result = 1;
        try {
            doThrow(new JspException()).when(tagWriter).startTag(anyString());
            result = tag.writeTagContent(tagWriter);
        } catch (JspException e) {
            e.printStackTrace();
        }
        assertThat(result, is(1));
    }
}
