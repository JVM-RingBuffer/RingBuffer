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

import org.ringbuffer.marshalling.array.SafeDirectAtomicBooleanArray;
import org.ringbuffer.marshalling.array.UnsafeDirectAtomicBooleanArray;

public interface DirectAtomicBooleanArray {
    void setPlain(long index, boolean value);

    void setOpaque(long index, boolean value);

    void setRelease(long index, boolean value);

    void setVolatile(long index, boolean value);

    boolean getPlain(long index);

    boolean getOpaque(long index);

    boolean getAcquire(long index);

    boolean getVolatile(long index);

    boolean compareAndSetVolatile(long index, boolean oldValue, boolean newValue);

    boolean weakComparePlainAndSetPlain(long index, boolean oldValue, boolean newValue);

    boolean weakComparePlainAndSetRelease(long index, boolean oldValue, boolean newValue);

    boolean weakCompareAcquireAndSetPlain(long index, boolean oldValue, boolean newValue);

    boolean weakCompareAndSetVolatile(long index, boolean oldValue, boolean newValue);

    boolean getPlainAndSetRelease(long index, boolean value);

    boolean getAcquireAndSetPlain(long index, boolean value);

    boolean getAndSetVolatile(long index, boolean value);

    boolean comparePlainAndExchangeRelease(long index, boolean oldValue, boolean newValue);

    boolean compareAcquireAndExchangePlain(long index, boolean oldValue, boolean newValue);

    boolean compareAndExchangeVolatile(long index, boolean oldValue, boolean newValue);

    boolean getPlainAndBitwiseAndRelease(long index, boolean mask);

    boolean getAcquireAndBitwiseAndPlain(long index, boolean mask);

    boolean getAndBitwiseAndVolatile(long index, boolean mask);

    boolean getPlainAndBitwiseOrRelease(long index, boolean mask);

    boolean getAcquireAndBitwiseOrPlain(long index, boolean mask);

    boolean getAndBitwiseOrVolatile(long index, boolean mask);

    boolean getPlainAndBitwiseXorRelease(long index, boolean mask);

    boolean getAcquireAndBitwiseXorPlain(long index, boolean mask);

    boolean getAndBitwiseXorVolatile(long index, boolean mask);

    void fill(boolean value, long length);

    interface Factory {
        DirectAtomicBooleanArray newInstance(long length);
    }

    Factory SAFE = SafeDirectAtomicBooleanArray::new;
    Factory UNSAFE = UnsafeDirectAtomicBooleanArray::new;
}
