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

import org.ringbuffer.lang.FloatBinaryOperator;
import org.ringbuffer.lang.FloatUnaryOperator;

import static org.ringbuffer.InternalUnsafe.UNSAFE;

public class AtomicFloatArray {
    public static void setPlain(float[] array, int index, float value) {
        UNSAFE.putFloat(array, elementOffset(index), value);
    }

    public static void setOpaque(float[] array, int index, float value) {
        UNSAFE.putFloatOpaque(array, elementOffset(index), value);
    }

    public static void setRelease(float[] array, int index, float value) {
        UNSAFE.putFloatRelease(array, elementOffset(index), value);
    }

    public static void setVolatile(float[] array, int index, float value) {
        UNSAFE.putFloatVolatile(array, elementOffset(index), value);
    }

    public static float getPlain(float[] array, int index) {
        return UNSAFE.getFloat(array, elementOffset(index));
    }

    public static float getOpaque(float[] array, int index) {
        return UNSAFE.getFloatOpaque(array, elementOffset(index));
    }

    public static float getAcquire(float[] array, int index) {
        return UNSAFE.getFloatAcquire(array, elementOffset(index));
    }

    public static float getVolatile(float[] array, int index) {
        return UNSAFE.getFloatVolatile(array, elementOffset(index));
    }

