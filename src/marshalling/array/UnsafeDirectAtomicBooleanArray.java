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

import org.ringbuffer.marshalling.DirectAtomicBooleanArray;
import org.ringbuffer.system.CleanerService;
import org.ringbuffer.system.InternalUnsafe;

public class UnsafeDirectAtomicBooleanArray implements DirectAtomicBooleanArray {
    private final long address;

    public UnsafeDirectAtomicBooleanArray(long length) {
        address = InternalUnsafe.UNSAFE.allocateMemory(length);
        CleanerService.freeMemory(this, address);
    }

    @Override
    public void setPlain(long index, boolean value) {
        InternalUnsafe.UNSAFE.putBoolean(null, address + index, value);
    }

    @Override
    public void setOpaque(long index, boolean value) {
        InternalUnsafe.UNSAFE.putBooleanOpaque(null, address + index, value);
    }

    @Override
    public void setRelease(long index, boolean value) {
        InternalUnsafe.UNSAFE.putBooleanRelease(null, address + index, value);
    }

    @Override
    public void setVolatile(long index, boolean value) {
        InternalUnsafe.UNSAFE.putBooleanVolatile(null, address + index, value);
    }

    @Override
    public boolean getPlain(long index) {
        return InternalUnsafe.UNSAFE.getBoolean(null, address + index);
    }

    @Override
    public boolean getOpaque(long index) {
        return InternalUnsafe.UNSAFE.getBooleanOpaque(null, address + index);
    }

    @Override
    public boolean getAcquire(long index) {
        return InternalUnsafe.UNSAFE.getBooleanAcquire(null, address + index);
    }

    @Override
    public boolean getVolatile(long index) {
        return InternalUnsafe.UNSAFE.getBooleanVolatile(null, address + index);
    }

    @Override
    public boolean compareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.compareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean weakComparePlainAndSetPlain(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.weakCompareAndSetBooleanPlain(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean weakComparePlainAndSetRelease(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.weakCompareAndSetBooleanRelease(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean weakCompareAcquireAndSetPlain(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.weakCompareAndSetBooleanAcquire(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean weakCompareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.weakCompareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean getPlainAndSetRelease(long index, boolean value) {
        return InternalUnsafe.UNSAFE.getAndSetBooleanRelease(null, address + index, value);
    }

    @Override
    public boolean getAcquireAndSetPlain(long index, boolean value) {
        return InternalUnsafe.UNSAFE.getAndSetBooleanAcquire(null, address + index, value);
    }

    @Override
    public boolean getAndSetVolatile(long index, boolean value) {
        return InternalUnsafe.UNSAFE.getAndSetBoolean(null, address + index, value);
    }

    @Override
    public boolean comparePlainAndExchangeRelease(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.compareAndExchangeBooleanRelease(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean compareAcquireAndExchangePlain(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.compareAndExchangeBooleanAcquire(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean compareAndExchangeVolatile(long index, boolean oldValue, boolean newValue) {
        return InternalUnsafe.UNSAFE.compareAndExchangeBoolean(null, address + index, oldValue, newValue);
    }

    @Override
    public boolean getPlainAndBitwiseAndRelease(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseAndBooleanRelease(null, address + index, mask);
    }

    @Override
    public boolean getAcquireAndBitwiseAndPlain(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseAndBooleanAcquire(null, address + index, mask);
    }

    @Override
    public boolean getAndBitwiseAndVolatile(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseAndBoolean(null, address + index, mask);
    }

    @Override
    public boolean getPlainAndBitwiseOrRelease(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseOrBooleanRelease(null, address + index, mask);
    }

    @Override
    public boolean getAcquireAndBitwiseOrPlain(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseOrBooleanAcquire(null, address + index, mask);
    }

    @Override
    public boolean getAndBitwiseOrVolatile(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseOrBoolean(null, address + index, mask);
    }

    @Override
    public boolean getPlainAndBitwiseXorRelease(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseXorBooleanRelease(null, address + index, mask);
    }

    @Override
    public boolean getAcquireAndBitwiseXorPlain(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseXorBooleanAcquire(null, address + index, mask);
    }

    @Override
    public boolean getAndBitwiseXorVolatile(long index, boolean mask) {
        return InternalUnsafe.UNSAFE.getAndBitwiseXorBoolean(null, address + index, mask);
    }

    @Override
    public void fill(boolean value, long length) {
        for (long i = 0L; i < length; i++) {
            setPlain(i, value);
        }
    }
}
