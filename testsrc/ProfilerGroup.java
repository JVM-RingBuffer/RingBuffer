package eu.menzani.ringbuffer;

class ProfilerGroup implements Measure {
    private String prefix;
    private long executionTime;
    private int size;

    void add(Profiler profiler) {
        if (prefix == null) {
            prefix = profiler.getPrefix();
        } else if (!prefix.equals(profiler.getPrefix())) {
            throw new IllegalArgumentException("All profilers must have the same prefix.");
        }
        executionTime += profiler.getExecutionTime();
        size++;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public long getExecutionTime() {
        return executionTime / size;
    }

    int size() {
        return size;
    }
}
