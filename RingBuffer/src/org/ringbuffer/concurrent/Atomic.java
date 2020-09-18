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

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static org.ringbuffer.InternalUnsafe.UNSAFE;

public class Atomic {
    public static <T> void setPlain(Object instance, long offset, T value) {
        UNSAFE.putObject(instance, offset, value);
    }

    public static <T> void setOpaque(Object instance, long offset, T value) {
        UNSAFE.putObjectOpaque(instance, offset, value);
    }

    public static <T> void setRelease(Object instance, long offset, T value) {
        UNSAFE.putObjectRelease(instance, offset, value);
    }

    public static <T> void setVolatile(Object instance, long offset, T value) {
        UNSAFE.putObjectVolatile(instance, offset, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPlain(Object instance, long offset) {
        return (T) UNSAFE.getObject(instance, offset);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOpaque(Object instance, long offset) {
        return (T) UNSAFE.getObjectOpaque(instance, offset);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAcquire(Object instance, long offset) {
        return (T) UNSAFE.getObjectAcquire(instance, offset);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getVolatile(Object instance, long offset) {
        return (T) UNSAFE.getObjectVolatile(instance, offset);
    }

    public static <T> boolean compareAndSetVolatile(Object instance, long offset, T oldValue, T newValue) {
        return UNSAFE.compareAndSetObject(instance, offset, oldValue, newValue);
    }

    public static <T> boolean weakComparePlainAndSetPlain(Object instance, long offset, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObjectPlain(instance, offset, oldValue, newValue);
    }

    public static <T> boolean weakComparePlainAndSetRelease(Object instance, long offset, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObjectRelease(instance, offset, oldValue, newValue);
    }

    public static <T> boolean weakCompareAcquireAndSetPlain(Object instance, long offset, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObjectAcquire(instance, offset, oldValue, newValue);
    }

    public static <T> boolean weakCompareAndSetVolatile(Object instance, long offset, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObject(instance, offset, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPlainAndSetRelease(Object instance, long offset, T value) {
        return (T) UNSAFE.getAndSetObjectRelease(instance, offset, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAcquireAndSetPlain(Object instance, long offset, T value) {
        return (T) UNSAFE.getAndSetObjectAcquire(instance, offset, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAndSetVolatile(Object instance, long offset, T value) {
        return (T) UNSAFE.getAndSetObject(instance, offset, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T comparePlainAndExchangeRelease(Object instance, long offset, T oldValue, T newValue) {
        return (T) UNSAFE.compareAndExchangeObjectRelease(instance, offset, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T compareAcquireAndExchangePlain(Object instance, long offset, T oldValue, T newValue) {
        return (T) UNSAFE.compareAndExchangeObjectAcquire(instance, offset, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T compareAndExchangeVolatile(Object instance, long offset, T oldValue, T newValue) {
        return (T) UNSAFE.compareAndExchangeObject(instance, offset, oldValue, newValue);
    }

    public static <T> T getAndUpdate(Object instance, long offset, UnaryOperator<T> updateFunction) {
        T prev = getVolatile(instance, offset), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.apply(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static <T> T updateAndGet(Object instance, long offset, UnaryOperator<T> updateFunction) {
        T prev = getVolatile(instance, offset), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.apply(prev);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static <T> T getAndAccumulate(Object instance, long offset, T constant, BinaryOperator<T> accumulatorFunction) {
        T prev = getVolatile(instance, offset), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.apply(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }

    public static <T> T accumulateAndGet(Object instance, long offset, T constant, BinaryOperator<T> accumulatorFunction) {
        T prev = getVolatile(instance, offset), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.apply(prev, constant);
            if (weakCompareAndSetVolatile(instance, offset, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(instance, offset)));
        }
    }
}
