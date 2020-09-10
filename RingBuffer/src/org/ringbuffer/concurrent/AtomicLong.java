/*
 * Copyright 2020 Francesco Menzani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use instance file except in compliance with the License.
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

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

import static org.ringbuffer.UnsafeAccess.UNSAFE;

public class AtomicLong {
    public static void setPlain(Object instance, long offset, long value) {
        UNSAFE.putLong(instance, offset, value);
    }

    public static void setOpaque(Object instance, long offset, long value) {
        UNSAFE.putLongOpaque(instance, offset, value);
    }

    public static void setRelease(Object instance, long offset, long value) {
        UNSAFE.putLongRelease(instance, offset, value);
    }

    public static void setVolatile(Object instance, long offset, long value) {
        UNSAFE.putLongVolatile(instance, offset, value);
    }

    public static long getPlain(Object instance, long offset) {
        return UNSAFE.getLong(instance, offset);
    }

    public static long getOpaque(Object instance, long offset) {
        return UNSAFE.getLongOpaque(instance, offset);
    }

    public static long getAcquire(Object instance, long offset) {
        return UNSAFE.getLongAcquire(instance, offset);
    }

    public static long getVolatile(Object instance, long offset) {
        return UNSAFE.getLongVolatile(instance, offset);
    }

    public static boolean compareAndSetVolatile(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.compareAndSetLong(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLongPlain(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLongRelease(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLongAcquire(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLong(instance, offset, oldValue, newValue);
    }

    public static long getAndIncrementPlain(Object instance, long offset) {
        return getAndAddPlain(instance, offset, 1L);
    }

    public static long getPlainAndIncrementRelease(Object instance, long offset) {
        return getPlainAndAddRelease(instance, offset, 1L);
    }

    public static long getAcquireAndIncrementPlain(Object instance, long offset) {
        return getAcquireAndAddPlain(instance, offset, 1L);
    }

    public static long getAndIncrementVolatile(Object instance, long offset) {
        return getAndAddVolatile(instance, offset, 1L);
    }

    public static long getAndDecrementPlain(Object instance, long offset) {
        return getAndAddPlain(instance, offset, -1L);
    }

    public static long getPlainAndDecrementRelease(Object instance, long offset) {
        return getPlainAndAddRelease(instance, offset, -1L);
    }

    public static long getAcquireAndDecrementPlain(Object instance, long offset) {
        return getAcquireAndAddPlain(instance, offset, -1L);
    }

    public static long getAndDecrementVolatile(Object instance, long offset) {
        return getAndAddVolatile(instance, offset, -1L);
    }

    public static long incrementAndGetPlain(Object instance, long offset) {
        return addAndGetPlain(instance, offset, 1L);
    }

    public static long incrementReleaseAndGetPlain(Object instance, long offset) {
        return addReleaseAndGetPlain(instance, offset, 1L);
    }

    public static long incrementPlainAndGetAcquire(Object instance, long offset) {
        return addPlainAndGetAcquire(instance, offset, 1L);
    }

    public static long incrementAndGetVolatile(Object instance, long offset) {
        return addAndGetVolatile(instance, offset, 1L);
    }

    public static void incrementPlain(Object instance, long offset) {
        addPlain(instance, offset, 1L);
    }

    public static void incrementPlainRelease(Object instance, long offset) {
        addPlainRelease(instance, offset, 1L);
    }

    public static void incrementAcquirePlain(Object instance, long offset) {
        addAcquirePlain(instance, offset, 1L);
    }

    public static void incrementVolatile(Object instance, long offset) {
        addVolatile(instance, offset, 1L);
    }

    public static long decrementAndGetPlain(Object instance, long offset) {
        return addAndGetPlain(instance, offset, -1L);
    }

    public static long decrementReleaseAndGetPlain(Object instance, long offset) {
        return addReleaseAndGetPlain(instance, offset, -1L);
    }

    public static long decrementPlainAndGetAcquire(Object instance, long offset) {
        return addPlainAndGetAcquire(instance, offset, -1L);
    }

    public static long decrementAndGetVolatile(Object instance, long offset) {
        return addAndGetVolatile(instance, offset, -1L);
    }

    public static void decrementPlain(Object instance, long offset) {
        addPlain(instance, offset, -1L);
    }

    public static void decrementPlainRelease(Object instance, long offset) {
        addPlainRelease(instance, offset, -1L);
    }

    public static void decrementAcquirePlain(Object instance, long offset) {
        addAcquirePlain(instance, offset, -1L);
    }

    public static void decrementVolatile(Object instance, long offset) {
        addVolatile(instance, offset, -1L);
    }

    public static long getAndAddPlain(Object instance, long offset, long value) {
        long oldValue = getPlain(instance, offset);
        setPlain(instance, offset, oldValue + value);
        return oldValue;
    }

    public static long getPlainAndAddRelease(Object instance, long offset, long value) {
        return UNSAFE.getAndAddLongRelease(instance, offset, value);
    }

    public static long getAcquireAndAddPlain(Object instance, long offset, long value) {
        return UNSAFE.getAndAddLongAcquire(instance, offset, value);
    }

    public static long getAndAddVolatile(Object instance, long offset, long value) {
        return UNSAFE.getAndAddLong(instance, offset, value);
    }

    public static long addAndGetPlain(Object instance, long offset, long value) {
        long newValue = getPlain(instance, offset) + value;
        setPlain(instance, offset, newValue);
        return newValue;
    }

    public static long addReleaseAndGetPlain(Object instance, long offset, long value) {
        return getPlainAndAddRelease(instance, offset, value) + value;
    }

    public static long addPlainAndGetAcquire(Object instance, long offset, long value) {
        return getAcquireAndAddPlain(instance, offset, value) + value;
    }

    public static long addAndGetVolatile(Object instance, long offset, long value) {
        return getAndAddVolatile(instance, offset, value) + value;
    }

    public static void addPlain(Object instance, long offset, long value) {
        setPlain(instance, offset, getPlain(instance, offset) + value);
    }

    public static void addPlainRelease(Object instance, long offset, long value) {
        UNSAFE.getAndAddLongRelease(instance, offset, value);
    }

    public static void addAcquirePlain(Object instance, long offset, long value) {
        UNSAFE.getAndAddLongAcquire(instance, offset, value);
    }

    public static void addVolatile(Object instance, long offset, long value) {
        UNSAFE.getAndAddLong(instance, offset, value);
    }

    public static long getPlainAndSetRelease(Object instance, long offset, long value) {
        return UNSAFE.getAndSetLongRelease(instance, offset, value);
    }

    public static long getAcquireAndSetPlain(Object instance, long offset, long value) {
        return UNSAFE.getAndSetLongAcquire(instance, offset, value);
    }

    public static long getAndSetVolatile(Object instance, long offset, long value) {
        return UNSAFE.getAndSetLong(instance, offset, value);
    }

    public static long comparePlainAndExchangeRelease(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.compareAndExchangeLongRelease(instance, offset, oldValue, newValue);
    }

    public static long compareAcquireAndExchangePlain(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.compareAndExchangeLongAcquire(instance, offset, oldValue, newValue);
    }

    public static long compareAndExchangeVolatile(Object instance, long offset, long oldValue, long newValue) {
        return UNSAFE.compareAndExchangeLong(instance, offset, oldValue, newValue);
    }

    public static long getPlainAndBitwiseAndRelease(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseAndLongRelease(instance, offset, mask);
    }

    public static long getAcquireAndBitwiseAndPlain(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseAndLongAcquire(instance, offset, mask);
    }

    public static long getAndBitwiseAndVolatile(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseAndLong(instance, offset, mask);
    }

    public static long getPlainAndBitwiseOrRelease(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseOrLongRelease(instance, offset, mask);
    }

    public static long getAcquireAndBitwiseOrPlain(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseOrLongAcquire(instance, offset, mask);
    }

    public static long getAndBitwiseOrVolatile(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseOrLong(instance, offset, mask);
    }

    public static long getPlainAndBitwiseXorRelease(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseXorLongRelease(instance, offset, mask);
    }

    public static long getAcquireAndBitwiseXorPlain(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseXorLongAcquire(instance, offset, mask);
    }

    public static long getAndBitwiseXorVolatile(Object instance, long offset, long mask) {
        return UNSAFE.getAndBitwiseXorLong(instance, offset, mask);
    }

    public static long getAndUpdate(Object instance, long offset, LongUnaryOperator updateFunction) {
        long prev = getVolatile(instance, offset), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static long updateAndGet(Object instance, long offset, LongUnaryOperator updateFunction) {
        long prev = getVolatile(instance, offset), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static long getAndAccumulate(Object instance, long offset, long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(instance, offset), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static long accumulateAndGet(Object instance, long offset, long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(instance, offset), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }
}
