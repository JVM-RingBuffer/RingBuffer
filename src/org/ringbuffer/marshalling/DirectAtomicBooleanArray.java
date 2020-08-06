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

package org.ringbuffer.marshalling;

import static org.ringbuffer.system.Unsafe.UNSAFE;

class DirectAtomicBooleanArray {
    static void setPlain(long address, long index, boolean value) {
        UNSAFE.putBoolean(null, address + index, value);
    }

    static void setOpaque(long address, long index, boolean value) {
        UNSAFE.putBooleanOpaque(null, address + index, value);
    }

    static void setRelease(long address, long index, boolean value) {
        UNSAFE.putBooleanRelease(null, address + index, value);
    }

    static void setVolatile(long address, long index, boolean value) {
        UNSAFE.putBooleanVolatile(null, address + index, value);
    }

    static boolean getPlain(long address, long index) {
        return UNSAFE.getBoolean(null, address + index);
    }

    static boolean getOpaque(long address, long index) {
        return UNSAFE.getBooleanOpaque(null, address + index);
    }

    static boolean getAcquire(long address, long index) {
        return UNSAFE.getBooleanAcquire(null, address + index);
    }

    static boolean getVolatile(long address, long index) {
        return UNSAFE.getBooleanVolatile(null, address + index);
    }

    static boolean compareAndSetVolatile(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    static boolean weakComparePlainAndSetPlain(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanPlain(null, address + index, oldValue, newValue);
    }

    static boolean weakComparePlainAndSetRelease(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanRelease(null, address + index, oldValue, newValue);
    }

    static boolean weakCompareAcquireAndSetPlain(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanAcquire(null, address + index, oldValue, newValue);
    }

    static boolean weakCompareAndSetVolatile(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBoolean(null, address + index, oldValue, newValue);
    }

    static boolean getPlainAndSetRelease(long address, long index, boolean value) {
        return UNSAFE.getAndSetBooleanRelease(null, address + index, value);
    }

    static boolean getAcquireAndSetPlain(long address, long index, boolean value) {
        return UNSAFE.getAndSetBooleanAcquire(null, address + index, value);
    }

    static boolean getAndSetVolatile(long address, long index, boolean value) {
        return UNSAFE.getAndSetBoolean(null, address + index, value);
    }

    static boolean comparePlainAndExchangeRelease(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanRelease(null, address + index, oldValue, newValue);
    }

    static boolean compareAcquireAndExchangePlain(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanAcquire(null, address + index, oldValue, newValue);
    }

    static boolean compareAndExchangeVolatile(long address, long index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBoolean(null, address + index, oldValue, newValue);
    }

    static boolean getPlainAndBitwiseAndRelease(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanRelease(null, address + index, mask);
    }

    static boolean getAcquireAndBitwiseAndPlain(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanAcquire(null, address + index, mask);
    }

    static boolean getAndBitwiseAndVolatile(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBoolean(null, address + index, mask);
    }

    static boolean getPlainAndBitwiseOrRelease(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanRelease(null, address + index, mask);
    }

    static boolean getAcquireAndBitwiseOrPlain(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanAcquire(null, address + index, mask);
    }

    static boolean getAndBitwiseOrVolatile(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBoolean(null, address + index, mask);
    }

    static boolean getPlainAndBitwiseXorRelease(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanRelease(null, address + index, mask);
    }

    static boolean getAcquireAndBitwiseXorPlain(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanAcquire(null, address + index, mask);
    }

    static boolean getAndBitwiseXorVolatile(long address, long index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBoolean(null, address + index, mask);
    }
}
