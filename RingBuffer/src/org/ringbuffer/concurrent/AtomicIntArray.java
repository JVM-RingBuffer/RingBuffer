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

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import static org.ringbuffer.UnsafeAccess.UNSAFE;

public class AtomicIntArray {
    public static void setPlain(int[] array, int index, int value) {
        UNSAFE.putInt(array, elementOffset(index), value);
    }

    public static void setOpaque(int[] array, int index, int value) {
        UNSAFE.putIntOpaque(array, elementOffset(index), value);
    }

    public static void setRelease(int[] array, int index, int value) {
        UNSAFE.putIntRelease(array, elementOffset(index), value);
    }

    public static void setVolatile(int[] array, int index, int value) {
        UNSAFE.putIntVolatile(array, elementOffset(index), value);
    }

    public static int getPlain(int[] array, int index) {
        return UNSAFE.getInt(array, elementOffset(index));
    }

    public static int getOpaque(int[] array, int index) {
        return UNSAFE.getIntOpaque(array, elementOffset(index));
    }

    public static int getAcquire(int[] array, int index) {
        return UNSAFE.getIntAcquire(array, elementOffset(index));
    }

    public static int getVolatile(int[] array, int index) {
        return UNSAFE.getIntVolatile(array, elementOffset(index));
    }

