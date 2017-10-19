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

/**
 * Concrete validation implementation class for {@link org.terasoluna.gfw.common.codelist.ExistInCodeList} custom annotation.
 * <p>
 * Used if the value of the field for which the custom annotation is used, is of type {@link CharSequence} ({@link String},
 * {@link StringBuilder}, etc ...) <br>
 * <br>
 * Validates whether the value of field is a valid code existing in the {@link org.terasoluna.gfw.common.codelist.CodeList}
 * specified <br>
 * as a parameter to the {@link org.terasoluna.gfw.common.codelist.ExistInCodeList} annotation.<br>
 * </p>
 * <p>
 * Note: This class has been renamed from {@code ExistInCodeListValidatorForString} at 5.1.0 to support the {@link CharSequence}
 * </p>
 */

public class ExistInCodeListValidatorForCharSequence extends
                                                     AbstractExistInCodeListValidator<CharSequence> {

    /**
     * Fetches the code value which is the target of validation
     * @see org.terasoluna.gfw.common.codelist.validator.AbstractExistInCodeListValidator#getCode(Object)
     */
    @Override
    protected String getCode(CharSequence value) {
        return value == null ? null : value.toString();
    }
}
