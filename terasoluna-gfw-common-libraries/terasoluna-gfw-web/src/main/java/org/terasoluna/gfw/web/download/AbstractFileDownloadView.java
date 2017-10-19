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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Abstract View class used for downloading binary files. <br>
 * <p>
 * Writes binary data to the response object. Also, depending on the requirement, the following should be implemented in
 * subclass: <br>
 * a) Fetching the stream that writes to the response body <br>
 * b) Setting the response header information <br>
 * </p>
 * <p>
 * In case the implementation class of this class is to be used, bean definition of
 * {@code org.springframework.web.servlet.view.BeanNameViewResolver} should be done.
 * </p>
 * <h3>Example of bean definition file</h3>
 * 
 * <pre>
 * &lt;bean id=&quot;beanNameViewResolver&quot;
 *     class=&quot;org.springframework.web.servlet.view.BeanNameViewResolver&quot;&gt;
 *     &lt;property name=&quot;order&quot; value=&quot;1&quot; /&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * Next, {@code viewName} property should be defined in the bean definition of the controller which is responsible for sending
 * binary data to response
 * </p>
 * <h3>Example of bean definition file</h3>
 * 
 * <pre>
 * &lt;bean id=<strong style=color:red>&quot;sampleFileDownloadView&quot;</strong> class=&quot;org.terasoluna.gfw.web.sample.SampleFileDownloadView&quot;&gt;
 *     &lt;!-- injections.. --&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * Further, the view name should be returned in the corresponding RequestMapping method method in a controller.
 * </p>
 * <h3>Example of bean definition file</h3>
 * 
 * <pre>
 * {@literal @}Controller
 * {@literal @}RequestMapping(&quot;sample&quot;)
 * public class SampleController {
 *     // ...
 *     {@literal @}RequestMapping(&quot;fileDownload&quot;)
 *     public String download(Model model) {
 *         // ...
 *         return <strong style=color:red>&quot;sampleFileDownloadView&quot;</strong>;
 *     }
 * }
 * </pre>
 */
public abstract class AbstractFileDownloadView extends AbstractView implements
                                               InitializingBean {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * chunk size
     */
    private int chunkSize = 256;

    /**
     * Renders the response.
     * @param model Model object
     * @param request current HTTP request
     * @param response current HTTP response
     * @throws IOException Input/output exception
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        logger.debug("FileDownload start.");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // Fetching download file data
            try {
                inputStream = getInputStream(model, request);
            } catch (IOException e) {
                // In case download fails
                logger.error(
                        "FileDownload Failed with getInputStream(). cause message is {}.",
                        e.getMessage());
                throw e;
            }
            if (inputStream == null) {
                throw new IOException("FileDownload Failed. InputStream is null.");
            }

            // Writing to output stream of HTTP response
            try {
                outputStream = new BufferedOutputStream(response
                        .getOutputStream());
            } catch (IOException e) {
                // In case download fails
                logger.error(
                        "FileDownload Failed with getOutputStream(). cause message is {}.",
                        e.getMessage());
                throw e;
            }

            // Editing header part
            addResponseHeader(model, request, response);

            try {
                writeResponseStream(inputStream, outputStream);
            } catch (IOException e) {
                // In case download fails
                logger.error(
                        "FileDownload Failed with writeResponseStream(). cause message is {}.",
                        e.getMessage());
                throw e;
            }
            try {
                outputStream.flush();
            } catch (IOException e) {
                // In case download fails
                logger.error(
                        "FileDownload Failed with flush. cause message is {}.",
                        e.getMessage());
                throw e;
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    logger.warn("Cannot close InputStream.", ioe);
                }
            }
        }
    }

    /**
     * Fetches the stream which writes to the response body
     * @param model Model object
     * @param request current HTTP Request
     * @return stream that will write to the request
     * @throws IOException Input/output exception
     */
    protected abstract InputStream getInputStream(Map<String, Object> model,
            HttpServletRequest request) throws IOException;

    /**
     * Writes the download file to the stream of HTTP response.
     * @param inputStream InputStream of file data to be downloaded
     * @param outputStream OutputStream of the response
     * @throws IOException Input/output exception (Exception handling should be done at the caller side)
     */
    protected void writeResponseStream(InputStream inputStream,
            OutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream == null) {
            return;
        }

        byte[] buffer = new byte[chunkSize];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
    }

    /**
     * Adds response header
     * @param model Model object
     * @param request current HTTP request
     * @param response current HTTP response
     */
    protected abstract void addResponseHeader(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response);

    /**
     * Set a chunk size.
     * @param chunkSize chunk size to buffer
     */
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    /**
     * Initializes the exception filter.
     * <p>
     * validate the chunkSize field.
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize must be over 1. specified chunkSize is \""
                    + chunkSize + "\".");
        }
    }

}
