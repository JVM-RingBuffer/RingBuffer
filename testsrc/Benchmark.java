package eu.menzani.ringbuffer;

import java.util.LinkedHashMap;
import java.util.Map;

class Benchmark {
    private static final Map<String, Result> results = new LinkedHashMap<>();

    static void add(Measure measure) {
        results.computeIfAbsent(measure.getPrefix(), Result::new)
                .update(measure.getExecutionTime());
    }

    static void reset() {
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
        private int count;
        private long minimum = Long.MAX_VALUE;

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
            }
        }

        void report() {
            String average = Measure.formatExecutionTime(sum / count);
            String minimum = Measure.formatExecutionTime(this.minimum);
            System.out.println(prefix + average + " avg, " + minimum + " min");
        }
    }
}
