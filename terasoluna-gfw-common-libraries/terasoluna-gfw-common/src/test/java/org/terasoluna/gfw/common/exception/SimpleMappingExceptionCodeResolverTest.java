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
package org.terasoluna.gfw.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.SimpleMappingExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.SystemException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * TestCase class of SimpleMappingExceptionCodeResolver.
 */
public class SimpleMappingExceptionCodeResolverTest {

    private SimpleMappingExceptionCodeResolver testTarget;

    /**
     * set up.
     */
    @Before
    public void setUp() {
        testTarget = new SimpleMappingExceptionCodeResolver();
    }

    /**
     * [resolveExceptionCode] Case of occur system exception with exists code.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is code of system exception.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_systemexception_exists_code() {

        // do setup.
        // nothing.

        // do test.
        String actualExceptionCode1 = testTarget.resolveExceptionCode(
                new SystemException("testcode001", "testmessage001"));
        String actualExceptionCode2 = testTarget.resolveExceptionCode(
                new SystemException("testcode002", "testmessage002"));

        // do assert.
        assertThat(actualExceptionCode1, is("testcode001"));
        assertThat(actualExceptionCode2, is("testcode002"));
    }

    /**
     * [resolveExceptionCode] Case of occur system exception(code not extist) & exists exception mapping.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is code of exception mapping.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_systemexception_not_exists_code() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("SystemException", "sys001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new SystemException(null, "testmessage001"));

        // do assert.
        assertThat(actualExceptionCode, is("sys001"));
    }

    /**
     * [resolveExceptionCode] Case of occur other exception & real class's define exists exception mapping.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is code of exception mapping.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_other_exception_match_realclass() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("BusinessException", "bus001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));
        // do assert.
        assertThat(actualExceptionCode, is("bus001"));
    }

    /**
     * [resolveExceptionCode] Case of occur other exception & parent class's define exists exception mapping.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>follow up to the parent class.</li>
     * <li>exception code is code of exception mapping.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_other_exception_match_parentclass() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("RuntimeException", "runtime001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));

        // do assert.
        assertThat(actualExceptionCode, is("runtime001"));

    }

    /**
     * [resolveExceptionCode] Case of occur other exception & root class's define exists exception mapping.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>follow up to the root class.</li>
     * <li>exception code is code of exception mapping.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_other_exception_match_rootclass() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("Throwable", "throwable001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));

        // do assert.
        assertThat(actualExceptionCode, is("throwable001"));

    }

    /**
     * [resolveExceptionCode] Case of occur other exception & not exists matched definition.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is default exception code.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_other_exception_not_match() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("Error", "throwable001");
        testTarget.setExceptionMappings(exceptionMappings);
        testTarget.setDefaultExceptionCode("defaultExceptionCode001");

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));

        // do assert.
        assertThat(actualExceptionCode, is("defaultExceptionCode001"));

    }

    /**
     * [resolveExceptionCode] Case of occur other exception & exists multiple definitions.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>done the matching process in the order in which were added.</li>
     * <li>all definitions is enabled.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_other_exception_multiple_definitions() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("SystemException", "systemexception");
        exceptionMappings.put("IOException", "ioexception");
        exceptionMappings.put("RuntimeException", "runtimeexception");
        testTarget.setExceptionMappings(exceptionMappings);
        testTarget.setDefaultExceptionCode("default");

        // do test.
        String actualExceptionCode1 = testTarget.resolveExceptionCode(
                new SystemException(null, "syserror"));
        String actualExceptionCode2 = testTarget.resolveExceptionCode(
                new FileNotFoundException("filenotfounderror"));
        String actualExceptionCode3 = testTarget.resolveExceptionCode(
                new NullPointerException("nullerror"));
        String actualExceptionCode4 = testTarget.resolveExceptionCode(
                new ParseException("parseerror", 0));

        // do assert.
        assertThat(actualExceptionCode1, is("systemexception"));
        assertThat(actualExceptionCode2, is("ioexception"));
        assertThat(actualExceptionCode3, is("runtimeexception"));
        assertThat(actualExceptionCode4, is("default"));

    }

    /**
     * [resolveExceptionCode] Case of define head match.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>head match is enabled.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_head_match() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("org.terasoluna.gfw.common.exception", "tera001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode1 = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));
        String actualExceptionCode2 = testTarget.resolveExceptionCode(
                new TestException());
        String actualExceptionCode3 = testTarget.resolveExceptionCode(
                new NullPointerException("nullpointer"));

        // do assert.
        assertThat(actualExceptionCode1, is("tera001"));
        assertThat(actualExceptionCode2, is("tera001"));
        assertThat(actualExceptionCode3, is(nullValue()));
    }

    /**
     * [resolveExceptionCode] Case of define partial match.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>partial match is enabled.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_partial_match() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("terasoluna.gfw.common", "teracommon001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));

        // do assert.
        assertThat(actualExceptionCode, is("teracommon001"));

    }

    /**
     * [resolveExceptionCode] Case of define backward match.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>backward match is enabled.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_backward_match() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("Exception", "exception001");
        testTarget.setExceptionMappings(exceptionMappings);

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new BusinessException(ResultMessages.error()));

        // do assert.
        assertThat(actualExceptionCode, is("exception001"));

    }

    /**
     * [resolveExceptionCode] Case of exception mapping is null.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is default exception code.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_exceptionMappings_null() {

        // do setup.
        testTarget.setDefaultExceptionCode("defaultExceptionCode002");

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new NullPointerException("testMessage"));

        // do assert.
        assertThat(actualExceptionCode, is("defaultExceptionCode002"));

    }

    /**
     * [resolveExceptionCode] Case of exception mapping is empty.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is default exception code.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_exceptionMappings_empty() {

        // do setup.
        testTarget.setDefaultExceptionCode("defaultExceptionCode003");
        testTarget.setExceptionMappings(new LinkedHashMap<String, String>());

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(
                new NullPointerException("testMessage"));

        // do assert.
        assertThat(actualExceptionCode, is("defaultExceptionCode003"));

    }

    /**
     * [resolveExceptionCode] exception is null.
     * <p>
     * [Expected Result]
     * <ol>
     * <li>exception code is defaultExceptionCode.</li>
     * </ol>
     * </p>
     */
    @Test
    public void testResolveExceptionCode_exception_isNull() {

        // do setup.
        LinkedHashMap<String, String> exceptionMappings = new LinkedHashMap<String, String>();
        exceptionMappings.put("SystemException", "sys001");
        testTarget.setExceptionMappings(exceptionMappings);
        testTarget.setDefaultExceptionCode("default001");

        // do test.
        String actualExceptionCode = testTarget.resolveExceptionCode(null);

        // do assert.
        assertThat(actualExceptionCode, is("default001"));
    }

    /**
     * exception class for test.
     */
    private class TestException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private TestException() {
            super();
        }
    }
}
