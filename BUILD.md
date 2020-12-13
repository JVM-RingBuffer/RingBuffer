## Build

1. Open the project in IntelliJ IDEA
2. Set project JDK to 11
3. Build all artifacts

## Benchmarks

1 million elements per concurrent thread are written/read.

`test` folder: write all elements _then_ read all elements  
`contention-test` folder: write and read at the same time all elements

1. Run `TestRunner` and terminate it
2. Configure `Test/cfg/test.properties`
3. Rerun `TestRunner`