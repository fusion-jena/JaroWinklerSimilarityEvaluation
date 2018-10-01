package de.uni_jena.cs.fusion.similarity.jarowinkler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_jena.cs.fusion.similarity.jarowinkler.JaroWinklerSimilarityMatcher;

public interface JaroWinklerSimilarityMatcherTest {

	JaroWinklerSimilarityMatcher<String> getMatcher(Collection<String> terms);

	default JaroWinklerSimilarityMatcher<String> getBaselineMatcher(Collection<String> terms) {
		return new NaiveJaroWinkler(terms);
	}

	static List<Map<String, Map<String, Double>>> testData() {
		List<Map<String, Map<String, Double>>> testData = new ArrayList<Map<String, Map<String, Double>>>();
		Map<String, Map<String, Double>> testCase;
		Map<String, Double> testElement;

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("aaabcd", new HashMap<String, Double>());
		testElement = testCase.get("aaabcd");
		testElement.put("aaacdb", 0.9416666666666667);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("ABC Corporation", new HashMap<String, Double>());
		testElement = testCase.get("ABC Corporation");
		testElement.put("ABC Corp", 0.90666d);
		testCase.put("D N H Enterprises Inc", new HashMap<String, Double>());
		testElement = testCase.get("D N H Enterprises Inc");
		testElement.put("D & H Enterprises, Inc.", 0.95251d);
		testCase.put("My Gym Children's Fitness Center", new HashMap<String, Double>());
		testElement = testCase.get("My Gym Children's Fitness Center");
		testElement.put("My Gym. Childrens Fitness", 0.942);
		testCase.put("PENNSYLVANIA", new HashMap<String, Double>());
		testElement = testCase.get("PENNSYLVANIA");
		testElement.put("PENNCISYLVNIA", 0.898018d);
		testCase.put("/opt/software1", new HashMap<String, Double>());
		testElement = testCase.get("/opt/software1");
		testElement.put("/opt/software2", 0.971428d);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("aaaaaa", new HashMap<String, Double>());
		testElement = testCase.get("aaaaaa");
		testElement.put("aaaaaa", 1.00);
		testElement.put("aaaaba", 0.93);
		testCase.put("aaaaba", new HashMap<String, Double>());
		testElement = testCase.get("aaaaba");
		testElement.put("aaaaba", 1.00);
		testElement.put("aaaaaa", 0.93);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("Ronald Alexander", new HashMap<String, Double>());
		testElement = testCase.get("Ronald Alexander");
		testElement.put("Roland Alexander", 0.983);
		testElement.put("Ronald Alexander", 1.00);
		testCase.put("Roland Alexander", new HashMap<String, Double>());
		testElement = testCase.get("Roland Alexander");
		testElement.put("Ronald Alexander", 0.983);
		testElement.put("Roland Alexander", 1.00);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("a", new HashMap<String, Double>());
		testElement = testCase.get("a");
		testElement.put("a", 1.00);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("ab", new HashMap<String, Double>());
		testElement = testCase.get("ab");
		testElement.put("ab", 1.00);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("Duke of Brunswick-Harburg Otto II", new HashMap<String, Double>());
		testElement = testCase.get("Duke of Brunswick-Harburg Otto II");
		testElement.put("Duke of Brunswick-Harburg Otto II", 1.00);
		testElement.put("Duke of Brunswick-Harburg Otto III", 0.99);
		testCase.put("Duke of Brunswick-Harburg Otto III", new HashMap<String, Double>());
		testElement = testCase.get("Duke of Brunswick-Harburg Otto III");
		testElement.put("Duke of Brunswick-Harburg Otto III", 1.00);
		testElement.put("Duke of Brunswick-Harburg Otto II", 0.99);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("Andre Lichtenberger", new HashMap<String, Double>());
		testElement = testCase.get("Andre Lichtenberger");
		testElement.put("Andre Lichtenberger", 1.00);
		testElement.put("Andrew Lichtenberger", 0.99);
		testCase.put("Andrew Lichtenberger", new HashMap<String, Double>());
		testElement = testCase.get("Andrew Lichtenberger");
		testElement.put("Andrew Lichtenberger", 1.00);
		testElement.put("Andre Lichtenberger", 0.99);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("Ida Bauer", new HashMap<String, Double>());
		testElement = testCase.get("Ida Bauer");
		testElement.put("Ida Bauer", 1.00);
		testCase.put("Ihor Korotetskyi", new HashMap<String, Double>());
		testElement = testCase.get("Ihor Korotetskyi");
		testElement.put("Ihor Korotetskyi", 1.00);
		testCase.put("Li Du", new HashMap<String, Double>());
		testElement = testCase.get("Li Du");
		testElement.put("Li Du", 1.00);
		testCase.put("Liping Ji", new HashMap<String, Double>());
		testElement = testCase.get("Liping Ji");
		testElement.put("Liping Ji", 1.00);

		///////////////////////////////
		testData.add(new HashMap<String, Map<String, Double>>());
		testCase = testData.get(testData.size() - 1);
		testCase.put("Colleen D'Agostino", new HashMap<String, Double>());
		testElement = testCase.get("Colleen D'Agostino");
		testElement.put("Raymond Bark-Jones", 0.50);

		return testData;
	}

