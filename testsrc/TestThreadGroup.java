package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.java.Assert;

import java.util.HashSet;
import java.util.Set;

class TestThreadGroup {
    private final Set<TestThread> testThreads = new HashSet<>();

    TestThreadGroup(TestThread.Factory testThreadFactory) {
        for (int i = 0; i < Test.CONCURRENCY; i++) {
            testThreads.add(testThreadFactory.newInstance(Test.NUM_ITERATIONS));
        }
    }

    void reportPerformance() {
        ProfilerGroup profilerGroup = new ProfilerGroup();
        for (TestThread testThread : testThreads) {
            testThread.waitForCompletion();
            profilerGroup.add(testThread.getProfiler());
        }
        Assert.equal(profilerGroup.size(), Test.CONCURRENCY);
        Benchmark.add(profilerGroup);
    }

    long getReaderSum() {
        return testThreads.stream().map(Reader.class::cast).mapToLong(Reader::getSum).sum();
    }
}
