/*
 * Copyright (C) 2013-2014 terasoluna.org
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
package org.terasoluna.gfw.common.exception;

import org.terasoluna.gfw.common.exception.ExceptionLevel;

/**
 * Provides Functionality to determine the {@link ExceptionLevel} based on the specified {@Link Exception} instance <br>
 */
public interface ExceptionLevelResolver {
    
	/**
	 * Determines the {@link ExceptionLevel} based on the instance of {@Link Exception} <br> 
	 * passed as an argument <br>
	 * @param ex Exception class
	 * @return level of exception
	 */
	ExceptionLevel resolveExceptionLevel(Exception ex);
}
