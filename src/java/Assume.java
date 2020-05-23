package eu.menzani.ringbuffer.java;

public class Assume {
    public static void lesser(int value, int cap) {
        if (value >= cap) {
            throw new IllegalArgumentException(value + " >= " + cap);
        }
    }

    public static void notLesser(int value, int cap) {
        if (value < cap) {
            throw new IllegalArgumentException(value + " < " + cap);
        }
    }

    public static void notLesser(long value, long cap) {
        if (value < cap) {
            throw new IllegalArgumentException(value + " < " + cap);
        }
    }

    public static void notGreater(int value, int cap) {
        if (value > cap) {
            throw new IllegalArgumentException(value + " > " + cap);
        }
    }

    public static void notNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(value + " < 0");
        }
    }

    public static void notNegative(long value) {
        if (value < 0L) {
            throw new IllegalArgumentException(value + " < 0");
        }
    }

    public static void notZero(int value) {
        if (value == 0) {
            throw new IllegalArgumentException();
        }
    }
}
