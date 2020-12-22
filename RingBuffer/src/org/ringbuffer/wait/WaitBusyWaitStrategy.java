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

import eu.menzani.system.Platform;

public class WaitBusyWaitStrategy {
    public static final BusyWaitStrategy DEFAULT_INSTANCE;

    static {
        if (Platform.current().isWindows()) {
            DEFAULT_INSTANCE = NtDelayBusyWaitStrategy.DEFAULT_INSTANCE;
        } else {
            DEFAULT_INSTANCE = ParkBusyWaitStrategy.DEFAULT_INSTANCE;
        }
    }

    public static BusyWaitStrategy getDefault() {
        if (Platform.current().isWindows()) {
            return NtDelayBusyWaitStrategy.getDefault();
        }
        return ParkBusyWaitStrategy.getDefault();
    }

    static BusyWaitStrategy createDefault(BusyWaitStrategy defaultInstance) {
        return ArrayMultiStepBusyWaitStrategy.endWith(defaultInstance)
                .after(YieldBusyWaitStrategy.getDefault(), 100)
                .build();
    }
}
