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

import org.ringbuffer.marshalling.DirectArrayIndexOutOfBoundsException;

public class SafeDirectAtomicBooleanArray extends UnsafeDirectAtomicBooleanArray {
    private final long length;

    public SafeDirectAtomicBooleanArray(long length) {
        super(length);
        this.length = length;
    }

    private void checkBounds(long index) {
        if (index < 0L || index >= length) {
            throw new DirectArrayIndexOutOfBoundsException(index);
        }
    }

    @Override
    public void setPlain(long index, boolean value) {
        checkBounds(index);
        super.setPlain(index, value);
    }

    @Override
    public void setOpaque(long index, boolean value) {
        checkBounds(index);
        super.setOpaque(index, value);
    }

    @Override
    public void setRelease(long index, boolean value) {
        checkBounds(index);
        super.setRelease(index, value);
    }

    @Override
    public void setVolatile(long index, boolean value) {
        checkBounds(index);
        super.setVolatile(index, value);
    }

    @Override
    public boolean getPlain(long index) {
        checkBounds(index);
        return super.getPlain(index);
    }

    @Override
    public boolean getOpaque(long index) {
        checkBounds(index);
        return super.getOpaque(index);
    }

    @Override
    public boolean getAcquire(long index) {
        checkBounds(index);
        return super.getAcquire(index);
    }

    @Override
    public boolean getVolatile(long index) {
        checkBounds(index);
        return super.getVolatile(index);
    }

    @Override
    public boolean compareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.compareAndSetVolatile(index, oldValue, newValue);
    }

    @Override
    public boolean weakComparePlainAndSetPlain(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.weakComparePlainAndSetPlain(index, oldValue, newValue);
    }

    @Override
    public boolean weakComparePlainAndSetRelease(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.weakComparePlainAndSetRelease(index, oldValue, newValue);
    }

    @Override
    public boolean weakCompareAcquireAndSetPlain(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.weakCompareAcquireAndSetPlain(index, oldValue, newValue);
    }

    @Override
    public boolean weakCompareAndSetVolatile(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.weakCompareAndSetVolatile(index, oldValue, newValue);
    }

    @Override
    public boolean getPlainAndSetRelease(long index, boolean value) {
        checkBounds(index);
        return super.getPlainAndSetRelease(index, value);
    }

    @Override
    public boolean getAcquireAndSetPlain(long index, boolean value) {
        checkBounds(index);
        return super.getAcquireAndSetPlain(index, value);
    }

    @Override
    public boolean getAndSetVolatile(long index, boolean value) {
        checkBounds(index);
        return super.getAndSetVolatile(index, value);
    }

    @Override
    public boolean comparePlainAndExchangeRelease(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.comparePlainAndExchangeRelease(index, oldValue, newValue);
    }

    @Override
    public boolean compareAcquireAndExchangePlain(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.compareAcquireAndExchangePlain(index, oldValue, newValue);
    }

    @Override
    public boolean compareAndExchangeVolatile(long index, boolean oldValue, boolean newValue) {
        checkBounds(index);
        return super.compareAndExchangeVolatile(index, oldValue, newValue);
    }

    @Override
    public boolean getPlainAndBitwiseAndRelease(long index, boolean mask) {
        checkBounds(index);
        return super.getPlainAndBitwiseAndRelease(index, mask);
    }

    @Override
    public boolean getAcquireAndBitwiseAndPlain(long index, boolean mask) {
        checkBounds(index);
        return super.getAcquireAndBitwiseAndPlain(index, mask);
    }

    @Override
    public boolean getAndBitwiseAndVolatile(long index, boolean mask) {
        checkBounds(index);
        return super.getAndBitwiseAndVolatile(index, mask);
    }

    @Override
    public boolean getPlainAndBitwiseOrRelease(long index, boolean mask) {
        checkBounds(index);
        return super.getPlainAndBitwiseOrRelease(index, mask);
    }

    @Override
    public boolean getAcquireAndBitwiseOrPlain(long index, boolean mask) {
        checkBounds(index);
        return super.getAcquireAndBitwiseOrPlain(index, mask);
    }

    @Override
    public boolean getAndBitwiseOrVolatile(long index, boolean mask) {
        checkBounds(index);
        return super.getAndBitwiseOrVolatile(index, mask);
    }

    @Override
    public boolean getPlainAndBitwiseXorRelease(long index, boolean mask) {
        checkBounds(index);
        return super.getPlainAndBitwiseXorRelease(index, mask);
    }

    @Override
    public boolean getAcquireAndBitwiseXorPlain(long index, boolean mask) {
        checkBounds(index);
        return super.getAcquireAndBitwiseXorPlain(index, mask);
    }

    @Override
    public boolean getAndBitwiseXorVolatile(long index, boolean mask) {
        checkBounds(index);
        return super.getAndBitwiseXorVolatile(index, mask);
    }

    @Override
    public void fill(boolean value, long length) {
        if (length != this.length) {
            throw new DirectArrayIndexOutOfBoundsException(length);
        }
        super.fill(value, length);
    }
}
