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

import org.ringbuffer.system.Unsafe;
import org.ringbuffer.wait.BusyWaitStrategy;
import org.ringbuffer.wait.HintBusyWaitStrategy;

/**
 * Coordinator thread:
 *
 * <pre>{@code
 * // For each worker thread:
 * synchronizer.waitUntilReady();
 * // Then, for each worker thread:
 * synchronizer.commenceExecution();
 * }</pre>
 * <p>
 * Worker threads:
 *
 * <pre>{@code
 * // Do startup logic
 * synchronizer.synchronize(); // Start all together
 * }</pre>
 */
public class ThreadSynchronizer {
    private static final long NOT_READY, DO_NOT_COMMENCE;
    private static final BusyWaitStrategy defaultSynchronizeBusyWaitStrategy = HintBusyWaitStrategy.getDefault();

    static {
        final Class<?> clazz = ThreadSynchronizer.class;
        NOT_READY = Unsafe.objectFieldOffset(clazz, "notReady");
        DO_NOT_COMMENCE = Unsafe.objectFieldOffset(clazz, "doNotCommence");
    }

    private final BusyWaitStrategy waitBusyWaitStrategy;

    public ThreadSynchronizer() {
        this(HintBusyWaitStrategy.getDefault());
    }

    public ThreadSynchronizer(BusyWaitStrategy waitBusyWaitStrategy) {
        this.waitBusyWaitStrategy = waitBusyWaitStrategy;
    }

    private boolean notReady;
    private boolean doNotCommence;

    {
        reset();
    }

    public void waitUntilReady() {
        waitBusyWaitStrategy.reset();
        while (AtomicBoolean.getOpaque(this, NOT_READY)) {
            waitBusyWaitStrategy.tick();
        }
    }

    public void commenceExecution() {
        AtomicBoolean.setOpaque(this, DO_NOT_COMMENCE, false);
    }

    public void synchronize() {
        synchronize(defaultSynchronizeBusyWaitStrategy);
    }

    public void synchronize(@ThreadLocal BusyWaitStrategy busyWaitStrategy) {
        AtomicBoolean.setOpaque(this, NOT_READY, false);
        while (AtomicBoolean.getOpaque(this, DO_NOT_COMMENCE)) {
            busyWaitStrategy.tick();
        }
    }

    /**
     * You also have to call {@link BusyWaitStrategy#reset() reset()} on every busy-wait strategy passed to
     * {@link #synchronize(BusyWaitStrategy) synchronize()}.
     */
    public void reset() {
        AtomicBoolean.setOpaque(this, NOT_READY, true);
        AtomicBoolean.setOpaque(this, DO_NOT_COMMENCE, true);
    }
}
