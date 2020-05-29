1. Open the project in IntelliJ IDEA
2. Set project JDK to at least JDK 11

## Benchmarks

After building from source, you may run the benchmarks.

`test` folder: write _then_ read  
`contention-test` folder: write and read at the same time

`AbstractRingBufferTest.CONCURRENCY` is the number of threads reading and/or writing.  
You may need to tweak `AbstractTestThread.spreader` which takes care of binding each thread to its own CPU.