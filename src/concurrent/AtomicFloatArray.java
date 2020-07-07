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
import org.ringbuffer.java.FloatBinaryOperator;
import org.ringbuffer.java.FloatUnaryOperator;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicFloatArray {
    private static final VarHandle VALUE = MethodHandles.arrayElementVarHandle(float[].class);

    private final float[] value;

    public AtomicFloatArray(int length) {
        Assume.notLesser(length, 1);
        value = new float[length];
    }

    public AtomicFloatArray(float[] value) {
        Assume.notLesser(value.length, 1);
        this.value = value;
    }

    public int length() {
        return value.length;
    }

    public void setPlain(int index, float value) {
        this.value[index] = value;
    }

    public void setOpaque(int index, float value) {
        VALUE.setOpaque(this.value, index, value);
    }

    public void setRelease(int index, float value) {
        VALUE.setRelease(this.value, index, value);
    }

    public void setVolatile(int index, float value) {
        VALUE.setVolatile(this.value, index, value);
    }

    public float getPlain(int index) {
        return value[index];
    }

    public float getOpaque(int index) {
        return (float) VALUE.getOpaque(value, index);
    }

    public float getAcquire(int index) {
        return (float) VALUE.getAcquire(value, index);
    }

    public float getVolatile(int index) {
        return (float) VALUE.getVolatile(value, index);
    }

    public boolean compareAndSetVolatile(int index, float oldValue, float newValue) {
        return VALUE.compareAndSet(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(int index, float oldValue, float newValue) {
        return VALUE.weakCompareAndSetPlain(value, index, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(int index, float oldValue, float newValue) {
        return VALUE.weakCompareAndSetRelease(value, index, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(int index, float oldValue, float newValue) {
        return VALUE.weakCompareAndSetAcquire(value, index, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(int index, float oldValue, float newValue) {
        return VALUE.weakCompareAndSet(value, index, oldValue, newValue);
    }

    public float getPlainAndIncrementRelease(int index) {
        return getPlainAndAddRelease(index, 1F);
    }

    public float getAcquireAndIncrementPlain(int index) {
        return getAcquireAndAddPlain(index, 1F);
    }

    public float getAndIncrementVolatile(int index) {
        return getAndAddVolatile(index, 1F);
    }

    public float getPlainAndDecrementRelease(int index) {
        return getPlainAndAddRelease(index, -1F);
    }

    public float getAcquireAndDecrementPlain(int index) {
        return getAcquireAndAddPlain(index, -1F);
    }

    public float getAndDecrementVolatile(int index) {
        return getAndAddVolatile(index, -1F);
    }

    public float incrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, 1F);
    }

    public float incrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, 1F);
    }

    public float incrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, 1F);
    }

    public void incrementVolatile(int index) {
        addVolatile(index, 1F);
    }

    public void incrementPlainRelease(int index) {
        addPlainRelease(index, 1F);
    }

    public void incrementAcquirePlain(int index) {
        addAcquirePlain(index, 1F);
    }

    public void incrementPlain(int index) {
        value[index]++;
    }

    public float decrementReleaseAndGetPlain(int index) {
        return addReleaseAndGetPlain(index, -1F);
    }

    public float decrementPlainAndGetAcquire(int index) {
        return addPlainAndGetAcquire(index, -1F);
    }

    public float decrementAndGetVolatile(int index) {
        return addAndGetVolatile(index, -1F);
    }

    public void decrementVolatile(int index) {
        addVolatile(index, -1F);
    }

    public void decrementPlainRelease(int index) {
        addPlainRelease(index, -1F);
    }

    public void decrementAcquirePlain(int index) {
        addAcquirePlain(index, -1F);
    }

    public void decrementPlain(int index) {
        value[index]--;
    }

    public float getPlainAndAddRelease(int index, float value) {
        return (float) VALUE.getAndAddRelease(this.value, index, value);
    }

    public float getAcquireAndAddPlain(int index, float value) {
        return (float) VALUE.getAndAddAcquire(this.value, index, value);
    }

    public float getAndAddVolatile(int index, float value) {
        return (float) VALUE.getAndAdd(this.value, index, value);
    }

    public float addReleaseAndGetPlain(int index, float value) {
        return (float) VALUE.getAndAddRelease(this.value, index, value) + value;
    }

    public float addPlainAndGetAcquire(int index, float value) {
        return (float) VALUE.getAndAddAcquire(this.value, index, value) + value;
    }

    public float addAndGetVolatile(int index, float value) {
        return (float) VALUE.getAndAdd(this.value, index, value) + value;
    }

    public void addVolatile(int index, float value) {
        VALUE.getAndAdd(this.value, index, value);
    }

    public void addPlainRelease(int index, float value) {
        VALUE.getAndAddRelease(this.value, index, value);
    }

    public void addAcquirePlain(int index, float value) {
        VALUE.getAndAddAcquire(this.value, index, value);
    }

    public void addPlain(int index, float value) {
        this.value[index] += value;
    }

    public float getPlainAndSetRelease(int index, float value) {
        return (float) VALUE.getAndSetRelease(this.value, index, value);
    }

    public float getAcquireAndSetPlain(int index, float value) {
        return (float) VALUE.getAndSetAcquire(this.value, index, value);
    }

    public float getAndSetVolatile(int index, float value) {
        return (float) VALUE.getAndSet(this.value, index, value);
    }

    public float comparePlainAndExchangeRelease(int index, float oldValue, float newValue) {
        return (float) VALUE.compareAndExchangeRelease(value, index, oldValue, newValue);
    }

    public float compareAcquireAndExchangePlain(int index, float oldValue, float newValue) {
        return (float) VALUE.compareAndExchangeAcquire(value, index, oldValue, newValue);
    }

    public float compareAndExchangeVolatile(int index, float oldValue, float newValue) {
        return (float) VALUE.compareAndExchange(value, index, oldValue, newValue);
    }

    public float getPlainAndBitwiseAndRelease(int index, float mask) {
        return (float) VALUE.getAndBitwiseAndRelease(value, index, mask);
    }

    public float getAcquireAndBitwiseAndPlain(int index, float mask) {
        return (float) VALUE.getAndBitwiseAndAcquire(value, index, mask);
    }

    public float getAndBitwiseAndVolatile(int index, float mask) {
        return (float) VALUE.getAndBitwiseAnd(value, index, mask);
    }

    public float getPlainAndBitwiseOrRelease(int index, float mask) {
        return (float) VALUE.getAndBitwiseOrRelease(value, index, mask);
    }

    public float getAcquireAndBitwiseOrPlain(int index, float mask) {
        return (float) VALUE.getAndBitwiseOrAcquire(value, index, mask);
    }

    public float getAndBitwiseOrVolatile(int index, float mask) {
        return (float) VALUE.getAndBitwiseOr(value, index, mask);
    }

    public float getPlainAndBitwiseXorRelease(int index, float mask) {
        return (float) VALUE.getAndBitwiseXorRelease(value, index, mask);
    }

    public float getAcquireAndBitwiseXorPlain(int index, float mask) {
        return (float) VALUE.getAndBitwiseXorAcquire(value, index, mask);
    }

    public float getAndBitwiseXorVolatile(int index, float mask) {
        return (float) VALUE.getAndBitwiseXor(value, index, mask);
    }

    public float getAndUpdate(int index, FloatUnaryOperator updateFunction) {
        float prev = getVolatile(index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsFloat(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public float updateAndGet(int index, FloatUnaryOperator updateFunction) {
        float prev = getVolatile(index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsFloat(prev);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public float getAndAccumulate(int index, float constant, FloatBinaryOperator accumulatorFunction) {
        float prev = getVolatile(index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsFloat(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public float accumulateAndGet(int index, float constant, FloatBinaryOperator accumulatorFunction) {
        float prev = getVolatile(index), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsFloat(prev, constant);
            if (weakCompareAndSetVolatile(index, prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile(index)));
        }
    }

    public void fill(float value) {
        for (int i = 0; i < this.value.length; i++) {
            this.value[i] = value;
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
