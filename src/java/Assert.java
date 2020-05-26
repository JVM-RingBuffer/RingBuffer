package eu.menzani.ringbuffer.java;

public class Assert {
    public static void equal(int left, int right) {
        assert left == right : left + " != " + right;
    }

    public static void equal(long left, long right) {
        assert left == right : left + " != " + right;
    }

    public static void notEqual(int left, int right) {
        assert left != right : left;
    }

    public static void notEqual(long left, long right) {
        assert left != right : left;
    }

    public static void lesser(int value, int cap) {
        assert value < cap : value + " >= " + cap;
    }

    public static void lesser(long value, long cap) {
        assert value < cap : value + " >= " + cap;
    }

    public static void notLesser(int value, int cap) {
        assert value >= cap : value + " < " + cap;
    }

    public static void notLesser(long value, long cap) {
        assert value >= cap : value + " < " + cap;
    }

    public static void greater(int value, int cap) {
        assert value > cap : value + " <= " + cap;
    }

    public static void greater(long value, long cap) {
        assert value > cap : value + " <= " + cap;
    }

    public static void notGreater(int value, int cap) {
        assert value <= cap : value + " > " + cap;
    }

    public static void notGreater(long value, long cap) {
        assert value <= cap : value + " > " + cap;
    }

    public static void positive(int value) {
        assert value > 0 : value;
    }

    public static void positive(long value) {
        assert value > 0L : value;
    }

    public static void notPositive(int value) {
        assert value <= 0 : value;
    }

    public static void notPositive(long value) {
        assert value <= 0L : value;
    }

    public static void negative(int value) {
        assert value < 0 : value;
    }

    public static void negative(long value) {
        assert value < 0L : value;
    }

    public static void notNegative(int value) {
        assert value >= 0 : value;
    }

    public static void notNegative(long value) {
        assert value >= 0L : value;
    }
}