	default void matchTest() throws IOException {
		List<Map<String, Map<String, Double>>> testData = testData();
		JaroWinklerSimilarityMatcher<String> matcher;
		Map<String, Double> match;

		for (Map<String, Map<String, Double>> testCase : testData) {
			Set<String> terms = new HashSet<String>();
			for (String key : testCase.keySet()) {
				terms.addAll(testCase.get(key).keySet());
			}
			matcher = getMatcher(terms);
			for (String key : testCase.keySet()) {
				match = matcher.match(0.5, key);
				for (String key2 : testCase.get(key).keySet()) {
					assertTrue("missing \"" + key + "\", \"" + key2 + "\"", match.containsKey(key2));
					assertEquals("wrong value \"" + key + "\", \"" + key2 + "\"", testCase.get(key).get(key2),
							match.get(key2), 0.01);
				}
			}
		}
	}

	default void matchBaselineTest() throws IOException {

		// equalMatchResult(0.99, 1);
		// equalMatchResult(0.95, 1);
		// equalMatchResult(0.91, 1);
		equalMatchResult(0.50, 1);

		// equalMatchResult(0.99, 10);
		// equalMatchResult(0.95, 10);
		// equalMatchResult(0.91, 10);
		equalMatchResult(0.50, 10);

		// equalMatchResult(0.99, 100);
		// equalMatchResult(0.95, 100);
		// equalMatchResult(0.91, 100);
		equalMatchResult(0.50, 100);

		// equalMatchResult(0.99, 1000);
		// equalMatchResult(0.95, 1000);
		// equalMatchResult(0.91, 1000);
		equalMatchResult(0.50, 1000);

		// equalMatchResult(0.99, 10000);
		// equalMatchResult(0.95, 10000);
		equalMatchResult(0.91, 10000);
		// equalMatchResult(0.50, 10000);

		// equalMatchResult(0.99, 100000);
		// equalMatchResult(0.95, 100000);
		equalMatchResult(0.91, 100000);
		// equalMatchResult(0.50, 100000);

		// equalMatchResult(0.99, 1000000);
		// equalMatchResult(0.95, 1000000);
		equalMatchResult(0.91, 1000000);
		// equalMatchResult(0.50, 1000000);
	}

	default void matchAllTest() throws IOException {
		List<Map<String, Map<String, Double>>> testData = testData();
		JaroWinklerSimilarityMatcher<String> matcher;
		Map<String, Map<String, Double>> match;

		for (Map<String, Map<String, Double>> testCase : testData) {
			Set<String> terms = new HashSet<String>();
			for (String key : testCase.keySet()) {
				terms.addAll(testCase.get(key).keySet());
			}
			matcher = getMatcher(terms);
			match = matcher.match(0.5, testCase.keySet());
			for (String key : testCase.keySet()) {
				assertTrue("missing \"" + key + "\"", match.containsKey(key));
				for (String key2 : testCase.get(key).keySet()) {
					assertTrue("missing \"" + key + "\", \"" + key2 + "\"", match.get(key).containsKey(key2));
					assertEquals("wrong value \"" + key + "\", \"" + key2 + "\"", testCase.get(key).get(key2),
							match.get(key).get(key2), 0.01);
				}
			}
		}
	}

