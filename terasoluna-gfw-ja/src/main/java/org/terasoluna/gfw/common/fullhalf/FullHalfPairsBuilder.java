/*
 * Copyright (C) 2013-2015 terasoluna.org
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
package org.terasoluna.gfw.common.fullhalf;

import java.util.LinkedHashSet;
import java.util.Set;

public class FullHalfPairsBuilder {
	private final Set<FullHalfPair> pairs = new LinkedHashSet<FullHalfPair>();
	private FullHalfPairs.AppendablePredicate predicate;

	public FullHalfPairsBuilder pair(String fullwidth, String halfwidth) {
		this.pairs.add(new FullHalfPair(fullwidth, halfwidth));
		return this;
	}

	public FullHalfPairsBuilder appendablePredicate(
			FullHalfPairs.AppendablePredicate predicate) {
		this.predicate = predicate;
		return this;
	}

	public FullHalfPairs build() {
		return new FullHalfPairs(this.pairs, this.predicate);
	}
}
