package perftest;

import java.util.LinkedHashMap;
import java.util.Map;

class Benchmark {
    private static final Map<String, Result> results = new LinkedHashMap<>();

    static void add(Profiler profiler) {
        results.computeIfAbsent(profiler.getPrefix(), Result::new)
                .update(profiler.getExecutionTime());
    }

    static void begin() {
        results.clear();
    }

    static void report() {
        for (Result result : results.values()) {
            result.report();
        }
    }

    private static class Result {
        private final String prefix;
        private long sum;
        private double count;
        private long minimum = Long.MAX_VALUE;
        private long maximum = Long.MIN_VALUE;

        Result(String prefix) {
            this.prefix = prefix;
        }

        void update(long value) {
            if (value != 0L) {
                sum += value;
                count++;
                if (value < minimum) {
                    minimum = value;
                }
                if (value > maximum) {
                    maximum = value;
                }
            }
        }

        void report() {
            String minimum = Profiler.formatExecutionTime(this.minimum);
            String average = Profiler.formatExecutionTime(Math.round(sum / count));
            String maximum = Profiler.formatExecutionTime(this.maximum);
            System.out.println(prefix + minimum + " min, " + average + " avg, " + maximum + " max");
        }
    }
}