	default void matchAllBaselineTest() throws IOException {

		// equalMatchAllResult(0.99, 1);
		// equalMatchAllResult(0.95, 1);
		// equalMatchAllResult(0.91, 1);
		equalMatchAllResult(0.50, 1);

		// equalMatchAllResult(0.99, 10);
		// equalMatchAllResult(0.95, 10);
		// equalMatchAllResult(0.91, 10);
		equalMatchAllResult(0.50, 10);

		// equalMatchAllResult(0.99, 100);
		// equalMatchAllResult(0.95, 100);
		// equalMatchAllResult(0.91, 100);
		equalMatchAllResult(0.50, 100);

		// equalMatchAllResult(0.99, 1000);
		// equalMatchAllResult(0.95, 1000);
		// equalMatchAllResult(0.91, 1000);
		equalMatchAllResult(0.50, 1000);

		// equalMatchAllResult(0.99, 10000);
		// equalMatchAllResult(0.95, 10000);
		equalMatchAllResult(0.91, 10000);
		// equalMatchAllResult(0.50, 10000);

		// equalMatchAllResult(0.99, 100000);
		// equalMatchAllResult(0.95, 100000);
		equalMatchAllResult(0.91, 100000);
		// equalMatchAllResult(0.50, 100000);

		// equalMatchAllResult(0.99, 1000000);
		// equalMatchAllResult(0.95, 1000000);
		equalMatchAllResult(0.91, 1000000);
		// equalMatchAllResult(0.50, 1000000);
	}

	default void equalMatchResult(double threshold, int testSize) throws IOException {
		System.out.println(threshold + " @ " + testSize);

		// get terms
		List<String> terms = new JaroWinklerSimilarityTestData(1).getTerms(testSize,0);

		// init matchers
		JaroWinklerSimilarityMatcher<String> matcher = getMatcher(terms);
		JaroWinklerSimilarityMatcher<String> baselineMatcher = getBaselineMatcher(terms);

		Map<String, Map<String, Double>> results = new HashMap<>();

		System.out.println("running matcher under test");
		for (String query : terms) {
			// get results of matcher under test
			results.put(query, matcher.match(threshold, query));
		}
		
		System.out.println("compare to baselin matcher results");
		for (String query : terms) {

			// get results of baseline matcher
			Map<String, Double> baseline = baselineMatcher.match(threshold, query);

			// check results
			assertEqualResult(matcher.getClass().toString(), query, baseline, results.get(query));
			assertEqualResult(baselineMatcher.getClass().toString(), query, results.get(query), baseline);
		}

	}

	default void equalMatchAllResult(double threshold, int testSize) throws IOException {
		System.out.println(threshold + " @ " + testSize);

		// get terms
		List<String> terms = new JaroWinklerSimilarityTestData(1).getTerms(testSize,0);

		// init matchers
		JaroWinklerSimilarityMatcher<String> matcher = getMatcher(terms);
		JaroWinklerSimilarityMatcher<String> baselineMatcher = getBaselineMatcher(terms);

		// get results of trieIndexJaroWinkler::match
		System.out.println("running matcher under test");
		Map<String, Map<String, Double>> results = matcher.match(threshold, terms);

		System.out.println("compare to baselin matcher results");
		for (String query : terms) {
			// get results of naive matcher
			Map<String, Double> baselineResults = baselineMatcher.match(threshold, query);

			// check results
			assertEqualResult(matcher.toString(), query, baselineResults, results.get(query));
			assertEqualResult(baselineMatcher.toString(), query, results.get(query), baselineResults);
		}
	}

	static void assertEqualResult(String message, String query, Map<String, Double> expected,
			Map<String, Double> actual) {
		for (String term : expected.keySet()) {
			// assertTrue(message + ": missing \"" + query + "\", \"" + term + "\"",
			// right.get(query).containsKey(term));
			if (!actual.containsKey(term)) {
				System.err.println(message + ": missing \"" + query + "\", \"" + term + "\"");
			} else {
				// assertEquals(message + ": wrong value \"" + query + "\", \"" + term + "\"",
				// left.get(query).get(term), right.get(query).get(term), 0.01);
				double expectedValue = expected.get(term);
				double actualValue = actual.get(term);
				if (Double.compare(expectedValue, actualValue) != 0 && Math.abs(expectedValue - actualValue) > 0.01) {
					System.err.println(message + ": wrong value \"" + query + "\", \"" + term + "\"\n\t" + actualValue
							+ " (actual)\n\t" + expectedValue + " (expected)");
				}
			}
		}
	}

	static void assertEqualResults(String message, Map<String, Map<String, Double>> expected,
			Map<String, Map<String, Double>> actual) {
		for (String query : expected.keySet()) {
			// assertTrue(message + ": missing \"" + query + "\"",
			// right.containsKey(query));
			if (!actual.containsKey(query)) {
				System.err.println(message + ": missing \"" + query + "\"");
			} else {
				assertEqualResult(message, query, expected.get(query), actual.get(query));
			}
		}
	}

}
