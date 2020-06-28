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
import org.ringbuffer.java.CleanerService;

import static org.ringbuffer.system.InternalUnsafe.*;

public class DirectAtomicBooleanArray {
    private final long address;

    public DirectAtomicBooleanArray(long length) {
        Assume.notLesser(length, 1L);
        address = UNSAFE.allocateMemory(length);
        CleanerService.freeMemory(this, address);
    }

    public void setPlain(long index, boolean value) {
        UNSAFE.putBoolean(null, address + index, value);
    }

    public void setOpaque(long index, boolean value) {
        UNSAFE.putBooleanOpaque(null, address + index, value);
    }

    public void setRelease(long index, boolean value) {
        UNSAFE.putBooleanRelease(null, address + index, value);
    }

    public void setVolatile(long index, boolean value) {
        UNSAFE.putBooleanVolatile(null, address + index, value);
    }

    public boolean getPlain(long index) {
        return UNSAFE.getBoolean(null, address + index);
    }

    public boolean getOpaque(long index) {
        return UNSAFE.getBooleanOpaque(null, address + index);
    }

    public boolean getAcquire(long index) {
        return UNSAFE.getBooleanAcquire(null, address + index);
    }

    public boolean getVolatile(long index) {
        return UNSAFE.getBooleanVolatile(null, address + index);
    }

    public boolean compareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanPlain(null, address + index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanRelease(null, address + index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanAcquire(null, address + index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    public boolean getPlainAndSetRelease(long index, boolean value) {
        return UNSAFE.getAndSetBooleanRelease(null, address + index, value);
    }

    public boolean getAcquireAndSetPlain(long index, boolean value) {
        return UNSAFE.getAndSetBooleanAcquire(null, address + index, value);
    }

    public boolean getAndSetVolatile(long index, boolean value) {
        return UNSAFE.getAndSetBoolean(null, address + index, value);
    }

    public boolean comparePlainAndExchangeRelease(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanRelease(null, address + index, oldValue, newValue);
    }

    public boolean compareAcquireAndExchangePlain(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanAcquire(null, address + index, oldValue, newValue);
    }

    public boolean compareAndExchangeVolatile(long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBoolean(null, address + index, oldValue, newValue);
    }

    public boolean getPlainAndBitwiseAndRelease(long index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanRelease(null, address + index, mask);
    }

    public boolean getAcquireAndBitwiseAndPlain(long index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanAcquire(null, address + index, mask);
    }

    public boolean getAndBitwiseAndVolatile(long index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBoolean(null, address + index, mask);
    }

    public boolean getPlainAndBitwiseOrRelease(long index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanRelease(null, address + index, mask);
    }

    public boolean getAcquireAndBitwiseOrPlain(long index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanAcquire(null, address + index, mask);
    }

    public boolean getAndBitwiseOrVolatile(long index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBoolean(null, address + index, mask);
    }

    public boolean getPlainAndBitwiseXorRelease(long index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanRelease(null, address + index, mask);
    }

    public boolean getAcquireAndBitwiseXorPlain(long index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanAcquire(null, address + index, mask);
    }

    public boolean getAndBitwiseXorVolatile(long index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBoolean(null, address + index, mask);
    }

    public void fill(boolean value, long length) {
        for (long i = 0L; i < length; i++) {
            UNSAFE.putBoolean(null, address + i, value);
        }
    }

    public String toString(long length) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        length--;
        for (long i = 0L; ; i++) {
            builder.append(getVolatile(i));
            if (i == length) {
                builder.append(']');
                return builder.toString();
            }
            builder.append(", ");
        }
    }
}
