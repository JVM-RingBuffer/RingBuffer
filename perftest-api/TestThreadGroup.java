package perftest;

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
        for (TestThread testThread : testThreads) {
            testThread.waitForCompletion();
            Benchmark.add(testThread.getProfiler());
        }
    }

    long getReaderSum() {
        return testThreads.stream().map(Reader.class::cast).mapToLong(Reader::getSum).sum();
    }
}
