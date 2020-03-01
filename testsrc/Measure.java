package eu.menzani.ringbuffer;

public interface Measure {
    String getPrefix();

    long getExecutionTime();

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
