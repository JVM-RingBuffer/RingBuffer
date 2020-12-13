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
import org.ringbuffer.lang.Lang;

public class WakeupBusyWaitStrategy implements BusyWaitStrategy {
    private static final long SHOULD_WAKEUP = Lang.objectFieldOffset(WakeupBusyWaitStrategy.class, "shouldWakeup");

    private final BusyWaitStrategy busyWaitStrategy;

    private boolean shouldWakeup;

    public WakeupBusyWaitStrategy() {
        this(HintBusyWaitStrategy.getDefault());
    }

    public WakeupBusyWaitStrategy(BusyWaitStrategy busyWaitStrategy) {
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
        var busyWaitStrategy = this.busyWaitStrategy;
        busyWaitStrategy.reset();
        while (AtomicBoolean.getOpaque(this, SHOULD_WAKEUP)) {
            busyWaitStrategy.tick();
        }
        resetFlag();
    }

    private void resetFlag() {
        AtomicBoolean.setOpaque(this, SHOULD_WAKEUP, true);
    }
}
