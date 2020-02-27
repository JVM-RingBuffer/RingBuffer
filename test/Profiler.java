package eu.menzani.ringbuffer;

class Profiler {
    private static boolean enabled;

    static boolean isEnabled() {
        return enabled;
    }

    static void enable() {
        enabled = true;
    }

    static void disable() {
        enabled = false;
    }

    private final String prefix;
    private final int divideBy;
    private long start;
    private long executionTime;

    Profiler(Object instance) {
        this(instance, 1);
    }

    Profiler(Object instance, int divideBy) {
        prefix = instance.getClass().getSimpleName() + ": ";
        this.divideBy = divideBy;
    }

    String getPrefix() {
        return prefix;
    }

    long getExecutionTime() {
        return executionTime;
    }

    void start() {
        start = System.nanoTime();
    }

    void stop() {
        final long end = System.nanoTime();
        executionTime = (end - start) / divideBy;
    }

    void report() {
        report(prefix, executionTime);
    }

    static void report(String prefix, long executionTime) {
        if (enabled) {
            if (executionTime < 2_000L) {
                System.out.println(prefix + executionTime + "ns");
            } else if (executionTime < 2_000_000L) {
                System.out.println(prefix + (executionTime / 1_000L) + "us");
            } else {
                System.out.println(prefix + (executionTime / 1_000_000L) + "ms");
            }
        }
    }
}
