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
package org.terasoluna.gfw.common.codelist.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.codelist.CodeList;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

/**
 * Abstract validation implementation class for {@link ExistInCodeList} custom annotation.
 * <p>
 * Validates whether the value of field for which the custom annotation is used, is a valid code existing in the
 * {@link CodeList} specified as a parameter to the {@link ExistInCodeList} annotation.
 * </p>
 */
public abstract class AbstractExistInCodeListValidator<T> extends
                                                      ApplicationObjectSupport
                                                      implements
                                                      ConstraintValidator<ExistInCodeList, T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * codelist
     */
    private CodeList codeList = null;

    /**
     * Initialize.
     * <p>
     * Get the codelist from applicationContext.
     * </p>
     */
    @Override
    public void initialize(ExistInCodeList codeConstraint) {
        this.codeList = getApplicationContext().getBean(codeConstraint
                .codeListId(), CodeList.class);
    }

    /**
     * Get string code from input value
     * @param value input value
     * @return code code in the String format
     */
    protected abstract String getCode(T value);

    /**
     * Validate.
     * @param value target value.
     * @param constraintContext constraint context.
     * @return if valid value, return true.
     */
    @Override
    public boolean isValid(T value,
            ConstraintValidatorContext constraintContext) {
        String code = getCode(value);

        if (!StringUtils.hasLength(code)) {
            return true;
        }
        if (logger.isTraceEnabled()) {
            logger.trace("check if {} exists in {}", code, codeList
                    .getCodeListId());
        }
        return codeList.asMap().containsKey(code);
    }

}
