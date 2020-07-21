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

import org.ringbuffer.java.FloatBinaryOperator;
import org.ringbuffer.java.FloatUnaryOperator;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class AtomicFloat {
    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(AtomicFloat.class, "value", float.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private float value;

    public AtomicFloat() {
    }

    public AtomicFloat(float value) {
        this.value = value;
    }

    public void setPlain(float value) {
        this.value = value;
    }

    public void setOpaque(float value) {
        VALUE.setOpaque(this, value);
    }

    public void setRelease(float value) {
        VALUE.setRelease(this, value);
    }

    public void setVolatile(float value) {
        VALUE.setVolatile(this, value);
    }

    public float getPlain() {
        return value;
    }

    public float getOpaque() {
        return (float) VALUE.getOpaque(this);
    }

    public float getAcquire() {
        return (float) VALUE.getAcquire(this);
    }

    public float getVolatile() {
        return (float) VALUE.getVolatile(this);
    }

    public boolean compareAndSetVolatile(float oldValue, float newValue) {
        return VALUE.compareAndSet(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetPlain(float oldValue, float newValue) {
        return VALUE.weakCompareAndSetPlain(this, oldValue, newValue);
    }

    public boolean weakComparePlainAndSetRelease(float oldValue, float newValue) {
        return VALUE.weakCompareAndSetRelease(this, oldValue, newValue);
    }

    public boolean weakCompareAcquireAndSetPlain(float oldValue, float newValue) {
        return VALUE.weakCompareAndSetAcquire(this, oldValue, newValue);
    }

    public boolean weakCompareAndSetVolatile(float oldValue, float newValue) {
        return VALUE.weakCompareAndSet(this, oldValue, newValue);
    }

    public float getPlainAndIncrementRelease() {
        return getPlainAndAddRelease(1F);
    }

    public float getAcquireAndIncrementPlain() {
        return getAcquireAndAddPlain(1F);
    }

    public float getAndIncrementVolatile() {
        return getAndAddVolatile(1F);
    }

    public float getPlainAndDecrementRelease() {
        return getPlainAndAddRelease(-1F);
    }

    public float getAcquireAndDecrementPlain() {
        return getAcquireAndAddPlain(-1F);
    }

    public float getAndDecrementVolatile() {
        return getAndAddVolatile(-1F);
    }

    public float incrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(1F);
    }

    public float incrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(1F);
    }

    public float incrementAndGetVolatile() {
        return addAndGetVolatile(1F);
    }

    public void incrementVolatile() {
        addVolatile(1F);
    }

    public void incrementPlainRelease() {
        addPlainRelease(1F);
    }

    public void incrementAcquirePlain() {
        addAcquirePlain(1F);
    }

    public void incrementPlain() {
        value++;
    }

    public float decrementReleaseAndGetPlain() {
        return addReleaseAndGetPlain(-1F);
    }

    public float decrementPlainAndGetAcquire() {
        return addPlainAndGetAcquire(-1F);
    }

    public float decrementAndGetVolatile() {
        return addAndGetVolatile(-1F);
    }

    public void decrementVolatile() {
        addVolatile(-1F);
    }

    public void decrementPlainRelease() {
        addPlainRelease(-1F);
    }

    public void decrementAcquirePlain() {
        addAcquirePlain(-1F);
    }

    public void decrementPlain() {
        value--;
    }

    public float getPlainAndAddRelease(float value) {
        return (float) VALUE.getAndAddRelease(this, value);
    }

    public float getAcquireAndAddPlain(float value) {
        return (float) VALUE.getAndAddAcquire(this, value);
    }

    public float getAndAddVolatile(float value) {
        return (float) VALUE.getAndAdd(this, value);
    }

    public float addReleaseAndGetPlain(float value) {
        return (float) VALUE.getAndAddRelease(this, value) + value;
    }

    public float addPlainAndGetAcquire(float value) {
        return (float) VALUE.getAndAddAcquire(this, value) + value;
    }

    public float addAndGetVolatile(float value) {
        return (float) VALUE.getAndAdd(this, value) + value;
    }

    public void addVolatile(float value) {
        VALUE.getAndAdd(this, value);
    }

    public void addPlainRelease(float value) {
        VALUE.getAndAddRelease(this, value);
    }

    public void addAcquirePlain(float value) {
        VALUE.getAndAddAcquire(this, value);
    }

    public void addPlain(float value) {
        this.value += value;
    }

    public float getPlainAndSetRelease(float value) {
        return (float) VALUE.getAndSetRelease(this, value);
    }

    public float getAcquireAndSetPlain(float value) {
        return (float) VALUE.getAndSetAcquire(this, value);
    }

    public float getAndSetVolatile(float value) {
        return (float) VALUE.getAndSet(this, value);
    }

    public float comparePlainAndExchangeRelease(float oldValue, float newValue) {
        return (float) VALUE.compareAndExchangeRelease(this, oldValue, newValue);
    }

    public float compareAcquireAndExchangePlain(float oldValue, float newValue) {
        return (float) VALUE.compareAndExchangeAcquire(this, oldValue, newValue);
    }

    public float compareAndExchangeVolatile(float oldValue, float newValue) {
        return (float) VALUE.compareAndExchange(this, oldValue, newValue);
    }

    public float getPlainAndBitwiseAndRelease(float mask) {
        return (float) VALUE.getAndBitwiseAndRelease(this, mask);
    }

    public float getAcquireAndBitwiseAndPlain(float mask) {
        return (float) VALUE.getAndBitwiseAndAcquire(this, mask);
    }

    public float getAndBitwiseAndVolatile(float mask) {
        return (float) VALUE.getAndBitwiseAnd(this, mask);
    }

    public float getPlainAndBitwiseOrRelease(float mask) {
        return (float) VALUE.getAndBitwiseOrRelease(this, mask);
    }

    public float getAcquireAndBitwiseOrPlain(float mask) {
        return (float) VALUE.getAndBitwiseOrAcquire(this, mask);
    }

    public float getAndBitwiseOrVolatile(float mask) {
        return (float) VALUE.getAndBitwiseOr(this, mask);
    }

    public float getPlainAndBitwiseXorRelease(float mask) {
        return (float) VALUE.getAndBitwiseXorRelease(this, mask);
    }

    public float getAcquireAndBitwiseXorPlain(float mask) {
        return (float) VALUE.getAndBitwiseXorAcquire(this, mask);
    }

    public float getAndBitwiseXorVolatile(float mask) {
        return (float) VALUE.getAndBitwiseXor(this, mask);
    }

    public float getAndUpdate(FloatUnaryOperator updateFunction) {
        float prev = getVolatile(), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsFloat(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public float updateAndGet(FloatUnaryOperator updateFunction) {
        float prev = getVolatile(), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = updateFunction.applyAsFloat(prev);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public float getAndAccumulate(float constant, FloatBinaryOperator accumulatorFunction) {
        float prev = getVolatile(), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsFloat(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return prev;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    public float accumulateAndGet(float constant, FloatBinaryOperator accumulatorFunction) {
        float prev = getVolatile(), next = 0F;
        for (boolean haveNext = false; ; ) {
            if (!haveNext)
                next = accumulatorFunction.applyAsFloat(prev, constant);
            if (weakCompareAndSetVolatile(prev, next))
                return next;
            haveNext = (prev == (prev = getVolatile()));
        }
    }

    @Override
    public String toString() {
        return Float.toString(getVolatile());
    }
}
