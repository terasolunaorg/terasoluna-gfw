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
package org.terasoluna.gfw.web.token.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringWriter;

import org.junit.Test;
import org.springframework.web.servlet.tags.form.TagWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;

/**
 * Test class for TransactionTokenTag
 */
public class TransactionTokenTagTest {

    /**
     * TransactionToken is null
     */
    @Test
    public void testWriteTagContentTagWriter01() throws Exception {

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
        int result = tag.writeTagContent(tagWriter);

        // assert
        assertThat(sw.getBuffer(), hasLength(0));
        assertThat(result, is(0));
    }

    /**
     * TransactionToken is not null
     */
    @Test
    public void testWriteTagContentTagWriter02() throws Exception {

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
        int result = tag.writeTagContent(tagWriter);

        // capture
        String expected = "<input type=\"hidden\" name=\""
                + TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER
                + "\" value=\"" + token.getTokenString() + "\"/>";

        // assert
        assertThat(sw.getBuffer(), hasToString(expected));
        assertThat(result, is(0));
    }

    /**
     * JspException occurs
     * @throws JspException
     */
    @Test
    public void testWriteTagContentTagWriter03() throws JspException {

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

        doThrow(new JspException()).when(tagWriter).startTag(anyString());

        // run
        assertThrows(JspException.class, () -> {
            tag.writeTagContent(tagWriter);
        });
    }
}
