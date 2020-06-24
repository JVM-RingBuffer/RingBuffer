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

package test.object;

import org.ringbuffer.object.FastConcurrentRingBuffer;
import org.ringbuffer.object.FastEmptyRingBuffer;
import test.Profiler;

public class FastManyToManyContentionTest extends RingBufferTest {
    public static final FastEmptyRingBuffer<Event> RING_BUFFER = new FastConcurrentRingBuffer<>(FAST_NOT_ONE_TO_ONE_SIZE);

    public static void main(String[] args) {
        new FastManyToManyContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 12;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createLatencyProfiler(TOTAL_ELEMENTS);
        FastWriter.startGroupAsync(RING_BUFFER, profiler);
        return FastReader.runGroupAsync(RING_BUFFER, profiler);
    }
}
