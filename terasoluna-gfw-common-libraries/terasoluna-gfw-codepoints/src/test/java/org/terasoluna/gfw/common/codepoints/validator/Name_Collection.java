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
package org.terasoluna.gfw.common.codepoints.validator;

import java.util.List;

import org.terasoluna.gfw.common.codepoints.ConsistOf;

public class Name_Collection {
    private List<@ConsistOf(AtoF.class) String> firstNames;

    private List<@ConsistOf(GtoL.class) String> lastNames;

    public Name_Collection(List<String> firstNames, List<String> lastNames) {
        this.firstNames = firstNames;
        this.lastNames = lastNames;
    }

    public Name_Collection() {
    }

    public List<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(List<String> firstNames) {
        this.firstNames = firstNames;
    }

    public List<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(List<String> lastNames) {
        this.lastNames = lastNames;
    }
}
