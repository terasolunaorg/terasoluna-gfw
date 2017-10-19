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
package org.terasoluna.gfw.common.exception.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service
public class TestService {

    @Autowired
    protected TestRepository repository;

    private final ThreadLocal<ResultMessagesNotificationException> currentThreadException = new ThreadLocal<ResultMessagesNotificationException>();

    public String getMessage() {
        ResultMessagesNotificationException exception = currentThreadException
                .get();
        if (exception != null) {
            throw exception;
        }
        return repository.getMessage();
    }

    public void setResultMessagesNotificationException(
            ResultMessagesNotificationException exception) {
        currentThreadException.set(exception);
    }
}
