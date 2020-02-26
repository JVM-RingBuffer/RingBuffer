package eu.menzani.ringbuffer;

abstract class TestThread extends Thread {
    private final int numIterations;
    private final Profiler profiler;
    final RingBuffer<Event> ringBuffer;

    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        this.numIterations = numIterations;
        profiler = new Profiler(this, "tick()");
        this.ringBuffer = ringBuffer;
        start();
    }

    Profiler getProfiler() {
        return profiler;
    }

    @Override
    public void run() {
        profiler.start();
        for (int i = 0; i < numIterations; i++) {
            tick(i);
        }
        profiler.stop();
    }

    abstract void tick(int i);

    void waitForCompletion() throws InterruptedException {}

    void reportPerformance() throws InterruptedException {
        waitForCompletion();
        profiler.report(numIterations);
    }

    interface Factory {
        TestThread newInstance(int numIterations, RingBuffer<Event> ringBuffer);
    }
}
