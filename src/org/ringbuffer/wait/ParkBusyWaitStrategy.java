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

package org.ringbuffer.wait;

import java.util.concurrent.locks.LockSupport;

public class ParkBusyWaitStrategy implements BusyWaitStrategy {
    public static final ParkBusyWaitStrategy DEFAULT_INSTANCE = new ParkBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return ArrayMultiStepBusyWaitStrategy.endWith(DEFAULT_INSTANCE)
                .after(YieldBusyWaitStrategy.getDefault(), 100)
                .build();
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        LockSupport.parkNanos(1L);
    }
}
