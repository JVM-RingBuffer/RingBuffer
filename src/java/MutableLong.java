package eu.menzani.ringbuffer.java;

public class MutableLong extends Number {
    private long value;

    public void increment() {
        value++;
    }

    public int incrementAndGetInt() {
        return (int) ++value;
    }

    public long incrementAndGet() {
        return ++value;
    }

    public int getIntAndIncrement() {
        return (int) value++;
    }

    public long getAndIncrement() {
        return value++;
    }

    public void decrement() {
        value--;
    }

    public int decrementAndGetInt() {
        return (int) --value;
    }

    public long decrementAndGet() {
        return --value;
    }

    public int getIntAndDecrement() {
        return (int) value--;
    }

    public long getAndDecrement() {
        return value--;
    }

    public void add(long value) {
        this.value += value;
    }

    public void subtract(long value) {
        this.value -= value;
    }

    public void multiply(long value) {
        this.value *= value;
    }

    public void divide(long value) {
        this.value /= value;
    }

    public void modulus(long value) {
        this.value %= value;
    }

    public void set(long value) {
        this.value = value;
    }

    public void set(float value) {
        this.value = (long) value;
    }

    public void set(double value) {
        this.value = (long) value;
    }

    public long get() {
        return value;
    }

    @Override
    public int intValue() {
        return (int) value;
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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        return value == ((MutableLong) object).value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
