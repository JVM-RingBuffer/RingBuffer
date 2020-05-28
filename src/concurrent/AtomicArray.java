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

import org.ringbuffer.java.Assume;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class AtomicArray<T> {
    private static final VarHandle VALUE = MethodHandles.arrayElementVarHandle(Object[].class);

    private final T[] value;

    public AtomicArray(int length) {
        Assume.notLesser(length, 1);
        @SuppressWarnings("unchecked")
        T[] value = (T[]) new Object[length];
        this.value = value;
    }

    public AtomicArray(T[] value) {
        Assume.notLesser(value.length, 1);
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public void setPlain(int index, T value) {
        this.value[index] = value;
    }

    public void setOpaque(int index, T value) {
        VALUE.setOpaque(this.value, index, value);
    }

    public void setRelease(int index, T value) {
        VALUE.setRelease(this.value, index, value);
    }

    public void setVolatile(int index, T value) {
        VALUE.setVolatile(this.value, index, value);
    }

    public T getPlain(int index) {
        return value[index];
    }

    @SuppressWarnings("unchecked")
    public T getOpaque(int index) {
        return (T) VALUE.getOpaque(value, index);
    }

    @SuppressWarnings("unchecked")
    public T getAcquire(int index) {
        return (T) VALUE.getAcquire(value, index);
    }

    @SuppressWarnings("unchecked")
    public T getVolatile(int index) {
        return (T) VALUE.getVolatile(value, index);
    }

    public boolean compareAndSetVolatile(int index, T oldValue, T newValue) {
        return VALUE.compareAndSet(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int index, T oldValue, T newValue) {
        return VALUE.weakCompareAndSetPlain(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int index, T oldValue, T newValue) {
        return VALUE.weakCompareAndSetRelease(value, index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int index, T oldValue, T newValue) {
        return VALUE.weakCompareAndSetAcquire(value, index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int index, T oldValue, T newValue) {
        return VALUE.weakCompareAndSet(value, index, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public T getPlainAndSetRelease(int index, T value) {
        return (T) VALUE.getAndSetRelease(this.value, index, value);
    }

    @SuppressWarnings("unchecked")
    public T getAcquireAndSetPlain(int index, T value) {
        return (T) VALUE.getAndSetAcquire(this.value, index, value);
    }

    @SuppressWarnings("unchecked")
    public T getAndSetVolatile(int index, T value) {
        return (T) VALUE.getAndSet(this.value, index, value);
    }

    @SuppressWarnings("unchecked")
    public T comparePlainAndExchangeRelease(int index, T oldValue, T newValue) {
        return (T) VALUE.compareAndExchangeRelease(value, index, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public T compareAcquireAndExchangePlain(int index, T oldValue, T newValue) {
        return (T) VALUE.compareAndExchangeAcquire(value, index, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public T compareAndExchangeVolatile(int index, T oldValue, T newValue) {
        return (T) VALUE.compareAndExchange(value, index, oldValue, newValue);
    }

    public T getAndUpdate(int index, UnaryOperator<T> updateFunction) {
        T prev = getVolatile(index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.apply(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public T updateAndGet(int index, UnaryOperator<T> updateFunction) {
        T prev = getVolatile(index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.apply(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public T getAndAccumulate(int index, T constant, BinaryOperator<T> accumulatorFunction) {
        T prev = getVolatile(index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.apply(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public T accumulateAndGet(int index, T constant, BinaryOperator<T> accumulatorFunction) {
        T prev = getVolatile(index), next = null;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.apply(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0, iMax = value.length - 1; ; i++) {
            builder.append(getVolatile(i));
            if (i == iMax) {
                builder.append(']');
                return builder.toString();
            }
            builder.append(", ");
        }
    }
}
