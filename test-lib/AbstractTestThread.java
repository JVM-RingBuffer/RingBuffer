/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import org.ringbuffer.java.Nullable;
import org.ringbuffer.system.ThreadSpreader;
import org.ringbuffer.system.Threads;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractTestThread extends Thread {
    private static final ThreadSpreader spreader = Threads.spreadOverCPUs()
            .fromFirstCPU()
            .toLastCPU()
            .skipHyperthreads()
            .build();

    static void resetThreadSpreader() {
        spreader.reset();
    }

    static {
        Threads.loadNativeLibrary();
    }

    private final int numIterations;
    private final Profiler profiler;
    protected final Object ringBuffer;

    private final CountDownLatch readyLatch = new CountDownLatch(1);
    private final CountDownLatch commenceLatch = new CountDownLatch(1);

    protected AbstractTestThread(int numIterations, Object ringBuffer) {
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
