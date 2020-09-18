/*
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

import static org.ringbuffer.InternalUnsafe.UNSAFE;

public class AtomicInt {
    public static void setPlain(Object instance, long offset, int value) {
        UNSAFE.putInt(instance, offset, value);
    }

    public static void setOpaque(Object instance, long offset, int value) {
        UNSAFE.putIntOpaque(instance, offset, value);
    }

    public static void setRelease(Object instance, long offset, int value) {
        UNSAFE.putIntRelease(instance, offset, value);
    }

    public static void setVolatile(Object instance, long offset, int value) {
        UNSAFE.putIntVolatile(instance, offset, value);
    }

    public static int getPlain(Object instance, long offset) {
        return UNSAFE.getInt(instance, offset);
    }

    public static int getOpaque(Object instance, long offset) {
        return UNSAFE.getIntOpaque(instance, offset);
    }

    public static int getAcquire(Object instance, long offset) {
        return UNSAFE.getIntAcquire(instance, offset);
    }

    public static int getVolatile(Object instance, long offset) {
        return UNSAFE.getIntVolatile(instance, offset);
    }

    public static boolean compareAndSetVolatile(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.compareAndSetInt(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetIntPlain(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetIntRelease(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetIntAcquire(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.weakCompareAndSetInt(instance, offset, oldValue, newValue);
    }

    public static int getAndIncrementPlain(Object instance, long offset) {
        return getAndAddPlain(instance, offset, 1);
    }

    public static int getPlainAndIncrementRelease(Object instance, long offset) {
        return getPlainAndAddRelease(instance, offset, 1);
    }

    public static int getAcquireAndIncrementPlain(Object instance, long offset) {
        return getAcquireAndAddPlain(instance, offset, 1);
    }

    public static int getAndIncrementVolatile(Object instance, long offset) {
        return getAndAddVolatile(instance, offset, 1);
    }

    public static int getAndDecrementPlain(Object instance, long offset) {
        return getAndAddPlain(instance, offset, -1);
    }

    public static int getPlainAndDecrementRelease(Object instance, long offset) {
        return getPlainAndAddRelease(instance, offset, -1);
    }

    public static int getAcquireAndDecrementPlain(Object instance, long offset) {
        return getAcquireAndAddPlain(instance, offset, -1);
    }

    public static int getAndDecrementVolatile(Object instance, long offset) {
        return getAndAddVolatile(instance, offset, -1);
    }

    public static int incrementAndGetPlain(Object instance, long offset) {
        return addAndGetPlain(instance, offset, 1);
    }

    public static int incrementReleaseAndGetPlain(Object instance, long offset) {
        return addReleaseAndGetPlain(instance, offset, 1);
    }

    public static int incrementPlainAndGetAcquire(Object instance, long offset) {
        return addPlainAndGetAcquire(instance, offset, 1);
    }

    public static int incrementAndGetVolatile(Object instance, long offset) {
        return addAndGetVolatile(instance, offset, 1);
    }

    public static void incrementPlain(Object instance, long offset) {
        addPlain(instance, offset, 1);
    }

    public static void incrementPlainRelease(Object instance, long offset) {
        addPlainRelease(instance, offset, 1);
    }

    public static void incrementAcquirePlain(Object instance, long offset) {
        addAcquirePlain(instance, offset, 1);
    }

    public static void incrementVolatile(Object instance, long offset) {
        addVolatile(instance, offset, 1);
    }

    public static int decrementAndGetPlain(Object instance, long offset) {
        return addAndGetPlain(instance, offset, -1);
    }

    public static int decrementReleaseAndGetPlain(Object instance, long offset) {
        return addReleaseAndGetPlain(instance, offset, -1);
    }

    public static int decrementPlainAndGetAcquire(Object instance, long offset) {
        return addPlainAndGetAcquire(instance, offset, -1);
    }

    public static int decrementAndGetVolatile(Object instance, long offset) {
        return addAndGetVolatile(instance, offset, -1);
    }

    public static void decrementPlain(Object instance, long offset) {
        addPlain(instance, offset, -1);
    }

    public static void decrementPlainRelease(Object instance, long offset) {
        addPlainRelease(instance, offset, -1);
    }

    public static void decrementAcquirePlain(Object instance, long offset) {
        addAcquirePlain(instance, offset, -1);
    }

    public static void decrementVolatile(Object instance, long offset) {
        addVolatile(instance, offset, -1);
    }

    public static int getAndAddPlain(Object instance, long offset, int value) {
        int oldValue = getPlain(instance, offset);
        setPlain(instance, offset, oldValue + value);
        return oldValue;
    }

    public static int getPlainAndAddRelease(Object instance, long offset, int value) {
        return UNSAFE.getAndAddIntRelease(instance, offset, value);
    }

    public static int getAcquireAndAddPlain(Object instance, long offset, int value) {
        return UNSAFE.getAndAddIntAcquire(instance, offset, value);
    }

    public static int getAndAddVolatile(Object instance, long offset, int value) {
        return UNSAFE.getAndAddInt(instance, offset, value);
    }

    public static int addAndGetPlain(Object instance, long offset, int value) {
        int newValue = getPlain(instance, offset) + value;
        setPlain(instance, offset, newValue);
        return newValue;
    }

    public static int addReleaseAndGetPlain(Object instance, long offset, int value) {
        return getPlainAndAddRelease(instance, offset, value) + value;
    }

    public static int addPlainAndGetAcquire(Object instance, long offset, int value) {
        return getAcquireAndAddPlain(instance, offset, value) + value;
    }

    public static int addAndGetVolatile(Object instance, long offset, int value) {
        return getAndAddVolatile(instance, offset, value) + value;
    }

    public static void addPlain(Object instance, long offset, int value) {
        setPlain(instance, offset, getPlain(instance, offset) + value);
    }

    public static void addPlainRelease(Object instance, long offset, int value) {
        UNSAFE.getAndAddIntRelease(instance, offset, value);
    }

    public static void addAcquirePlain(Object instance, long offset, int value) {
        UNSAFE.getAndAddIntAcquire(instance, offset, value);
    }

    public static void addVolatile(Object instance, long offset, int value) {
        UNSAFE.getAndAddInt(instance, offset, value);
    }

    public static int getPlainAndSetRelease(Object instance, long offset, int value) {
        return UNSAFE.getAndSetIntRelease(instance, offset, value);
    }

    public static int getAcquireAndSetPlain(Object instance, long offset, int value) {
        return UNSAFE.getAndSetIntAcquire(instance, offset, value);
    }

    public static int getAndSetVolatile(Object instance, long offset, int value) {
        return UNSAFE.getAndSetInt(instance, offset, value);
    }

    public static int comparePlainAndExchangeRelease(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.compareAndExchangeIntRelease(instance, offset, oldValue, newValue);
    }

    public static int compareAcquireAndExchangePlain(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.compareAndExchangeIntAcquire(instance, offset, oldValue, newValue);
    }

    public static int compareAndExchangeVolatile(Object instance, long offset, int oldValue, int newValue) {
        return UNSAFE.compareAndExchangeInt(instance, offset, oldValue, newValue);
    }

    public static int getPlainAndBitwiseAndRelease(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseAndIntRelease(instance, offset, mask);
    }

    public static int getAcquireAndBitwiseAndPlain(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseAndIntAcquire(instance, offset, mask);
    }

    public static int getAndBitwiseAndVolatile(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseAndInt(instance, offset, mask);
    }

    public static int getPlainAndBitwiseOrRelease(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseOrIntRelease(instance, offset, mask);
    }

    public static int getAcquireAndBitwiseOrPlain(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseOrIntAcquire(instance, offset, mask);
    }

    public static int getAndBitwiseOrVolatile(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseOrInt(instance, offset, mask);
    }

    public static int getPlainAndBitwiseXorRelease(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseXorIntRelease(instance, offset, mask);
    }

    public static int getAcquireAndBitwiseXorPlain(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseXorIntAcquire(instance, offset, mask);
    }

    public static int getAndBitwiseXorVolatile(Object instance, long offset, int mask) {
        return UNSAFE.getAndBitwiseXorInt(instance, offset, mask);
    }

    public static int getAndUpdate(Object instance, long offset, IntUnaryOperator updateFunction) {
        int prev = getVolatile(instance, offset), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static int updateAndGet(Object instance, long offset, IntUnaryOperator updateFunction) {
        int prev = getVolatile(instance, offset), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsInt(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static int getAndAccumulate(Object instance, long offset, int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(instance, offset), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static int accumulateAndGet(Object instance, long offset, int constant, IntBinaryOperator accumulatorFunction) {
        int prev = getVolatile(instance, offset), next = 0;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsInt(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }
}
