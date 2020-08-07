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
import org.ringbuffer.concurrent.PaddedReentrantLock;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

public class ReentrantBusyWaitLock implements Lock {
    private final BusyWaitStrategy busyWaitStrategy;
    private final PaddedReentrantLock lock;

    public ReentrantBusyWaitLock() {
        this(false, HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    public ReentrantBusyWaitLock(boolean fair) {
        this(fair, HintBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    public ReentrantBusyWaitLock(BusyWaitStrategy busyWaitStrategy) {
        this(false, busyWaitStrategy);
    }

    public ReentrantBusyWaitLock(boolean fair, BusyWaitStrategy busyWaitStrategy) {
        lock = new PaddedReentrantLock(fair);
        this.busyWaitStrategy = busyWaitStrategy;
    }

    @Override
    public void lock() {
        busyWaitStrategy.reset();
        while (!lock.tryLock()) {
            busyWaitStrategy.tick();
        }
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    /**
     * Supports creating a separate implementation to allow inlining of polymorphic calls.
     *
     * @see CopiedClass
     */
    public static CopiedClass<Lock> copyClass() {
        return ClassCopy.copyClass(ReentrantBusyWaitLock.class);
    }
}
