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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.terasoluna.gfw.common.exception.ExceptionLevel;

public class DefaultExceptionLevelResolverTest {

    private DefaultExceptionLevelResolver testTarget;

    @Before
    public void setUp() throws Exception {
        this.testTarget = new DefaultExceptionLevelResolver(new SimpleMappingExceptionCodeResolver());
    }

    @Test
    public void testResolveExceptionLevel_info() {
        // do setup.
        Exception exception = new SystemException("i", "info.");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.INFO;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

    @Test
    public void testResolveExceptionLevel_warn() {
        // do setup.
        Exception exception = new SystemException("W.i", "warn.");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.WARN;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

    @Test
    public void testResolveExceptionLevel_error() {
        // do setup.
        Exception exception = new SystemException("Error.i.e", "error");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.ERROR;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

    @Test
    public void testResolveExceptionLevel_undefine() {
        // do setup.
        Exception exception = new SystemException("F.i.e", "undefine");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.ERROR;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

    @Test
    public void testResolveExceptionLevel_exceptionCode_is_unresolve() {
        // do setup.
        Exception exception = new Exception("error.");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.ERROR;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

    @Test
    public void testResolveExceptionLevel_exceptionCode_is_empty() {
        // do setup.
        Exception exception = new SystemException("", "error.");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.ERROR;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

    @Test
    public void testResolveExceptionCode_resolver_is_undefine() {
        // do setup.
        testTarget = new DefaultExceptionLevelResolver();
        Exception exception = new SystemException("i.w.e", "error.");

        // do test.
        ExceptionLevel actualExceptionLevel = testTarget.resolveExceptionLevel(
                exception);

        // do assert.
        ExceptionLevel expectedExceptionLevel = ExceptionLevel.ERROR;
        assertThat(actualExceptionLevel, is(expectedExceptionLevel));
    }

}
