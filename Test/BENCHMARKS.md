## Benchmarks

1 million elements per concurrent thread are written/read.

[bench](bench) folder: write all elements _then_ read all elements  
[contention-bench](contention-bench) folder: write and read at the same time all elements

1. Run `BenchmarkRunner` and terminate it
2. Configure `cfg/benchmarks.properties`
3. Rerun `BenchmarkRunner`
