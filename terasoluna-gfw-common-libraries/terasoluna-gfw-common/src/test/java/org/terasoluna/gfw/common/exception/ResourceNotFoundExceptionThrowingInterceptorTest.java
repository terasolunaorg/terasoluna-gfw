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
package org.terasoluna.gfw.common.exception;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.terasoluna.gfw.common.exception.test.ResourceNotFoundRepository;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml",
        "classpath:org/terasoluna/gfw/common/exception/ResourceNotFoundExceptionThrowingInterceptorTest.xml" })
public class ResourceNotFoundExceptionThrowingInterceptorTest {

    private static MessageSource messageSource = new StaticMessageSource() {
        {
            addMessage("noargs", LocaleContextHolder.getLocale(),
                    "message[{0},{1},{2}]");
            addMessage("args", LocaleContextHolder.getLocale(),
                    "message[{0},{1},{2}]");
        }
    };

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Inject
    private ResourceNotFoundRepository repository;

    @Test
    public void testObjectIsNull() {
        expectCode(StandardResultMessageType.ERROR, "object");
        repository.object(null);
    }

    @Test
    public void testObjectIsNotNull() {
        repository.object("");
    }

    @Test
    public void testArrayIsNull() {
        expectCode(StandardResultMessageType.ERROR, "array");
        repository.array(null);
    }

    @Test
    public void testArrayIsEmpty() {
        expectCode(StandardResultMessageType.ERROR, "array");
        repository.array(new Object[0]);
    }

    @Test
    public void testArrayIsNotEmpty() {
        repository.array(new Object[] { "" });
    }

    @Test
    public void testIterableIsNull() {
        expectCode(StandardResultMessageType.ERROR, "iterable");
        repository.iterable(null);
    }

    @Test
    public void testIterableIsEmpty() {
        expectCode(StandardResultMessageType.ERROR, "iterable");
        repository.iterable(Collections.emptySet());
    }

    @Test
    public void testIterableIsNotEmpty() {
        repository.iterable(Collections.singleton(""));
    }

    @Test
    public void testCollectionIsNull() {
        expectCode(StandardResultMessageType.ERROR, "collection");
        repository.collection(null);
    }

    @Test
    public void testCollectionIsEmpty() {
        expectCode(StandardResultMessageType.ERROR, "collection");
        repository.collection(Collections.emptyList());
    }

    @Test
    public void testCollectionIsNotEmpty() {
        repository.collection(Collections.singletonList(""));
    }

    @Test
    public void testMapIsNull() {
        expectCode(StandardResultMessageType.ERROR, "map");
        repository.map(null);
    }

    @Test
    public void testMapIsEmpty() {
        expectCode(StandardResultMessageType.ERROR, "map");
        repository.map(Collections.emptyMap());
    }

    @Test
    public void testMapIsNotEmpty() {
        repository.map(Collections.singletonMap("", ""));
    }

    @Test
    public void testStreamIsNull() {
        expectCode(StandardResultMessageType.ERROR, "stream");
        repository.stream(null);
    }

    @Test
    public void testStreamIsEmpty() {
        expectCode(StandardResultMessageType.ERROR, "stream");
        repository.stream(Collections.emptyList().stream());
    }

    @Test
    public void testStreamIsNotEmpty() {
        repository.stream(Collections.singletonList(new Object()).stream());
    }

    @Test
    public void testArray2IsNull() {
        expectCode(StandardResultMessageType.ERROR, "array2");
        repository.array2(null);
    }

    @Test
    public void testArray2IsEmpty() {
        repository.array2(new Object[0]);
    }

    @Test
    public void testArray2IsNotEmpty() {
        repository.array2(new Object[] { "" });
    }

    @Test
    public void testIterable2IsNull() {
        expectCode(StandardResultMessageType.ERROR, "iterable2");
        repository.iterable2(null);
    }

    @Test
    public void testIterable2IsEmpty() {
        repository.iterable2(Collections.emptySet());
    }

    @Test
    public void testIterable2IsNotEmpty() {
        repository.iterable2(Collections.singleton(""));
    }

    @Test
    public void testCollection2IsNull() {
        expectCode(StandardResultMessageType.ERROR, "collection2");
        repository.collection2(null);
    }

    @Test
    public void testCollection2IsEmpty() {
        repository.collection2(Collections.emptyList());
    }

    @Test
    public void testCollection2IsNotEmpty() {
        repository.collection2(Collections.singletonList(""));
    }

    @Test
    public void testMap2IsNull() {
        expectCode(StandardResultMessageType.ERROR, "map2");
        repository.map2(null);
    }

    @Test
    public void testMap2IsEmpty() {
        repository.map2(Collections.emptyMap());
    }

    @Test
    public void testMap2IsNotEmpty() {
        repository.map2(Collections.singletonMap("", ""));
    }

    @Test
    public void testStream2IsNull() {
        expectCode(StandardResultMessageType.ERROR, "stream2");
        repository.stream2(null);
    }

    @Test
    public void testStream2IsEmpty() {
        repository.stream2(Collections.emptyList().stream());
    }

    @Test
    public void testStream2IsNotEmpty() {
        repository.stream2(Collections.<Object> singletonList("").stream());
    }

    @Test
    public void testNoArgs() {
        try {
            repository.noargs();
        } catch (ResourceNotFoundException e) {
            assertMessage(e.getResultMessages(), "message[{0},{1},{2}]");
            return;
        }
        fail("not throwen.");
    }

    @Test
    public void testArgs() {
        try {
            repository.args("a", "b", "c");
        } catch (ResourceNotFoundException e) {
            assertMessage(e.getResultMessages(), "message[a,b,c]");
            return;
        }
        fail("not throwen.");
    }

    @Test
    public void testText() {
        expectText(StandardResultMessageType.ERROR, "text");
        repository.text();
    }

    @Test
    public void testNoMessage() {
        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        ((Logger) LoggerFactory.getLogger(
                ResourceNotFoundExceptionThrowingInterceptor.class))
                        .addAppender(mockAppender);

        expectType(StandardResultMessageType.ERROR);
        try {
            repository.nomessage();
        } catch (ResourceNotFoundException e) {
            verify(mockAppender, times(1)).doAppend(argThat(arg -> arg
                    .getLevel() == Level.WARN));
            throw e;
        }
    }

    @Test
    public void testExtend() {
        expectCode(StandardResultMessageType.ERROR, "extend");
        repository.extend();
    }

    private void expectType(StandardResultMessageType type) {
        exception.expect(ResourceNotFoundException.class);
        exception.expect(hasProperty("resultMessages", hasProperty("type", is(
                type))));
    }

    private void expectCode(StandardResultMessageType type, String code) {
        expectType(type);
        exception.expect(hasProperty("resultMessages", hasProperty("list",
                contains(hasProperty("code", is(code))))));
    }

    private void expectText(StandardResultMessageType type, String text) {
        expectType(type);
        exception.expect(hasProperty("resultMessages", hasProperty("list",
                contains(hasProperty("text", is(text))))));
    }

    private void assertMessage(ResultMessages messages, String expected) {
        ResultMessage message = messages.getList().iterator().next();
        assertThat(messageSource.getMessage(message.getCode(), message
                .getArgs(), LocaleContextHolder.getLocale()), is(expected));
    }
}