    public static boolean compareAndSetVolatile(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.compareAndSetFloat(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetPlain(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.weakCompareAndSetFloatPlain(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakComparePlainAndSetRelease(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.weakCompareAndSetFloatRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAcquireAndSetPlain(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.weakCompareAndSetFloatAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static boolean weakCompareAndSetVolatile(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.weakCompareAndSetFloat(array, elementOffset(index), oldValue, newValue);
    }

    public static float getPlainAndIncrementRelease(float[] array, int index) {
        return getPlainAndAddRelease(array, index, 1F);
    }

    public static float getAcquireAndIncrementPlain(float[] array, int index) {
        return getAcquireAndAddPlain(array, index, 1F);
    }

    public static float getAndIncrementVolatile(float[] array, int index) {
        return getAndAddVolatile(array, index, 1F);
    }

    public static float getPlainAndDecrementRelease(float[] array, int index) {
        return getPlainAndAddRelease(array, index, -1F);
    }

    public static float getAcquireAndDecrementPlain(float[] array, int index) {
        return getAcquireAndAddPlain(array, index, -1F);
    }

    public static float getAndDecrementVolatile(float[] array, int index) {
        return getAndAddVolatile(array, index, -1F);
    }

    public static float incrementReleaseAndGetPlain(float[] array, int index) {
        return addReleaseAndGetPlain(array, index, 1F);
    }

    public static float incrementPlainAndGetAcquire(float[] array, int index) {
        return addPlainAndGetAcquire(array, index, 1F);
    }

    public static float incrementAndGetVolatile(float[] array, int index) {
        return addAndGetVolatile(array, index, 1F);
    }

    public static void incrementPlainRelease(float[] array, int index) {
        addPlainRelease(array, index, 1F);
    }

    public static void incrementAcquirePlain(float[] array, int index) {
        addAcquirePlain(array, index, 1F);
    }

    public static void incrementVolatile(float[] array, int index) {
        addVolatile(array, index, 1F);
    }

    public static float decrementReleaseAndGetPlain(float[] array, int index) {
        return addReleaseAndGetPlain(array, index, -1F);
    }

    public static float decrementPlainAndGetAcquire(float[] array, int index) {
        return addPlainAndGetAcquire(array, index, -1F);
    }

    public static float decrementAndGetVolatile(float[] array, int index) {
        return addAndGetVolatile(array, index, -1F);
    }

    public static void decrementPlainRelease(float[] array, int index) {
        addPlainRelease(array, index, -1F);
    }

    public static void decrementAcquirePlain(float[] array, int index) {
        addAcquirePlain(array, index, -1F);
    }

    public static void decrementVolatile(float[] array, int index) {
        addVolatile(array, index, -1F);
    }

    public static float getPlainAndAddRelease(float[] array, int index, float value) {
        return UNSAFE.getAndAddFloatRelease(array, elementOffset(index), value);
    }

    public static float getAcquireAndAddPlain(float[] array, int index, float value) {
        return UNSAFE.getAndAddFloatAcquire(array, elementOffset(index), value);
    }

    public static float getAndAddVolatile(float[] array, int index, float value) {
        return UNSAFE.getAndAddFloat(array, elementOffset(index), value);
    }

    public static float addReleaseAndGetPlain(float[] array, int index, float value) {
        return getPlainAndAddRelease(array, index, value) + value;
    }

    public static float addPlainAndGetAcquire(float[] array, int index, float value) {
        return getAcquireAndAddPlain(array, index, value) + value;
    }

    public static float addAndGetVolatile(float[] array, int index, float value) {
        return getAndAddVolatile(array, index, value) + value;
    }

    public static void addPlainRelease(float[] array, int index, float value) {
        UNSAFE.getAndAddFloatRelease(array, elementOffset(index), value);
    }

    public static void addAcquirePlain(float[] array, int index, float value) {
        UNSAFE.getAndAddFloatAcquire(array, elementOffset(index), value);
    }

    public static void addVolatile(float[] array, int index, float value) {
        UNSAFE.getAndAddFloat(array, elementOffset(index), value);
    }

    public static float getPlainAndSetRelease(float[] array, int index, float value) {
        return UNSAFE.getAndSetFloatRelease(array, elementOffset(index), value);
    }

    public static float getAcquireAndSetPlain(float[] array, int index, float value) {
        return UNSAFE.getAndSetFloatAcquire(array, elementOffset(index), value);
    }

    public static float getAndSetVolatile(float[] array, int index, float value) {
        return UNSAFE.getAndSetFloat(array, elementOffset(index), value);
    }

    public static float comparePlainAndExchangeRelease(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.compareAndExchangeFloatRelease(array, elementOffset(index), oldValue, newValue);
    }

    public static float compareAcquireAndExchangePlain(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.compareAndExchangeFloatAcquire(array, elementOffset(index), oldValue, newValue);
    }

    public static float compareAndExchangeVolatile(float[] array, int index, float oldValue, float newValue) {
        return UNSAFE.compareAndExchangeFloat(array, elementOffset(index), oldValue, newValue);
    }

    public static float getAndUpdate(float[] array, int index, FloatUnaryOperator updateFunction) {
        float prev = getVolatile(array, index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsFloat(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static float updateAndGet(float[] array, int index, FloatUnaryOperator updateFunction) {
        float prev = getVolatile(array, index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsFloat(prev);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static float getAndAccumulate(float[] array, int index, float constant, FloatBinaryOperator accumulatorFunction) {
        float prev = getVolatile(array, index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsFloat(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static float accumulateAndGet(float[] array, int index, float constant, FloatBinaryOperator accumulatorFunction) {
        float prev = getVolatile(array, index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsFloat(prev, constant);
            if (weakCompareAndSetVolatile(array, index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(array, index)));
        }
    }

    public static void fillOpaque(float[] array, float value) {
        for (int i = 0; i < array.length; i++) {
            setOpaque(array, i, value);
        }
    }

    public static void fillRelease(float[] array, float value) {
        for (int i = 0; i < array.length; i++) {
            setRelease(array, i, value);
        }
    }

    public static void fillVolatile(float[] array, float value) {
        for (int i = 0; i < array.length; i++) {
            setVolatile(array, i, value);
        }
    }

    public static String toStringOpaque(float[] array) {
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

    public static String toStringAcquire(float[] array) {
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

    public static String toStringVolatile(float[] array) {
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
        return UNSAFE.ARRAY_FLOAT_BASE_OFFSET + UNSAFE.ARRAY_FLOAT_INDEX_SCALE * index;
    }
}
