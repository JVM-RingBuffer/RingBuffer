package test;

import java.util.ArrayList;
import java.util.List;

public abstract class Benchmark {
    private static Benchmark instance;

    public static Benchmark current() {
        return instance;
    }

    private final List<BenchmarkResult> results = new ArrayList<>(5);

    protected Benchmark() {
        instance = this;
    }

    final BenchmarkResult getResult(String profilerName) {
        for (BenchmarkResult result : results) {
            if (result.profilerName.equals(profilerName)) {
                return result;
            }
        }
        BenchmarkResult result = new BenchmarkResult(profilerName);
        results.add(result);
        return result;
    }

    protected int getWarmupRepeatTimes() {
        return getRepeatTimes();
    }

    protected abstract int getRepeatTimes();

    protected abstract int getNumIterations();

    public final void run() {
        int numIterations = getNumIterations();
        for (int i = getWarmupRepeatTimes(); i > 0; i--) {
            test(numIterations);
        }
        results.clear();
        for (int i = getRepeatTimes(); i > 0; i--) {
            test(numIterations);
        }
        for (BenchmarkResult result : results) {
            result.report();
        }
    }

    protected abstract void test(int i);
}
