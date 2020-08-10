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

package test.lock;

import org.ringbuffer.lock.SpinLock;
import org.ringbuffer.system.ThreadSpreader;
import org.ringbuffer.system.Threads;
import test.Benchmark;
import test.Profiler;
import test.ThreadSynchronizer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpinLockTest extends Benchmark {
    private static final int concurrency = 6;
    private static final int numIterations = 1_000_000;

    public static void main(String[] args) {
        new SpinLockTest().runBenchmark();
    }

    @Override
    protected void test(int i) {
        Worker.spreader.reset();
        Worker[] workers = new Worker[concurrency];
        for (int j = 0; j < workers.length; j++) {
            Worker worker = new Worker();
            worker.start();
            workers[j] = worker;
        }
        Worker.sum = 0;
        for (Worker worker : workers) {
            worker.synchronizer.waitUntilReady();
        }
        for (Worker worker : workers) {
            worker.synchronizer.commenceExecution();
        }
        for (Worker worker : workers) {
            ThreadSynchronizer.waitForCompletion(worker);
        }
        assertEquals(numIterations * concurrency, Worker.sum);
    }

    private static class Worker extends Thread {
        static final ThreadSpreader spreader = Threads.spreadOverCPUs()
                .fromFirstCPU()
                .toLastCPU()
                .skipHyperthreads()
                .build();

        static {
            Threads.loadNativeLibrary();
        }

        final ThreadSynchronizer synchronizer = new ThreadSynchronizer();

        private static final SpinLock lock = new SpinLock();
        static int sum;

        @Override
        public void run() {
            spreader.bindCurrentThreadToNextCPU();
            Threads.setCurrentThreadPriorityToRealtime();
            Profiler profiler = new Profiler(SpinLockTest.class, numIterations);
            synchronizer.synchronize();

            profiler.start();
            for (int i = 0; i < numIterations; i++) {
                lock.lock();
                sum++;
                lock.unlock();
            }
            profiler.stop();
        }
    }
}
