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

import org.ringbuffer.classcopy.CopiedClass;

import java.util.List;

public interface MultiStepBusyWaitStrategy extends BusyWaitStrategy {
    List<BusyWaitStrategy> getStrategies();

    List<Integer> getStrategiesTicks();

    interface Builder {
        Builder endWith(BusyWaitStrategy finalStrategy);

        Builder after(BusyWaitStrategy strategy, int strategyTicks);

        /**
         * A separate busy-wait strategy implementation will be created to allow inlining of polymorphic calls.
         *
         * @see CopiedClass
         */
        Builder copyClass();

        MultiStepBusyWaitStrategy build();
    }
}
