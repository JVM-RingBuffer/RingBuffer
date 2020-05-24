package test;

public class TestThreadGroup {
    private final TestThread[] testThreads;

    public TestThreadGroup(TestThread.Factory testThreadFactory) {
        testThreads = new TestThread[RingBufferTest.CONCURRENCY];
        for (int i = 0; i < testThreads.length; i++) {
            testThreads[i] = testThreadFactory.newInstance(RingBufferTest.NUM_ITERATIONS);
        }
    }

    public void start() {
        for (TestThread testThread : testThreads) {
            testThread.start();
        }
        for (TestThread testThread : testThreads) {
            testThread.waitUntilReady();
        }
        for (TestThread testThread : testThreads) {
            testThread.commenceExecution();
        }
    }

    public void waitForCompletion() {
        for (TestThread testThread : testThreads) {
            testThread.waitForCompletion();
        }
    }

    public long getReaderSum() {
        long sum = 0L;
        for (TestThread testThread : testThreads) {
            sum += ((Reader) testThread).getSum();
        }
        return sum;
    }
}
