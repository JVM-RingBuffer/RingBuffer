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

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static org.ringbuffer.UnsafeAccess.UNSAFE;

public class AtomicDoubleArray {
    public static void setPlain(double[] array, int index, double value) {
        UNSAFE.putDouble(array, elementOffset(index), value);
    }

    public static void setOpaque(double[] array, int index, double value) {
        UNSAFE.putDoubleOpaque(array, elementOffset(index), value);
    }

    public static void setRelease(double[] array, int index, double value) {
        UNSAFE.putDoubleRelease(array, elementOffset(index), value);
    }

    public static void setVolatile(double[] array, int index, double value) {
        UNSAFE.putDoubleVolatile(array, elementOffset(index), value);
    }

    public static double getPlain(double[] array, int index) {
        return UNSAFE.getDouble(array, elementOffset(index));
    }

    public static double getOpaque(double[] array, int index) {
        return UNSAFE.getDoubleOpaque(array, elementOffset(index));
    }

    public static double getAcquire(double[] array, int index) {
        return UNSAFE.getDoubleAcquire(array, elementOffset(index));
    }

    public static double getVolatile(double[] array, int index) {
        return UNSAFE.getDoubleVolatile(array, elementOffset(index));
    }

    public static boolean compareAndSetVolatile(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.compareAndSetDouble(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDoublePlain(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDoubleRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDoubleAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDouble(array, elementOffset(index), oldValue, newValue);
    }

    public static double getPlainAndIncrementRelease(double[] array, int index) {
        return getPlainAndAddRelease(array, index, 1D);
    }

    public static double getAcquireAndIncrementPlain(double[] array, int index) {
        return getAcquireAndAddPlain(array, index, 1D);
    }

    public static double getAndIncrementVolatile(double[] array, int index) {
        return getAndAddVolatile(array, index, 1D);
    }

    public static double getPlainAndDecrementRelease(double[] array, int index) {
        return getPlainAndAddRelease(array, index, -1D);
    }

    public static double getAcquireAndDecrementPlain(double[] array, int index) {
        return getAcquireAndAddPlain(array, index, -1D);
    }

    public static double getAndDecrementVolatile(double[] array, int index) {
        return getAndAddVolatile(array, index, -1D);
    }

    public static double incrementReleaseAndGetPlain(double[] array, int index) {
        return addReleaseAndGetPlain(array, index, 1D);
    }

    public static double incrementPlainAndGetAcquire(double[] array, int index) {
        return addPlainAndGetAcquire(array, index, 1D);
    }

    public static double incrementAndGetVolatile(double[] array, int index) {
        return addAndGetVolatile(array, index, 1D);
    }

    public static void incrementPlainRelease(double[] array, int index) {
        addPlainRelease(array, index, 1D);
    }

    public static void incrementAcquirePlain(double[] array, int index) {
        addAcquirePlain(array, index, 1D);
    }

    public static void incrementVolatile(double[] array, int index) {
        addVolatile(array, index, 1D);
    }

    public static double decrementReleaseAndGetPlain(double[] array, int index) {
        return addReleaseAndGetPlain(array, index, -1D);
    }

    public static double decrementPlainAndGetAcquire(double[] array, int index) {
        return addPlainAndGetAcquire(array, index, -1D);
    }

    public static double decrementAndGetVolatile(double[] array, int index) {
        return addAndGetVolatile(array, index, -1D);
    }

    public static void decrementPlainRelease(double[] array, int index) {
        addPlainRelease(array, index, -1D);
    }

    public static void decrementAcquirePlain(double[] array, int index) {
        addAcquirePlain(array, index, -1D);
    }

    public static void decrementVolatile(double[] array, int index) {
        addVolatile(array, index, -1D);
    }

    public static double getPlainAndAddRelease(double[] array, int index, double value) {
        return UNSAFE.getAndAddDoubleRelease(array, elementOffset(index), value);
    }

    public static double getAcquireAndAddPlain(double[] array, int index, double value) {
        return UNSAFE.getAndAddDoubleAcquire(array, elementOffset(index), value);
    }

    public static double getAndAddVolatile(double[] array, int index, double value) {
        return UNSAFE.getAndAddDouble(array, elementOffset(index), value);
    }

    public static double addReleaseAndGetPlain(double[] array, int index, double value) {
        return getPlainAndAddRelease(array, index, value) + value;
    }

    public static double addPlainAndGetAcquire(double[] array, int index, double value) {
        return getAcquireAndAddPlain(array, index, value) + value;
    }

    public static double addAndGetVolatile(double[] array, int index, double value) {
        return getAndAddVolatile(array, index, value) + value;
    }

    public static void addPlainRelease(double[] array, int index, double value) {
        UNSAFE.getAndAddDoubleRelease(array, elementOffset(index), value);
    }

    public static void addAcquirePlain(double[] array, int index, double value) {
        UNSAFE.getAndAddDoubleAcquire(array, elementOffset(index), value);
    }

    public static void addVolatile(double[] array, int index, double value) {
        UNSAFE.getAndAddDouble(array, elementOffset(index), value);
    }

    public static double getPlainAndSetRelease(double[] array, int index, double value) {
        return UNSAFE.getAndSetDoubleRelease(array, elementOffset(index), value);
    }

    public static double getAcquireAndSetPlain(double[] array, int index, double value) {
        return UNSAFE.getAndSetDoubleAcquire(array, elementOffset(index), value);
    }

    public static double getAndSetVolatile(double[] array, int index, double value) {
        return UNSAFE.getAndSetDouble(array, elementOffset(index), value);
    }

    public static double comparePlainAndExchangeRelease(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.compareAndExchangeDoubleRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static double compareAcquireAndExchangePlain(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.compareAndExchangeDoubleAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static double compareAndExchangeVolatile(double[] array, int index, double oldValue, double newValue) {
        return UNSAFE.compareAndExchangeDouble(array, elementOffset(index), oldValue, newValue);
    }

    public static double getAndUpdate(double[] array, int index, DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(array, index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static double updateAndGet(double[] array, int index, DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(array, index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static double getAndAccumulate(double[] array, int index, double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(array, index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static double accumulateAndGet(double[] array, int index, double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(array, index), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static void fillOpaque(double[] array, double value) {
        for (int i = 0; i < array.length; i++) {
            setOpaque(array, i, value);
        }
    }

    public static void fillRelease(double[] array, double value) {
        for (int i = 0; i < array.length; i++) {
            setRelease(array, i, value);
        }
    }

    public static void fillVolatile(double[] array, double value) {
        for (int i = 0; i < array.length; i++) {
            setVolatile(array, i, value);
        }
    }

    public static String toStringOpaque(double[] array) {
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

    public static String toStringAcquire(double[] array) {
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

    public static String toStringVolatile(double[] array) {
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
        return UNSAFE.ARRAY_DOUBLE_BASE_OFFSET + UNSAFE.ARRAY_DOUBLE_INDEX_SCALE * index;
    }
}
