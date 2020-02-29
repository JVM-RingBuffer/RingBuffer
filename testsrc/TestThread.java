package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.system.ThreadBind;

abstract class TestThread extends Thread {
    private static final ThreadBind.Spread spread = ThreadBind.spread();

    static {
        ThreadBind.loadNativeLibrary();
    }

    private final boolean bindToCPU;
    final int numIterations;
    private final Profiler profiler;
    final RingBuffer<Event> ringBuffer;

    TestThread(boolean bindToCPU, int numIterations, RingBuffer<Event> ringBuffer) {
        this.bindToCPU = bindToCPU;
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
        if (bindToCPU) {
            spread.bindCurrentThreadToNextCPU();
        }
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
