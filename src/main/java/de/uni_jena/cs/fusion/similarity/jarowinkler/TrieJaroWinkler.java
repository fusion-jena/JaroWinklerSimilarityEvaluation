package de.uni_jena.cs.fusion.similarity.jarowinkler;

/*-
 * #%L
 * Jaro-Winkler Similarity Evaluation
 * %%
 * Copyright (C) 2018 Heinz Nixdorf Chair for Distributed Information Systems, Friedrich Schiller University Jena
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collection;
import java.util.Map;

public class TrieJaroWinkler<T> implements JaroWinklerSimilarityMatcher<T> {

	private final JaroWinklerSimilarity<T> jws;

	private TrieJaroWinkler(JaroWinklerSimilarity<T> jws) {
		this.jws = jws;
	}

	public static <T> TrieJaroWinkler<T> of(Map<String, T> terms) {
		return new TrieJaroWinkler<T>(JaroWinklerSimilarity.of(terms));
	}

	public static TrieJaroWinkler<String> of(Collection<String> terms) {
		return new TrieJaroWinkler<String>(JaroWinklerSimilarity.of(terms));
	}

	public Map<T, Double> match(double threshold, String queryString) {
		return this.jws.match(queryString, threshold);
	}

	/**
	 * Returns the Jaro-Winkler Similarity of two given {@link String}s.
	 * 
	 * @param first
	 *            first {@link String} to match
	 * @param second
	 *            second {@link String} to match
	 * @return similarity of {@code first} and {@code second}
	 */
	public static double match(String first, String second) {
		return JaroWinklerSimilarity.calculate(first, second);
	}

}
