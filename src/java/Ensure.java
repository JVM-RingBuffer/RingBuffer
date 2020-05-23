package eu.menzani.ringbuffer.java;

public class Ensure {
    public static void notGreater(int value, int cap) {
        if (value > cap) {
            throw new IllegalStateException(value + " > " + cap);
        }
    }

    public static void notZero(int value) {
        if (value == 0) {
            throw new IllegalStateException();
        }
    }
}
