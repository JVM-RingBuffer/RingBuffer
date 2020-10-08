/*
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

package org.ringbuffer.wait;

import org.ringbuffer.concurrent.AtomicBoolean;
import org.ringbuffer.system.Unsafe;

public class WakeupableBusyWaitStrategy implements BusyWaitStrategy {
    private static final long SHOULD_WAKEUP = Unsafe.objectFieldOffset(WakeupableBusyWaitStrategy.class, "shouldWakeup");

    private final int maxIterations;
    private final BusyWaitStrategy busyWaitStrategy;

    private boolean shouldWakeup;

    public WakeupableBusyWaitStrategy(int maxIterations) {
        this(maxIterations, HintBusyWaitStrategy.getDefault());
    }

    public WakeupableBusyWaitStrategy(int maxIterations, BusyWaitStrategy busyWaitStrategy) {
        this.maxIterations = maxIterations;
        this.busyWaitStrategy = busyWaitStrategy;
        resetFlag();
    }

    public void wakeup() {
        AtomicBoolean.setOpaque(this, SHOULD_WAKEUP, false);
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        busyWaitStrategy.reset();
        for (int i = maxIterations; i != 0 && AtomicBoolean.getOpaque(this, SHOULD_WAKEUP); i--) {
            busyWaitStrategy.tick();
        }
        resetFlag();
    }

    private void resetFlag() {
        AtomicBoolean.setOpaque(this, SHOULD_WAKEUP, true);
    }
}
