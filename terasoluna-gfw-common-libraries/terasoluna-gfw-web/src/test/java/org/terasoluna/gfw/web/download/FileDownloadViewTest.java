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
package org.terasoluna.gfw.web.download;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class FileDownloadViewTest {

    private FileDownloadView fileDownloadView;

    private HttpServletRequest request;

    private HttpServletResponse response;

    Map<String, Object> model;

    private InputStream inputStream;

    private class FileDownloadView extends AbstractFileDownloadView {
        private InputStream inputStream;

        private boolean throwIOException = false;

        public void setThrowIOException(boolean throwIOException) {
            this.throwIOException = throwIOException;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        protected InputStream getInputStream(Map<String, Object> model,
                HttpServletRequest request) throws IOException {

            // This is just for simulating exception throwing
            if (throwIOException) {
                throw new IOException();
            }

            return inputStream;
        }

        @Override
        protected void addResponseHeader(Map<String, Object> model,
                HttpServletRequest request, HttpServletResponse response) {
            response.setHeader("MethodCalled", "true");
        }
    }

    @Before
    public void setup() throws FileNotFoundException {

        inputStream = this.getClass().getResourceAsStream("test.txt");

        fileDownloadView = new FileDownloadView();
        fileDownloadView.setInputStream(inputStream);
        fileDownloadView.setThrowIOException(false);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        model = new HashMap<String, Object>();

    }

    @Test
    public void testrenderMergedOutputModel() throws IOException {
        fileDownloadView.renderMergedOutputModel(model, request, response);
        assertThat(response.getHeader("MethodCalled"), is("true"));
    }

    @Test(expected = IOException.class)
    public void testrenderMergedOutputModelWithNullInputStream() throws IOException {
        fileDownloadView.setInputStream(null);
        fileDownloadView.renderMergedOutputModel(model, request, response);
    }

    @Test(expected = IOException.class)
    public void testrenderMergedOutputModelWithInvalidInputStream() throws IOException {
        fileDownloadView.setInputStream(null);
        // Set flag so that getInputStream will throw exception in any case
        fileDownloadView.setThrowIOException(true);
        fileDownloadView.renderMergedOutputModel(model, request, response);
    }

    @Test
    public void testWriteResponseStreamWithBothStreamsNull() {
        try {
            fileDownloadView.writeResponseStream(null, null);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testWriteResponseStreamWithNullOutputStream() {
        try {
            fileDownloadView.writeResponseStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSetChunkSize() {
        try {
            fileDownloadView.setChunkSize(512);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = IOException.class)
    public void testOutputStreamException() throws IOException {
        // Set Mock Behavior
        response = mock(HttpServletResponse.class);
        when(response.getOutputStream()).thenThrow(new IOException());
        fileDownloadView.renderMergedOutputModel(model, request, response);
    }

    @Test
    public void testAfterPropertiesSet_chunkSize_is0() throws IOException {
        // Set Mock Behavior
        fileDownloadView.setChunkSize(0);
        try {
            fileDownloadView.afterPropertiesSet();
            fail("must occur IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "chunkSize must be over 1. specified chunkSize is \"0\"."));
        }
    }

    @Test
    public void testAfterPropertiesSet_chunkSize_isNegative1() throws IOException {
        // Set Mock Behavior
        fileDownloadView.setChunkSize(-1);
        try {
            fileDownloadView.afterPropertiesSet();
            fail("must occur IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(
                    "chunkSize must be over 1. specified chunkSize is \"-1\"."));
        }
    }

    @Test
    public void testAfterPropertiesSet_chunkSize_is1() throws IOException {
        // Set Mock Behavior
        fileDownloadView.setChunkSize(1);
        try {
            fileDownloadView.afterPropertiesSet();
        } catch (IllegalArgumentException e) {
            fail("must not occur IllegalArgumentException.");
        }
    }
}
