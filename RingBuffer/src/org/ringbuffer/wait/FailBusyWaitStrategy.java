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

public class FailBusyWaitStrategy implements BusyWaitStrategy {
    public static final FailBusyWaitStrategy READING_TOO_SLOW = new FailBusyWaitStrategy(false);
    public static final FailBusyWaitStrategy WRITING_TOO_SLOW = new FailBusyWaitStrategy(true);

    public static BusyWaitStrategy readingTooSlow(int timeBudget) {
        return LinkedMultiStepBusyWaitStrategy.endWith(READING_TOO_SLOW)
                .after(HintBusyWaitStrategy.DEFAULT_INSTANCE, timeBudget)
                .build();
    }

    public static BusyWaitStrategy writingTooSlow(int timeBudget) {
        return LinkedMultiStepBusyWaitStrategy.endWith(WRITING_TOO_SLOW)
                .after(HintBusyWaitStrategy.DEFAULT_INSTANCE, timeBudget)
                .build();
    }

    private final boolean isReading;

    private FailBusyWaitStrategy(boolean isReading) {
        this.isReading = isReading;
    }

    @Override
    public void reset() {
    }

    @Override
    public void tick() {
        if (isReading) {
            throw BusyWaitException.whileReading("The writing side is slower than expected.");
        }
        throw BusyWaitException.whileWriting("The reading side is slower than expected.");
    }
}