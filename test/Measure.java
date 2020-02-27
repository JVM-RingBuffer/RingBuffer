package eu.menzani.ringbuffer;

public interface Measure {
    String getPrefix();

    long getExecutionTime();

    default void report() {
        long executionTime = getExecutionTime();
        if (executionTime < 2_000L) {
            System.out.println(getPrefix() + executionTime + "ns");
        } else if (executionTime < 2_000_000L) {
            System.out.println(getPrefix() + (executionTime / 1_000L) + "us");
        } else {
            System.out.println(getPrefix() + (executionTime / 1_000_000L) + "ms");
        }
    }
}
