package eu.menzani.ringbuffer.java;

public class Assume {
    public static void notEqual(int left, int right) {
        if (left == right) {
            throw new IllegalIntArgumentException(left);
        }
    }

    public static void notEqual(long left, long right) {
        if (left == right) {
            throw new IllegalLongArgumentException(left);
        }
    }

    public static void lesser(int value, int cap) {
        if (value >= cap) {
            throw new IllegalIntArgumentException(value, " >= ", cap);
        }
    }

    public static void lesser(long value, long cap) {
        if (value >= cap) {
            throw new IllegalLongArgumentException(value, " >= ", cap);
        }
    }

    public static void notLesser(int value, int cap) {
        if (value < cap) {
            throw new IllegalIntArgumentException(value, " < ", cap);
        }
    }

    public static void notLesser(long value, long cap) {
        if (value < cap) {
            throw new IllegalLongArgumentException(value, " < ", cap);
        }
    }

    public static void greater(int value, int cap) {
        if (value <= cap) {
            throw new IllegalIntArgumentException(value, " <= ", cap);
        }
    }

    public static void greater(long value, long cap) {
        if (value <= cap) {
            throw new IllegalLongArgumentException(value, " <= ", cap);
        }
    }

    public static void notGreater(int value, int cap) {
        if (value > cap) {
            throw new IllegalIntArgumentException(value, " > ", cap);
        }
    }

    public static void notGreater(long value, long cap) {
        if (value > cap) {
            throw new IllegalLongArgumentException(value, " > ", cap);
        }
    }

    public static void positive(int value) {
        if (value <= 0) {
            throw new IllegalIntArgumentException(value);
        }
    }

    public static void positive(long value) {
        if (value <= 0L) {
            throw new IllegalLongArgumentException(value);
        }
    }

    public static void notPositive(int value) {
        if (value > 0) {
            throw new IllegalIntArgumentException(value);
        }
    }

    public static void notPositive(long value) {
        if (value > 0L) {
            throw new IllegalLongArgumentException(value);
        }
    }

    public static void negative(int value) {
        if (value >= 0) {
            throw new IllegalIntArgumentException(value);
        }
    }

    public static void negative(long value) {
        if (value >= 0L) {
            throw new IllegalLongArgumentException(value);
        }
    }

    public static void notNegative(int value) {
        if (value < 0) {
            throw new IllegalIntArgumentException(value);
        }
    }

    public static void notNegative(long value) {
        if (value < 0L) {
            throw new IllegalLongArgumentException(value);
        }
    }

    public static void notZero(int value) {
        if (value == 0) {
            throw new IllegalArgumentException();
        }
    }

    public static void notZero(long value) {
        if (value == 0L) {
            throw new IllegalArgumentException();
        }
    }

    public static void notNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
    }
}
