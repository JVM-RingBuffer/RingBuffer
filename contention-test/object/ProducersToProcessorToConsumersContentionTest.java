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
import org.ringbuffer.object.PrefilledClearingRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.wait.YieldBusyWaitStrategy;
import test.Profiler;

public class ProducersToProcessorToConsumersContentionTest extends RingBufferTest {
    public static final RingBuffer<Event> PRODUCERS_RING_BUFFER =
            RingBuffer.<Event>withCapacity(BLOCKING_SIZE)
                    .manyWriters()
                    .oneReader()
                    .blocking()
                    .withGC()
                    .build();
    public static final PrefilledClearingRingBuffer<Event> CONSUMERS_RING_BUFFER =
            PrefilledRingBuffer.<Event>withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .fillWith(FILLER)
                    .oneWriter()
                    .manyReaders()
                    .waitingWith(YieldBusyWaitStrategy.getDefault())
                    .build();

    public static void main(String[] args) {
        new ProducersToProcessorToConsumersContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 10;
    }

    @Override
    protected long getSum() {
        return MANY_WRITERS_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createLatencyProfiler(TOTAL_ELEMENTS);
        Writer.startGroupAsync(PRODUCERS_RING_BUFFER, profiler);
        Processor.startAsync(TOTAL_ELEMENTS, PRODUCERS_RING_BUFFER);
        return BatchReader.runGroupAsync(BATCH_SIZE, CONSUMERS_RING_BUFFER, profiler);
    }
}
