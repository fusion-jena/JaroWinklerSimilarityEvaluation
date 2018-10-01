package de.uni_jena.cs.fusion.similarity.jarowinkler;

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
