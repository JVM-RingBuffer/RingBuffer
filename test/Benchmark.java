package eu.menzani.ringbuffer;

import java.util.HashMap;
import java.util.Map;

class Benchmark {
    private static final boolean enabled = Boolean.getBoolean("benchmark");
    private static final Map<String, Result> results = new HashMap<>();

    static boolean isEnabled() {
        return enabled;
    }

    static void add(Measure measure) {
        results.computeIfAbsent(measure.getPrefix(), Result::new)
                .update(measure.getExecutionTime());
    }

    static void report() {
        if (enabled) {
            for (Result result : results.values()) {
                result.report();
            }
        }
    }

    private static class Result implements Measure {
        private final String prefix;
        private long value = Long.MAX_VALUE;

        private Result(String prefix) {
            this.prefix = prefix;
        }

        private void update(long newValue) {
            if (newValue < value) {
                value = newValue;
            }
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        @Override
        public long getExecutionTime() {
            return value;
        }
    }
}
