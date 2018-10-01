package de.uni_jena.cs.fusion.similarity.jarowinkler;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

public class QuickJaroWinklerTest implements JaroWinklerSimilarityMatcherTest {

	@Test
	public void matchAll() throws IOException {
		matchAllTest();
	}

	@Test
	public void matchAllBaseline() throws IOException {
		matchAllBaselineTest();
	}

	@Override
	public QuickJaroWinkler getMatcher(Collection<String> terms) {
		return new QuickJaroWinkler(terms);
	}
}