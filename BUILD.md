## Build

1. Open the project in IntelliJ IDEA (launch with `idea.no.launcher=true`)
2. Set project JDK to at least JDK 11
3. Build Artifact: RingBuffer

## Benchmarks

1 million elements per producer are written, and 1 million elements per consumer are read.

`test` folder: write all elements _then_ read all elements  
`contention-test` folder: write and read at the same time all elements

`AbstractRingBufferTest.CONCURRENCY` is the number of concurrent producers and the number of concurrent consumers where applicable.  
If simultaneous multithreading is not two-way or not enabled, tweak `AbstractTestThread.spreader`.