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

import org.ringbuffer.system.Unsafe;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

import static org.ringbuffer.system.Unsafe.UNSAFE;

public class AtomicLongArray {
    public static void setPlain(long[] array, int index, long value) {
        UNSAFE.putLong(array, elementOffset(index), value);
    }

    public static void setOpaque(long[] array, int index, long value) {
        UNSAFE.putLongOpaque(array, elementOffset(index), value);
    }

    public static void setRelease(long[] array, int index, long value) {
        UNSAFE.putLongRelease(array, elementOffset(index), value);
    }

    public static void setVolatile(long[] array, int index, long value) {
        UNSAFE.putLongVolatile(array, elementOffset(index), value);
    }

    public static long getPlain(long[] array, int index) {
        return UNSAFE.getLong(array, elementOffset(index));
    }

    public static long getOpaque(long[] array, int index) {
        return UNSAFE.getLongOpaque(array, elementOffset(index));
    }

    public static long getAcquire(long[] array, int index) {
        return UNSAFE.getLongAcquire(array, elementOffset(index));
    }

    public static long getVolatile(long[] array, int index) {
        return UNSAFE.getLongVolatile(array, elementOffset(index));
    }

    public static boolean compareAndSetVolatile(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.compareAndSetLong(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLongPlain(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLongRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLongAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.weakCompareAndSetLong(array, elementOffset(index), oldValue, newValue);
    }

    public static long getPlainAndIncrementRelease(long[] array, int index) {
        return getPlainAndAddRelease(array, index, 1L);
    }

    public static long getAcquireAndIncrementPlain(long[] array, int index) {
        return getAcquireAndAddPlain(array, index, 1L);
    }

    public static long getAndIncrementVolatile(long[] array, int index) {
        return getAndAddVolatile(array, index, 1L);
    }

    public static long getPlainAndDecrementRelease(long[] array, int index) {
        return getPlainAndAddRelease(array, index, -1L);
    }

    public static long getAcquireAndDecrementPlain(long[] array, int index) {
        return getAcquireAndAddPlain(array, index, -1L);
    }

    public static long getAndDecrementVolatile(long[] array, int index) {
        return getAndAddVolatile(array, index, -1L);
    }

    public static long incrementReleaseAndGetPlain(long[] array, int index) {
        return addReleaseAndGetPlain(array, index, 1L);
    }

    public static long incrementPlainAndGetAcquire(long[] array, int index) {
        return addPlainAndGetAcquire(array, index, 1L);
    }

    public static long incrementAndGetVolatile(long[] array, int index) {
        return addAndGetVolatile(array, index, 1L);
    }

    public static void incrementPlainRelease(long[] array, int index) {
        addPlainRelease(array, index, 1L);
    }

    public static void incrementAcquirePlain(long[] array, int index) {
        addAcquirePlain(array, index, 1L);
    }

    public static void incrementVolatile(long[] array, int index) {
        addVolatile(array, index, 1L);
    }

    public static long decrementReleaseAndGetPlain(long[] array, int index) {
        return addReleaseAndGetPlain(array, index, -1L);
    }

    public static long decrementPlainAndGetAcquire(long[] array, int index) {
        return addPlainAndGetAcquire(array, index, -1L);
    }

    public static long decrementAndGetVolatile(long[] array, int index) {
        return addAndGetVolatile(array, index, -1L);
    }

    public static void decrementPlainRelease(long[] array, int index) {
        addPlainRelease(array, index, -1L);
    }

    public static void decrementAcquirePlain(long[] array, int index) {
        addAcquirePlain(array, index, -1L);
    }

    public static void decrementVolatile(long[] array, int index) {
        addVolatile(array, index, -1L);
    }

    public static long getPlainAndAddRelease(long[] array, int index, long value) {
        return UNSAFE.getAndAddLongRelease(array, elementOffset(index), value);
    }

    public static long getAcquireAndAddPlain(long[] array, int index, long value) {
        return UNSAFE.getAndAddLongAcquire(array, elementOffset(index), value);
    }

    public static long getAndAddVolatile(long[] array, int index, long value) {
        return UNSAFE.getAndAddLong(array, elementOffset(index), value);
    }

    public static long addReleaseAndGetPlain(long[] array, int index, long value) {
        return getPlainAndAddRelease(array, index, value) + value;
    }

    public static long addPlainAndGetAcquire(long[] array, int index, long value) {
        return getAcquireAndAddPlain(array, index, value) + value;
    }

    public static long addAndGetVolatile(long[] array, int index, long value) {
        return getAndAddVolatile(array, index, value) + value;
    }

    public static void addPlainRelease(long[] array, int index, long value) {
        UNSAFE.getAndAddLongRelease(array, elementOffset(index), value);
    }

    public static void addAcquirePlain(long[] array, int index, long value) {
        UNSAFE.getAndAddLongAcquire(array, elementOffset(index), value);
    }

    public static void addVolatile(long[] array, int index, long value) {
        UNSAFE.getAndAddLong(array, elementOffset(index), value);
    }

    public static long getPlainAndSetRelease(long[] array, int index, long value) {
        return UNSAFE.getAndSetLongRelease(array, elementOffset(index), value);
    }

    public static long getAcquireAndSetPlain(long[] array, int index, long value) {
        return UNSAFE.getAndSetLongAcquire(array, elementOffset(index), value);
    }

    public static long getAndSetVolatile(long[] array, int index, long value) {
        return UNSAFE.getAndSetLong(array, elementOffset(index), value);
    }

    public static long comparePlainAndExchangeRelease(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.compareAndExchangeLongRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static long compareAcquireAndExchangePlain(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.compareAndExchangeLongAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static long compareAndExchangeVolatile(long[] array, int index, long oldValue, long newValue) {
        return UNSAFE.compareAndExchangeLong(array, elementOffset(index), oldValue, newValue);
    }

    public static long getPlainAndBitwiseAndRelease(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseAndLongRelease(array, elementOffset(index), mask);
    }

    public static long getAcquireAndBitwiseAndPlain(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseAndLongAcquire(array, elementOffset(index), mask);
    }

    public static long getAndBitwiseAndVolatile(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseAndLong(array, elementOffset(index), mask);
    }

    public static long getPlainAndBitwiseOrRelease(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseOrLongRelease(array, elementOffset(index), mask);
    }

    public static long getAcquireAndBitwiseOrPlain(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseOrLongAcquire(array, elementOffset(index), mask);
    }

    public static long getAndBitwiseOrVolatile(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseOrLong(array, elementOffset(index), mask);
    }

    public static long getPlainAndBitwiseXorRelease(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseXorLongRelease(array, elementOffset(index), mask);
    }

    public static long getAcquireAndBitwiseXorPlain(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseXorLongAcquire(array, elementOffset(index), mask);
    }

    public static long getAndBitwiseXorVolatile(long[] array, int index, long mask) {
        return UNSAFE.getAndBitwiseXorLong(array, elementOffset(index), mask);
    }

    public static long getAndUpdate(long[] array, int index, LongUnaryOperator updateFunction) {
        long prev = getVolatile(array, index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static long updateAndGet(long[] array, int index, LongUnaryOperator updateFunction) {
        long prev = getVolatile(array, index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsLong(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static long getAndAccumulate(long[] array, int index, long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(array, index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static long accumulateAndGet(long[] array, int index, long constant, LongBinaryOperator accumulatorFunction) {
        long prev = getVolatile(array, index), next = 0L;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsLong(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static void fillOpaque(long[] array, long value) {
        for (int i = 0; i < array.length; i++) {
            setOpaque(array, i, value);
        }
    }

    public static void fillRelease(long[] array, long value) {
        for (int i = 0; i < array.length; i++) {
            setRelease(array, i, value);
        }
    }

    public static void fillVolatile(long[] array, long value) {
        for (int i = 0; i < array.length; i++) {
            setVolatile(array, i, value);
        }
    }

    public static String toStringOpaque(long[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0, iMax = array.length - 1; ; i++) {
            builder.append(getOpaque(array, i));
            if (i == iMax) {
                builder.append(']');
                return builder.toString();
            }
            builder.append(", ");
        }
    }

    public static String toStringAcquire(long[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0, iMax = array.length - 1; ; i++) {
            builder.append(getAcquire(array, i));
            if (i == iMax) {
                builder.append(']');
                return builder.toString();
            }
            builder.append(", ");
        }
    }

    public static String toStringVolatile(long[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0, iMax = array.length - 1; ; i++) {
            builder.append(getVolatile(array, i));
            if (i == iMax) {
                builder.append(']');
                return builder.toString();
            }
            builder.append(", ");
        }
    }

    public static long elementOffset(int index) {
        return Unsafe.ARRAY_LONG_BASE_OFFSET + Unsafe.ARRAY_LONG_INDEX_SCALE * index;
    }
}
