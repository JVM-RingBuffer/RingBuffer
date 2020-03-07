package eu.menzani.ringbuffer.java;

public class Assert {
    public static void equal(int left, int right) {
        assert left == right : left + " != " + right;
    }

    public static void notGreater(int value, int cap) {
        assert value <= cap : value + " > " + cap;
    }
}
