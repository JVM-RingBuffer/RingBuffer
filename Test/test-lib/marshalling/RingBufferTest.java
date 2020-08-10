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

package test.marshalling;

import org.ringbuffer.java.Numbers;
import org.ringbuffer.marshalling.Offsets;
import test.AbstractRingBufferTest;

abstract class RingBufferTest extends AbstractRingBufferTest {
    static final int BLOCKING_SIZE = Numbers.getNextPowerOfTwo(5 * Offsets.INT);
    static final int ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(NUM_ITERATIONS * Offsets.INT + 1);
    static final int NOT_ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(TOTAL_ELEMENTS * Offsets.INT + 1);
}
