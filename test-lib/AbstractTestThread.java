package test;

import eu.menzani.ringbuffer.AbstractRingBuffer;
import eu.menzani.ringbuffer.system.ThreadSpreader;
import eu.menzani.ringbuffer.system.Threads;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractTestThread extends Thread {
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
    protected final AbstractRingBuffer ringBuffer;

    private final CountDownLatch readyLatch = new CountDownLatch(1);
    private final CountDownLatch commenceLatch = new CountDownLatch(1);

    protected AbstractTestThread(int numIterations, AbstractRingBuffer ringBuffer) {
        this.numIterations = numIterations;
        profiler = new Profiler(getProfilerName(), numIterations);
        this.ringBuffer = ringBuffer;
    }

    protected abstract String getProfilerName();

    protected void startNow() {
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

    protected void waitForCompletion() {
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }

    protected int getNumIterations() {
        return numIterations;
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

    protected abstract void loop();
}
