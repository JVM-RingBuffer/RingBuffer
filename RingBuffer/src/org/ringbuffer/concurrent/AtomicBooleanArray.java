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

import static org.ringbuffer.UnsafeAccess.UNSAFE;

public class AtomicBooleanArray {
    public static void setPlain(boolean[] array, int index, boolean value) {
        UNSAFE.putBoolean(array, elementOffset(index), value);
    }

    public static void setOpaque(boolean[] array, int index, boolean value) {
        UNSAFE.putBooleanOpaque(array, elementOffset(index), value);
    }

    public static void setRelease(boolean[] array, int index, boolean value) {
        UNSAFE.putBooleanRelease(array, elementOffset(index), value);
    }

    public static void setVolatile(boolean[] array, int index, boolean value) {
        UNSAFE.putBooleanVolatile(array, elementOffset(index), value);
    }

    public static boolean getPlain(boolean[] array, int index) {
        return UNSAFE.getBoolean(array, elementOffset(index));
    }

    public static boolean getOpaque(boolean[] array, int index) {
        return UNSAFE.getBooleanOpaque(array, elementOffset(index));
    }

    public static boolean getAcquire(boolean[] array, int index) {
        return UNSAFE.getBooleanAcquire(array, elementOffset(index));
    }

    public static boolean getVolatile(boolean[] array, int index) {
        return UNSAFE.getBooleanVolatile(array, elementOffset(index));
    }

    public static boolean compareAndSetVolatile(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndSetBoolean(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanPlain(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBooleanAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.weakCompareAndSetBoolean(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean getPlainAndSetRelease(boolean[] array, int index, boolean value) {
        return UNSAFE.getAndSetBooleanRelease(array, elementOffset(index), value);
    }

    public static boolean getAcquireAndSetPlain(boolean[] array, int index, boolean value) {
        return UNSAFE.getAndSetBooleanAcquire(array, elementOffset(index), value);
    }

    public static boolean getAndSetVolatile(boolean[] array, int index, boolean value) {
        return UNSAFE.getAndSetBoolean(array, elementOffset(index), value);
    }

    public static boolean comparePlainAndExchangeRelease(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean compareAcquireAndExchangePlain(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBooleanAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean compareAndExchangeVolatile(boolean[] array, int index, boolean oldValue, boolean newValue) {
        return UNSAFE.compareAndExchangeBoolean(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean getPlainAndBitwiseAndRelease(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanRelease(array, elementOffset(index), mask);
    }

    public static boolean getAcquireAndBitwiseAndPlain(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBooleanAcquire(array, elementOffset(index), mask);
    }

    public static boolean getAndBitwiseAndVolatile(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseAndBoolean(array, elementOffset(index), mask);
    }

    public static boolean getPlainAndBitwiseOrRelease(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanRelease(array, elementOffset(index), mask);
    }

    public static boolean getAcquireAndBitwiseOrPlain(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBooleanAcquire(array, elementOffset(index), mask);
    }

    public static boolean getAndBitwiseOrVolatile(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseOrBoolean(array, elementOffset(index), mask);
    }

    public static boolean getPlainAndBitwiseXorRelease(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanRelease(array, elementOffset(index), mask);
    }

    public static boolean getAcquireAndBitwiseXorPlain(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBooleanAcquire(array, elementOffset(index), mask);
    }

    public static boolean getAndBitwiseXorVolatile(boolean[] array, int index, boolean mask) {
        return UNSAFE.getAndBitwiseXorBoolean(array, elementOffset(index), mask);
    }

    public static void fillOpaque(boolean[] array, boolean value) {
        for (int i = 0; i < array.length; i++) {
            setOpaque(array, i, value);
        }
    }

    public static void fillRelease(boolean[] array, boolean value) {
        for (int i = 0; i < array.length; i++) {
            setRelease(array, i, value);
        }
    }

    public static void fillVolatile(boolean[] array, boolean value) {
        for (int i = 0; i < array.length; i++) {
            setVolatile(array, i, value);
        }
    }

    public static String toStringOpaque(boolean[] array) {
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

    public static String toStringAcquire(boolean[] array) {
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

    public static String toStringVolatile(boolean[] array) {
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

    public static long elementOffset(int index) {
        return UNSAFE.ARRAY_BOOLEAN_BASE_OFFSET + UNSAFE.ARRAY_BOOLEAN_INDEX_SCALE * index;
    }
}
