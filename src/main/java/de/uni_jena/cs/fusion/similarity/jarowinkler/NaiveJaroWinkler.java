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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.similarity.JaroWinklerDistance;

/**
 * 
 * @author Jan Martin Keil
 *
 */
public class NaiveJaroWinkler implements JaroWinklerSimilarityMatcher<String> {

	Collection<String> terms;
	private final static JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

	public NaiveJaroWinkler(Collection<String> c) {
		this.terms = c;
	}

	@Override
	public Map<String, Map<String, Double>> match(double threshold, Collection<String> queries) {
		Map<String, Map<String, Double>> results = new HashMap<>();
		for (String query : queries) {
			if (Thread.interrupted()) break;
			results.put(query, this.match(threshold, query));
		}
		return results;
	}

	@Override
	public Map<String, Double> match(double threshold, String query) {
		Map<String, Double> results = new HashMap<>();
		for (String term : this.terms) {
			double similarity = jaroWinklerDistance.apply(query, term);
			if (similarity >= threshold) {
				results.put(term, similarity);
			}
		}
		return results;
	}
}
