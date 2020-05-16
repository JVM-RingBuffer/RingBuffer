package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

class BenchmarkResult {
    private static final NumberFormat formatter = new DecimalFormat("#.##");

    final String profilerName;
    private long sum;
    private double count;
    private long minimum = Long.MAX_VALUE;
    private long maximum = 0L;

    BenchmarkResult(String profilerName) {
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
