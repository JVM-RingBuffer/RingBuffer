package eu.menzani.ringbuffer;

class Profiler implements Measure {
    private final String prefix;
    private final int divideBy;
    private long start;
    private long executionTime;

    Profiler(Object instance, int divideBy) {
        prefix = instance.getClass().getSimpleName() + ": ";
        this.divideBy = divideBy;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    void start() {
        start = System.nanoTime();
    }

    void stop() {
        final long end = System.nanoTime();
        executionTime = (end - start) / divideBy;
    }
}
