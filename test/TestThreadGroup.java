package eu.menzani.ringbuffer;

import java.util.HashSet;
import java.util.Set;

class TestThreadGroup {
    private final Set<TestThread> testThreads = new HashSet<>();

    TestThreadGroup(TestThread.Factory testThreadFactory, RingBuffer<Event> ringBuffer) {
        for (int i = 0; i < RingBufferTest.CONCURRENCY; i++) {
            testThreads.add(testThreadFactory.newInstance(RingBufferTest.NUM_ITERATIONS, ringBuffer));
        }
    }

    void reportPerformance() {
        ProfilerGroup profilerGroup = new ProfilerGroup();
        for (TestThread testThread : testThreads) {
            testThread.waitForCompletion();
            profilerGroup.add(testThread.getProfiler());
        }
        assert profilerGroup.getSize() == RingBufferTest.CONCURRENCY;
        profilerGroup.report();
    }

    long getReaderSum() {
        return testThreads.stream().map(Reader.class::cast).mapToLong(Reader::getSum).sum();
    }
}
