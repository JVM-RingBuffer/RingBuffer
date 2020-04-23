package eu.menzani.ringbuffer.java;

public class Int {
    public static int ceilDiv(int dividend, int divisor) {
        return (dividend - 1) / divisor + 1;
    }

    public static int getNextPowerOfTwo(int value) {
        int highestOneBit = Integer.highestOneBit(value);
        if (value == highestOneBit) {
            return value;
        }
        return highestOneBit << 1;
    }
}
