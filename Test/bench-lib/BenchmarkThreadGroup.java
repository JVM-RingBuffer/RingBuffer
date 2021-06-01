package bench;

import eu.menzani.benchmark.Profiler;
import eu.menzani.lang.Optional;

import java.util.function.IntFunction;

public class BenchmarkThreadGroup {
    private final AbstractBenchmarkThread[] benchmarkThreads;

    public BenchmarkThreadGroup(IntFunction<AbstractBenchmarkThread> benchmarkThreadFactory) {
        benchmarkThreads = new AbstractBenchmarkThread[Config.concurrentProducersAndConsumers];
        for (int i = 0; i < benchmarkThreads.length; i++) {
            benchmarkThreads[i] = benchmarkThreadFactory.apply(AbstractRingBufferBenchmark.NUM_ITERATIONS);
        }
    }

    public void start(@Optional Profiler profiler) {
        for (AbstractBenchmarkThread benchmarkThread : benchmarkThreads) {
            benchmarkThread.start();
        }
        for (AbstractBenchmarkThread benchmarkThread : benchmarkThreads) {
            benchmarkThread.waitUntilReady();
        }
        for (AbstractBenchmarkThread benchmarkThread : benchmarkThreads) {
            benchmarkThread.commenceExecution();
        }
        if (profiler != null) {
            profiler.start();
        }
    }

    public void waitForCompletion(@Optional Profiler profiler) {
        for (AbstractBenchmarkThread benchmarkThread : benchmarkThreads) {
            benchmarkThread.waitForCompletion();
        }
        if (profiler != null) {
            profiler.stop();
        }
    }

    public long getReaderSum() {
        long sum = 0L;
        for (AbstractBenchmarkThread benchmarkThread : benchmarkThreads) {
            sum += ((AbstractReader) benchmarkThread).getSum();
        }
        return sum;
    }
}
