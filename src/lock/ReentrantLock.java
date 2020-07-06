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

/**
 * Generates garbage.
 */
public class ReentrantLock implements Lock {
    @Contended
    private final java.util.concurrent.locks.ReentrantLock lock;

    public ReentrantLock() {
        this(false);
    }

    public ReentrantLock(boolean fair) {
        lock = new java.util.concurrent.locks.ReentrantLock(fair);
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
