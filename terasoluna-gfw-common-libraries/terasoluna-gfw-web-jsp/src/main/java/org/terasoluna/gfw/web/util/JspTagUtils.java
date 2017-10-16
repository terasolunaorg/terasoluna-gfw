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
package org.terasoluna.gfw.web.util;

import javax.servlet.jsp.JspTagException;

import org.springframework.util.StringUtils;

/**
 * Utility class about jsp tag.
 * <p>
 * WARNING: <br>
 * This class is not common library, because was created for eliminate duplicated code of common library(JSP Tag class).<br>
 * Therefore, There is a possibility that the modification(delete class or method, change method signature) performed without
 * announcement.<br>
 * </p>
 * @since 1.0.1
 */
public final class JspTagUtils {

    /**
     * Default Constructor.
     * <p>
     * do nothing.
     * </p>
     */
    private JspTagUtils() {
        // do nothing.
    }

    /**
     * Convert to the Boolean value.
     * @param attributeValue string value of attribute
     * @param defaultValue If attribute value is not text(null or blank or whitespace only), apply this value.
     * @param attributeName attribute name (If attribute value is not true or false, sets in exception message)
     * @return converted Boolean value
     * @throws JspTagException If value that is not true or false is specified.
     */
    public static boolean toBoolean(String attributeValue, boolean defaultValue,
            String attributeName) throws JspTagException {
        if (StringUtils.hasText(attributeValue)) {
            if ("true".equalsIgnoreCase(attributeValue) || "false"
                    .equalsIgnoreCase(attributeValue)) {
                return Boolean.parseBoolean(attributeValue);
            } else {
                throw new JspTagException("The value of " + attributeName
                        + " must be either true or false.");
            }
        }
        return defaultValue;
    }

}
