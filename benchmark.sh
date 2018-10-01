mkdir -p results
cd results
mkdir -p run1
mkdir -p run2
mkdir -p run3
cd run1
sbatch ../benchmark_FULL_PREPARED.sh
sbatch ../benchmark_FULL_UNPREPARED.sh
sbatch ../benchmark_HALF_PREPARED.sh
sbatch ../benchmark_HALF_UNPREPARED.sh
sbatch ../benchmark_NONE_PREPARED.sh
sbatch ../benchmark_NONE_UNPREPARED.sh
cd ../run2
sbatch ../benchmark_FULL_PREPARED.sh
sbatch ../benchmark_FULL_UNPREPARED.sh
sbatch ../benchmark_HALF_PREPARED.sh
sbatch ../benchmark_HALF_UNPREPARED.sh
sbatch ../benchmark_NONE_PREPARED.sh
sbatch ../benchmark_NONE_UNPREPARED.sh
cd ../run3
sbatch ../benchmark_FULL_PREPARED.sh
sbatch ../benchmark_FULL_UNPREPARED.sh
sbatch ../benchmark_HALF_PREPARED.sh
sbatch ../benchmark_HALF_UNPREPARED.sh
sbatch ../benchmark_NONE_PREPARED.sh
sbatch ../benchmark_NONE_UNPREPARED.sh
