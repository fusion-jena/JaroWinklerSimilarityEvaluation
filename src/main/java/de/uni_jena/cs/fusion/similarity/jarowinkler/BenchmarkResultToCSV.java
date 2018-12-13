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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BenchmarkResultToCSV {

	private final static String ALMOST_EQUAL_TO = "≈";

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		int execution = 0;
		if (args.length > 2) {
			execution = Integer.parseInt(args[2]);
		}
		Pattern parameterPattern = Pattern.compile("([^=(, ]+) = ([^,)]+)");
		Pattern measurePattern = Pattern.compile(" (\\d+): ([^ ]+) ");
		String parameterValues = null;
		boolean parameterValuesUsed = false;
		boolean headerNeeded = !outputFile.exists() || outputFile.length() == 0L;
		String separator = ",";

		try (BufferedReader in = new BufferedReader(new FileReader(inputFile))) {
			try (Writer out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outputFile, true), "utf-8"))) {
				String line;
				while ((line = in.readLine()) != null) {
					if (line.startsWith("# Parameters:")) {
						Matcher parameterMatcher = parameterPattern.matcher(line);
						if (Objects.nonNull(parameterValues) && !parameterValuesUsed) {
							out.write(parameterValues + separator + execution + separator + separator + "FAILURE" + "\n");
						}
						if (headerNeeded) {
							LinkedList<String> parameterNameList = new LinkedList<>();
							while (parameterMatcher.find()) {
								parameterNameList.add(parameterMatcher.group(1));
							}
							out.write(String.join(separator, parameterNameList) + separator + "execution" + separator
									+ "iteration" + separator + "measure\n");
							parameterMatcher.reset();
							headerNeeded = false;
						}
						LinkedList<String> parameterValueList = new LinkedList<>();
						while (parameterMatcher.find()) {
							parameterValueList.add(parameterMatcher.group(2));
						}
						parameterValuesUsed = false;
						parameterValues = String.join(separator, parameterValueList);
					} else if (line.startsWith("Iteration")) {
						Matcher measureMatcher = measurePattern.matcher(line);
						if (measureMatcher.find()) {
							String iteration = measureMatcher.group(1);
							String measure = measureMatcher.group(2);
							if (measure.contains("interrupt")) {
								out.write(parameterValues + separator + execution + separator + separator + "INTERRUPT" + "\n");
							} else if (line.contains(ALMOST_EQUAL_TO)) {
								out.write(parameterValues + separator + execution + separator + separator + "LOW_PRECISION" + "\n");
							} else {
								out.write(parameterValues + separator + execution + separator + iteration + separator
										+ measure.replace(',', '.') + "\n");
							}
							parameterValuesUsed = true;
						}
					}
				}
				if (Objects.nonNull(parameterValues) && !parameterValuesUsed) {
					out.write(parameterValues + separator + execution + separator + separator + "FAILURE" + "\n");
				}
			}
		}
	}
}
