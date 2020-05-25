package test;

import eu.menzani.ringbuffer.AbstractRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import eu.menzani.ringbuffer.object.EmptyRingBuffer;
import eu.menzani.ringbuffer.object.PrefilledOverwritingRingBuffer;
import eu.menzani.ringbuffer.object.PrefilledRingBuffer;
import eu.menzani.ringbuffer.object.RingBuffer;
import eu.menzani.ringbuffer.system.ThreadSpreader;
import eu.menzani.ringbuffer.system.Threads;

import java.util.concurrent.CountDownLatch;

public abstract class TestThread extends Thread {
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
    private final AbstractRingBuffer ringBuffer;

    private final CountDownLatch readyLatch = new CountDownLatch(1);
    private final CountDownLatch commenceLatch = new CountDownLatch(1);

    protected TestThread(int numIterations, AbstractRingBuffer ringBuffer) {
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

    @SuppressWarnings("unchecked")
    protected RingBuffer<Event> getRingBuffer() {
        return (RingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    protected EmptyRingBuffer<Event> getEmptyRingBuffer() {
        return (EmptyRingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    PrefilledOverwritingRingBuffer<Event> getPrefilledOverwritingRingBuffer() {
        return (PrefilledOverwritingRingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) ringBuffer;
    }

    protected MarshallingRingBuffer getHeapRingBuffer() {
        return (MarshallingRingBuffer) ringBuffer;
    }

    protected MarshallingBlockingRingBuffer getHeapBlockingRingBuffer() {
        return (MarshallingBlockingRingBuffer) ringBuffer;
    }

    protected DirectMarshallingRingBuffer getNativeRingBuffer() {
        return (DirectMarshallingRingBuffer) ringBuffer;
    }

    protected DirectMarshallingBlockingRingBuffer getNativeBlockingRingBuffer() {
        return (DirectMarshallingBlockingRingBuffer) ringBuffer;
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

    protected interface Factory {
        TestThread newInstance(int numIterations);
    }
}
