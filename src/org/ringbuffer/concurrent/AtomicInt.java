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
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class AtomicInt {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(AtomicInt.class, "value", int.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private int value;

    public AtomicInt() {}

    public AtomicInt(int value) {
        this.value = value;
    }

    public void setPlain(int value) {
        this.value = value;
    }

    public void setOpaque(int value) {
        VALUE.setOpaque(this, value);
    }

    public void setRelease(int value) {
        VALUE.setRelease(this, value);
    }

    public void setVolatile(int value) {
        VALUE.setVolatile(this, value);
    }

    public int getPlain() {
        return value;
    }

    public int getOpaque() {
        return (int) VALUE.getOpaque(this);
    }

    public int getAcquire() {
        return (int) VALUE.getAcquire(this);
    }

    public int getVolatile() {
        return (int) VALUE.getVolatile(this);
    }

    public boolean compareAndSetVolatile(int oldValue, int newValue) {
        return VALUE.compareAndSet(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int oldValue, int newValue) {
        return VALUE.weakCompareAndSetPlain(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int oldValue, int newValue) {
        return VALUE.weakCompareAndSetRelease(this, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int oldValue, int newValue) {
        return VALUE.weakCompareAndSetAcquire(this, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int oldValue, int newValue) {
        return VALUE.weakCompareAndSet(this, oldValue, newValue);
    }

    public int getPlainAndIncrementRelease() {
        return getPlainAndAddRelease(1);
    }

    public int getAcquireAndIncrementPlain() {
        return getAcquireAndAddPlain(1);
    }

    public int getAndIncrementVolatile() {
        return getAndAddVolatile(1);
    }

    public int getPlainAndDecrementRelease() {
        return getPlainAndAddRelease(-1);
    }

    public int getAcquireAndDecrementPlain() {
        return getAcquireAndAddPlain(-1);
    }

    public int getAndDecrementVolatile() {
        return getAndAddVolatile(-1);
    }

    public int incrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(1);
    }

    public int incrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(1);
    }

    public int incrementAndGetVolatile() {
        return addAndGetVolatile(1);
    }

    public void incrementVolatile() {
        addVolatile(1);
    }

    public void incrementPlainRelease() {
        addPlainRelease(1);
    }

    public void incrementAcquirePlain() {
        addAcquirePlain(1);
    }

    public void incrementPlain() {
        value++;
    }

    public int decrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(-1);
    }

    public int decrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(-1);
    }

    public int decrementAndGetVolatile() {
        return addAndGetVolatile(-1);
    }

    public void decrementVolatile() {
        addVolatile(-1);
    }

    public void decrementPlainRelease() {
        addPlainRelease(-1);
    }

    public void decrementAcquirePlain() {
        addAcquirePlain(-1);
    }

    public void decrementPlain() {
        value--;
    }

    public int getPlainAndAddRelease(int value) {
        return (int) VALUE.getAndAddRelease(this, value);
    }

    public int getAcquireAndAddPlain(int value) {
        return (int) VALUE.getAndAddAcquire(this, value);
    }

    public int getAndAddVolatile(int value) {
        return (int) VALUE.getAndAdd(this, value);
    }

    public int addReleaseAndGetPlain(int value) {
        return (int) VALUE.getAndAddRelease(this, value) + value;
    }

    public int addPlainAndGetAcquire(int value) {
        return (int) VALUE.getAndAddAcquire(this, value) + value;
    }

    public int addAndGetVolatile(int value) {
        return (int) VALUE.getAndAdd(this, value) + value;
    }

    public void addVolatile(int value) {
        VALUE.getAndAdd(this, value);
    }

    public void addPlainRelease(int value) {
        VALUE.getAndAddRelease(this, value);
    }

    public void addAcquirePlain(int value) {
        VALUE.getAndAddAcquire(this, value);
    }

    public void addPlain(int value) {
        this.value += value;
    }

    public int getPlainAndSetRelease(int value) {
        return (int) VALUE.getAndSetRelease(this, value);
    }

    public int getAcquireAndSetPlain(int value) {
        return (int) VALUE.getAndSetAcquire(this, value);
    }

    public int getAndSetVolatile(int value) {
        return (int) VALUE.getAndSet(this, value);
    }

    public int comparePlainAndExchangeRelease(int oldValue, int newValue) {
        return (int) VALUE.compareAndExchangeRelease(this, oldValue, newValue);
    }

    public int compareAcquireAndExchangePlain(int oldValue, int newValue) {
        return (int) VALUE.compareAndExchangeAcquire(this, oldValue, newValue);
    }

    public int compareAndExchangeVolatile(int oldValue, int newValue) {
        return (int) VALUE.compareAndExchange(this, oldValue, newValue);
    }

    public int getPlainAndBitwiseAndRelease(int mask) {
        return (int) VALUE.getAndBitwiseAndRelease(this, mask);
    }

    public int getAcquireAndBitwiseAndPlain(int mask) {
        return (int) VALUE.getAndBitwiseAndAcquire(this, mask);
    }

    public int getAndBitwiseAndVolatile(int mask) {
        return (int) VALUE.getAndBitwiseAnd(this, mask);
    }

    public int getPlainAndBitwiseOrRelease(int mask) {
        return (int) VALUE.getAndBitwiseOrRelease(this, mask);
    }

    public int getAcquireAndBitwiseOrPlain(int mask) {
        return (int) VALUE.getAndBitwiseOrAcquire(this, mask);
    }

    public int getAndBitwiseOrVolatile(int mask) {
        return (int) VALUE.getAndBitwiseOr(this, mask);
    }

    public int getPlainAndBitwiseXorRelease(int mask) {
        return (int) VALUE.getAndBitwiseXorRelease(this, mask);
    }

    public int getAcquireAndBitwiseXorPlain(int mask) {
        return (int) VALUE.getAndBitwiseXorAcquire(this, mask);
    }

    public int getAndBitwiseXorVolatile(int mask) {
        return (int) VALUE.getAndBitwiseXor(this, mask);
    }

    public int getAndUpdate(IntUnaryOperator updateFunction) {
        int prev = getVolatile(), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public int updateAndGet(IntUnaryOperator updateFunction) {
        int prev = getVolatile(), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public int getAndAccumulate(int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public int accumulateAndGet(int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    @Override
    public String toString() {
        return Integer.toString(getVolatile());
    }
}
