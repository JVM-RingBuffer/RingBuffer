package eu.menzani.ringbuffer.java;

public class Ensure {
    public static void notGreater(int value, int cap) {
        if (value > cap) {
            throw new IllegalStateException(value + " > " + cap);
        }
    }
}
