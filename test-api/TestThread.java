package test;

import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.EmptyRingBuffer;

abstract class TestThread extends Thread {
    private final int numIterations;
    private final Profiler profiler;
    private final RingBuffer<Event> ringBuffer, ringBuffer2;

    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        this(numIterations, ringBuffer, null);
    }

    TestThread(int numIterations, RingBuffer<Event> ringBuffer, RingBuffer<Event> ringBuffer2) {
        this.numIterations = numIterations;
        profiler = new Profiler(this, numIterations);
        this.ringBuffer = ringBuffer;
        this.ringBuffer2 = ringBuffer2;
    }

    int getNumIterations() {
        return numIterations;
    }

    EmptyRingBuffer<Event> getRingBuffer() {
        return (EmptyRingBuffer<Event>) ringBuffer;
    }

    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) ringBuffer;
    }

    EmptyRingBuffer<Event> getRingBuffer2() {
        return (EmptyRingBuffer<Event>) ringBuffer2;
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
        RingBufferTest.BENCHMARK.add(profiler);
    }

    interface Factory {
        TestThread newInstance(int numIterations);
    }
}
