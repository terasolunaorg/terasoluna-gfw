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

import org.joda.time.DateTime;
import org.terasoluna.gfw.common.date.jodatime.JdbcFixedJodaTimeDateFactory;

/**
 * <strong>This class exists for backward compatibility with 1.0.x.</strong><br>
 * <br>
 * Concrete Implementation class of {@link DateFactory}.
 * <P>
 * The {@link DateTime} value which is to be returned as current system date is stored in database. <br>
 * </P>
 * @deprecated please use instead of this class. {@link org.terasoluna.gfw.common.date.jodatime.JdbcFixedJodaTimeDateFactory}
 */
@Deprecated
public class JdbcFixedDateFactory extends JdbcFixedJodaTimeDateFactory
                                  implements DateFactory {
}
