package de.uni_jena.cs.fusion.similarity.jarowinkler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.similarity.JaroWinklerDistance;

/**
 * Uses modified methods from org.apache.commons.lang3
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
