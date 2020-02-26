package eu.menzani.ringbuffer;

class Profiler {
    private final String prefix;
    private long start;
    private long executionTime;

    Profiler(Object instance, String methodName) {
        prefix = instance.getClass().getSimpleName() + '#' + methodName + ": ";
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
        executionTime = end - start;
    }

    void report() {
        report(1);
    }

    void report(int divideBy) {
        report(prefix, executionTime / divideBy);
    }

    static void report(String prefix, long executionTime) {
        if (executionTime < 2_000L) {
            System.out.println(prefix + executionTime + "ns");
        } else if (executionTime < 2_000_000L) {
            System.out.println(prefix + (executionTime / 1_000L) + "us");
        } else {
            System.out.println(prefix + (executionTime / 1_000_000L) + "ms");
        }
    }
}
