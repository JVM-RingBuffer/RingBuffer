package org.ringbuffer.concurrent;

import org.ringbuffer.java.Assume;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicBooleanArray {
    private static final VarHandle VALUE = MethodHandles.arrayElementVarHandle(boolean[].class);

    private final boolean[] value;

    public AtomicBooleanArray(int length) {
        Assume.notLesser(length, 1);
        value = new boolean[length];
    }

    public AtomicBooleanArray(boolean[] value) {
        Assume.notLesser(value.length, 1);
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public void setPlain(int index, boolean value) {
        this.value[index] = value;
    }

    public void setOpaque(int index, boolean value) {
        VALUE.setOpaque(this.value, index, value);
    }

    public void setRelease(int index, boolean value) {
        VALUE.setRelease(this.value, index, value);
    }

    public void setVolatile(int index, boolean value) {
        VALUE.setVolatile(this.value, index, value);
    }

    public boolean getPlain(int index) {
        return value[index];
    }

    public boolean getOpaque(int index) {
        return (boolean) VALUE.getOpaque(value, index);
    }

    public boolean getAcquire(int index) {
        return (boolean) VALUE.getAcquire(value, index);
    }

    public boolean getVolatile(int index) {
        return (boolean) VALUE.getVolatile(value, index);
    }

    public boolean compareAndSetVolatile(int index, boolean oldValue, boolean newValue) {
        return VALUE.compareAndSet(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int index, boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSetPlain(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int index, boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSetRelease(value, index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int index, boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSetAcquire(value, index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int index, boolean oldValue, boolean newValue) {
        return VALUE.weakCompareAndSet(value, index, oldValue, newValue);
    }

    public boolean getPlainAndSetRelease(int index, boolean value) {
        return (boolean) VALUE.getAndSetRelease(this.value, index, value);
    }

    public boolean getAcquireAndSetPlain(int index, boolean value) {
        return (boolean) VALUE.getAndSetAcquire(this.value, index, value);
    }

    public boolean getAndSetVolatile(int index, boolean value) {
        return (boolean) VALUE.getAndSet(this.value, index, value);
    }

    public boolean comparePlainAndExchangeRelease(int index, boolean oldValue, boolean newValue) {
        return (boolean) VALUE.compareAndExchangeRelease(value, index, oldValue, newValue);
    }

    public boolean compareAcquireAndExchangePlain(int index, boolean oldValue, boolean newValue) {
        return (boolean) VALUE.compareAndExchangeAcquire(value, index, oldValue, newValue);
    }

    public boolean compareAndExchangeVolatile(int index, boolean oldValue, boolean newValue) {
        return (boolean) VALUE.compareAndExchange(value, index, oldValue, newValue);
    }

    public boolean getPlainAndBitwiseAndRelease(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseAndRelease(value, index, mask);
    }

    public boolean getAcquireAndBitwiseAndPlain(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseAndAcquire(value, index, mask);
    }

    public boolean getAndBitwiseAndVolatile(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseAnd(value, index, mask);
    }

    public boolean getPlainAndBitwiseOrRelease(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseOrRelease(value, index, mask);
    }

    public boolean getAcquireAndBitwiseOrPlain(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseOrAcquire(value, index, mask);
    }

    public boolean getAndBitwiseOrVolatile(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseOr(value, index, mask);
    }

    public boolean getPlainAndBitwiseXorRelease(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseXorRelease(value, index, mask);
    }

    public boolean getAcquireAndBitwiseXorPlain(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseXorAcquire(value, index, mask);
    }

    public boolean getAndBitwiseXorVolatile(int index, boolean mask) {
        return (boolean) VALUE.getAndBitwiseXor(value, index, mask);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0, iMax = value.length - 1; ; i++) {
            builder.append(getVolatile(i));
            if (i == iMax) {
                builder.append(']');
                return builder.toString();
            }
            builder.append(", ");
        }
    }
}
