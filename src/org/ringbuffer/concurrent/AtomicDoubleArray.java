/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ringbuffer.concurrent;

import org.ringbuffer.java.Assume;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class AtomicDoubleArray {
    private static final VarHandle VALUE = MethodHandles.arrayElementVarHandle(double[].class);

    private final double[] value;

    public AtomicDoubleArray(int length) {
        Assume.notLesser(length, 1);
        value = new double[length];
    }

    public AtomicDoubleArray(double[] value) {
        Assume.notLesser(value.length, 1);
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public void setPlain(int index, double value) {
        this.value[index] = value;
    }

    public void setOpaque(int index, double value) {
        VALUE.setOpaque(this.value, index, value);
    }

    public void setRelease(int index, double value) {
        VALUE.setRelease(this.value, index, value);
    }

    public void setVolatile(int index, double value) {
        VALUE.setVolatile(this.value, index, value);
    }

    public double getPlain(int index) {
        return value[index];
    }

    public double getOpaque(int index) {
        return (double) VALUE.getOpaque(value, index);
    }

    public double getAcquire(int index) {
        return (double) VALUE.getAcquire(value, index);
    }

    public double getVolatile(int index) {
        return (double) VALUE.getVolatile(value, index);
    }

    public boolean compareAndSetVolatile(int index, double oldValue, double newValue) {
        return VALUE.compareAndSet(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int index, double oldValue, double newValue) {
        return VALUE.weakCompareAndSetPlain(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int index, double oldValue, double newValue) {
        return VALUE.weakCompareAndSetRelease(value, index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int index, double oldValue, double newValue) {
        return VALUE.weakCompareAndSetAcquire(value, index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int index, double oldValue, double newValue) {
        return VALUE.weakCompareAndSet(value, index, oldValue, newValue);
    }

    public double getPlainAndIncrementRelease(int index) {
        return getPlainAndAddRelease(index, 1D);
    }

    public double getAcquireAndIncrementPlain(int index) {
        return getAcquireAndAddPlain(index, 1D);
    }

    public double getAndIncrementVolatile(int index) {
        return getAndAddVolatile(index, 1D);
    }

    public double getPlainAndDecrementRelease(int index) {
        return getPlainAndAddRelease(index, -1D);
    }

    public double getAcquireAndDecrementPlain(int index) {
        return getAcquireAndAddPlain(index, -1D);
    }

    public double getAndDecrementVolatile(int index) {
        return getAndAddVolatile(index, -1D);
    }

    public double incrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, 1D);
    }

    public double incrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, 1D);
    }

    public double incrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, 1D);
    }

    public void incrementVolatile(int index) {
        addVolatile(index, 1D);
    }

    public void incrementPlainRelease(int index) {
        addPlainRelease(index, 1D);
    }

    public void incrementAcquirePlain(int index) {
        addAcquirePlain(index, 1D);
    }

    public void incrementPlain(int index) {
        value[index]++;
    }

    public double decrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, -1D);
    }

    public double decrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, -1D);
    }

    public double decrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, -1D);
    }

    public void decrementVolatile(int index) {
        addVolatile(index, -1D);
    }

    public void decrementPlainRelease(int index) {
        addPlainRelease(index, -1D);
    }

    public void decrementAcquirePlain(int index) {
        addAcquirePlain(index, -1D);
    }

    public void decrementPlain(int index) {
        value[index]--;
    }

    public double getPlainAndAddRelease(int index, double value) {
        return (double) VALUE.getAndAddRelease(this.value, index, value);
    }

    public double getAcquireAndAddPlain(int index, double value) {
        return (double) VALUE.getAndAddAcquire(this.value, index, value);
    }

    public double getAndAddVolatile(int index, double value) {
        return (double) VALUE.getAndAdd(this.value, index, value);
    }

    public double addReleaseAndGetPlain(int index, double value) {
        return (double) VALUE.getAndAddRelease(this.value, index, value) + value;
    }

    public double addPlainAndGetAcquire(int index, double value) {
        return (double) VALUE.getAndAddAcquire(this.value, index, value) + value;
    }

    public double addAndGetVolatile(int index, double value) {
        return (double) VALUE.getAndAdd(this.value, index, value) + value;
    }

    public void addVolatile(int index, double value) {
        VALUE.getAndAdd(this.value, index, value);
    }

    public void addPlainRelease(int index, double value) {
        VALUE.getAndAddRelease(this.value, index, value);
    }

    public void addAcquirePlain(int index, double value) {
        VALUE.getAndAddAcquire(this.value, index, value);
    }

    public void addPlain(int index, double value) {
        this.value[index] += value;
    }

    public double getPlainAndSetRelease(int index, double value) {
        return (double) VALUE.getAndSetRelease(this.value, index, value);
    }

    public double getAcquireAndSetPlain(int index, double value) {
        return (double) VALUE.getAndSetAcquire(this.value, index, value);
    }

    public double getAndSetVolatile(int index, double value) {
        return (double) VALUE.getAndSet(this.value, index, value);
    }

    public double comparePlainAndExchangeRelease(int index, double oldValue, double newValue) {
        return (double) VALUE.compareAndExchangeRelease(value, index, oldValue, newValue);
    }

    public double compareAcquireAndExchangePlain(int index, double oldValue, double newValue) {
        return (double) VALUE.compareAndExchangeAcquire(value, index, oldValue, newValue);
    }

    public double compareAndExchangeVolatile(int index, double oldValue, double newValue) {
        return (double) VALUE.compareAndExchange(value, index, oldValue, newValue);
    }

    public double getPlainAndBitwiseAndRelease(int index, double mask) {
        return (double) VALUE.getAndBitwiseAndRelease(value, index, mask);
    }

    public double getAcquireAndBitwiseAndPlain(int index, double mask) {
        return (double) VALUE.getAndBitwiseAndAcquire(value, index, mask);
    }

    public double getAndBitwiseAndVolatile(int index, double mask) {
        return (double) VALUE.getAndBitwiseAnd(value, index, mask);
    }

    public double getPlainAndBitwiseOrRelease(int index, double mask) {
        return (double) VALUE.getAndBitwiseOrRelease(value, index, mask);
    }

    public double getAcquireAndBitwiseOrPlain(int index, double mask) {
        return (double) VALUE.getAndBitwiseOrAcquire(value, index, mask);
    }

    public double getAndBitwiseOrVolatile(int index, double mask) {
        return (double) VALUE.getAndBitwiseOr(value, index, mask);
    }

    public double getPlainAndBitwiseXorRelease(int index, double mask) {
        return (double) VALUE.getAndBitwiseXorRelease(value, index, mask);
    }

    public double getAcquireAndBitwiseXorPlain(int index, double mask) {
        return (double) VALUE.getAndBitwiseXorAcquire(value, index, mask);
    }

    public double getAndBitwiseXorVolatile(int index, double mask) {
        return (double) VALUE.getAndBitwiseXor(value, index, mask);
    }

    public double getAndUpdate(int index, DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public double updateAndGet(int index, DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public double getAndAccumulate(int index, double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public double accumulateAndGet(int index, double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public void fill(double value) {
        for (int i = 0; i < this.value.length; i++) {
            this.value[i] = value;
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
