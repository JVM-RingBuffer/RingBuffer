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

import jdk.internal.vm.annotation.Contended;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public class PaddedAtomicLong {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(PaddedAtomicLong.class, "value", long.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Contended
    private long value;

    public PaddedAtomicLong() {
    }

    public PaddedAtomicLong(long value) {
        this.value = value;
    }

    public void setPlain(long value) {
        this.value = value;
    }

    public void setOpaque(long value) {
        VALUE.setOpaque(this, value);
    }

    public void setRelease(long value) {
        VALUE.setRelease(this, value);
    }

    public void setVolatile(long value) {
        VALUE.setVolatile(this, value);
    }

    public long getPlain() {
        return value;
    }

    public long getOpaque() {
        return (long) VALUE.getOpaque(this);
    }

    public long getAcquire() {
        return (long) VALUE.getAcquire(this);
    }

    public long getVolatile() {
        return (long) VALUE.getVolatile(this);
    }

    public boolean compareAndSetVolatile(long oldValue, long newValue) {
        return VALUE.compareAndSet(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(long oldValue, long newValue) {
        return VALUE.weakCompareAndSetPlain(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(long oldValue, long newValue) {
        return VALUE.weakCompareAndSetRelease(this, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(long oldValue, long newValue) {
        return VALUE.weakCompareAndSetAcquire(this, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(long oldValue, long newValue) {
        return VALUE.weakCompareAndSet(this, oldValue, newValue);
    }

    public long getPlainAndIncrementRelease() {
        return getPlainAndAddRelease(1L);
    }

    public long getAcquireAndIncrementPlain() {
        return getAcquireAndAddPlain(1L);
    }

    public long getAndIncrementVolatile() {
        return getAndAddVolatile(1L);
    }

    public long getPlainAndDecrementRelease() {
        return getPlainAndAddRelease(-1L);
    }

    public long getAcquireAndDecrementPlain() {
        return getAcquireAndAddPlain(-1L);
    }

    public long getAndDecrementVolatile() {
        return getAndAddVolatile(-1L);
    }

    public long incrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(1L);
    }

    public long incrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(1L);
    }

    public long incrementAndGetVolatile() {
        return addAndGetVolatile(1L);
    }

    public void incrementVolatile() {
        addVolatile(1L);
    }

    public void incrementPlainRelease() {
        addPlainRelease(1L);
    }

    public void incrementAcquirePlain() {
        addAcquirePlain(1L);
    }

    public void incrementPlain() {
        value++;
    }

    public long decrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(-1L);
    }

    public long decrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(-1L);
    }

    public long decrementAndGetVolatile() {
        return addAndGetVolatile(-1L);
    }

    public void decrementVolatile() {
        addVolatile(-1L);
    }

    public void decrementPlainRelease() {
        addPlainRelease(-1L);
    }

    public void decrementAcquirePlain() {
        addAcquirePlain(-1L);
    }

    public void decrementPlain() {
        value--;
    }

    public long getPlainAndAddRelease(long value) {
        return (long) VALUE.getAndAddRelease(this, value);
    }

    public long getAcquireAndAddPlain(long value) {
        return (long) VALUE.getAndAddAcquire(this, value);
    }

    public long getAndAddVolatile(long value) {
        return (long) VALUE.getAndAdd(this, value);
    }

    public long addReleaseAndGetPlain(long value) {
        return (long) VALUE.getAndAddRelease(this, value) + value;
    }

    public long addPlainAndGetAcquire(long value) {
        return (long) VALUE.getAndAddAcquire(this, value) + value;
    }

    public long addAndGetVolatile(long value) {
        return (long) VALUE.getAndAdd(this, value) + value;
    }

    public void addVolatile(long value) {
        VALUE.getAndAdd(this, value);
    }

    public void addPlainRelease(long value) {
        VALUE.getAndAddRelease(this, value);
    }

    public void addAcquirePlain(long value) {
        VALUE.getAndAddAcquire(this, value);
    }

    public void addPlain(long value) {
        this.value += value;
    }

    public long getPlainAndSetRelease(long value) {
        return (long) VALUE.getAndSetRelease(this, value);
    }

    public long getAcquireAndSetPlain(long value) {
        return (long) VALUE.getAndSetAcquire(this, value);
    }

    public long getAndSetVolatile(long value) {
        return (long) VALUE.getAndSet(this, value);
    }

    public long comparePlainAndExchangeRelease(long oldValue, long newValue) {
        return (long) VALUE.compareAndExchangeRelease(this, oldValue, newValue);
    }

    public long compareAcquireAndExchangePlain(long oldValue, long newValue) {
        return (long) VALUE.compareAndExchangeAcquire(this, oldValue, newValue);
    }

    public long compareAndExchangeVolatile(long oldValue, long newValue) {
        return (long) VALUE.compareAndExchange(this, oldValue, newValue);
    }

    public long getPlainAndBitwiseAndRelease(long mask) {
        return (long) VALUE.getAndBitwiseAndRelease(this, mask);
    }

    public long getAcquireAndBitwiseAndPlain(long mask) {
        return (long) VALUE.getAndBitwiseAndAcquire(this, mask);
    }

    public long getAndBitwiseAndVolatile(long mask) {
        return (long) VALUE.getAndBitwiseAnd(this, mask);
    }

    public long getPlainAndBitwiseOrRelease(long mask) {
        return (long) VALUE.getAndBitwiseOrRelease(this, mask);
    }

    public long getAcquireAndBitwiseOrPlain(long mask) {
        return (long) VALUE.getAndBitwiseOrAcquire(this, mask);
    }

    public long getAndBitwiseOrVolatile(long mask) {
        return (long) VALUE.getAndBitwiseOr(this, mask);
    }

    public long getPlainAndBitwiseXorRelease(long mask) {
        return (long) VALUE.getAndBitwiseXorRelease(this, mask);
    }

    public long getAcquireAndBitwiseXorPlain(long mask) {
        return (long) VALUE.getAndBitwiseXorAcquire(this, mask);
    }

    public long getAndBitwiseXorVolatile(long mask) {
        return (long) VALUE.getAndBitwiseXor(this, mask);
    }

    public long getAndUpdate(LongUnaryOperator updateFunction) {
        long prev = getVolatile(), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public long updateAndGet(LongUnaryOperator updateFunction) {
        long prev = getVolatile(), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public long getAndAccumulate(long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public long accumulateAndGet(long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    @Override
    public String toString() {
        return Long.toString(getVolatile());
    }
}
