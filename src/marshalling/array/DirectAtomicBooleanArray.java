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

package org.ringbuffer.marshalling.array;

import org.ringbuffer.java.Assume;
import org.ringbuffer.marshalling.DirectByteArrayIndexOutOfBoundsException;
import org.ringbuffer.system.CleanerService;

import static org.ringbuffer.system.InternalUnsafe.*;

public class DirectAtomicBooleanArray {
    private final long address;
    private final long length;

    public DirectAtomicBooleanArray(long length) {
        Assume.notLesser(length, 1L);
        address = UNSAFE.allocateMemory(length);
        CleanerService.freeMemory(this, address);
        this.length = length;
    }

    private void checkBounds(long index) {
        if (index < 0L || index >= length) {
            throw new DirectByteArrayIndexOutOfBoundsException(index);
        }
    }

    public void setPlain(long index, boolean value) {
        checkBounds(index);
        UNSAFE.putBoolean(null, address + index, value);
    }

    public void setOpaque(long index, boolean value) {
        checkBounds(index);
        UNSAFE.putBooleanOpaque(null, address + index, value);
    }

    public void setRelease(long index, boolean value) {
        checkBounds(index);
        UNSAFE.putBooleanRelease(null, address + index, value);
    }

    public void setVolatile(long index, boolean value) {
        checkBounds(index);
        UNSAFE.putBooleanVolatile(null, address + index, value);
    }

    public boolean getPlain(long index) {
        checkBounds(index);
        return UNSAFE.getBoolean(null, address + index);
    }

    public boolean getOpaque(long index) {
        checkBounds(index);
        return UNSAFE.getBooleanOpaque(null, address + index);
    }

    public boolean getAcquire(long index) {
        checkBounds(index);
        return UNSAFE.getBooleanAcquire(null, address + index);
    }

    public boolean getVolatile(long index) {
        checkBounds(index);
        return UNSAFE.getBooleanVolatile(null, address + index);
    }

    public boolean compareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.compareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.weakCompareAndSetBooleanPlain(null, address + index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.weakCompareAndSetBooleanRelease(null, address + index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.weakCompareAndSetBooleanAcquire(null, address + index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.weakCompareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    public boolean getPlainAndSetRelease(long index, boolean value) {
        checkBounds(index);
        return UNSAFE.getAndSetBooleanRelease(null, address + index, value);
    }

    public boolean getAcquireAndSetPlain(long index, boolean value) {
        checkBounds(index);
        return UNSAFE.getAndSetBooleanAcquire(null, address + index, value);
    }

    public boolean getAndSetVolatile(long index, boolean value) {
        checkBounds(index);
        return UNSAFE.getAndSetBoolean(null, address + index, value);
    }

    public boolean comparePlainAndExchangeRelease(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.compareAndExchangeBooleanRelease(null, address + index, oldValue, newValue);
    }

    public boolean compareAcquireAndExchangePlain(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.compareAndExchangeBooleanAcquire(null, address + index, oldValue, newValue);
    }

    public boolean compareAndExchangeVolatile(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return UNSAFE.compareAndExchangeBoolean(null, address + index, oldValue, newValue);
    }

    public boolean getPlainAndBitwiseAndRelease(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseAndBooleanRelease(null, address + index, mask);
    }

    public boolean getAcquireAndBitwiseAndPlain(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseAndBooleanAcquire(null, address + index, mask);
    }

    public boolean getAndBitwiseAndVolatile(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseAndBoolean(null, address + index, mask);
    }

    public boolean getPlainAndBitwiseOrRelease(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseOrBooleanRelease(null, address + index, mask);
    }

    public boolean getAcquireAndBitwiseOrPlain(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseOrBooleanAcquire(null, address + index, mask);
    }

    public boolean getAndBitwiseOrVolatile(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseOrBoolean(null, address + index, mask);
    }

    public boolean getPlainAndBitwiseXorRelease(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseXorBooleanRelease(null, address + index, mask);
    }

    public boolean getAcquireAndBitwiseXorPlain(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseXorBooleanAcquire(null, address + index, mask);
    }

    public boolean getAndBitwiseXorVolatile(long index, boolean mask) {
        checkBounds(index);
        return UNSAFE.getAndBitwiseXorBoolean(null, address + index, mask);
    }

    public void fill(boolean value) {
        for (long i = 0L; i < length; i++) {
            setPlain(i, value);
        }
    }
}
