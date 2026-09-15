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
package org.terasoluna.gfw.common.exception.test;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.terasoluna.gfw.common.exception.annotation.ResourceMustBeFound;
import org.terasoluna.gfw.common.exception.annotation.ResourceMustBeFound.NotFoundCondition;

public interface ResourceNotFoundRepository {

    @ResourceMustBeFound("object")
    Object object(Object target);

    @ResourceMustBeFound("optional")
    Optional<Object> optional(Optional<Object> target);

    @ResourceMustBeFound("array")
    Object[] array(Object[] target);

    @ResourceMustBeFound("iterable")
    Iterable<Object> iterable(Iterable<Object> target);

    @ResourceMustBeFound("collection")
    Collection<Object> collection(Collection<Object> target);

    @ResourceMustBeFound("map")
    Map<Object, Object> map(Map<Object, Object> target);

    @ResourceMustBeFound("stream")
    Stream<Object> stream(Stream<Object> target);

    @ResourceMustBeFound(code = "array2", notFoundIs = NotFoundCondition.NULL)
    Object[] array2(Object[] target);

    @ResourceMustBeFound(code = "iterable2", notFoundIs = NotFoundCondition.NULL)
    Iterable<Object> iterable2(Iterable<Object> target);

    @ResourceMustBeFound(code = "collection2", notFoundIs = NotFoundCondition.NULL)
    Collection<Object> collection2(Collection<Object> target);

    @ResourceMustBeFound(code = "map2", notFoundIs = NotFoundCondition.NULL)
    Map<Object, Object> map2(Map<Object, Object> target);

    @ResourceMustBeFound(code = "stream2", notFoundIs = NotFoundCondition.NULL)
    Stream<Object> stream2(Stream<Object> target);

    @ResourceMustBeFound("noargs")
    String noargs();

    @ResourceMustBeFound("args")
    String args(String a, String b, String c);

    @ResourceMustBeFound(text = "text")
    String text();

    @ResourceMustBeFound
    String nomessage();

    @ResourceMustNotNull(code = "extend")
    String extend();
}
