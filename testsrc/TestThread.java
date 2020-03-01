package eu.menzani.ringbuffer;

abstract class TestThread extends Thread {
    final int numIterations;
    private final Profiler profiler;
    final RingBuffer<Event> ringBuffer;

    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        this.numIterations = numIterations;
        profiler = new Profiler(this, numIterations);
        this.ringBuffer = ringBuffer;
        start();
    }

    Profiler getProfiler() {
        return profiler;
    }

    @Override
    public void run() {
        profiler.start();
        loop();
        profiler.stop();
    }

    abstract void loop();

    void waitForCompletion() {}

    void reportPerformance() {
        waitForCompletion();
        Benchmark.add(profiler);
    }

    interface Factory {
        TestThread newInstance(int numIterations);
    }
}
