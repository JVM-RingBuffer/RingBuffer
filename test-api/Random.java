package eu.menzani.ringbuffer;

public class Random extends java.util.Random {
    public static final Random INSTANCE = new Random();

    public byte nextByte() {
        return (byte) next(8);
    }

    public char nextChar() {
        return (char) (nextInt(26) + 97);
    }

    public short nextShort() {
        return (short) next(16);
    }

    public float nextFloat() {
        return super.nextFloat() * Float.MAX_VALUE;
    }

    public double nextDouble() {
        return super.nextDouble() * Double.MAX_VALUE;
    }
}
