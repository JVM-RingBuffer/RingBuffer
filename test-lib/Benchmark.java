package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Benchmark {
    private static Benchmark instance;

    public static Benchmark current() {
        return instance;
    }

    private final List<Result> results = new ArrayList<>(5);

    protected Benchmark() {
        instance = this;
    }

    final Result getResult(String profilerName) {
        for (Result result : results) {
            if (result.profilerName.equals(profilerName)) {
                return result;
            }
        }
        Result result = new Result(profilerName);
        results.add(result);
        return result;
    }

    protected int getWarmupRepeatTimes() {
        return getRepeatTimes();
    }

    protected abstract int getRepeatTimes();

    protected abstract int getNumIterations();

    public final void runBenchmark() {
        int numIterations = getNumIterations();
        for (int i = getWarmupRepeatTimes(); i > 0; i--) {
            test(numIterations);
        }
        results.clear();
        for (int i = getRepeatTimes(); i > 0; i--) {
            test(numIterations);
        }
        for (Result result : results) {
            result.report();
        }
    }

    protected abstract void test(int i);

    static class Result {
        private static final NumberFormat formatter = new DecimalFormat("#.##");

        final String profilerName;
        private long sum;
        private double count;
        private long minimum = Long.MAX_VALUE;
        private long maximum = 0L;

        Result(String profilerName) {
            this.profilerName = profilerName;
        }

        synchronized void update(long value) {
            sum += value;
            count++;
            if (value < minimum) {
                minimum = value;
            }
            if (value > maximum) {
                maximum = value;
            }
        }

        void report() {
            long sum;
            double count;
            long minimum;
            long maximum;
            synchronized (this) {
                sum = this.sum;
                count = this.count;
                minimum = this.minimum;
                maximum = this.maximum;
            }
            double average = sum / count;
            String report = profilerName + ": " + formatExecutionTime(average);
            if (count > 1D && minimum != Long.MAX_VALUE) {
                double absoluteVariance = Math.max(maximum - average, average - minimum);
                long relativeVariance = Math.round(absoluteVariance / average * 100D);
                report += " ± " + relativeVariance + "% (" + formatExecutionTime(minimum) + ')';
            }
            System.out.println(report);
        }

        private static String formatExecutionTime(double value) {
            if (value < 2_000D) {
                return formatter.format(value) + "ns";
            }
            if (value < 2_000_000D) {
                return formatter.format(value / 1_000D) + "us";
            }
            return formatter.format(value / 1_000_000D) + "ms";
        }
    }
}
