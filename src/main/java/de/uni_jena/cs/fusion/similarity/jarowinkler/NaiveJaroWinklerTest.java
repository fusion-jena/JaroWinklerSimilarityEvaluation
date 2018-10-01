package de.uni_jena.cs.fusion.similarity.jarowinkler;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

public class NaiveJaroWinklerTest implements JaroWinklerSimilarityMatcherTest {

	@Test
	public void matchAll() throws IOException {
		matchAllTest();
	}

	@Override
	public JaroWinklerSimilarityMatcher<String> getMatcher(Collection<String> terms) {
		return new NaiveJaroWinkler(terms);
	}
}
