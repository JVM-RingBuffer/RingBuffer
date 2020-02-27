package eu.menzani.ringbuffer;

import eu.menzani.ringbuffer.system.ThreadBind;

abstract class TestThread extends Thread {
    private static final ThreadBind spread = ThreadBind.spread();

    private final boolean bind;
    final int numIterations;
    private final Profiler profiler;
    final RingBuffer<Event> ringBuffer;

    TestThread(boolean bind, int numIterations, RingBuffer<Event> ringBuffer) {
        this.bind = bind;
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
        if (bind && Profiler.isEnabled()) {
            spread.bindCurrentThread();
        }
        profiler.start();
        loop();
        profiler.stop();
    }

    abstract void loop();

    void waitForCompletion() {}

    void reportPerformance() {
        waitForCompletion();
        profiler.report();
    }

    interface Factory {
        TestThread newInstance(int numIterations);
    }
}
