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

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static org.ringbuffer.system.Unsafe.UNSAFE;

public class AtomicArray<T> {
    public static <T> void setOpaque(T[] array, int index, T value) {
        UNSAFE.putObjectOpaque(array, elementOffset(index), value);
    }

    public static <T> void setRelease(T[] array, int index, T value) {
        UNSAFE.putObjectRelease(array, elementOffset(index), value);
    }

    public static <T> void setVolatile(T[] array, int index, T value) {
        UNSAFE.putObjectVolatile(array, elementOffset(index), value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOpaque(T[] array, int index) {
        return (T) UNSAFE.getObjectOpaque(array, elementOffset(index));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAcquire(T[] array, int index) {
        return (T) UNSAFE.getObjectAcquire(array, elementOffset(index));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getVolatile(T[] array, int index) {
        return (T) UNSAFE.getObjectVolatile(array, elementOffset(index));
    }

    public static <T> boolean compareAndSetVolatile(T[] array, int index, T oldValue, T newValue) {
        return UNSAFE.compareAndSetObject(array, elementOffset(index), oldValue, newValue);
    }

    public static <T> boolean weakComparePlainAndSetPlain(T[] array, int index, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObjectPlain(array, elementOffset(index), oldValue, newValue);
    }

    public static <T> boolean weakComparePlainAndSetRelease(T[] array, int index, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObjectRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static <T> boolean weakCompareAcquireAndSetPlain(T[] array, int index, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObjectAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static <T> boolean weakCompareAndSetVolatile(T[] array, int index, T oldValue, T newValue) {
        return UNSAFE.weakCompareAndSetObject(array, elementOffset(index), oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPlainAndSetRelease(T[] array, int index, T value) {
        return (T) UNSAFE.getAndSetObjectRelease(array, elementOffset(index), value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAcquireAndSetPlain(T[] array, int index, T value) {
        return (T) UNSAFE.getAndSetObjectAcquire(array, elementOffset(index), value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAndSetVolatile(T[] array, int index, T value) {
        return (T) UNSAFE.getAndSetObject(array, elementOffset(index), value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T comparePlainAndExchangeRelease(T[] array, int index, T oldValue, T newValue) {
        return (T) UNSAFE.compareAndExchangeObjectRelease(array, elementOffset(index), oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T compareAcquireAndExchangePlain(T[] array, int index, T oldValue, T newValue) {
        return (T) UNSAFE.compareAndExchangeObjectAcquire(array, elementOffset(index), oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T compareAndExchangeVolatile(T[] array, int index, T oldValue, T newValue) {
        return (T) UNSAFE.compareAndExchangeObject(array, elementOffset(index), oldValue, newValue);
    }

    public static <T> T getAndUpdate(T[] array, int index, UnaryOperator<T> updateFunction) {
        T prev = getVolatile(array, index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.apply(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static <T> T updateAndGet(T[] array, int index, UnaryOperator<T> updateFunction) {
        T prev = getVolatile(array, index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.apply(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static <T> T getAndAccumulate(T[] array, int index, T constant, BinaryOperator<T> accumulatorFunction) {
        T prev = getVolatile(array, index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.apply(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static <T> T accumulateAndGet(T[] array, int index, T constant, BinaryOperator<T> accumulatorFunction) {
        T prev = getVolatile(array, index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.apply(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static <T> void fillOpaque(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            setOpaque(array, i, value);
        }
    }

    public static <T> void fillRelease(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            setRelease(array, i, value);
        }
    }

    public static <T> void fillVolatile(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            setVolatile(array, i, value);
        }
    }

    public static String toStringOpaque(Object[] array) {
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

    public static String toStringAcquire(Object[] array) {
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

    public static String toStringVolatile(Object[] array) {
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

    public static int elementOffset(int index) {
        return jdk.internal.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET + jdk.internal.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE * index;
    }
}
