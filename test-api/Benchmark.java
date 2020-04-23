package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class Benchmark {
    private final Map<String, Result> results = new LinkedHashMap<>();

    public void add(Profiler profiler) {
        results.computeIfAbsent(profiler.getPrefix(), Result::new)
                .update(profiler.getExecutionTime());
    }

    public void begin() {
        results.clear();
    }

    public void report() {
        for (Result result : results.values()) {
            result.report();
        }
    }

    private static class Result {
        private final String prefix;
        private long sum;
        private double count;
        private long minimum = Long.MAX_VALUE;
        private long maximum = 0L;

        Result(String prefix) {
            this.prefix = prefix;
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
            long average = Math.round(sum / count);
            String report = prefix + formatExecutionTime(average);
            if (minimum != Long.MAX_VALUE) {
                long absoluteVariance = Math.max(maximum - average, average - minimum);
                int relativeVariance = Math.round((float) absoluteVariance / average * 100F);
                report += " ± " + relativeVariance + '%';
            }
            System.out.println(report);
        }

        private static String formatExecutionTime(long value) {
            if (value < 2_000L) {
                return value + "ns";
            }
            if (value < 2_000_000L) {
                return (value / 1_000L) + "us";
            }
            return (value / 1_000_000L) + "ms";
        }
    }
}
