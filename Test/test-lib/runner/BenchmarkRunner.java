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

package test.runner;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.lang.UncaughtException;
import test.AbstractRingBufferTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BenchmarkRunner extends Thread {
    private final BlockingQueue<Class<?>> queue = new LinkedBlockingQueue<>();
    private final TestRunner testRunner;

    private final Lock processLock = new ReentrantLock();
    private Process process;

    BenchmarkRunner(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    void runTest(Class<? extends AbstractRingBufferTest> testClass) {
        queue.add(testClass);
    }

    void setProcess(Process process) {
        this.process = process;
    }

    void terminate() {
        processLock.lock();
        try {
            if (process != null) {
                process.destroy();
            }
        } finally {
            processLock.unlock();
        }
    }

    @Override
    public void run() {
        BenchmarkListener benchmarkListener = new BenchmarkListener();
        try {
            while (true) {
                Class<?> testClass = queue.take();
                Benchmark benchmark = (Benchmark) testClass.getDeclaredConstructor().newInstance();
                benchmark.runBenchmark(benchmarkListener);
            }
        } catch (ReflectiveOperationException e) {
            throw new UncaughtException(e);
        } catch (InterruptedException e) {
            throw new AssertionError();
        }
    }

    private class BenchmarkListener implements eu.menzani.benchmark.BenchmarkListener {
        @Override
        public void beginProcessCreate() {
            processLock.lock();
        }

        @Override
        public void onProcessCreated(Process process) {
            setProcess(process);
            processLock.unlock();
        }

        @Override
        public void onOutputLineAdded(String line) {
        }

        @Override
        public void updateOutput(String output) {
            testRunner.setOutput(output);
        }

        @Override
        public void onEnd() {
            testRunner.allowRun();
        }
    }
}
