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

package test.object;

import eu.menzani.lang.Numbers;
import test.AbstractRingBufferTest;

import java.util.function.Supplier;

public abstract class RingBufferTest extends AbstractRingBufferTest {
    static final int BLOCKING_SIZE = 5;
    protected static final int ONE_TO_ONE_SIZE = NUM_ITERATIONS + 1;
    protected static final int NOT_ONE_TO_ONE_SIZE = TOTAL_ELEMENTS + 1;
    protected static final int FAST_ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(ONE_TO_ONE_SIZE);
    protected static final int FAST_NOT_ONE_TO_ONE_SIZE = Numbers.getNextPowerOfTwo(NOT_ONE_TO_ONE_SIZE);

    static final int BATCH_SIZE = 20;
    static final int BLOCKING_BATCH_SIZE = 4;

    static final Supplier<Event> FILLER = () -> new Event(0);
}
