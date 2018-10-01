package de.uni_jena.cs.fusion.similarity.jarowinkler;

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