    public static boolean compareAndSetVolatile(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.compareAndSetInt(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetIntPlain(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetIntRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetIntAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetInt(array, elementOffset(index), oldValue, newValue);
    }

    public static int getPlainAndIncrementRelease(int[] array, int index) {
        return getPlainAndAddRelease(array, index, 1);
    }

    public static int getAcquireAndIncrementPlain(int[] array, int index) {
        return getAcquireAndAddPlain(array, index, 1);
    }

    public static int getAndIncrementVolatile(int[] array, int index) {
        return getAndAddVolatile(array, index, 1);
    }

    public static int getPlainAndDecrementRelease(int[] array, int index) {
        return getPlainAndAddRelease(array, index, -1);
    }

    public static int getAcquireAndDecrementPlain(int[] array, int index) {
        return getAcquireAndAddPlain(array, index, -1);
    }

    public static int getAndDecrementVolatile(int[] array, int index) {
        return getAndAddVolatile(array, index, -1);
    }

    public static int incrementReleaseAndGetPlain(int[] array, int index) {
        return addReleaseAndGetPlain(array, index, 1);
    }

    public static int incrementPlainAndGetAcquire(int[] array, int index) {
        return addPlainAndGetAcquire(array, index, 1);
    }

    public static int incrementAndGetVolatile(int[] array, int index) {
        return addAndGetVolatile(array, index, 1);
    }

    public static void incrementPlainRelease(int[] array, int index) {
        addPlainRelease(array, index, 1);
    }

    public static void incrementAcquirePlain(int[] array, int index) {
        addAcquirePlain(array, index, 1);
    }

    public static void incrementVolatile(int[] array, int index) {
        addVolatile(array, index, 1);
    }

    public static int decrementReleaseAndGetPlain(int[] array, int index) {
        return addReleaseAndGetPlain(array, index, -1);
    }

    public static int decrementPlainAndGetAcquire(int[] array, int index) {
        return addPlainAndGetAcquire(array, index, -1);
    }

    public static int decrementAndGetVolatile(int[] array, int index) {
        return addAndGetVolatile(array, index, -1);
    }

    public static void decrementPlainRelease(int[] array, int index) {
        addPlainRelease(array, index, -1);
    }

    public static void decrementAcquirePlain(int[] array, int index) {
        addAcquirePlain(array, index, -1);
    }

    public static void decrementVolatile(int[] array, int index) {
        addVolatile(array, index, -1);
    }

    public static int getPlainAndAddRelease(int[] array, int index, int value) {
        return UNSAFE.getAndAddIntRelease(array, elementOffset(index), value);
    }

    public static int getAcquireAndAddPlain(int[] array, int index, int value) {
        return UNSAFE.getAndAddIntAcquire(array, elementOffset(index), value);
    }

    public static int getAndAddVolatile(int[] array, int index, int value) {
        return UNSAFE.getAndAddInt(array, elementOffset(index), value);
    }

    public static int addReleaseAndGetPlain(int[] array, int index, int value) {
        return getPlainAndAddRelease(array, index, value) + value;
    }

    public static int addPlainAndGetAcquire(int[] array, int index, int value) {
        return getAcquireAndAddPlain(array, index, value) + value;
    }

    public static int addAndGetVolatile(int[] array, int index, int value) {
        return getAndAddVolatile(array, index, value) + value;
    }

    public static void addPlainRelease(int[] array, int index, int value) {
        UNSAFE.getAndAddIntRelease(array, elementOffset(index), value);
    }

    public static void addAcquirePlain(int[] array, int index, int value) {
        UNSAFE.getAndAddIntAcquire(array, elementOffset(index), value);
    }

    public static void addVolatile(int[] array, int index, int value) {
        UNSAFE.getAndAddInt(array, elementOffset(index), value);
    }

    public static int getPlainAndSetRelease(int[] array, int index, int value) {
        return UNSAFE.getAndSetIntRelease(array, elementOffset(index), value);
    }

    public static int getAcquireAndSetPlain(int[] array, int index, int value) {
        return UNSAFE.getAndSetIntAcquire(array, elementOffset(index), value);
    }

    public static int getAndSetVolatile(int[] array, int index, int value) {
        return UNSAFE.getAndSetInt(array, elementOffset(index), value);
    }

    public static int comparePlainAndExchangeRelease(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.compareAndExchangeIntRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static int compareAcquireAndExchangePlain(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.compareAndExchangeIntAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static int compareAndExchangeVolatile(int[] array, int index, int oldValue, int newValue) {
        return UNSAFE.compareAndExchangeInt(array, elementOffset(index), oldValue, newValue);
    }

    public static int getPlainAndBitwiseAndRelease(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseAndIntRelease(array, elementOffset(index), mask);
    }

    public static int getAcquireAndBitwiseAndPlain(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseAndIntAcquire(array, elementOffset(index), mask);
    }

    public static int getAndBitwiseAndVolatile(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseAndInt(array, elementOffset(index), mask);
    }

    public static int getPlainAndBitwiseOrRelease(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseOrIntRelease(array, elementOffset(index), mask);
    }

    public static int getAcquireAndBitwiseOrPlain(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseOrIntAcquire(array, elementOffset(index), mask);
    }

    public static int getAndBitwiseOrVolatile(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseOrInt(array, elementOffset(index), mask);
    }

    public static int getPlainAndBitwiseXorRelease(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseXorIntRelease(array, elementOffset(index), mask);
    }

    public static int getAcquireAndBitwiseXorPlain(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseXorIntAcquire(array, elementOffset(index), mask);
    }

    public static int getAndBitwiseXorVolatile(int[] array, int index, int mask) {
        return UNSAFE.getAndBitwiseXorInt(array, elementOffset(index), mask);
    }

    public static int getAndUpdate(int[] array, int index, IntUnaryOperator updateFunction) {
        int prev = getVolatile(array, index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static int updateAndGet(int[] array, int index, IntUnaryOperator updateFunction) {
        int prev = getVolatile(array, index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static int getAndAccumulate(int[] array, int index, int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(array, index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static int accumulateAndGet(int[] array, int index, int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(array, index), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static void fillOpaque(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            setOpaque(array, i, value);
        }
    }

    public static void fillRelease(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            setRelease(array, i, value);
        }
    }

    public static void fillVolatile(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            setVolatile(array, i, value);
        }
    }

    public static String toStringOpaque(int[] array) {
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

    public static String toStringAcquire(int[] array) {
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

    public static String toStringVolatile(int[] array) {
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
        return UNSAFE.ARRAY_INT_BASE_OFFSET + UNSAFE.ARRAY_INT_INDEX_SCALE * index;
    }
}
