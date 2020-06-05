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

import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.SleepBusyWaitStrategy;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantBusyWaitLock implements Lock {
    private final BusyWaitStrategy busyWaitStrategy;
    private final ReentrantLock lock = new ReentrantLock();

    public ReentrantBusyWaitLock() {
        this(SleepBusyWaitStrategy.DEFAULT_INSTANCE);
    }

    public ReentrantBusyWaitLock(BusyWaitStrategy busyWaitStrategy) {
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
}
