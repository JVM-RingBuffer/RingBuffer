package test.runner;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.lang.Nonblocking;
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
                Class<?> testClass = Nonblocking.take(queue);
                Benchmark benchmark = (Benchmark) testClass.getDeclaredConstructor().newInstance();
                benchmark.launchBenchmark(benchmarkListener);
            }
        } catch (ReflectiveOperationException e) {
            throw new UncaughtException(e);
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
