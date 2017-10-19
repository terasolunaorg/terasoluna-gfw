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
package org.terasoluna.gfw.common.codepoints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.terasoluna.gfw.common.codepoints.CodePoints;
import org.terasoluna.gfw.common.codepoints.ConsistOf;

/**
 * Validator implementation corresponding to {@link ConsistOf} annotation. This validator checks whether all code points in the
 * given string are included in any {@link CodePoints} class specified by {@link ConsistOf#value()}.
 * @since 5.1.0
 */
public class ConsistOfValidator implements
                                ConstraintValidator<ConsistOf, CharSequence> {
    /**
     * Array of CodePoints to check
     */
    private CodePoints[] codePointsArray;

    /**
     * initialize to validate with {@link ConsistOf}
     * @param consistOf {@link ConsistOf} annotation
     */
    @Override
    public void initialize(ConsistOf consistOf) {
        Class<? extends CodePoints>[] classes = consistOf.value();
        this.codePointsArray = new CodePoints[classes.length];
        for (int i = 0; i < classes.length; i++) {
            this.codePointsArray[i] = CodePoints.of(classes[i]);
        }
    }

    /**
     * Validate whether all code points in the given string are included in any {@link CodePoints} class specified by
     * {@link ConsistOf#value()}
     * @param value the string to check
     * @param context validation context
     * @return {@code true} if all code points in the given string are included in any {@link CodePoints} class specified by
     *         {@link ConsistOf#value()} or the given string is {@code null}. {@code false} otherwise.
     */
    @Override
    public boolean isValid(CharSequence value,
            ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String s = value.toString();
        return CodePoints.containsAllInAnyCodePoints(s, codePointsArray);
    }
}
