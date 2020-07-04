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

import org.ringbuffer.object.RingBuffer;
import test.Profiler;

public class FastOneToOneContentionTest extends RingBufferTest {
    public static final RingBuffer<Event> RING_BUFFER =
            RingBuffer.<Event>withCapacity(FAST_ONE_TO_ONE_SIZE)
                    .oneReader()
                    .oneWriter()
                    .fast()
                    .build();

    public static void main(String[] args) {
        new FastOneToOneContentionTest().runBenchmark();
    }

    final RingBuffer<Event> ringBuffer;

    FastOneToOneContentionTest() {
        this(RING_BUFFER);
    }

    protected FastOneToOneContentionTest(RingBuffer<Event> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    protected int getRepeatTimes() {
        return 50;
    }

    @Override
    protected long getSum() {
        return ONE_TO_ONE_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createThroughputProfiler(NUM_ITERATIONS);
        Writer.startAsync(NUM_ITERATIONS, ringBuffer, profiler);
        return Reader.runAsync(NUM_ITERATIONS, ringBuffer, profiler);
    }
}
