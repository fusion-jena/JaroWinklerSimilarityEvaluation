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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aksw.limes.metrics.speedup.JaroWinklerLengthFilter;
import org.aksw.limes.metrics.speedup.JaroWinklerMatcher;
import org.aksw.limes.metrics.speedup.JaroWinklerMetric;

import de.uni_jena.cs.fusion.similarity.jarowinkler.JaroWinklerSimilarityMatcher;

public class QuickJaroWinkler implements JaroWinklerSimilarityMatcher<String> {
	
	private List<String> terms;
	private int cores = 1;
	
	QuickJaroWinkler(Collection<String> c) {
		terms = new ArrayList<String>(c);
	}

	@Override
	public Map<String, Double> match(double threshold, String string) {
		// NOTE: do not use singletonList 
		List<String> queries = new ArrayList<String>(1);
		queries.add(string);
		JaroWinklerMetric jw = new JaroWinklerMetric(false, false, false);
		jw.addFilter(new JaroWinklerLengthFilter(threshold));
		return new JaroWinklerMatcher(this.terms, queries, jw, threshold, cores).match().get(string);
	}
	
	@Override
	public Map<String, Map<String, Double>> match(double threshold, Collection<String> strings) {
		List<String> queries = new ArrayList<String>(strings);
		JaroWinklerMetric jw = new JaroWinklerMetric(false, false, false);
		jw.addFilter(new JaroWinklerLengthFilter(threshold));
		return new JaroWinklerMatcher(this.terms, queries, jw, threshold, cores).match();
	}
}
