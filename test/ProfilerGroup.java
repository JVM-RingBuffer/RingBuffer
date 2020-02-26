package eu.menzani.ringbuffer;

import java.util.HashSet;
import java.util.Set;

class ProfilerGroup {
    private final Set<Profiler> profilers = new HashSet<>();
    private String prefix;

    int getSize() {
        return profilers.size();
    }

    void add(Profiler profiler) {
        if (prefix == null) {
            prefix = profiler.getPrefix();
        } else if (!prefix.equals(profiler.getPrefix())) {
            throw new IllegalArgumentException("All profilers must be of the same method.");
        }
        profilers.add(profiler);
    }

    void report() {
        long executionTime = profilers.stream().mapToLong(Profiler::getExecutionTime).sum() / profilers.size();
        Profiler.report(prefix, executionTime);
    }
}
