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
package org.terasoluna.gfw.common.codelist;

import org.springframework.beans.factory.BeanNameAware;

/**
 * Abstract implementation class of {@link CodeList} Functionality <br>
 */
public abstract class AbstractCodeList implements CodeList, BeanNameAware {
    /**
     * Property to hold codelist Id
     */
    private String codeListId;

    /**
     * Setter method for bean name of the {@link CodeList} bean
     * @param beanName name or id of the bean defining {@link CodeList}
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    @Override
    public void setBeanName(String beanName) {
        this.codeListId = beanName;
    }

    /**
     * Getter method for Id of {@link CodeList}
     * @return String Id of the {@link CodeList} defining bean
     * @see org.terasoluna.gfw.common.codelist.CodeList#getCodeListId()
     */
    @Override
    public String getCodeListId() {
        return codeListId;
    }
}
