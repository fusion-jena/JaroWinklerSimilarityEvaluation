package de.uni_jena.cs.fusion.similarity.jarowinkler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface JaroWinklerSimilarityMatcher<T> {

	public final static int COMMON_PREFIX_LENGTH_LIMIT = 4;
	public final static double BOOST_THRESHOLD = 0.7;
	public final static double BOOST_FACTOR = 0.1;

	/**
	 * 
	 * @param threshold
	 *            minimum similarity value to pass matching
	 * @param query
	 *            {@link String} to match
	 * @return {@link Map} of passed entities and the corresponding similarity
	 */
	public Map<T, Double> match(double threshold, String query);

	/**
	 * 
	 * @param threshold
	 *            minimum similarity value to pass matching
	 * @param queries
	 *            {@link Collection} of {@link String}s to match
	 * @return {@link Map} containing the matched {@link String}s and a
	 *         {@link Map} of passed entities and the corresponding similarity
	 */
	default Map<String, Map<T, Double>> match(double threshold, Collection<String> queries) {
		Map<String, Map<T, Double>> results = new HashMap<>();
		for (String query : queries) {
			if (Thread.interrupted()) break;
			results.put(query, match(threshold, query));
		}
		return results;
	}

	default Map<String, Map<T, Double>> mergeResults(Map<String, Map<T, Double>> left,
			Map<String, Map<T, Double>> right) {
		if (left == null || left.isEmpty()) {
			return right;
		} else if (right == null || right.isEmpty()) {
			return left;
		} else {
			for (String key : right.keySet()) {
				if (left.containsKey(key)) {
					left.get(key).putAll(right.get(key));
				} else {
					left.put(key, right.get(key));
				}
			}
			return left;
		}
	}
}
