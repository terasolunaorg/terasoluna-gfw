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

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TestFacade {

    @Autowired
    protected TestService service;

    private final ThreadLocal<Long> currentThreadSleepSec = new ThreadLocal<Long>() {
        protected Long initialValue() {
            return Long.valueOf(0);
        };
    };

    public String getMessage() {
        long sleepSec = currentThreadSleepSec.get().longValue();
        try {
            TimeUnit.SECONDS.sleep(sleepSec);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        return service.getMessage();
    }

    public void setSleepTime(Long sleepSec) {
        this.currentThreadSleepSec.set(sleepSec);
    }

}
