package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Benchmark {
    private static Benchmark instance;

    public static Benchmark current() {
        return instance;
    }

    private final Map<String, Result> results = new LinkedHashMap<>();

    protected Benchmark() {
        instance = this;
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
        for (Result result : results.values()) {
            result.report();
        }
    }

    protected abstract void test(int i);

    public final Profiler newProfiler() {
        return new Profiler(this, getNumIterations());
    }

    public final void add(Profiler profiler) {
        results.computeIfAbsent(profiler.getName(), Result::new)
                .update(profiler.getExecutionTime());
    }

    private static class Result {
        private static final NumberFormat formatter = new DecimalFormat("#.##");

        private final String profilerName;
        private long sum;
        private double count;
        private long minimum = Long.MAX_VALUE;
        private long maximum = 0L;

        Result(String profilerName) {
            this.profilerName = profilerName;
        }

        void update(long value) {
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
            double average = sum / count;
            String report = profilerName + ": " + formatExecutionTime(average);
            if (minimum != Long.MAX_VALUE) {
                double absoluteVariance = Math.max(maximum - average, average - minimum);
                long relativeVariance = Math.round(absoluteVariance / average * 100D);
                report += " ± " + relativeVariance + '%';
            }
            System.out.println(report + " (" + formatExecutionTime(sum) + ')');
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
