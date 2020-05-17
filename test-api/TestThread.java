package test;

import eu.menzani.ringbuffer.EmptyRingBuffer;
import eu.menzani.ringbuffer.OverwritingPrefilledRingBuffer;
import eu.menzani.ringbuffer.PrefilledRingBuffer;
import eu.menzani.ringbuffer.RingBuffer;
import eu.menzani.ringbuffer.system.ThreadSpreader;
import eu.menzani.ringbuffer.system.Threads;

import java.util.concurrent.CountDownLatch;

abstract class TestThread extends Thread {
    private static final ThreadSpreader spreader = Threads.spreadOverCPUs()
            .fromFirstCPU().toCPU(2 * 6 - 1).skipHyperthreads().build();

    static void resetThreadSpreader() {
        spreader.reset();
    }

    static {
        Threads.loadNativeLibrary();
    }

    private final int numIterations;
    private final Profiler profiler;
    private final RingBuffer<Event> ringBuffer;

    private final CountDownLatch readyLatch = new CountDownLatch(1);
    private final CountDownLatch commenceLatch = new CountDownLatch(1);

    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        this.numIterations = numIterations;
        profiler = new Profiler(getProfilerName(), numIterations);
        this.ringBuffer = ringBuffer;
    }

    abstract String getProfilerName();

    void startNow() {
        start();
        waitUntilReady();
        commenceExecution();
    }

    void waitUntilReady() {
        try {
            readyLatch.await();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }

    void commenceExecution() {
        commenceLatch.countDown();
    }

    void waitForCompletion() {
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
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

    OverwritingPrefilledRingBuffer<Event> getOverwritingPrefilledRingBuffer() {
        return (OverwritingPrefilledRingBuffer<Event>) ringBuffer;
    }

    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) ringBuffer;
    }

    @Override
    public void run() {
        spreader.bindCurrentThreadToNextCPU();
        Threads.setCurrentThreadPriorityToRealtime();

        readyLatch.countDown();
        try {
            commenceLatch.await();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }

        profiler.start();
        loop();
        profiler.stop();
    }

    abstract void loop();

    interface Factory {
        TestThread newInstance(int numIterations);
    }
}
