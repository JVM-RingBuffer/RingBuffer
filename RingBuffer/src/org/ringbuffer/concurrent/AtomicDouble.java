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

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static org.ringbuffer.UnsafeAccess.UNSAFE;

public class AtomicDouble {
    public static void setPlain(Object instance, long offset, double value) {
        UNSAFE.putDouble(instance, offset, value);
    }

    public static void setOpaque(Object instance, long offset, double value) {
        UNSAFE.putDoubleOpaque(instance, offset, value);
    }

    public static void setRelease(Object instance, long offset, double value) {
        UNSAFE.putDoubleRelease(instance, offset, value);
    }

    public static void setVolatile(Object instance, long offset, double value) {
        UNSAFE.putDoubleVolatile(instance, offset, value);
    }

    public static double getPlain(Object instance, long offset) {
        return UNSAFE.getDouble(instance, offset);
    }

    public static double getOpaque(Object instance, long offset) {
        return UNSAFE.getDoubleOpaque(instance, offset);
    }

    public static double getAcquire(Object instance, long offset) {
        return UNSAFE.getDoubleAcquire(instance, offset);
    }

    public static double getVolatile(Object instance, long offset) {
        return UNSAFE.getDoubleVolatile(instance, offset);
    }

    public static boolean compareAndSetVolatile(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.compareAndSetDouble(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDoublePlain(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDoubleRelease(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDoubleAcquire(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.weakCompareAndSetDouble(instance, offset, oldValue, newValue);
    }

    public static double getAndIncrementPlain(Object instance, long offset) {
        return getAndAddPlain(instance, offset, 1D);
    }

    public static double getPlainAndIncrementRelease(Object instance, long offset) {
        return getPlainAndAddRelease(instance, offset, 1D);
    }

    public static double getAcquireAndIncrementPlain(Object instance, long offset) {
        return getAcquireAndAddPlain(instance, offset, 1D);
    }

    public static double getAndIncrementVolatile(Object instance, long offset) {
        return getAndAddVolatile(instance, offset, 1D);
    }

    public static double getAndDecrementPlain(Object instance, long offset) {
        return getAndAddPlain(instance, offset, -1D);
    }

    public static double getPlainAndDecrementRelease(Object instance, long offset) {
        return getPlainAndAddRelease(instance, offset, -1D);
    }

    public static double getAcquireAndDecrementPlain(Object instance, long offset) {
        return getAcquireAndAddPlain(instance, offset, -1D);
    }

    public static double getAndDecrementVolatile(Object instance, long offset) {
        return getAndAddVolatile(instance, offset, -1D);
    }

    public static double incrementAndGetPlain(Object instance, long offset) {
        return addAndGetPlain(instance, offset, 1D);
    }

    public static double incrementReleaseAndGetPlain(Object instance, long offset) {
        return addReleaseAndGetPlain(instance, offset, 1D);
    }

    public static double incrementPlainAndGetAcquire(Object instance, long offset) {
        return addPlainAndGetAcquire(instance, offset, 1D);
    }

    public static double incrementAndGetVolatile(Object instance, long offset) {
        return addAndGetVolatile(instance, offset, 1D);
    }

    public static void incrementPlain(Object instance, long offset) {
        addPlain(instance, offset, 1D);
    }

    public static void incrementPlainRelease(Object instance, long offset) {
        addPlainRelease(instance, offset, 1D);
    }

    public static void incrementAcquirePlain(Object instance, long offset) {
        addAcquirePlain(instance, offset, 1D);
    }

    public static void incrementVolatile(Object instance, long offset) {
        addVolatile(instance, offset, 1D);
    }

    public static double decrementAndGetPlain(Object instance, long offset) {
        return addAndGetPlain(instance, offset, -1D);
    }

    public static double decrementReleaseAndGetPlain(Object instance, long offset) {
        return addReleaseAndGetPlain(instance, offset, -1D);
    }

    public static double decrementPlainAndGetAcquire(Object instance, long offset) {
        return addPlainAndGetAcquire(instance, offset, -1D);
    }

    public static double decrementAndGetVolatile(Object instance, long offset) {
        return addAndGetVolatile(instance, offset, -1D);
    }

    public static void decrementPlain(Object instance, long offset) {
        addPlain(instance, offset, -1D);
    }

    public static void decrementPlainRelease(Object instance, long offset) {
        addPlainRelease(instance, offset, -1D);
    }

    public static void decrementAcquirePlain(Object instance, long offset) {
        addAcquirePlain(instance, offset, -1D);
    }

    public static void decrementVolatile(Object instance, long offset) {
        addVolatile(instance, offset, -1D);
    }

    public static double getAndAddPlain(Object instance, long offset, double value) {
        double oldValue = getPlain(instance, offset);
        setPlain(instance, offset, oldValue + value);
        return oldValue;
    }

    public static double getPlainAndAddRelease(Object instance, long offset, double value) {
        return UNSAFE.getAndAddDoubleRelease(instance, offset, value);
    }

    public static double getAcquireAndAddPlain(Object instance, long offset, double value) {
        return UNSAFE.getAndAddDoubleAcquire(instance, offset, value);
    }

    public static double getAndAddVolatile(Object instance, long offset, double value) {
        return UNSAFE.getAndAddDouble(instance, offset, value);
    }

    public static double addAndGetPlain(Object instance, long offset, double value) {
        double newValue = getPlain(instance, offset) + value;
        setPlain(instance, offset, newValue);
        return newValue;
    }

    public static double addReleaseAndGetPlain(Object instance, long offset, double value) {
        return getPlainAndAddRelease(instance, offset, value) + value;
    }

    public static double addPlainAndGetAcquire(Object instance, long offset, double value) {
        return getAcquireAndAddPlain(instance, offset, value) + value;
    }

    public static double addAndGetVolatile(Object instance, long offset, double value) {
        return getAndAddVolatile(instance, offset, value) + value;
    }

    public static void addPlain(Object instance, long offset, double value) {
        setPlain(instance, offset, getPlain(instance, offset) + value);
    }

    public static void addPlainRelease(Object instance, long offset, double value) {
        UNSAFE.getAndAddDoubleRelease(instance, offset, value);
    }

    public static void addAcquirePlain(Object instance, long offset, double value) {
        UNSAFE.getAndAddDoubleAcquire(instance, offset, value);
    }

    public static void addVolatile(Object instance, long offset, double value) {
        UNSAFE.getAndAddDouble(instance, offset, value);
    }

    public static double getPlainAndSetRelease(Object instance, long offset, double value) {
        return UNSAFE.getAndSetDoubleRelease(instance, offset, value);
    }

    public static double getAcquireAndSetPlain(Object instance, long offset, double value) {
        return UNSAFE.getAndSetDoubleAcquire(instance, offset, value);
    }

    public static double getAndSetVolatile(Object instance, long offset, double value) {
        return UNSAFE.getAndSetDouble(instance, offset, value);
    }

    public static double comparePlainAndExchangeRelease(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.compareAndExchangeDoubleRelease(instance, offset, oldValue, newValue);
    }

    public static double compareAcquireAndExchangePlain(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.compareAndExchangeDoubleAcquire(instance, offset, oldValue, newValue);
    }

    public static double compareAndExchangeVolatile(Object instance, long offset, double oldValue, double newValue) {
        return UNSAFE.compareAndExchangeDouble(instance, offset, oldValue, newValue);
    }

    public static double getAndUpdate(Object instance, long offset, DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(instance, offset), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static double updateAndGet(Object instance, long offset, DoubleUnaryOperator updateFunction) {
        double prev = getVolatile(instance, offset), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsDouble(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static double getAndAccumulate(Object instance, long offset, double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(instance, offset), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static double accumulateAndGet(Object instance, long offset, double constant, DoubleBinaryOperator accumulatorFunction) {
        double prev = getVolatile(instance, offset), next = 0D;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsDouble(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }
}
