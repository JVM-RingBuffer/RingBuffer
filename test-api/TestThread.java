package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;
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

    RingBuffer<Event> getRingBuffer() {
        return ringBuffer;
    }

    EmptyRingBuffer<Event> getEmptyRingBuffer() {
        return (EmptyRingBuffer<Event>) ringBuffer;
    }

    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) ringBuffer;
    }

    @Override
    public void run() {
        profiler.start();
        loop();
        profiler.stop();
    }

    abstract void loop();

    void reportPerformance() {
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
        Benchmark.current().add(profiler);
    }

    interface Factory {
        TestThread newInstance(int numIterations);
    }
}
