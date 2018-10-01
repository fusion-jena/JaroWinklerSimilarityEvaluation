package de.uni_jena.cs.fusion.similarity.jarowinkler;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

public class TrieJaroWinklerTest implements JaroWinklerSimilarityMatcherTest {
	
	@Test
	public void match() throws IOException {
		matchTest();
	}

	@Test
	public void matchBaseline() throws IOException {
		matchBaselineTest();
	}

	@Override
	public TrieJaroWinkler<String> getMatcher(Collection<String> terms) {
		return TrieJaroWinkler.of(terms);
	}
}