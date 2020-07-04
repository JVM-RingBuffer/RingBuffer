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

import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import org.ringbuffer.marshalling.FastDirectMarshallingRingBuffer;
import test.Profiler;

public class FastManyReadersDirectMarshallingContentionTest extends RingBufferTest {
    public static final FastDirectMarshallingRingBuffer RING_BUFFER =
            DirectMarshallingRingBuffer.withCapacity(NOT_ONE_TO_ONE_SIZE)
                    .manyReaders()
                    .oneWriter()
                    .fast()
                    .build();

    public static void main(String[] args) {
        new FastManyReadersDirectMarshallingContentionTest().runBenchmark();
    }

    @Override
    protected int getRepeatTimes() {
        return 40;
    }

    @Override
    protected long getSum() {
        return ONE_TO_MANY_SUM;
    }

    @Override
    protected long testSum() {
        Profiler profiler = createLatencyProfiler(TOTAL_ELEMENTS);
        FastDirectWriter.startAsync(TOTAL_ELEMENTS, RING_BUFFER, profiler);
        return FastDirectReader.runGroupAsync(RING_BUFFER, profiler);
    }
}