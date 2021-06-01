package bench.launcher;

import eu.menzani.benchmark.Benchmark;
import eu.menzani.benchmark.BenchmarkListener;
import eu.menzani.lang.Lang;
import eu.menzani.lang.Nonblocking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BenchmarkLauncher extends Thread {
    private final BlockingQueue<Class<? extends Benchmark>> queue = new LinkedBlockingQueue<>();
    private final BenchmarkRunner benchmarkRunner;

    private final Lock processLock = new ReentrantLock();
    private Process process;

    BenchmarkLauncher(BenchmarkRunner benchmarkRunner) {
        this.benchmarkRunner = benchmarkRunner;
    }

    void launchBenchmark(Class<? extends Benchmark> benchmarkClass) {
        queue.add(benchmarkClass);
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
            Class<? extends Benchmark> benchmarkClass = Nonblocking.take(queue);
            Benchmark benchmark = Lang.newInstance(benchmarkClass);
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
            benchmarkRunner.setOutput(output);
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
            benchmarkRunner.allowRun();
        }
    }
}
