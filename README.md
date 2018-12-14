[![DOI](https://zenodo.org/badge/149863193.svg)](https://zenodo.org/badge/latestdoi/149863193)

# Efficient Bounded Jaro-Winkler Similarity Based Search - Supplementary Materials

## Content

* [results/benchmark.csv](results/benchmark.csv) - Measurement data
* [paper-evaluation-results-supplementary-tables.pdf](paper-evaluation-results-supplementary-tables.pdf) - Additional comparison tables

## How To Execute

1. [clone.sh](clone.sh) - Get code of other approaches
2. [build.sh](build.sh) - Build the benchmark JAR
3. [benchmark.sh](benchmark.sh) - Execute the benchmark in the cluster
4. [results.sh](results.sh) - Merge the measurements into one CSV file ([results/benchmark.csv](results/benchmark.csv))
5. [evaluation.R](evaluation.R) - Analyze the measurements and generate tables (LaTex) and figures (PDF)
6. [paper-evaluation-results-supplementary-tables.tex](paper-evaluation-results-supplementary-tables.tex) - Compile the result LaTex tables to [PDF](paper-evaluation-results-supplementary-tables.pdf)

## Acknowledgments
Part of this work was funded by DFG in the scope of the LakeBase project within the Scientific Library Services and Information Systems (LIS) program. The computational experiments were performed on resources of Friedrich Schiller University Jena supported in part by DFG grants INST 275/334-1 FUGG and INST 275/363-1 FUGG.