package perftest;

import eu.menzani.ringbuffer.RingBuffer;

abstract class TestThread extends Thread {
    private final int numIterations;
    private final Profiler profiler;
    private final RingBuffer<Event> ringBuffer;

    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        this.numIterations = numIterations;
        profiler = new Profiler(this, numIterations);
        this.ringBuffer = ringBuffer;
    }

    int getNumIterations() {
        return numIterations;
    }

    Profiler getProfiler() {
        return profiler;
    }

    RingBuffer<Event> getRingBuffer() {
        return ringBuffer;
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
        RingBufferTest.BENCHMARK.add(profiler);
    }

    interface Factory {
        TestThread newInstance(int numIterations);
    }
}
