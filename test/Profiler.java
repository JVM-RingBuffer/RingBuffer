package eu.menzani.ringbuffer;

class Profiler {
    private final String prefix;
    private final int divideBy;
    private long start;
    private long executionTime;

    Profiler(Object instance, String methodName) {
        this(instance, methodName, 1);
    }

    Profiler(Object instance, String methodName, int divideBy) {
        prefix = instance.getClass().getSimpleName() + '#' + methodName + ": ";
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
        if (executionTime < 2_000L) {
            System.out.println(prefix + executionTime + "ns");
        } else if (executionTime < 2_000_000L) {
            System.out.println(prefix + (executionTime / 1_000L) + "us");
        } else {
            System.out.println(prefix + (executionTime / 1_000_000L) + "ms");
        }
    }
}
