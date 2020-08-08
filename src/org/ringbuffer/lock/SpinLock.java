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

import org.ringbuffer.classcopy.CopiedClass;
import org.ringbuffer.concurrent.AtomicBoolean;
import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.NoopBusyWaitStrategy;

abstract class SpinLock_pad0 {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

abstract class SpinLock_state extends SpinLock_pad0 {
    boolean state;
}

abstract class SpinLock_pad1 extends SpinLock_state {
    long p000, p001, p002, p003, p004, p005, p006, p007;
    long p008, p009, p010, p011, p012, p013, p014, p015;
}

public class SpinLock extends SpinLock_pad1 implements Lock {
    private static final long STATE = Unsafe.objectFieldOffset(SpinLock_state.class, "state");

    private final BusyWaitStrategy additionalBusyWaitStrategy;

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
