package de.uni_jena.cs.fusion.similarity.jarowinkler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import de.uni_jena.cs.fusion.similarity.jarowinkler.JaroWinklerSimilarityTestData.Coverage;

@BenchmarkMode(Mode.Throughput)
@Fork(value = 1)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 120, timeUnit = TimeUnit.SECONDS)
@Threads(value = 1)
@State(Scope.Thread)
public class JaroWinklerSimilarityMatcherBenchmark {

	public static enum Matcher {
		NAIVE, TRIE, QUICK
	};

	@Param({ "0.99", "0.95", "0.91" })
	public double p3_threshold;

	@Param({ "1", "10", "100", "1000", "10000", "100000", "1000000" })
	public int p2_termSize;

	@Param({ "1", "10", "100", "1000", "10000", "100000", "1000000" })
	public int p1_querySize;

	@Param({ "FULL", "HALF", "NONE" })
	public Coverage p5_coverage;

	@Param({ "TRIE", "QUICK", "NAIVE" })
	public Matcher p6_matcher;

	@Param({ "true", "false" })
	public boolean p4_prepared;

	public List<String> terms;
	public List<String> queries;
	public JaroWinklerSimilarityMatcher<String> preparedMatcher;
	private int iteration = 0;
	private final static JaroWinklerSimilarityTestData data = new JaroWinklerSimilarityTestData(30);

	public JaroWinklerSimilarityMatcher<String> prepareMatcher(Matcher matcher, List<String> terms) {
		switch (matcher) {
		case NAIVE:
			return new NaiveJaroWinkler(terms);
		case QUICK:
			return new QuickJaroWinkler(terms);
		case TRIE:
			return TrieJaroWinkler.of(terms);
		default:
			throw new InvalidParameterException();
		}
	}

	public JaroWinklerSimilarityMatcher<String> getMatcher(Matcher matcher, List<String> terms) {
		if (this.p4_prepared) {
			return this.preparedMatcher;
		} else {
			return prepareMatcher(matcher, terms);
		}
	}

	@Setup(Level.Iteration)
	public void setup() throws IOException {
		this.terms = data.getTerms(this.p2_termSize, this.iteration);
		this.queries = data.getQueries(this.p1_querySize, this.p2_termSize, this.iteration, p5_coverage);
		if (this.p4_prepared) {
			this.preparedMatcher = prepareMatcher(this.p6_matcher, this.terms);
		}
		this.iteration++;
	}

	@Benchmark
	public void match(Blackhole blackhole) {
		if (this.p6_matcher == Matcher.NAIVE && (this.p4_prepared || this.p3_threshold != 0.99)) {
			throw new RuntimeException(
					"Parameters preparation and threshold do not affect naive matcher. Preventing duplicated execution by throwing exception.");
		}
		JaroWinklerSimilarityMatcher<String> matcher = getMatcher(this.p6_matcher, this.terms);
		blackhole.consume(matcher.match(this.p3_threshold, this.queries));
	}
}