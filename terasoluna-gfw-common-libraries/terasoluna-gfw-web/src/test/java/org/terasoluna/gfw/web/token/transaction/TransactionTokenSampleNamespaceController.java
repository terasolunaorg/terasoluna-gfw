/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/token_namespace/*")
@TransactionTokenCheck(namespace = "testTokenAttrByNameSpace")
public class TransactionTokenSampleNamespaceController {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionTokenSampleNamespaceController.class);

    @RequestMapping(value = "/first")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public void first() {
        logger.debug("first");
    }

}
