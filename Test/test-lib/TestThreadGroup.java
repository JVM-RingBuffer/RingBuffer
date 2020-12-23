package test;

import eu.menzani.benchmark.Profiler;
import eu.menzani.lang.Optional;

import java.util.function.IntFunction;

public class TestThreadGroup {
    private final AbstractTestThread[] testThreads;

    public TestThreadGroup(IntFunction<AbstractTestThread> testThreadFactory) {
        testThreads = new AbstractTestThread[Config.concurrentProducersAndConsumers];
        for (int i = 0; i < testThreads.length; i++) {
            testThreads[i] = testThreadFactory.apply(AbstractRingBufferTest.NUM_ITERATIONS);
        }
    }

    public void start(@Optional Profiler profiler) {
        for (AbstractTestThread testThread : testThreads) {
            testThread.start();
        }
        for (AbstractTestThread testThread : testThreads) {
            testThread.waitUntilReady();
        }
        for (AbstractTestThread testThread : testThreads) {
            testThread.commenceExecution();
        }
        if (profiler != null) {
            profiler.start();
        }
    }

    public void waitForCompletion(@Optional Profiler profiler) {
        for (AbstractTestThread testThread : testThreads) {
            testThread.waitForCompletion();
        }
        if (profiler != null) {
            profiler.stop();
        }
    }

    public long getReaderSum() {
        long sum = 0L;
        for (AbstractTestThread testThread : testThreads) {
            sum += ((AbstractReader) testThread).getSum();
        }
        return sum;
    }
}
