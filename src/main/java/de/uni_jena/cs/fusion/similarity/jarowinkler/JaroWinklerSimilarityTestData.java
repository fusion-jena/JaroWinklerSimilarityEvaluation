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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JaroWinklerSimilarityTestData {

	private List<List<String>> terms = new ArrayList<List<String>>();

	public static enum Coverage {
		FULL, HALF, NONE
	};

	public JaroWinklerSimilarityTestData(int iterations) {
		List<String> rawTerms = new ArrayList<String>();

		// read terms
		InputStream inputStream = JaroWinklerSimilarityTestData.class.getClassLoader().getResourceAsStream("names.txt");
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"))) {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				rawTerms.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < iterations; i++) {
			// copy terms
			List<String> copiedTerms = new ArrayList<String>(rawTerms);
			// shuffle terms
			Collections.shuffle(copiedTerms, new Random(i));
			// store shuffled terms
			terms.add(copiedTerms);
		}
	}

	public List<String> getTerms(int termSize, int iteration) {
		return new ArrayList<String>(terms.get(iteration).subList(0, termSize));
	}

	public List<String> getQueries(int querySize, int termSize, int iteration, Coverage coverage) {
		int offset;
		switch (coverage) {
		case FULL:
			offset = 0;
			break;
		case HALF:
			offset = Math.max(termSize - querySize / 2, 0);
			break;
		case NONE:
			offset = termSize;
			break;
		default:
			throw new IllegalArgumentException("Unexpected coverage value.");
		}
		return new ArrayList<String>(terms.get(iteration).subList(offset, offset + querySize));
	}

}
