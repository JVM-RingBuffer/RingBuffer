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

public class SleepBusyWaitStrategy implements BusyWaitStrategy {
    public static final SleepBusyWaitStrategy DEFAULT_INSTANCE = new SleepBusyWaitStrategy();

    public static BusyWaitStrategy getDefault() {
        return WaitBusyWaitStrategy.createDefault(DEFAULT_INSTANCE);
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
