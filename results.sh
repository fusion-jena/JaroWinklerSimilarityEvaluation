javac -d results src/main/java/de/uni_jena/cs/fusion/similarity/jarowinkler/BenchmarkResultToCSV.java
cd results
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run1/FULL_FALSE.log benchmark.csv 1
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run1/FULL_TRUE.log benchmark.csv 1
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run1/HALF_FALSE.log benchmark.csv 1
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run1/HALF_TRUE.log benchmark.csv 1
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run1/NONE_FALSE.log benchmark.csv 1
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run1/NONE_TRUE.log benchmark.csv 1

java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run2/FULL_FALSE.log benchmark.csv 2
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run2/FULL_TRUE.log benchmark.csv 2
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run2/HALF_FALSE.log benchmark.csv 2
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run2/HALF_TRUE.log benchmark.csv 2
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run2/NONE_FALSE.log benchmark.csv 2
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run2/NONE_TRUE.log benchmark.csv 2

java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run3/FULL_FALSE.log benchmark.csv 3
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run3/FULL_TRUE.log benchmark.csv 3
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run3/HALF_FALSE.log benchmark.csv 3
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run3/HALF_TRUE.log benchmark.csv 3
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run3/NONE_FALSE.log benchmark.csv 3
java de.uni_jena.cs.fusion.similarity.jarowinkler.BenchmarkResultToCSV run3/NONE_TRUE.log benchmark.csv 3
cd ..


