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

import eu.menzani.system.LinuxSleep;

public class NanosleepBusyWaitStrategy implements BusyWaitStrategy {
    public static final NanosleepBusyWaitStrategy DEFAULT_INSTANCE = new NanosleepBusyWaitStrategy(5);

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    public static BusyWaitStrategy getDefault(int nanoseconds) {
        return WaitBusyWaitStrategy.createDefault(new NanosleepBusyWaitStrategy(nanoseconds));
    }

    private final int nanoseconds;

    public NanosleepBusyWaitStrategy(int nanoseconds) {
        this.nanoseconds = nanoseconds;
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        LinuxSleep.sleep(nanoseconds);
    }
}
