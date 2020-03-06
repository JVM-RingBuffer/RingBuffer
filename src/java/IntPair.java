package eu.menzani.ringbuffer.java;

public class IntPair {
    public static long of(int first, int second) {
        return (long) second << 32 | first & 0xFFFFFFFFL;
    }

    public static int getFirst(long intPair) {
        return (int) intPair;
    }

    public static int getSecond(long intPair) {
        return (int) (intPair >> 32);
    }
}
