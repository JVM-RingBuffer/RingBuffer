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

import org.ringbuffer.AbstractRingBufferBuilder;
import org.ringbuffer.lock.Lock;
import org.ringbuffer.wait.BusyWaitStrategy;

import java.lang.invoke.MethodHandles;

abstract class AbstractBaseMarshallingRingBufferBuilder<T> extends AbstractRingBufferBuilder<T> {
    private static final MethodHandles.Lookup implLookup = MethodHandles.lookup();

    @Override
    protected MethodHandles.Lookup getImplLookup() {
        return implLookup;
    }

    AbstractBaseMarshallingRingBufferBuilder() {}

    AbstractBaseMarshallingRingBufferBuilder(AbstractBaseMarshallingRingBufferBuilder<?> builder) {
        super(builder);
    }

    @Override
    protected Lock getWriteLock() {
        return super.getWriteLock();
    }

    @Override
    protected Lock getReadLock() {
        return super.getReadLock();
    }

    @Override
    protected BusyWaitStrategy getWriteBusyWaitStrategy() {
        return super.getWriteBusyWaitStrategy();
    }

    @Override
    protected BusyWaitStrategy getReadBusyWaitStrategy() {
        return super.getReadBusyWaitStrategy();
    }
}
