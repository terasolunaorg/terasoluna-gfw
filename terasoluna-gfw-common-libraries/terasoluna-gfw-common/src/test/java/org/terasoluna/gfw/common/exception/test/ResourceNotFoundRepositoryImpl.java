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

import org.springframework.stereotype.Repository;

@Repository
public class ResourceNotFoundRepositoryImpl implements
                                            ResourceNotFoundRepository {

    @Override
    public Object object(Object target) {
        return target;
    }

    @Override
    public Optional<Object> optional(Optional<Object> target) {
        return target;
    }

    @Override
    public Object[] array(Object[] target) {
        return target;
    }

    @Override
    public Iterable<Object> iterable(Iterable<Object> target) {
        return target;
    }

    @Override
    public Collection<Object> collection(Collection<Object> target) {
        return target;
    }

    @Override
    public Map<Object, Object> map(Map<Object, Object> target) {
        return target;
    }

    @Override
    public Stream<Object> stream(Stream<Object> target) {
        return target;
    }

    @Override
    public Object[] array2(Object[] target) {
        return target;
    }

    @Override
    public Iterable<Object> iterable2(Iterable<Object> target) {
        return target;
    }

    @Override
    public Collection<Object> collection2(Collection<Object> target) {
        return target;
    }

    @Override
    public Map<Object, Object> map2(Map<Object, Object> target) {
        return target;
    }

    @Override
    public Stream<Object> stream2(Stream<Object> target) {
        return target;
    }

    @Override
    public String noargs() {
        return null;
    }

    @Override
    public String args(String a, String b, String c) {
        return null;
    }

    @Override
    public String text() {
        return null;
    }

    @Override
    public String nomessage() {
        return null;
    }

    @Override
    public String extend() {
        return null;
    }
}
