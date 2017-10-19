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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * A custom annotation that provides a functionality to perform a token check for preventing consecutive form submissions.
 * <p>
 * This token is called Transaction Token since it provides the functionality to define a transaction over calls to multiple
 * handlerMethods in a controller within a single session. All the requests to these handlerMethods will be checked for a valid
 * token before the method is executed.
 * <p>
 * In order to specify the start and end of token check transaction, {@code type= TransactionTokenType.BEGIN}) and
 * {@code type= TransactionTokenType.END}) can be used respectively. If no {@code type} is specified, by default the value of
 * {@code type} attribute is set to indicate that, calls to corresponding handler method are <i>inside</i> a token check
 * transaction. (by default, {@code type= TransactionTokenType.IN})
 * <p>
 * The structure of transaction token is {@code tokenName~tokenKey~tokenValue}. By default, the delimiter used to separate the
 * name, key and value components of the token is {@code ~ (tilde)}.
 * <p>
 * {@code TransactionTokenCheck} can be applied at class level as well as method level. class level annotation defines a common
 * namespace at controller level. At method level, {@code value} attribute of @TransactionTokenCheck annotation can be used to
 * give a name to the transaction. This along with the namespace at class level for the {@code tokenName} part of the token
 * string. Giving same values as @RequestMapping tag at controller level and method level is recommended<br>
 * <p>
 * By default, number of retries to create a key for an {@code tokenName} is 10. Once all the tries are completed, it follows
 * FIFO algorithm to replace old keys will new ones. This default value can be changed by through settings in applicationContext
 * file. Please refer to guideline for details regarding the configuration and usage method.
 * <p>
 * By default, if no namespace is defined and {@code value} attribute at method level is also not specified, "globalToken" is
 * used as {@code tokenName} for all the transaction tokens generated.
 */
@Documented
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionTokenCheck {

    /**
     * @return The value of generated {@code TransactionToken}<br>
     */
    @AliasFor("namespace")
    String value() default "";

    /**
     * @return The namespace of generated {@code TransactionToken}<br>
     */
    @AliasFor("value")
    String namespace() default "";

    /**
     * @return Type of the {@code TransactionToken}. Default value is {@code TransactionTokenType.IN}
     */
    TransactionTokenType type() default TransactionTokenType.IN;
}
