#!/bin/bash
#SBATCH -N 1
#SBATCH -c 36
#SBATCH -p s_standard
COVERAGE=FULL
PREPARED=FALSE
java -jar ../target/benchmarks.jar -p p5_coverage=$COVERAGE -p p4_prepared=$PREPARED | tee ${COVERAGE}_$PREPARED.log