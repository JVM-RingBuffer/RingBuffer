package eu.menzani.ringbuffer.java;

public class MutableInt extends Number implements Cloneable {
    private static final long serialVersionUID = 0L;

    private int value;

    public MutableInt() {}

    public MutableInt(int value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public int incrementAndGet() {
        return ++value;
    }

    public int getAndIncrement() {
        return value++;
    }

    public void decrement() {
        value--;
    }

    public int decrementAndGet() {
        return --value;
    }

    public int getAndDecrement() {
        return value--;
    }

    public void add(int value) {
        this.value += value;
    }

    public void subtract(int value) {
        this.value -= value;
    }

    public void multiply(int value) {
        this.value *= value;
    }

    public void divide(int value) {
        this.value /= value;
    }

    /**
     * Both numbers must not be negative.
     */
    public void ceilDiv(int value) {
        this.value = ceilDiv(this.value, value);
    }

    public void modulus(int value) {
        this.value %= value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void set(float value) {
        this.value = (int) value;
    }

    public void set(double value) {
        this.value = (int) value;
    }

    public int get() {
        return value;
    }

    public boolean isPositive() {
        return value > 0;
    }

    public boolean isNotNegative() {
        return value >= 0;
    }

    public boolean isNegative() {
        return value < 0;
    }

    public boolean isNotPositive() {
        return value <= 0;
    }

    public boolean isZero() {
        return value == 0;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public byte byteValue() {
        return (byte) value;
    }

    @Override
    public short shortValue() {
        return (short) value;
    }

    @Override
    public MutableInt clone() {
        try {
            return (MutableInt) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        return value == ((MutableInt) object).value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /**
     * Both numbers must not be negative.
     */
    public static int ceilDiv(int dividend, int divisor) {
        return (dividend - 1) / divisor + 1;
    }
}
