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

import static org.ringbuffer.system.Unsafe.UNSAFE;

public class AtomicBoolean {
    public static void setPlain(Object instance, long offset, boolean value) {
        UNSAFE.putBoolean(instance, offset, value);
    }

    public static void setOpaque(Object instance, long offset, boolean value) {
        UNSAFE.putBooleanOpaque(instance, offset, value);
    }

    public static void setRelease(Object instance, long offset, boolean value) {
        UNSAFE.putBooleanRelease(instance, offset, value);
    }

    public static void setVolatile(Object instance, long offset, boolean value) {
        UNSAFE.putBooleanVolatile(instance, offset, value);
    }

    public static boolean getPlain(Object instance, long offset) {
        return UNSAFE.getBoolean(instance, offset);
    }

    public static boolean getOpaque(Object instance, long offset) {
        return UNSAFE.getBooleanOpaque(instance, offset);
    }

    public static boolean getAcquire(Object instance, long offset) {
        return UNSAFE.getBooleanAcquire(instance, offset);
    }

    public static boolean getVolatile(Object instance, long offset) {
        return UNSAFE.getBooleanVolatile(instance, offset);
    }

    public static boolean compareAndSetVolatile(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndSetBoolean(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanPlain(instance, offset, oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanRelease(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanAcquire(instance, offset, oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBoolean(instance, offset, oldValue, newValue);
    }

    public static boolean getPlainAndSetRelease(Object instance, long offset, boolean value) {
        return UNSAFE.getAndSetBooleanRelease(instance, offset, value);
    }

    public static boolean getAcquireAndSetPlain(Object instance, long offset, boolean value) {
        return UNSAFE.getAndSetBooleanAcquire(instance, offset, value);
    }

    public static boolean getAndSetVolatile(Object instance, long offset, boolean value) {
        return UNSAFE.getAndSetBoolean(instance, offset, value);
    }

    public static boolean comparePlainAndExchangeRelease(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanRelease(instance, offset, oldValue, newValue);
    }

    public static boolean compareAcquireAndExchangePlain(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanAcquire(instance, offset, oldValue, newValue);
    }

    public static boolean compareAndExchangeVolatile(Object instance, long offset, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBoolean(instance, offset, oldValue, newValue);
    }

    public static boolean getPlainAndBitwiseAndRelease(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanRelease(instance, offset, mask);
    }

    public static boolean getAcquireAndBitwiseAndPlain(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanAcquire(instance, offset, mask);
    }

    public static boolean getAndBitwiseAndVolatile(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseAndBoolean(instance, offset, mask);
    }

    public static boolean getPlainAndBitwiseOrRelease(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanRelease(instance, offset, mask);
    }

    public static boolean getAcquireAndBitwiseOrPlain(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanAcquire(instance, offset, mask);
    }

    public static boolean getAndBitwiseOrVolatile(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseOrBoolean(instance, offset, mask);
    }

    public static boolean getPlainAndBitwiseXorRelease(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanRelease(instance, offset, mask);
    }

    public static boolean getAcquireAndBitwiseXorPlain(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanAcquire(instance, offset, mask);
    }

    public static boolean getAndBitwiseXorVolatile(Object instance, long offset, boolean mask) {
        return UNSAFE.getAndBitwiseXorBoolean(instance, offset, mask);
    }
}
