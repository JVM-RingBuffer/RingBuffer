package eu.menzani.ringbuffer;

public class IntPair {
    public static long of(int first, int second) {
        return (long) first << 32 | second & 0xFFFFFFFFL;
    }

    public static int getFirst(long intPair) {
        return (int) (intPair >> 32);
    }

    public static int getSecond(long intPair) {
        return (int) intPair;
    }
}
