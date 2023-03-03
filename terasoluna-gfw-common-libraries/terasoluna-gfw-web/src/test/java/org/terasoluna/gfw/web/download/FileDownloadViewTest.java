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
package org.terasoluna.gfw.web.download;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FileDownloadViewTest {

    @SuppressWarnings("unchecked")
    private Appender<ILoggingEvent> mockAppender = mock(Appender.class);

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

    private class FileDownloadViewWriteFailed extends FileDownloadView {
        @Override
        protected void writeResponseStream(InputStream inputStream,
                OutputStream outputStream) throws IOException {
            throw new IOException("test intentionally failed");
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

        // mock logger
        Logger logger = (Logger) LoggerFactory.getLogger(
                FileDownloadView.class);
        logger.addAppender(mockAppender);
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

    @Test(expected = IOException.class)
    public void testrenderMergedOutputModelWithWriteStreamFailed() throws IOException {
        FileDownloadView fdlv = new FileDownloadViewWriteFailed();
        fdlv.setInputStream(inputStream);
        fdlv.setThrowIOException(false);

        fdlv.renderMergedOutputModel(model, request, response);
        verify(mockAppender).doAppend(argThat(argument -> argument.getLevel()
                .equals(Level.ERROR)));
    }

    @Test(expected = IOException.class)
    public void testrenderMergedOutputModelFlushFailed() throws IOException {
        ServletOutputStream sos = mock(ServletOutputStream.class);
        doThrow(new IOException("test intentionally failed")).when(sos).flush();
        response = mock(MockHttpServletResponse.class);
        when(response.getOutputStream()).thenReturn(sos);

        fileDownloadView.renderMergedOutputModel(model, request, response);
        verify(mockAppender).doAppend(argThat(argument -> argument.getLevel()
                .equals(Level.ERROR)));
    }

    @Test(expected = None.class)
    public void testrenderMergedOutputModelCloseFailed() throws IOException {
        InputStream is = mock(InputStream.class);
        doThrow(new IOException("test intentionally failed")).when(is).close();
        fileDownloadView.setInputStream(is);

        fileDownloadView.renderMergedOutputModel(model, request, response);
        verify(mockAppender).doAppend(argThat(argument -> argument.getLevel()
                .equals(Level.WARN)));
    }

    @Test(expected = None.class)
    public void testWriteResponseStreamWithBothStreamsNull() throws Exception {
        fileDownloadView.writeResponseStream(null, null);
    }

    @Test(expected = None.class)
    public void testWriteResponseStreamWithNullOutputStream() throws Exception {
        fileDownloadView.writeResponseStream(inputStream, null);
    }

    @Test(expected = None.class)
    public void testSetChunkSize() throws Exception {
        fileDownloadView.setChunkSize(512);
    }

    @Test
    public void testOutputStreamException() {
        // Set Mock Behavior
        response = mock(HttpServletResponse.class);

        assertThrows(IOException.class, () -> {
            when(response.getOutputStream()).thenThrow(new IOException());
            fileDownloadView.renderMergedOutputModel(model, request, response);
        });
    }

    @Test
    public void testAfterPropertiesSet_chunkSize_is0() {
        // Set Mock Behavior
        fileDownloadView.setChunkSize(0);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    fileDownloadView.afterPropertiesSet();
                });
        assertThat(e.getMessage(), is(
                "chunkSize must be over 1. specified chunkSize is \"0\"."));
    }

    @Test
    public void testAfterPropertiesSet_chunkSize_isNegative1() {
        // Set Mock Behavior
        fileDownloadView.setChunkSize(-1);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> {
                    fileDownloadView.afterPropertiesSet();
                });
        assertThat(e.getMessage(), is(
                "chunkSize must be over 1. specified chunkSize is \"-1\"."));
    }

    @Test(expected = None.class)
    public void testAfterPropertiesSet_chunkSize_is1() {
        // Set Mock Behavior
        fileDownloadView.setChunkSize(1);

        fileDownloadView.afterPropertiesSet();
    }
}
