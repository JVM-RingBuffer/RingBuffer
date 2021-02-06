package test.runner;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.benchmark.BenchmarkListener;
import eu.menzani.lang.Lang;
import eu.menzani.lang.Nonblocking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BenchmarkRunner extends Thread {
    private final BlockingQueue<Class<? extends Benchmark>> queue = new LinkedBlockingQueue<>();
    private final TestRunner testRunner;

    private final Lock processLock = new ReentrantLock();
    private Process process;

    BenchmarkRunner(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    void runTest(Class<? extends Benchmark> testClass) {
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
        BenchmarkListener benchmarkListener = new BenchmarkListenerImpl();
        while (true) {
            Class<? extends Benchmark> testClass = Nonblocking.take(queue);
            Benchmark benchmark = Lang.newInstance(testClass);
            benchmark.launchBenchmark(benchmarkListener);
        }
    }

    private class BenchmarkListenerImpl implements BenchmarkListener {
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
        public void onErrorLineAdded(String line) {
            System.err.println(line);
        }

        @Override
        public void updateError(String error) {
        }

        @Override
        public void onEnd() {
            testRunner.allowRun();
        }
    }
}
