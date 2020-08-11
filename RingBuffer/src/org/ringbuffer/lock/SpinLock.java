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

package org.ringbuffer.lock;

import jdk.internal.vm.annotation.Contended;
import org.ringbuffer.classcopy.CopiedClass;
import org.ringbuffer.concurrent.AtomicBoolean;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.NoopBusyWaitStrategy;

@Contended
public class SpinLock implements Lock {
    private static final long STATE = Unsafe.objectFieldOffset(SpinLock.class, "state");

    private final BusyWaitStrategy additionalBusyWaitStrategy;
    @Contended
    private boolean state;

    public SpinLock() {
        this(NoopBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    public SpinLock(BusyWaitStrategy additionalBusyWaitStrategy) {
        this.additionalBusyWaitStrategy = additionalBusyWaitStrategy;
    }

    @Override
    public void lock() {
        while (AtomicBoolean.getAcquireAndSetPlain(this, STATE, true)) {
            additionalBusyWaitStrategy.reset();
            while (state) {
                Thread.onSpinWait();
                additionalBusyWaitStrategy.tick();
            }
        }
    }

    @Override
    public void unlock() {
        AtomicBoolean.setRelease(this, STATE, false);
    }

    /**
     * Supports creating a separate implementation to allow inlining of polymorphic calls.
     *
     * @see CopiedClass
     */
    public static CopiedClass<Lock> copyClass() {
        return ClassCopy.copyClass(SpinLock.class);
    }
}
