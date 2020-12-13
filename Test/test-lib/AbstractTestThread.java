/*
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

import org.ringbuffer.concurrent.ThreadSynchronizer;
import org.ringbuffer.lang.Optional;
import org.ringbuffer.system.ThreadSpreader;
import org.ringbuffer.system.Threads;

public abstract class AbstractTestThread extends Thread {
    private static final ThreadSpreader spreader = Threads.spreadOverCPUs()
            .fromFirstCPU()
            .toLastCPU()
            .increment(Config.hardwareThreadsPerCore)
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
        try {
            join();
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
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
