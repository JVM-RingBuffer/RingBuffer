package perftest;

class TestThreadGroup {
    private final TestThread[] testThreads;

    TestThreadGroup(TestThread.Factory testThreadFactory) {
        testThreads = new TestThread[RingBufferTest.CONCURRENCY];
        for (int i = 0; i < testThreads.length; i++) {
            testThreads[i] = testThreadFactory.newInstance(RingBufferTest.NUM_ITERATIONS);
        }
    }

    void start() {
        for (TestThread testThread : testThreads) {
            testThread.start();
        }
    }

    void reportPerformance() {
        for (TestThread testThread : testThreads) {
            testThread.reportPerformance();
        }
    }

    long getReaderSum() {
        long sum = 0L;
        for (TestThread testThread : testThreads) {
            sum += ((Reader) testThread).getSum();
        }
        return sum;
    }
}
