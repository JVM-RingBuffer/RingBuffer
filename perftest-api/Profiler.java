package perftest;

class Profiler {
    private final String prefix;
    private final double divideBy;
    private long start;
    private long executionTime;

    Profiler(int divideBy) {
        prefix = "";
        this.divideBy = divideBy;
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
        executionTime = Math.round((end - start) / divideBy);
    }

    void report() {
        final long end = System.nanoTime();
        System.out.println(formatExecutionTime(end - start));
    }

    static String formatExecutionTime(long value) {
        if (value < 2_000L) {
            return value + "ns";
        }
        if (value < 2_000_000L) {
            return (value / 1_000L) + "us";
        }
        return (value / 1_000_000L) + "ms";
    }
}