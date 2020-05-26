package eu.menzani.ringbuffer.concurrent;

import eu.menzani.ringbuffer.java.Assume;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class AtomicIntArray {
    private static final VarHandle VALUE = MethodHandles.arrayElementVarHandle(int[].class);

    private final int[] value;

    public AtomicIntArray(int length) {
        Assume.notLesser(length, 1);
        value = new int[length];
    }

    public AtomicIntArray(int[] value) {
        Assume.notLesser(value.length, 1);
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public void setPlain(int index, int value) {
        this.value[index] = value;
    }

    public void setOpaque(int index, int value) {
        VALUE.setOpaque(this.value, index, value);
    }

    public void setRelease(int index, int value) {
        VALUE.setRelease(this.value, index, value);
    }

    public void setVolatile(int index, int value) {
        VALUE.setVolatile(this.value, index, value);
    }

    public int getPlain(int index) {
        return value[index];
    }

    public int getOpaque(int index) {
        return (int) VALUE.getOpaque(value, index);
    }

    public int getAcquire(int index) {
        return (int) VALUE.getAcquire(value, index);
    }

    public int getVolatile(int index) {
        return (int) VALUE.getVolatile(value, index);
    }

    public boolean compareAndSetVolatile(int index, int oldValue, int newValue) {
        return VALUE.compareAndSet(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int index, int oldValue, int newValue) {
        return VALUE.weakCompareAndSetPlain(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int index, int oldValue, int newValue) {
        return VALUE.weakCompareAndSetRelease(value, index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int index, int oldValue, int newValue) {
        return VALUE.weakCompareAndSetAcquire(value, index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int index, int oldValue, int newValue) {
        return VALUE.weakCompareAndSet(value, index, oldValue, newValue);
    }

    public int getPlainAndIncrementRelease(int index) {
        return getPlainAndAddRelease(index, 1);
    }

    public int getAcquireAndIncrementPlain(int index) {
        return getAcquireAndAddPlain(index, 1);
    }

    public int getAndIncrementVolatile(int index) {
        return getAndAddVolatile(index, 1);
    }

    public int getPlainAndDecrementRelease(int index) {
        return getPlainAndAddRelease(index, -1);
    }

    public int getAcquireAndDecrementPlain(int index) {
        return getAcquireAndAddPlain(index, -1);
    }

    public int getAndDecrementVolatile(int index) {
        return getAndAddVolatile(index, -1);
    }

    public int incrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, 1);
    }

    public int incrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, 1);
    }

    public int incrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, 1);
    }

    public void incrementVolatile(int index) {
        addVolatile(index, 1);
    }

    public void incrementPlainRelease(int index) {
        addPlainRelease(index, 1);
    }

    public void incrementAcquirePlain(int index) {
        addAcquirePlain(index, 1);
    }

    public void incrementPlain(int index) {
        value[index]++;
    }

    public int decrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, -1);
    }

    public int decrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, -1);
    }

    public int decrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, -1);
    }

    public void decrementVolatile(int index) {
        addVolatile(index, -1);
    }

    public void decrementPlainRelease(int index) {
        addPlainRelease(index, -1);
    }

    public void decrementAcquirePlain(int index) {
        addAcquirePlain(index, -1);
    }

    public void decrementPlain(int index) {
        value[index]--;
    }

    public int getPlainAndAddRelease(int index, int value) {
        return (int) VALUE.getAndAddRelease(this.value, index, value);
    }

    public int getAcquireAndAddPlain(int index, int value) {
        return (int) VALUE.getAndAddAcquire(this.value, index, value);
    }

    public int getAndAddVolatile(int index, int value) {
        return (int) VALUE.getAndAdd(this.value, index, value);
    }

    public int addReleaseAndGetPlain(int index, int value) {
        return (int) VALUE.getAndAddRelease(this.value, index, value) + value;
    }

    public int addPlainAndGetAcquire(int index, int value) {
        return (int) VALUE.getAndAddAcquire(this.value, index, value) + value;
    }

    public int addAndGetVolatile(int index, int value) {
        return (int) VALUE.getAndAdd(this.value, index, value) + value;
    }

    public void addVolatile(int index, int value) {
        VALUE.getAndAdd(this.value, index, value);
    }

    public void addPlainRelease(int index, int value) {
        VALUE.getAndAddRelease(this.value, index, value);
    }

    public void addAcquirePlain(int index, int value) {
        VALUE.getAndAddAcquire(this.value, index, value);
    }

    public void addPlain(int index, int value) {
        this.value[index] += value;
    }

    public int getPlainAndSetRelease(int index, int value) {
        return (int) VALUE.getAndSetRelease(this.value, index, value);
    }

    public int getAcquireAndSetPlain(int index, int value) {
        return (int) VALUE.getAndSetAcquire(this.value, index, value);
    }

    public int getAndSetVolatile(int index, int value) {
        return (int) VALUE.getAndSet(this.value, index, value);
    }

    public int comparePlainAndExchangeRelease(int index, int oldValue, int newValue) {
        return (int) VALUE.compareAndExchangeRelease(value, index, oldValue, newValue);
    }

    public int compareAcquireAndExchangePlain(int index, int oldValue, int newValue) {
        return (int) VALUE.compareAndExchangeAcquire(value, index, oldValue, newValue);
    }

    public int compareAndExchangeVolatile(int index, int oldValue, int newValue) {
        return (int) VALUE.compareAndExchange(value, index, oldValue, newValue);
    }

    public int getPlainAndBitwiseAndRelease(int index, int mask) {
        return (int) VALUE.getAndBitwiseAndRelease(value, index, mask);
    }

    public int getAcquireAndBitwiseAndPlain(int index, int mask) {
        return (int) VALUE.getAndBitwiseAndAcquire(value, index, mask);
    }

    public int getAndBitwiseAndVolatile(int index, int mask) {
        return (int) VALUE.getAndBitwiseAnd(value, index, mask);
    }

    public int getPlainAndBitwiseOrRelease(int index, int mask) {
        return (int) VALUE.getAndBitwiseOrRelease(value, index, mask);
    }

    public int getAcquireAndBitwiseOrPlain(int index, int mask) {
        return (int) VALUE.getAndBitwiseOrAcquire(value, index, mask);
    }

    public int getAndBitwiseOrVolatile(int index, int mask) {
        return (int) VALUE.getAndBitwiseOr(value, index, mask);
    }

    public int getPlainAndBitwiseXorRelease(int index, int mask) {
        return (int) VALUE.getAndBitwiseXorRelease(value, index, mask);
    }

    public int getAcquireAndBitwiseXorPlain(int index, int mask) {
        return (int) VALUE.getAndBitwiseXorAcquire(value, index, mask);
    }

    public int getAndBitwiseXorVolatile(int index, int mask) {
        return (int) VALUE.getAndBitwiseXor(value, index, mask);
    }

    public int getAndUpdate(int index, IntUnaryOperator updateFunction) {
        int prev = getVolatile(index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public int updateAndGet(int index, IntUnaryOperator updateFunction) {
        int prev = getVolatile(index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public int getAndAccumulate(int index, int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public int accumulateAndGet(int index, int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
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
