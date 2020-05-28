package org.ringbuffer.concurrent;

import org.ringbuffer.java.Assume;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public class AtomicLongArray {
    private static final VarHandle VALUE = MethodHandles.arrayElementVarHandle(long[].class);

    private final long[] value;

    public AtomicLongArray(int length) {
        Assume.notLesser(length, 1);
        value = new long[length];
    }

    public AtomicLongArray(long[] value) {
        Assume.notLesser(value.length, 1);
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public void setPlain(int index, long value) {
        this.value[index] = value;
    }

    public void setOpaque(int index, long value) {
        VALUE.setOpaque(this.value, index, value);
    }

    public void setRelease(int index, long value) {
        VALUE.setRelease(this.value, index, value);
    }

    public void setVolatile(int index, long value) {
        VALUE.setVolatile(this.value, index, value);
    }

    public long getPlain(int index) {
        return value[index];
    }

    public long getOpaque(int index) {
        return (long) VALUE.getOpaque(value, index);
    }

    public long getAcquire(int index) {
        return (long) VALUE.getAcquire(value, index);
    }

    public long getVolatile(int index) {
        return (long) VALUE.getVolatile(value, index);
    }

    public boolean compareAndSetVolatile(int index, long oldValue, long newValue) {
        return VALUE.compareAndSet(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int index, long oldValue, long newValue) {
        return VALUE.weakCompareAndSetPlain(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int index, long oldValue, long newValue) {
        return VALUE.weakCompareAndSetRelease(value, index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int index, long oldValue, long newValue) {
        return VALUE.weakCompareAndSetAcquire(value, index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int index, long oldValue, long newValue) {
        return VALUE.weakCompareAndSet(value, index, oldValue, newValue);
    }

    public long getPlainAndIncrementRelease(int index) {
        return getPlainAndAddRelease(index, 1L);
    }

    public long getAcquireAndIncrementPlain(int index) {
        return getAcquireAndAddPlain(index, 1L);
    }

    public long getAndIncrementVolatile(int index) {
        return getAndAddVolatile(index, 1L);
    }

    public long getPlainAndDecrementRelease(int index) {
        return getPlainAndAddRelease(index, -1L);
    }

    public long getAcquireAndDecrementPlain(int index) {
        return getAcquireAndAddPlain(index, -1L);
    }

    public long getAndDecrementVolatile(int index) {
        return getAndAddVolatile(index, -1L);
    }

    public long incrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, 1L);
    }

    public long incrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, 1L);
    }

    public long incrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, 1L);
    }

    public void incrementVolatile(int index) {
        addVolatile(index, 1L);
    }

    public void incrementPlainRelease(int index) {
        addPlainRelease(index, 1L);
    }

    public void incrementAcquirePlain(int index) {
        addAcquirePlain(index, 1L);
    }

    public void incrementPlain(int index) {
        value[index]++;
    }

    public long decrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, -1L);
    }

    public long decrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, -1L);
    }

    public long decrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, -1L);
    }

    public void decrementVolatile(int index) {
        addVolatile(index, -1L);
    }

    public void decrementPlainRelease(int index) {
        addPlainRelease(index, -1L);
    }

    public void decrementAcquirePlain(int index) {
        addAcquirePlain(index, -1L);
    }

    public void decrementPlain(int index) {
        value[index]--;
    }

    public long getPlainAndAddRelease(int index, long value) {
        return (long) VALUE.getAndAddRelease(this.value, index, value);
    }

    public long getAcquireAndAddPlain(int index, long value) {
        return (long) VALUE.getAndAddAcquire(this.value, index, value);
    }

    public long getAndAddVolatile(int index, long value) {
        return (long) VALUE.getAndAdd(this.value, index, value);
    }

    public long addReleaseAndGetPlain(int index, long value) {
        return (long) VALUE.getAndAddRelease(this.value, index, value) + value;
    }

    public long addPlainAndGetAcquire(int index, long value) {
        return (long) VALUE.getAndAddAcquire(this.value, index, value) + value;
    }

    public long addAndGetVolatile(int index, long value) {
        return (long) VALUE.getAndAdd(this.value, index, value) + value;
    }

    public void addVolatile(int index, long value) {
        VALUE.getAndAdd(this.value, index, value);
    }

    public void addPlainRelease(int index, long value) {
        VALUE.getAndAddRelease(this.value, index, value);
    }

    public void addAcquirePlain(int index, long value) {
        VALUE.getAndAddAcquire(this.value, index, value);
    }

    public void addPlain(int index, long value) {
        this.value[index] += value;
    }

    public long getPlainAndSetRelease(int index, long value) {
        return (long) VALUE.getAndSetRelease(this.value, index, value);
    }

    public long getAcquireAndSetPlain(int index, long value) {
        return (long) VALUE.getAndSetAcquire(this.value, index, value);
    }

    public long getAndSetVolatile(int index, long value) {
        return (long) VALUE.getAndSet(this.value, index, value);
    }

    public long comparePlainAndExchangeRelease(int index, long oldValue, long newValue) {
        return (long) VALUE.compareAndExchangeRelease(value, index, oldValue, newValue);
    }

    public long compareAcquireAndExchangePlain(int index, long oldValue, long newValue) {
        return (long) VALUE.compareAndExchangeAcquire(value, index, oldValue, newValue);
    }

    public long compareAndExchangeVolatile(int index, long oldValue, long newValue) {
        return (long) VALUE.compareAndExchange(value, index, oldValue, newValue);
    }

    public long getPlainAndBitwiseAndRelease(int index, long mask) {
        return (long) VALUE.getAndBitwiseAndRelease(value, index, mask);
    }

    public long getAcquireAndBitwiseAndPlain(int index, long mask) {
        return (long) VALUE.getAndBitwiseAndAcquire(value, index, mask);
    }

    public long getAndBitwiseAndVolatile(int index, long mask) {
        return (long) VALUE.getAndBitwiseAnd(value, index, mask);
    }

    public long getPlainAndBitwiseOrRelease(int index, long mask) {
        return (long) VALUE.getAndBitwiseOrRelease(value, index, mask);
    }

    public long getAcquireAndBitwiseOrPlain(int index, long mask) {
        return (long) VALUE.getAndBitwiseOrAcquire(value, index, mask);
    }

    public long getAndBitwiseOrVolatile(int index, long mask) {
        return (long) VALUE.getAndBitwiseOr(value, index, mask);
    }

    public long getPlainAndBitwiseXorRelease(int index, long mask) {
        return (long) VALUE.getAndBitwiseXorRelease(value, index, mask);
    }

    public long getAcquireAndBitwiseXorPlain(int index, long mask) {
        return (long) VALUE.getAndBitwiseXorAcquire(value, index, mask);
    }

    public long getAndBitwiseXorVolatile(int index, long mask) {
        return (long) VALUE.getAndBitwiseXor(value, index, mask);
    }

    public long getAndUpdate(int index, LongUnaryOperator updateFunction) {
        long prev = getVolatile(index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public long updateAndGet(int index, LongUnaryOperator updateFunction) {
        long prev = getVolatile(index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public long getAndAccumulate(int index, long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public long accumulateAndGet(int index, long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
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
