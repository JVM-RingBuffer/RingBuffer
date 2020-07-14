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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class AtomicDouble {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(AtomicDouble.class, "value", double.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private double value;

    public AtomicDouble() {}

    public AtomicDouble(double value) {
        this.value = value;
    }

    public void setPlain(double value) {
        this.value = value;
    }

    public void setOpaque(double value) {
        VALUE.setOpaque(this, value);
    }

    public void setRelease(double value) {
        VALUE.setRelease(this, value);
    }

    public void setVolatile(double value) {
        VALUE.setVolatile(this, value);
    }

    public double getPlain() {
        return value;
    }

    public double getOpaque() {
        return (double) VALUE.getOpaque(this);
    }

    public double getAcquire() {
        return (double) VALUE.getAcquire(this);
    }

    public double getVolatile() {
        return (double) VALUE.getVolatile(this);
    }

    public boolean compareAndSetVolatile(double oldValue, double newValue) {
        return VALUE.compareAndSet(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(double oldValue, double newValue) {
        return VALUE.weakCompareAndSetPlain(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(double oldValue, double newValue) {
        return VALUE.weakCompareAndSetRelease(this, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(double oldValue, double newValue) {
        return VALUE.weakCompareAndSetAcquire(this, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(double oldValue, double newValue) {
        return VALUE.weakCompareAndSet(this, oldValue, newValue);
    }

    public double getPlainAndIncrementRelease() {
        return getPlainAndAddRelease(1D);
    }

    public double getAcquireAndIncrementPlain() {
        return getAcquireAndAddPlain(1D);
    }

    public double getAndIncrementVolatile() {
        return getAndAddVolatile(1D);
    }

    public double getPlainAndDecrementRelease() {
        return getPlainAndAddRelease(-1D);
    }

    public double getAcquireAndDecrementPlain() {
        return getAcquireAndAddPlain(-1D);
    }

    public double getAndDecrementVolatile() {
        return getAndAddVolatile(-1D);
    }

    public double incrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(1D);
    }

    public double incrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(1D);
    }

    public double incrementAndGetVolatile() {
        return addAndGetVolatile(1D);
    }

    public void incrementVolatile() {
        addVolatile(1D);
    }

    public void incrementPlainRelease() {
        addPlainRelease(1D);
    }

    public void incrementAcquirePlain() {
        addAcquirePlain(1D);
    }

    public void incrementPlain() {
        value++;
    }

    public double decrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(-1D);
    }

    public double decrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(-1D);
    }

    public double decrementAndGetVolatile() {
        return addAndGetVolatile(-1D);
    }

    public void decrementVolatile() {
        addVolatile(-1D);
    }

    public void decrementPlainRelease() {
        addPlainRelease(-1D);
    }

    public void decrementAcquirePlain() {
        addAcquirePlain(-1D);
    }

    public void decrementPlain() {
        value--;
    }

    public double getPlainAndAddRelease(double value) {
        return (double) VALUE.getAndAddRelease(this, value);
    }

    public double getAcquireAndAddPlain(double value) {
        return (double) VALUE.getAndAddAcquire(this, value);
    }

    public double getAndAddVolatile(double value) {
        return (double) VALUE.getAndAdd(this, value);
    }

    public double addReleaseAndGetPlain(double value) {
        return (double) VALUE.getAndAddRelease(this, value) + value;
    }

    public double addPlainAndGetAcquire(double value) {
        return (double) VALUE.getAndAddAcquire(this, value) + value;
    }

    public double addAndGetVolatile(double value) {
        return (double) VALUE.getAndAdd(this, value) + value;
    }

    public void addVolatile(double value) {
        VALUE.getAndAdd(this, value);
    }

    public void addPlainRelease(double value) {
        VALUE.getAndAddRelease(this, value);
    }

    public void addAcquirePlain(double value) {
        VALUE.getAndAddAcquire(this, value);
    }

    public void addPlain(double value) {
        this.value += value;
    }

    public double getPlainAndSetRelease(double value) {
        return (double) VALUE.getAndSetRelease(this, value);
    }

    public double getAcquireAndSetPlain(double value) {
        return (double) VALUE.getAndSetAcquire(this, value);
    }

    public double getAndSetVolatile(double value) {
        return (double) VALUE.getAndSet(this, value);
    }

    public double comparePlainAndExchangeRelease(double oldValue, double newValue) {
        return (double) VALUE.compareAndExchangeRelease(this, oldValue, newValue);
    }

    public double compareAcquireAndExchangePlain(double oldValue, double newValue) {
        return (double) VALUE.compareAndExchangeAcquire(this, oldValue, newValue);
    }

    public double compareAndExchangeVolatile(double oldValue, double newValue) {
        return (double) VALUE.compareAndExchange(this, oldValue, newValue);
    }

    public double getPlainAndBitwiseAndRelease(double mask) {
        return (double) VALUE.getAndBitwiseAndRelease(this, mask);
    }

    public double getAcquireAndBitwiseAndPlain(double mask) {
        return (double) VALUE.getAndBitwiseAndAcquire(this, mask);
    }

    public double getAndBitwiseAndVolatile(double mask) {
        return (double) VALUE.getAndBitwiseAnd(this, mask);
    }

    public double getPlainAndBitwiseOrRelease(double mask) {
        return (double) VALUE.getAndBitwiseOrRelease(this, mask);
    }

    public double getAcquireAndBitwiseOrPlain(double mask) {
        return (double) VALUE.getAndBitwiseOrAcquire(this, mask);
    }

    public double getAndBitwiseOrVolatile(double mask) {
        return (double) VALUE.getAndBitwiseOr(this, mask);
    }

    public double getPlainAndBitwiseXorRelease(double mask) {
        return (double) VALUE.getAndBitwiseXorRelease(this, mask);
    }

    public double getAcquireAndBitwiseXorPlain(double mask) {
        return (double) VALUE.getAndBitwiseXorAcquire(this, mask);
    }

    public double getAndBitwiseXorVolatile(double mask) {
        return (double) VALUE.getAndBitwiseXor(this, mask);
    }

    public double getAndUpdate(DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public double updateAndGet(DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public double getAndAccumulate(double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public double accumulateAndGet(double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    @Override
    public String toString() {
        return Double.toString(getVolatile());
    }
}
