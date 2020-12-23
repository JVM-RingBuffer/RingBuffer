package test;

import eu.menzani.benchmark.Profiler;
import eu.menzani.concurrent.ThreadSynchronizer;
import eu.menzani.lang.Nonblocking;
import eu.menzani.lang.Optional;
import eu.menzani.system.ThreadSpreader;
import eu.menzani.system.Threads;

public abstract class AbstractTestThread extends Thread {
    private static final ThreadSpreader spreader = Threads.spreadOverCPUs()
            .fromFirstCPU()
            .toLastCPU()
            .skipHyperthreads()
            .build();

    static void resetThreadSpreader() {
        spreader.reset();
    }

    private final int numIterations;
    protected final Object dataStructure;
    private final ThreadSynchronizer synchronizer = new ThreadSynchronizer();

    protected AbstractTestThread(int numIterations, Object dataStructure) {
        this.numIterations = numIterations;
        this.dataStructure = dataStructure;
    }

    protected void startNow(@Optional Profiler profiler) {
        start();
        waitUntilReady();
        commenceExecution();
        if (profiler != null) {
            profiler.start();
        }
    }

    void waitUntilReady() {
        synchronizer.waitUntilReady();
    }

    void commenceExecution() {
        synchronizer.commenceExecution();
    }

    void waitForCompletion() {
        Nonblocking.join(this);
    }

    protected void waitForCompletion(@Optional Profiler profiler) {
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
        Profiler profiler = new Profiler(this, numIterations);
        synchronizer.synchronize();

        profiler.start();
        loop();
        profiler.stop();
    }

    protected abstract void loop();
}
