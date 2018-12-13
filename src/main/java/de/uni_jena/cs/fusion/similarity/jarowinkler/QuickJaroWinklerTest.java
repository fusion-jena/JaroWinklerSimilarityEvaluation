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