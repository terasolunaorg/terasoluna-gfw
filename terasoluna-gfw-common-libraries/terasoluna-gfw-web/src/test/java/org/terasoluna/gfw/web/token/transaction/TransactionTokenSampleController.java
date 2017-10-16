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
package org.terasoluna.gfw.web.token.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/token/*")
@TransactionTokenCheck("testTokenAttr")
public class TransactionTokenSampleController {

    private static final Logger logger = LoggerFactory.getLogger(
            TransactionTokenSampleController.class);

    @ModelAttribute("transactionTokenSampleForm")
    public SampleForm createForm() {
        logger.debug("createForm");
        return new SampleForm();
    }

    @RequestMapping(value = "/first")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public void first(SampleForm form, Model model) {
        logger.debug("createForm");
    }

    @RequestMapping(value = "/second")
    @TransactionTokenCheck
    public void second(SampleForm form, Model model,
            TransactionTokenContext ttcontext) {
        logger.debug("second");
        logger.debug("ttcontext:" + ttcontext);
    }

    @RequestMapping(value = "/third")
    @TransactionTokenCheck(type = TransactionTokenType.END)
    public void third(SampleForm form, Model model) {
        logger.debug("third");
    }

    @RequestMapping(value = "/fourth")
    public void fourth(SampleForm form, Model model) {
        logger.debug("third");
    }

    @RequestMapping(value = "/fifth")
    @TransactionTokenCheck(type = TransactionTokenType.CHECK)
    public void fifth(SampleForm form, Model model) {
        logger.debug("fifth");
    }
}
