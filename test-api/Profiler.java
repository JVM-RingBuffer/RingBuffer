package test;

public class Profiler {
    private final String prefix;
    private final double divideBy;
    private long start;
    private long executionTime;

    public Profiler(Object instance, int divideBy) {
        prefix = instance.getClass().getSimpleName() + ": ";
        this.divideBy = divideBy;
    }

    String getPrefix() {
        return prefix;
    }

    long getExecutionTime() {
        return executionTime;
    }

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        final long end = System.nanoTime();
        long elapsed = end - start;
        executionTime = Math.round(elapsed / divideBy);
    }
}
