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
package org.terasoluna.gfw.common.date;

import org.terasoluna.gfw.common.date.jodatime.DefaultJodaTimeDateFactory;

/**
 * <strong>This class exists for backward compatibility with 1.0.x.</strong><br>
 * <br>
 * Default implementation of {@link DateFactory}<br>
 * this class returns current system date as it is.
 * @deprecated please use instead of this class. {@link org.terasoluna.gfw.common.date.jodatime.DefaultJodaTimeDateFactory}
 */
@Deprecated
public class DefaultDateFactory extends DefaultJodaTimeDateFactory implements
                                DateFactory {
}
