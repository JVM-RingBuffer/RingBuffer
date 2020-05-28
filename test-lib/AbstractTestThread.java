package test;

import org.ringbuffer.AbstractRingBuffer;
import org.ringbuffer.java.Nullable;
import org.ringbuffer.system.ThreadSpreader;
import org.ringbuffer.system.Threads;

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
        profiler = new Profiler(this, numIterations);
        this.ringBuffer = ringBuffer;
    }

    protected void startNow(@Nullable Profiler profiler) {
        start();
        waitUntilReady();
        commenceExecution();
        if (profiler != null) {
            profiler.start();
        }
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

    protected void waitForCompletion(@Nullable Profiler profiler) {
        waitForCompletion();
        if (profiler != null) {
            profiler.stop();
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
