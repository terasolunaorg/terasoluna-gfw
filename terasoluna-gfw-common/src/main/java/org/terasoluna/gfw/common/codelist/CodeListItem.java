/*
 * Copyright (C) 2013 terasoluna.org
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
package org.terasoluna.gfw.common.codelist;

/**
 * interface of item in codelist
 *
 * @since 1.1.0
 */
public interface CodeListItem {
    /**
     * Returns the label of item
     *
     * @return label of the item
     */
    String getCodeLabel();

    /**
     * Returns the value of item
     *
     * @return value of the item
     */
    String getCodeValue();
}
